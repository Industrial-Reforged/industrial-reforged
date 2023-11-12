package com.indref.industrial_reforged.api.blocks.generator;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.container.IEnergyBlock;
import com.indref.industrial_reforged.content.blocks.CableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class GeneratorBlockEntity extends BlockEntity implements IEnergyBlock {
    public GeneratorBlockEntity(BlockEntityType<?> blockEntityType, BlockPos p_155229_, BlockState p_155230_) {
        super(blockEntityType, p_155229_, p_155230_);
    }

    @Override
    public void onChanged() {
        IndustrialReforged.LOGGER.info("Energy Changed!");
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        IEnergyBlock energyBlock = (IEnergyBlock) blockEntity;
        energyBlock.tryFill(blockEntity, getGenerationAmount());

        Vec3i[] neighbors = {
                new Vec3i(1, 0, 0),
                new Vec3i(0, 1, 0),
                new Vec3i(0, 0, 1),
                new Vec3i(-1, 0, 0),
                new Vec3i(0, -1, 0),
                new Vec3i(0, 0, -1),
        };

        for (Vec3i pos : neighbors) {
            BlockEntity blockEntity1 = level.getBlockEntity(blockPos.offset(pos));
            BlockState block = level.getBlockState(blockPos);
            if (block.getBlock() instanceof CableBlock cableBlock) {

            } else if (blockEntity1 instanceof IEnergyBlock energyBlock1) {

            }
        }
    }

    public abstract int getGenerationAmount();
}
