package com.test.eraser.utils;

import net.minecraft.world.level.entity.EntityAccess;

public interface EraseEntityLookupBridge<T extends EntityAccess> {
    void eraseEntity(T entity);
}