package com.indref.industrial_reforged.content.multiblocks;

import com.indref.industrial_reforged.api.multiblocks.IMultiblock;
import com.indref.industrial_reforged.api.multiblocks.MultiblockDirection;
import com.indref.industrial_reforged.content.IRBlocks;
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

public class FireBoxMultiblock implements IMultiblock {
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
                0, IRBlocks.REFACTORY_BRICK.get(),
                1, getController()
        );
    }

    @Override
    public @Nullable MultiblockDirection getFixedDirection() {
        return MultiblockDirection.SOUTH;
    }

    @Override
    public void formBlock(Level level, BlockPos blockPos, int index, int indexY) {
        BlockState currentBlock = level.getBlockState(blockPos);
        MultiblockHelper.setAndUpdate(level, blockPos, currentBlock, currentBlock.setValue(FireBoxMultiblock.FIREBOX_PART,
                FireBoxMultiblock.PartIndex.getPartIndexByIndices(index, indexY)));
    }

    public enum PartIndex implements StringRepresentable {
        UNFORMED("unformed", -1, -1),
        TOP_LEFT("top_left", 0, 0),
        TOP_MIDDLE("top_middle", 1, 0),
        TOP_RIGHT("top_right", 2, 0),
        MIDDLE_LEFT("middle_left", 3, 0),
        MIDDLE_MIDDLE("middle_middle", 4, 0),
        MIDDLE_RIGHT("middle_right", 5, 0),
        BOTTOM_LEFT("bottom_left", 6, 0),
        BOTTOM_MIDDLE("bottom_middle", 7, 0),
        BOTTOM_RIGHT("bottom_right", 8, 0);

        private final int index;
        private final int yIndex;
        private final String name;

        PartIndex(String name, int index, int yIndex) {
            this.name = name;
            this.index = index;
            this.yIndex = yIndex;
        }

        public static FireBoxMultiblock.PartIndex getPartIndexByIndices(int index, int yIndex) {
            for (FireBoxMultiblock.PartIndex part : FireBoxMultiblock.PartIndex.values()) {
                if (part != UNFORMED && part.index == index && part.yIndex == yIndex) {
                    return part;
                }
            }

            throw new IllegalStateException("Indexing error from partIndex enum. Multi: FireBoxMultiblock. Index: "+index+" yIndex:"+yIndex);
        }

        @Override
        public @NotNull String getSerializedName() {
            return name;
        }
    }
}
