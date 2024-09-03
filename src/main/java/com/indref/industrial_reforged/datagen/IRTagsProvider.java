package com.indref.industrial_reforged.datagen;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.registries.IRTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class IRTagsProvider {
    protected static class ItemsProvider extends ItemTagsProvider {
        public ItemsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags) {
            super(output, lookupProvider, blockTags);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
        }


        private @NotNull IntrinsicTagAppender<Item> tag(TagKey<Item> itemTagKey, ItemLike... items) {
            IntrinsicTagAppender<Item> tag = tag(itemTagKey);
            for (ItemLike item : items) {
                tag.add(item.asItem());
            }
            return tag;
        }
    }

    protected static class BlocksProvider extends BlockTagsProvider {
        public BlocksProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, IndustrialReforged.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {

        }
    }
}
