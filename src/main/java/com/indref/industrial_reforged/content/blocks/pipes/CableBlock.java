package com.indref.industrial_reforged.content.blocks.pipes;

import com.indref.industrial_reforged.IRRegistries;
import com.indref.industrial_reforged.api.blocks.EnergyTierBlock;
import com.indref.industrial_reforged.api.blocks.transfer.PipeBlock;
import com.indref.industrial_reforged.client.renderer.debug.NetworkNodeRenderer;
import com.indref.industrial_reforged.client.transportation.ClientNodes;
import com.indref.industrial_reforged.registries.IRNetworks;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.util.capabilities.CapabilityUtils;
import com.indref.industrial_reforged.util.items.TooltipUtils;
import com.portingdeadmods.portingdeadlibs.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;

public class CableBlock extends PipeBlock implements EnergyTierBlock {
    private final Supplier<EnergyTier> energyTier;

    public CableBlock(Properties properties, int width, Supplier<EnergyTier> energyTier) {
        super(properties, width);
        this.energyTier = energyTier;
    }

    // TODO: Create nodes for connections to interactors
    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);

        if (level instanceof ServerLevel serverLevel) {
            int connectionsAmount = 0;
            boolean[] connections = new boolean[6];
            Direction[] directions = new Direction[6];
            Set<BlockPos> interactors = new HashSet<>();
            Set<Direction> interactorConnections = new HashSet<>();

            for (Direction dir : Direction.values()) {
                boolean connected = state.getValue(CONNECTION[dir.get3DDataValue()]);
                connections[dir.get3DDataValue()] = connected;
                if (connected) {
                    directions[dir.get3DDataValue()] = dir;
                    connectionsAmount++;
                    if (IRNetworks.ENERGY_NETWORK.get().checkForInteractorAt(serverLevel, pos, dir)) {
                        interactors.add(pos.relative(dir));
                        interactorConnections.add(dir);
                    }
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

                Direction direction0 = null;
                Direction direction1 = null;
                for (Direction direction : directions) {
                    if (direction != null) {
                        if (direction0 == null) {
                            direction0 = direction;
                        } else {
                            direction1 = direction;
                        }
                    }
                }

                if (!interactors.isEmpty()) {
                    IRNetworks.ENERGY_NETWORK.get().addNodeAndUpdate(serverLevel, pos, directions, false, interactors, interactorConnections);
                } else if (direction0 != null && direction1 != null) {
                    IRNetworks.ENERGY_NETWORK.get().addConnection(serverLevel, pos, direction0, direction1);
                }
            } else {
                IRNetworks.ENERGY_NETWORK.get().addNodeAndUpdate(serverLevel, pos, directions, connectionsAmount == 1, interactors, interactorConnections);
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
        if (level instanceof ServerLevel serverLevel) {
            if (IRNetworks.ENERGY_NETWORK.get().checkForInteractorAt(serverLevel, blockPos, facingDirection)) {
                int connectionsAmount = 0;
                Direction[] directions = new Direction[6];
                for (Direction direction : Direction.values()) {
                    boolean value = blockState.getValue(CONNECTION[direction.get3DDataValue()]);
                    if (value) {
                        directions[direction.get3DDataValue()] = direction;
                        connectionsAmount++;
                    }
                }

                IRNetworks.ENERGY_NETWORK.get().addNodeAndUpdate(serverLevel, blockPos, directions, connectionsAmount == 1, Collections.singleton(facingBlockPos), Collections.singleton(facingDirection));
            }
        }
        return super.updateShape(blockState, facingDirection, facingBlockState, level, blockPos, facingBlockPos);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        super.onRemove(state, level, pos, newState, movedByPiston);

        if (level instanceof ServerLevel serverLevel) {
            if (!state.is(newState.getBlock())) {
                if (IRNetworks.ENERGY_NETWORK.get().hasNodeAt(serverLevel, pos)) {
                    IRNetworks.ENERGY_NETWORK.get().removeNodeAndUpdate(serverLevel, pos);
                } else {
                    List<Direction> directions = getDirections(state);
                    if (directions.size() == 2) {
                        Direction direction0 = directions.getFirst();
                        Direction direction1 = directions.get(1);
                        if (direction0 == direction1.getOpposite()) {
                            IRNetworks.ENERGY_NETWORK.get().removeConnection(serverLevel, pos, direction0, direction1);
                        }
                    }
                }

            }
        }

    }

    private static List<Direction> getDirections(BlockState state) {
        List<Direction> directions = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            if (state.getValue(CONNECTION[direction.get3DDataValue()])) {
                directions.add(direction);
            }
        }
        return directions;
    }

    public EnergyTier getEnergyTier() {
        return this.energyTier.get();
    }

    @Override
    public boolean canConnectToPipe(BlockState connectTo) {
        return connectTo.getBlock() instanceof CableBlock cableBlock && cableBlock.getEnergyTier() == this.getEnergyTier();
    }

    @Override
    public boolean canConnectTo(BlockEntity connectTo) {
        return CapabilityUtils.energyStorageCapability(connectTo) != null
                || CapabilityUtils.blockEntityCapability(Capabilities.EnergyStorage.BLOCK, connectTo) != null;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        TooltipUtils.addEnergyTierTooltip(tooltipComponents, this.getEnergyTier());

    }

}
