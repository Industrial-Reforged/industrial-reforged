package com.indref.industrial_reforged.registries.blocks.machines;

import com.indref.industrial_reforged.api.blocks.WrenchableBlock;
import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.api.blocks.container.RotatableContainerBlock;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.blockentities.generators.BasicGeneratorBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BasicGeneratorBlock extends RotatableContainerBlock implements WrenchableBlock {
    public BasicGeneratorBlock(Properties p_49224_) {
        super(p_49224_);
    }

    @Override
    public boolean tickingEnabled() {
        return true;
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(BasicGeneratorBlock::new);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new BasicGeneratorBlockEntity(p_153215_, p_153216_);
    }

    @Override
    public BlockEntityType<? extends ContainerBlockEntity> getBlockEntityType() {
        return IRBlockEntityTypes.BASIC_GENERATOR.get();
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState p_60503_, Level level, BlockPos blockPos, Player player, BlockHitResult p_60508_) {
        player.openMenu((BasicGeneratorBlockEntity) level.getBlockEntity(blockPos), blockPos);
        return InteractionResult.SUCCESS;
    }
}
