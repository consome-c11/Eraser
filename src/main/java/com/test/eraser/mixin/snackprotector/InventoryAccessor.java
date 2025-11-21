package com.test.eraser.mixin.snackprotector;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Inventory.class)
public interface InventoryAccessor {
    @Mutable
    @Accessor("items")
    void setItems(NonNullList<ItemStack> items);

    @Accessor("items")
    NonNullList<ItemStack> getItems();

    @Mutable
    @Accessor("armor")
    void setArmor(NonNullList<ItemStack> armor);

    @Accessor("armor")
    NonNullList<ItemStack> getArmor();

    @Mutable
    @Accessor("offhand")
    void setOffhand(NonNullList<ItemStack> offhand);

    @Accessor("offhand")
    NonNullList<ItemStack> getOffhand();
}