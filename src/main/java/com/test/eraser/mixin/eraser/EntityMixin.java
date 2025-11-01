package com.test.eraser.mixin.eraser;

import com.test.eraser.additional.ModDamageSources;
import com.test.eraser.additional.SnackArmor;
import com.test.eraser.logic.ILivingEntity;
import com.test.eraser.utils.SynchedEntityDataUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Method;
import java.util.List;

@Mixin(value = Entity.class, priority = 500)
public class EntityMixin {

    @Inject(
            method = "changeDimension(Lnet/minecraft/server/level/ServerLevel;)Lnet/minecraft/world/entity/Entity;",
            at = @At("HEAD"),
            cancellable = false
    )
    private void onChangeDimensionHead(ServerLevel pDestination, CallbackInfoReturnable<Entity> cir) {
        Entity ent = (Entity)(Object)this;

        if (ent.level().isClientSide()) return;
        if (!(ent instanceof LivingEntity living)) return;
        if (!(ent instanceof ILivingEntity self)) return;
        //if(self.isErased()) living.setRemoved(Entity.RemovalReason.CHANGED_DIMENSION);
    }

}