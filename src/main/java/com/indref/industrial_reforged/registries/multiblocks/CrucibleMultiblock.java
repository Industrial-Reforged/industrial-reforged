package com.indref.industrial_reforged.registries.multiblocks;

import com.indref.industrial_reforged.api.multiblocks.IMultiblock;
import com.indref.industrial_reforged.api.multiblocks.MultiblockDirection;
import com.indref.industrial_reforged.api.tiers.CrucibleTiers;
import com.indref.industrial_reforged.api.tiers.templates.CrucibleTier;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.blocks.misc.CrucibleWallBlock;
import com.indref.industrial_reforged.util.MultiblockHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record CrucibleMultiblock(CrucibleTier tier) implements IMultiblock {
    public static final CrucibleMultiblock CERAMIC = new CrucibleMultiblock(CrucibleTiers.CERAMIC);

    @Override
    public Block getController() {
        return tier.getController();
    }

    @Override
    public List<List<Integer>> getLayout() {
        return List.of(
                List.of(
                        0, 0, 0,
                        0, 2, 0,
                        0, 0, 0
                ),
                List.of(
                        0, 0, 0,
                        0, 1, 0,
                        0, 0, 0
                )
        );
    }

    @Override
    public Map<Integer, @Nullable Block> getDefinition() {
        Map<Integer, Block> def = new HashMap<>();
        def.put(0, tier.getCrucibleWallBlock());
        def.put(1, null);
        def.put(2, tier.getController());
        return def;
    }

    @Override
    public @Nullable MultiblockDirection getFixedDirection() {
        return MultiblockDirection.NORTH;
    }

    @Override
    public void formBlock(Level level, MultiblockDirection direction, BlockPos blockPos, int index, int indexY) {
        BlockState currentBlock = level.getBlockState(blockPos);
        if (currentBlock.is(Blocks.TERRACOTTA)) {
            MultiblockHelper.setAndUpdate(level, blockPos, currentBlock, IRBlocks.CERAMIC_CRUCIBLE_WALL.get()
                    .defaultBlockState()
                    .setValue(CrucibleWallBlock.CRUCIBLE_WALL, switch (index) {
                        case 0, 2, 6, 8 -> switch (indexY) {
                            case 0 -> CrucibleWallBlock.WallStates.EDGE_BOTTOM;
                            case 1 -> CrucibleWallBlock.WallStates.EDGE_TOP;
                            default -> null;
                        };
                        case 1, 3, 5, 7 -> switch (indexY) {
                            case 0 -> CrucibleWallBlock.WallStates.WALL_BOTTOM;
                            case 1 -> CrucibleWallBlock.WallStates.WALL_TOP;
                            default -> null;
                        };
                        default -> CrucibleWallBlock.WallStates.WALL_TOP;
                    })
                    .setValue(CrucibleWallBlock.FACING, switch (index) {
                        case 0, 3 -> Direction.NORTH;
                        case 1, 2 -> Direction.EAST;
                        case 5, 8 -> Direction.SOUTH;
                        case 6, 7 -> Direction.WEST;
                        default -> null;
                    }));
        } else if (currentBlock.is(IRBlocks.TERRACOTTA_SLAB.get())) {
            MultiblockHelper.setAndUpdate(level, blockPos, currentBlock, IRBlocks.CERAMIC_CRUCIBLE_CONTROLLER.get().defaultBlockState());
        }
    }

    @Override
    public void unformBlock(Level level, BlockPos blockPos) {

    }
}
