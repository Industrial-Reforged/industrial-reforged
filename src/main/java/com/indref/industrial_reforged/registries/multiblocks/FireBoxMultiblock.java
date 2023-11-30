package com.indref.industrial_reforged.registries.multiblocks;

import com.indref.industrial_reforged.api.multiblocks.IMultiblock;
import com.indref.industrial_reforged.api.multiblocks.MultiblockDirection;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.util.MultiblockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public record FireBoxMultiblock() implements IMultiblock {
    public static final FireBoxMultiblock INSTANCE = new FireBoxMultiblock();
    public static final EnumProperty<FireBoxMultiblock.PartIndex> FIREBOX_PART = EnumProperty.create("firebox_part", FireBoxMultiblock.PartIndex.class);

    @Override
    public Block getController() {
        return IRBlocks.COIL.get();
    }

    @Override
    public List<List<Integer>> getLayout() {
        return List.of(
                List.of(
                        0, 0, 0,
                        0, 1, 0,
                        0, 0, 0
                )
        );
    }

    @Override
    public Map<Integer, Block> getDefinition() {
        return Map.of(
                0, IRBlocks.REFRACTORY_BRICK.get(),
                1, getController()
        );
    }

    @Override
    public @Nullable MultiblockDirection getFixedDirection() {
        return MultiblockDirection.SOUTH;
    }

    @Override
    public void formBlock(Level level, MultiblockDirection direction, BlockPos blockPos, int index, int indexY) {
        BlockState currentBlock = level.getBlockState(blockPos);
        MultiblockHelper.setAndUpdate(level, blockPos, currentBlock, currentBlock.setValue(FireBoxMultiblock.FIREBOX_PART,
                switch (index) {
                    case 1, 3, 5, 7 -> PartIndex.HATCH;
                    case 4 -> PartIndex.COIL;
                    default -> PartIndex.UNFORMED;
                }));
    }

    @Override
    public void unformBlock(Level level, BlockPos blockPos) {
        BlockState currentBlock = level.getBlockState(blockPos);
        MultiblockHelper.setAndUpdate(level, blockPos, currentBlock, currentBlock.setValue(FireBoxMultiblock.FIREBOX_PART,
                PartIndex.UNFORMED));
    }

    public enum PartIndex implements StringRepresentable {
        UNFORMED("unformed"),
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
