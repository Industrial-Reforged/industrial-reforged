package com.indref.industrial_reforged.util;

import com.indref.industrial_reforged.api.blocks.container.IEnergyBlock;
import com.indref.industrial_reforged.api.blocks.generator.GeneratorBlock;
import com.indref.industrial_reforged.api.blocks.generator.GeneratorBlockEntity;
import com.indref.industrial_reforged.api.blocks.transfer.PipeBlock;
import com.indref.industrial_reforged.capabilities.energy.network.EnergyNet;
import com.indref.industrial_reforged.registries.blocks.CableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public final class BlockUtils {
    public static BlockPos[] getBlocksAroundSelf(BlockPos selfPos) {
        return new BlockPos[] {
                selfPos.offset(1, 0, 0),
                selfPos.offset(0, 1, 0),
                selfPos.offset(0, 0, 1),
                selfPos.offset(-1, 0, 0),
                selfPos.offset(0, -1, 0),
                selfPos.offset(0, 0, -1)
        };
    }

    public static BlockPos[] getBlocksAroundSelf3x3(BlockPos selfPos) {
        return new BlockPos[] {
                selfPos.offset(1, 0, 0),
                selfPos.offset(0, 0, 1),
                selfPos.offset(-1, 0, 0),
                selfPos.offset(0, 0, -1),
                selfPos.offset(1, 0, -1),
                selfPos.offset(-1, 0, 1),
                selfPos.offset(1, 0, 1),
                selfPos.offset(-1, 0, -1),
        };
    }

    public static EnergyNet.EnergyTypes getEnergyType(Level level, BlockPos blockPos) {
        if (level.getBlockState(blockPos).getBlock() instanceof CableBlock) {
            return EnergyNet.EnergyTypes.TRANSMITTERS;
        } else if (level.getBlockEntity(blockPos) instanceof GeneratorBlockEntity) {
            return EnergyNet.EnergyTypes.PRODUCERS;
        } else if (level.getBlockEntity(blockPos) instanceof IEnergyBlock && !(level.getBlockEntity(blockPos) instanceof GeneratorBlockEntity)) {
            return EnergyNet.EnergyTypes.CONSUMERS;
        }
        return null;
    }
}
