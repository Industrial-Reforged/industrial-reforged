package com.indref.industrial_reforged.tags;

import com.indref.industrial_reforged.IndustrialReforged;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class IRTags {
    public static class Blocks {
        public static final TagKey<Block> MINEABLE_WITH_DRILL = create("mineable/drill");
        private static TagKey<Block> create(String p_203847_) {
            return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, p_203847_));
        }
    }
}
