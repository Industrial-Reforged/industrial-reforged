package com.indref.industrial_reforged.api.blocks.generator;

import com.indref.industrial_reforged.api.blocks.container.IEnergyBlock;
import com.indref.industrial_reforged.capabilities.IRAttachmentTypes;
import com.indref.industrial_reforged.capabilities.IRCapabilities;
import com.indref.industrial_reforged.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class GeneratorBlock extends BaseEntityBlock {
    /**
     * List of blocks next to generator that can be filled
     */
    private List<BlockPos> suppliesTo;

    public <T extends GeneratorBlockEntity> GeneratorBlock(Properties properties) {
        super(properties);
        this.suppliesTo = new ArrayList<>();
    }

    public abstract BlockEntityType<? extends GeneratorBlockEntity> getBlockEntity();

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide()) return null;

        return createTickerHelper(blockEntityType, getBlockEntity(),
                (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1, pPos, pState1));
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult hitResult) {
        BlockEntity be = level.getBlockEntity(blockPos);
        IEnergyStorage es = level.getCapability(IRCapabilities.EnergyStorage.BLOCK, blockPos, blockState, be, null);
        es.setEnergyStored(100);
        return InteractionResult.SUCCESS;
    }
}
