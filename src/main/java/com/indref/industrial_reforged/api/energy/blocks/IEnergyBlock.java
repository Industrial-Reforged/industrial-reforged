package com.indref.industrial_reforged.api.energy.blocks;

import com.indref.industrial_reforged.api.blocks.IScannable;
import com.indref.industrial_reforged.api.capabilities.energy.EnergyStorageProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

/**
 * Interface for implementing Blocks that store EU
 */
public interface IEnergyBlock extends IScannable {
    EnergyStorageProvider getEnergyStorage();

    @Override
    default List<List<Component>> displayText(BlockState blockState) {
        return List.of(
                List.of(blockState.getBlock().getName())
        );
    }
}
