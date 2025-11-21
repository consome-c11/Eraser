package com.test.eraser.mixin.snackprotector;

import com.test.eraser.utils.ProtectedNonNullList;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Inventory.class)
public class InventoryMixin {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onConstruct(Player player, CallbackInfo ci) {
        NonNullList<ItemStack> originalItems = ((InventoryAccessor) this).getItems();
        ProtectedNonNullList protectedItems = new ProtectedNonNullList(originalItems, ItemStack.EMPTY, player);
        ((InventoryAccessor) this).setItems(protectedItems);

        NonNullList<ItemStack> originalArmor = ((InventoryAccessor) this).getArmor();
        ProtectedNonNullList protectedArmor = new ProtectedNonNullList(originalArmor, ItemStack.EMPTY, player);
        ((InventoryAccessor) this).setArmor(protectedArmor);

        NonNullList<ItemStack> originalOffhand = ((InventoryAccessor) this).getOffhand();
        ProtectedNonNullList protectedOffhand = new ProtectedNonNullList(originalOffhand, ItemStack.EMPTY, player);
        ((InventoryAccessor) this).setOffhand(protectedOffhand);
    }

    @Inject(method = "setPickedItem(Lnet/minecraft/world/item/ItemStack;)V", at = @At("HEAD"))
    private void onSetPickedItemStart(ItemStack stack, CallbackInfo ci) {
        ProtectedNonNullList list = (ProtectedNonNullList) ((InventoryAccessor) this).getItems();
        list.setEditingAllowed(true);
    }

    @Inject(method = "setPickedItem(Lnet/minecraft/world/item/ItemStack;)V", at = @At("RETURN"))
    private void onSetPickedItemEnd(ItemStack stack, CallbackInfo ci) {
        ProtectedNonNullList list = (ProtectedNonNullList) ((InventoryAccessor) this).getItems();
        list.setEditingAllowed(false);
    }

    @Inject(method = "pickSlot(I)V", at = @At("HEAD"))
    private void onPickSlotStart(int index, CallbackInfo ci) {
        ProtectedNonNullList list = (ProtectedNonNullList) ((InventoryAccessor) this).getItems();
        list.setEditingAllowed(true);
    }

    @Inject(method = "pickSlot(I)V", at = @At("RETURN"))
    private void onPickSlotEnd(int index, CallbackInfo ci) {
        ProtectedNonNullList list = (ProtectedNonNullList) ((InventoryAccessor) this).getItems();
        list.setEditingAllowed(false);
    }

    @Inject(method = "add(ILnet/minecraft/world/item/ItemStack;)Z", at = @At("HEAD"))
    private void onAddStart(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        ProtectedNonNullList list = (ProtectedNonNullList) ((InventoryAccessor) this).getItems();
        list.setEditingAllowed(true);
    }

    @Inject(method = "add(ILnet/minecraft/world/item/ItemStack;)Z", at = @At("RETURN"))
    private void onAddEnd(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        ProtectedNonNullList list = (ProtectedNonNullList) ((InventoryAccessor) this).getItems();
        list.setEditingAllowed(false);
    }

    @Inject(method = "load(Lnet/minecraft/nbt/ListTag;)V", at = @At("HEAD"))
    private void onLoadStart(ListTag tag, CallbackInfo ci) {
        ProtectedNonNullList itemsList = (ProtectedNonNullList) ((InventoryAccessor) this).getItems();
        ProtectedNonNullList armorList = (ProtectedNonNullList) ((InventoryAccessor) this).getArmor();
        ProtectedNonNullList offhandList = (ProtectedNonNullList) ((InventoryAccessor) this).getOffhand();

        itemsList.setIsLoadContext(true);
        armorList.setIsLoadContext(true);
        offhandList.setIsLoadContext(true);

        itemsList.setEditingAllowed(true);
        armorList.setEditingAllowed(true);
        offhandList.setEditingAllowed(true);
    }

    @Inject(method = "load(Lnet/minecraft/nbt/ListTag;)V", at = @At("RETURN"))
    private void onLoadEnd(ListTag tag, CallbackInfo ci) {
        ProtectedNonNullList itemsList = (ProtectedNonNullList) ((InventoryAccessor) this).getItems();
        ProtectedNonNullList armorList = (ProtectedNonNullList) ((InventoryAccessor) this).getArmor();
        ProtectedNonNullList offhandList = (ProtectedNonNullList) ((InventoryAccessor) this).getOffhand();

        itemsList.setEditingAllowed(false);
        armorList.setEditingAllowed(false);
        offhandList.setEditingAllowed(false);

        itemsList.setIsLoadContext(false);
        armorList.setIsLoadContext(false);
        offhandList.setIsLoadContext(false);
    }

    /*@Inject(method = "clearContent()V", at = @At("HEAD"))
    private void onClearStart(CallbackInfo ci) {
        ProtectedNonNullList itemsList = (ProtectedNonNullList) ((InventoryAccessor) this).getItems();
        ProtectedNonNullList armorList = (ProtectedNonNullList) ((InventoryAccessor) this).getArmor();
        ProtectedNonNullList offhandList = (ProtectedNonNullList) ((InventoryAccessor) this).getOffhand();
        itemsList.setEditingAllowed(true);
        armorList.setEditingAllowed(true);
        offhandList.setEditingAllowed(true);
    }

    @Inject(method = "clearContent()V", at = @At("RETURN"))
    private void onClearEnd(CallbackInfo ci) {
        ProtectedNonNullList itemsList = (ProtectedNonNullList) ((InventoryAccessor) this).getItems();
        ProtectedNonNullList armorList = (ProtectedNonNullList) ((InventoryAccessor) this).getArmor();
        ProtectedNonNullList offhandList = (ProtectedNonNullList) ((InventoryAccessor) this).getOffhand();
        itemsList.setEditingAllowed(false);
        armorList.setEditingAllowed(false);
        offhandList.setEditingAllowed(false);
    }*/

}