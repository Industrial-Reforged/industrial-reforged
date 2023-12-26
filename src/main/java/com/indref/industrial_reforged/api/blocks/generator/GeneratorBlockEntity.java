package com.indref.industrial_reforged.api.blocks.generator;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.container.IEnergyBlock;
import com.indref.industrial_reforged.registries.blocks.CableBlock;
import com.indref.industrial_reforged.util.BlockUtils;
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

    public void onEnergyChanged() {
        IndustrialReforged.LOGGER.info("Energy Changed!");
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        IEnergyBlock energyBlock = (IEnergyBlock) blockEntity;
        energyBlock.tryFillEnergy(blockEntity, getGenerationAmount());

        for (BlockPos pos : BlockUtils.getBlocksAroundSelf(blockPos)) {
            BlockEntity blockEntity1 = level.getBlockEntity(pos);
            BlockState block = level.getBlockState(blockPos);
            if (block.getBlock() instanceof CableBlock cableBlock) {

            } else if (blockEntity1 instanceof IEnergyBlock energyBlock1) {
                energyBlock.tryDrainEnergy(blockEntity, energyBlock.getEnergyTier().getMaxOutput());
            }
        }
    }

    public abstract int getGenerationAmount();
}
