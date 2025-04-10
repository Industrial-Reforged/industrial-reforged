package com.indref.industrial_reforged.content.blocks.pipes;

import com.indref.industrial_reforged.api.blocks.transfer.PipeBlock;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.client.renderer.debug.NetworkNodeRenderer;
import com.indref.industrial_reforged.registries.IRNetworks;
import com.indref.industrial_reforged.transportation.deprecated.EnergyNet;
import com.indref.industrial_reforged.data.saved.EnergyNetsSavedData;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.transportation.energy.NetworkManager;
import com.indref.industrial_reforged.transportation.energy.NetworkNode;
import com.indref.industrial_reforged.util.capabilities.CapabilityUtils;
import com.indref.industrial_reforged.util.EnergyNetUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

public class CableBlock extends PipeBlock {
    private final Holder<EnergyTier> energyTier;

    public CableBlock(Properties properties, int width, Holder<EnergyTier> energyTier) {
        super(properties, width);
        this.energyTier = energyTier;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);

        int connectionsAmount = 0;
        boolean[] connections = new boolean[6];
        Direction[] directions = new Direction[6];

        for (Direction dir : Direction.values()) {
            boolean value = state.getValue(CONNECTION[dir.get3DDataValue()]);
            connections[dir.get3DDataValue()] = value;
            if (value) {
                directions[dir.get3DDataValue()] = dir;
                connectionsAmount++;
            } else {
                directions[dir.get3DDataValue()] = null;
            }
        }

        if (connectionsAmount != 2
                || ((!connections[0] || !connections[1])
                && (!connections[2] || !connections[3])
                && (!connections[4] || !connections[5]))) {

            NetworkManager.addNode(IRNetworks.ENERGY_NETWORK.get(), level, pos, directions);
        } else {
            if (NetworkManager.hasNode(pos)) {
                NetworkManager.removeNode(level, pos);
            }
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        NetworkNodeRenderer.selectedNode = NetworkManager.getNode(pos);
        return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull BlockState updateShape(BlockState blockState, Direction facingDirection, BlockState facingBlockState, LevelAccessor level, BlockPos blockPos, BlockPos facingBlockPos) {

//        if (NetworkManager.hasNode(blockPos) && NetworkManager.hasNode(facingBlockPos)) {
//            NetworkManager.getNode(blockPos).getNext().put(facingDirection, NetworkManager.getNode(facingBlockPos));
//        }

        return super.updateShape(blockState, facingDirection, facingBlockState, level, blockPos, facingBlockPos);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        super.onRemove(state, level, pos, newState, movedByPiston);

        if (!state.is(newState.getBlock())) {
            if (NetworkManager.hasNode(pos)) {
                NetworkManager.removeNode(level, pos);
            }
        }
    }

    public Holder<EnergyTier> getEnergyTier() {
        return this.energyTier;
    }

    @Override
    public boolean canConnectToPipe(BlockState connectTo) {
        return connectTo.getBlock() instanceof CableBlock;
    }

    @Override
    public boolean canConnectTo(BlockEntity connectTo) {
        return CapabilityUtils.energyStorageCapability(connectTo) != null
                || CapabilityUtils.blockEntityCapability(Capabilities.EnergyStorage.BLOCK, connectTo) != null;
    }

}
