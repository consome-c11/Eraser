package com.test.eraser.utils;

import com.test.eraser.additional.SnackArmor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;

import java.util.Collection;
import java.util.List;

public class ProtectedNonNullList extends NonNullList<ItemStack> {
    private boolean editingAllowed = false;
    private final Player player;
    private boolean isLoadContext = false;

    public ProtectedNonNullList(Collection<ItemStack> delegate, ItemStack defaultElement, Player player) {
        super((List<ItemStack>)delegate, defaultElement);
        this.player = player;
    }

    public void setEditingAllowed(boolean allowed) {
        this.editingAllowed = allowed;
    }

    public void setIsLoadContext(boolean loadContext) {
        this.isLoadContext = loadContext;
    }

    public boolean isEditingAllowed() {
        return this.editingAllowed;
    }

    private boolean isProtectionActive() {
        if (isLoadContext || !SnackArmor.SnackProtector.isFullSet(player)) {
            return true;
        }
        return this.editingAllowed;
    }

    @Override
    public ItemStack set(int index, ItemStack element) {
        if (isProtectionActive()) {
            return super.set(index, element);
        }
        return this.get(index);
    }

    @Override
    public void clear() {
        if (isProtectionActive()) {
            super.clear();
        }
    }
}