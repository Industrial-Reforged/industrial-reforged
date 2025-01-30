package com.indref.industrial_reforged.content.blocks.misc;

import com.indref.industrial_reforged.api.blocks.WrenchableBlock;
import com.indref.industrial_reforged.api.blockentities.container.ContainerBlockEntity;
import com.indref.industrial_reforged.api.blocks.container.RotatableContainerBlock;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.content.blockentities.DrainBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DrainBlock extends RotatableContainerBlock implements WrenchableBlock {
    public DrainBlock(Properties p_49224_) {
        super(p_49224_);
    }

    @Override
    public boolean tickingEnabled() {
        return true;
    }

    @Override
    public BlockEntityType<? extends ContainerBlockEntity> getBlockEntityType() {
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
            player.sendSystemMessage(Component.translatable("drain.info.0").append(fluid.getFluidType().getDescription().getString() + ", " + fluidAmount + "mb"));
        }
        return InteractionResult.SUCCESS;
    }
}
