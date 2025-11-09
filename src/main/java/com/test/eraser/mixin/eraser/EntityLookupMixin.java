package com.test.eraser.mixin.eraser;

import com.test.eraser.utils.EraseEntityLookupBridge;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.EntityLookup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityLookup.class)
public abstract class EntityLookupMixin<T extends EntityAccess>
        implements EraseEntityLookupBridge<T>, EntityLookupAccessor<T> {

    @Unique
    @Override
    public void eraseEntity(T entity) {
        this.getByUuid().remove(entity.getUUID());
        this.getById().remove(entity.getId());
    }
}