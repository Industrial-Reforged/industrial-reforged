package com.indref.industrial_reforged.test;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.IWrenchable;
import com.indref.industrial_reforged.api.blocks.container.IEnergyBlock;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.blockentities.EnergyTestBE;
import com.indref.industrial_reforged.networking.IRPackets;
import com.indref.industrial_reforged.networking.packets.S2CEnergySync;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnergyTestBlock extends BaseEntityBlock implements IWrenchable {
    public EnergyTestBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new EnergyTestBE(blockPos, blockState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide()) return null;

        return createTickerHelper(blockEntityType, IRBlockEntityTypes.ENERGY_TEST.get(),
                (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1, pPos, pState1));
    }

    @Override
    public @NotNull InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (!level.isClientSide()) {
            BlockEntity entity = level.getBlockEntity(blockPos);
            if (entity instanceof IEnergyBlock energyBlock) {
                energyBlock.setStored(entity, energyBlock.getStored(entity)+10000);
                // todo: try moving this to scanner overlay
                IRPackets.sendToClients(new S2CEnergySync(energyBlock.getStored(entity), blockPos));
                IndustrialReforged.LOGGER.info("Right-click");
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}
