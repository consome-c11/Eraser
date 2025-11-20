package com.test.eraser.mixin.snackprotector;

import com.test.eraser.additional.SnackArmor;
import com.test.eraser.mixin.eraser.LivingEntityAccessor;
import com.test.eraser.mixin.eraser.SynchedEntityDataAccessor;
import com.test.eraser.utils.SynchedEntityDataUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LivingEntity.class, priority = 0)
public abstract class LivingEntityMixin {
    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void snackProtector$cancelHurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self instanceof Player player && SnackArmor.SnackProtector.isFullSet(player)) {
            cir.cancel();
        }//event living entityevent = attackhandler
    }

    @Inject(method = "setHealth", at = @At("HEAD"), cancellable = true)
    private void snackProtector$canselsetHealth(float hp, CallbackInfo cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self instanceof Player player && SnackArmor.SnackProtector.isFullSet(player)) {
            cir.cancel();
        }
    }

    @Inject(method = "getHealth", at = @At("HEAD"), cancellable = true)
    private void snackProtector$getHealth(CallbackInfoReturnable<Float>  cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self instanceof Player player && SnackArmor.SnackProtector.isFullSet(player)) {
            cir.setReturnValue(player.getMaxHealth());
            cir.cancel();
        }
    }

    @Inject(method = "die", at = @At("HEAD"), cancellable = true)
    private void snackProtector$cancelDie(DamageSource source, CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self instanceof Player player && SnackArmor.SnackProtector.isFullSet(player)) {
            ((LivingEntityAccessor)self).setDeadFlag(false);
            ci.cancel();
            //self.setHealth(self.getMaxHealth());
        }
    }

    @Inject(method = "isAlive", at = @At("HEAD"), cancellable = true)
    private void snackProtector$isAlive(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self instanceof Player player && SnackArmor.SnackProtector.isFullSet(player)) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }

    @Inject(method = "isDeadOrDying", at = @At("HEAD"), cancellable = true)
    private void snackProtector$isDeadOrDying(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (self instanceof Player player && SnackArmor.SnackProtector.isFullSet(player)) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}