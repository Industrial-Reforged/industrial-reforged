package com.indref.industrial_reforged.api.blocks.generator;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.container.IEnergyBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Objects;

public abstract class GeneratorBlockEntity extends BlockEntity implements IEnergyBlock {
    public GeneratorBlockEntity(BlockEntityType<?> blockEntityType, BlockPos p_155229_, BlockState p_155230_) {
        super(blockEntityType, p_155229_, p_155230_);
    }

    @Override
    public void onEnergyChanged() {
        IndustrialReforged.LOGGER.info("Energy Changed!");
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState, List<BlockPos> suppliesTo) {
        /*
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        IEnergyBlock energyBlock = (IEnergyBlock) blockEntity;
        energyBlock.tryFill(blockEntity, getGenerationAmount());

        // filling blocks next to generator
        for (BlockPos pos : suppliesTo) {
            BlockEntity blockEntity1 = level.getBlockEntity(pos);
            if (blockEntity1 instanceof IEnergyBlock energyBlock1) {
                energyBlock1.tryFill(blockEntity1, 10);
            }
        }
        IndustrialReforged.LOGGER.info("Supplies To"+suppliesTo);
         */

        Vec3i[] neighbors = {
                new Vec3i(1, 0, 0),
                new Vec3i(0, 1, 0),
                new Vec3i(0, 0, 1),
                new Vec3i(-1, 0, 0),
                new Vec3i(0, -1, 0),
                new Vec3i(0, 0, -1),
        };

        for (Vec3i pos : neighbors) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos.offset(pos));
            if (blockEntity instanceof IEnergyBlock energyBlock) {
                energyBlock.tryFill(blockEntity, 10);
            }
        }
    }

    public abstract int getGenerationAmount();
}
