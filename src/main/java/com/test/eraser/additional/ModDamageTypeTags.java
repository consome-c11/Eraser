package com.test.eraser.additional;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ModDamageTypeTags extends TagsProvider<DamageType> {
    public ModDamageTypeTags(PackOutput output,
                             CompletableFuture<HolderLookup.Provider> lookupProvider,
                             ExistingFileHelper helper) {
        super(output, Registries.DAMAGE_TYPE, lookupProvider, "eraser", helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(DamageTypeTags.BYPASSES_ARMOR).add(ModDamageTypes.ERASE);
        this.tag(DamageTypeTags.BYPASSES_SHIELD).add(ModDamageTypes.ERASE);
        this.tag(DamageTypeTags.BYPASSES_INVULNERABILITY).add(ModDamageTypes.ERASE);
        this.tag(DamageTypeTags.BYPASSES_COOLDOWN).add(ModDamageTypes.ERASE);
        this.tag(DamageTypeTags.BYPASSES_EFFECTS).add(ModDamageTypes.ERASE);
        this.tag(DamageTypeTags.BYPASSES_RESISTANCE).add(ModDamageTypes.ERASE);
        this.tag(DamageTypeTags.BYPASSES_ENCHANTMENTS).add(ModDamageTypes.ERASE);
        this.tag(DamageTypeTags.ALWAYS_HURTS_ENDER_DRAGONS).add(ModDamageTypes.ERASE);
    }
}
