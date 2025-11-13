package com.test.eraser.logic;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Unique;

public interface IServerLevel {

    boolean forceSetBlock(BlockPos pos, BlockState newState, int flags, boolean isMoving);
}
