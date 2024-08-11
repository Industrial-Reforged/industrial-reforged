package com.indref.industrial_reforged.registries.multiblocks;

import com.indref.industrial_reforged.api.multiblocks.MultiblockDirection;
import com.indref.industrial_reforged.api.tiers.FireboxTier;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.controller.FireboxBlockEntity;
import com.indref.industrial_reforged.util.BlockUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

public record FireboxMultiblock(FireboxTier tier) implements IFireboxMultiblock {
    public static final EnumProperty<FireboxMultiblock.PartIndex> FIREBOX_PART = EnumProperty.create("firebox_part", FireboxMultiblock.PartIndex.class);

    @Override
    public Block getController() {
        return IRBlocks.COIL.get();
    }

    @Override
    public int[][] getLayout() {
        return new int[][]{
                {
                        0, 0, 0,
                        0, 1, 0,
                        0, 0, 0
                }
        };
    }

    @Override
    public Map<Integer, Block> getDefinition() {
        return Map.of(
                0, IRBlocks.REFRACTORY_BRICK.get(),
                1, getController()
        );
    }

    @Override
    public Optional<MultiblockDirection> getFixedDirection() {
        return Optional.of(MultiblockDirection.SOUTH);
    }

    @Override
    public boolean isFormed(Level level, BlockPos blockPos, BlockPos controllerPos) {
        BlockState blockState = level.getBlockState(blockPos);

        if (!getDefinition().containsValue(blockState.getBlock())) return false;

        return !blockState.getValue(FIREBOX_PART).equals(PartIndex.UNFORMED);
    }

    @Override
    public Optional<BlockState> formBlock(Level level, MultiblockDirection direction, BlockPos blockPos, BlockPos controllerPos, int index, int indexY, Player player) {
        BlockState currentBlock = level.getBlockState(blockPos);
        if (currentBlock.is(IRBlocks.REFRACTORY_BRICK.get()) || currentBlock.is(IRBlocks.COIL.get())) {
            return Optional.of(currentBlock.setValue(FireboxMultiblock.FIREBOX_PART,
                    switch (index) {
                        case 1, 3, 5, 7 -> PartIndex.HATCH;
                        case 4 -> PartIndex.COIL;
                        default -> PartIndex.BRICK;
                    }));
        }
        return Optional.empty();
    }

    @Override
    public FireboxTier getTier() {
        return tier;
    }

    @Override
    public Optional<BlockPos> getControllerPos(BlockPos multiblockPos, Level level) {
        if (level.getBlockEntity(multiblockPos) instanceof FireboxBlockEntity)
            return Optional.of(multiblockPos);

        for (BlockPos pos : BlockUtils.getBlocksAroundSelf3x3(multiblockPos)) {
            if (level.getBlockEntity(pos) instanceof FireboxBlockEntity) {
                return Optional.of(pos);
            }
        }
        return Optional.empty();
    }

    public enum PartIndex implements StringRepresentable {
        UNFORMED("unformed"),
        BRICK("brick"),
        HATCH("hatch"),
        COIL("coil");
        private final String name;

        PartIndex(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return name;
        }
    }
}
