package com.indref.industrial_reforged.registries.blockentities.multiblocks.controller;

import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.CommonHooks;

public class SmallFireboxBlockEntity extends FireboxBlockEntity {
    public SmallFireboxBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(IRBlockEntityTypes.SMALL_FIREBOX.get(), p_155229_, p_155230_, 1800);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Small Firebox");
    }
}
