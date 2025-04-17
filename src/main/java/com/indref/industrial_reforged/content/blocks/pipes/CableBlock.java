package com.indref.industrial_reforged.content.blocks.pipes;

import com.indref.industrial_reforged.api.blocks.transfer.PipeBlock;
import com.indref.industrial_reforged.api.transportation.NetworkNode;
import com.indref.industrial_reforged.client.renderer.debug.NetworkNodeRenderer;
import com.indref.industrial_reforged.client.transportation.ClientNodes;
import com.indref.industrial_reforged.registries.IRNetworks;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.util.capabilities.CapabilityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class CableBlock extends PipeBlock {
    private final Supplier<EnergyTier> energyTier;

    public CableBlock(Properties properties, int width, Supplier<EnergyTier> energyTier) {
        super(properties, width);
        this.energyTier = energyTier;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);

        if (level instanceof ServerLevel serverLevel) {
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

            if ((connectionsAmount == 2
                    && ((connections[0] && connections[1])
                    || (connections[2] && connections[3])
                    || (connections[4] && connections[5]))) || connectionsAmount == 0) {
                if (IRNetworks.ENERGY_NETWORK.get().hasNodeAt(serverLevel, pos)) {
                    IRNetworks.ENERGY_NETWORK.get().removeNodeAndUpdate(serverLevel, pos);
                }

                for (Direction direction : directions) {
                    if (direction != null) {
                        NetworkNode<Integer> nextNode = IRNetworks.ENERGY_NETWORK.get().findNextNode(null, serverLevel, pos, direction);
                        if (nextNode != null) {
                            nextNode.setChanged(serverLevel, null, direction);
                        }
                    }
                }
            } else {
                IRNetworks.ENERGY_NETWORK.get().addNodeAndUpdate(serverLevel, pos, directions, connectionsAmount == 1);
            }
        }

    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide()) {
            NetworkNodeRenderer.selectedNode = ClientNodes.NODES.get(IRNetworks.ENERGY_NETWORK.get()).get(pos);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
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

        if (level instanceof ServerLevel serverLevel) {
            if (!state.is(newState.getBlock())) {
                if (IRNetworks.ENERGY_NETWORK.get().hasNodeAt(serverLevel, pos)) {
                    IRNetworks.ENERGY_NETWORK.get().removeNodeAndUpdate(serverLevel, pos);
                }
            }
        }

    }

    public Supplier<EnergyTier> getEnergyTier() {
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
