package com.indref.industrial_reforged.content.blocks.machines.primitive;

import com.indref.industrial_reforged.api.blockentities.IRContainerBlockEntity;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.content.blockentities.DrainBlockEntity;
import com.indref.industrial_reforged.translations.IRTranslations;
import com.mojang.serialization.MapCodec;
import com.portingdeadmods.portingdeadlibs.api.blocks.RotatableContainerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public class DrainBlock extends RotatableContainerBlock {
    public DrainBlock(Properties p_49224_) {
        super(p_49224_);
    }

    @Override
    public boolean tickingEnabled() {
        return true;
    }

    @Override
    public BlockEntityType<? extends IRContainerBlockEntity> getBlockEntityType() {
        return IRBlockEntityTypes.DRAIN.get();
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(DrainBlock::new);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState p_60503_, Level level, BlockPos blockPos, Player player, BlockHitResult p_60508_) {
        if (!level.isClientSide()) {
            DrainBlockEntity blockEntity = (DrainBlockEntity) level.getBlockEntity(blockPos);
            IFluidHandler fluidTank = blockEntity.getFluidHandler();
            Fluid fluid = fluidTank.getFluidInTank(0).getFluid();
            int fluidAmount = fluidTank.getFluidInTank(0).getAmount();
            player.displayClientMessage(fluidTank.getFluidInTank(0).getHoverName().copy()
                    .append(", ")
                    .append(IRTranslations.Tooltip.FLUID_AMOUNT_WITH_CAPACITY.component(fluidAmount, fluidTank.getTankCapacity(0)))
                    .append(" ")
                    .append(IRTranslations.General.FLUID_UNIT.component()), true);
        }
        return InteractionResult.SUCCESS;
    }
}
