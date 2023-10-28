package com.indref.industrial_reforged.test;

import com.indref.industrial_reforged.api.multiblocks.IMultiblock;
import com.indref.industrial_reforged.content.IRBlocks;
import com.indref.industrial_reforged.util.MultiblockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class TestMultiblock implements IMultiblock {
    public static final EnumProperty<PartIndex> TEST_PART = EnumProperty.create("test_part", PartIndex.class);
    @Override
    public Block getController() {
        return IRBlocks.TEST_CONTROLLER.get();
    }

    @Override
    public List<List<Integer>> getLayout() {
        return List.of(
                List.of(
                        0, 0, 0,
                        0, 0, 0,
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
    public Map<Integer, Block> getDefinition() {
        // Giving the numbers from the layout a purpose
        return Map.of(
                0, IRBlocks.TEST_PART.get(),
                1, IRBlocks.TEST_CONTROLLER.get()
        );
    }

    @Override
    public void formBlock(Level level, BlockPos blockPos, int index, int indexY) {
        MultiblockHelper.setAndUpdate(level, blockPos, level.getBlockState(blockPos), level.getBlockState(blockPos)
                .setValue(TestMultiblock.TEST_PART, TestMultiblock.PartIndex.getPartIndexByIndices(0, 0)));
    }

    public enum PartIndex implements StringRepresentable {
        UNFORMED("unformed", -1, -1),
        FORMED("formed", 0, 0);
        private final int index;
        private final int yIndex;
        private final String name;

        PartIndex(String name, int index, int yIndex) {
            this.name = name;
            this.index = index;
            this.yIndex = yIndex;
        }

        public static PartIndex getPartIndexByIndices(int index, int yIndex) {
            for (PartIndex part : PartIndex.values()) {
                if (part != UNFORMED && part.index == index && part.yIndex == yIndex) {
                    return part;
                }
            }
            throw new IllegalStateException("SUS error form enum indexing");
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
