package com.test.eraser.mixin.snackprotector;

import com.test.eraser.additional.SnackArmor;
import com.test.eraser.mixin.eraser.LivingEntityAccessor;
import com.test.eraser.mixin.eraser.SynchedEntityDataAccessor;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.network.syncher.SynchedEntityData$DataItem")
public abstract class SynchedEntityDataItemMixin<T> {
    @Shadow public abstract EntityDataAccessor<T> getAccessor();
    @Shadow private T value;

    @Unique private T oldValue;
    @Unique private static final EntityDataAccessor<Float> HEALTH_ID = LivingEntityAccessor.getDataHealthId();
    @Unique private static java.lang.reflect.Field this$0;

    @Unique
    private SynchedEntityData getOuterSynchedData() {
        try {
            if (this$0 == null) {
                this$0 = getClass().getDeclaredField("this$0");
                this$0.setAccessible(true);
            }
            return (SynchedEntityData) this$0.get(this);
        } catch (Exception e) {
            return null;
        }
    }

    @Inject(method = "setValue", at = @At("HEAD"))
    private void captureOldValue(T newValue, CallbackInfo ci) {
        if (getAccessor() == HEALTH_ID) {
            this.oldValue = this.value;
        }
    }

    @Inject(method = "setDirty", at = @At("HEAD"), cancellable = true)
    private void postProtectHealthUpdate(boolean dirty, CallbackInfo ci) {
        if (getAccessor() != HEALTH_ID || oldValue == null) {
            return;
        }

        SynchedEntityData synchedData = getOuterSynchedData();
        if (synchedData == null) {
            return;
        }

        Entity entity = ((SynchedEntityDataAccessor) synchedData).getEntity();
        if (entity == null || entity.level().isClientSide) {
            return;
        }

        if (!(entity instanceof LivingEntity living) || !(living instanceof Player player)) {
            return;
        }

        if (!SnackArmor.SnackProtector.isFullSet(player)) {
            return;
        }

        if (oldValue instanceof Float oldHealth && this.value instanceof Float newHealth) {
            if (newHealth <= oldHealth) {
                this.value = oldValue;
                ci.cancel();
            }
        }
    }
}