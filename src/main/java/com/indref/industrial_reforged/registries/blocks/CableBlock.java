package com.indref.industrial_reforged.registries.blocks;

import com.indref.industrial_reforged.api.blocks.container.IEnergyBlock;
import com.indref.industrial_reforged.api.blocks.transfer.PipeBlock;
import com.indref.industrial_reforged.api.tiers.templates.EnergyTier;
import com.indref.industrial_reforged.capabilities.energy.network.EnergyNet;
import com.indref.industrial_reforged.capabilities.energy.network.IEnergyNets;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.blockentities.CableBlockEntity;
import com.indref.industrial_reforged.util.BlockUtils;
import com.indref.industrial_reforged.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CableBlock extends PipeBlock {
    private final EnergyTier energyTier;

    private List<BlockPos> checkedBlocks = new ArrayList<>();
    public CableBlock(Properties properties, int width, EnergyTier energyTier) {
        super(properties, width);
        this.energyTier = energyTier;
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState oldState, boolean p_60570_) {
        // perform this check to ensure that block is actually placed and not just block states updating
        if (oldState.is(Blocks.AIR)) {
            IEnergyNets nets = Util.getEnergyNets(level);
            // Adds the net
            EnergyNet net = nets.getOrCreateNetAndPush(blockPos);
            for (BlockPos pos : BlockUtils.getBlocksAroundSelf(blockPos)) {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof CableBlockEntity) {
                    if (nets.getNetwork(pos) != net) {
                        nets.mergeNets(net, nets.getNetwork(pos));
                    }
                }
            }
        }
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newState, boolean p_60519_) {
        // perform this check to ensure that block is actually removed and not just block states updating
        if (newState.is(Blocks.AIR)) {
            super.onRemove(blockState, level, blockPos, newState, p_60519_);
            IEnergyNets nets = Util.getEnergyNets(level);
            nets.splitNets(blockPos);
            nets.removeNetwork(blockPos);
        }
    }

    public EnergyTier getEnergyTier() {
        return this.energyTier;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CableBlockEntity(blockPos, blockState);
    }

    @Override
    public InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_, InteractionHand p_60507_, BlockHitResult p_60508_) {
        p_60506_.sendSystemMessage(Component.literal("checked posses: "));
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean canConnectToPipe(Block connectTo) {
        return connectTo instanceof CableBlock;
    }

    @Override
    public boolean canConnectTo(BlockEntity connectTo) {
        return connectTo instanceof IEnergyBlock && !(connectTo instanceof CableBlockEntity);
    }
}
