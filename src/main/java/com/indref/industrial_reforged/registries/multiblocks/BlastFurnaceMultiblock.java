package com.indref.industrial_reforged.registries.multiblocks;

import com.indref.industrial_reforged.api.multiblocks.Multiblock;
import com.indref.industrial_reforged.api.multiblocks.MultiblockDirection;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.util.MultiblockUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public record BlastFurnaceMultiblock() implements Multiblock {
    public static final EnumProperty<BrickStates> BRICK_STATE = EnumProperty.create("brick_state", BlastFurnaceMultiblock.BrickStates.class);

    @Override
    public Block getController() {
        return IRBlocks.BLAST_FURNACE_HATCH.get();
    }

    @Override
    public List<List<Integer>> getLayout() {
        return MultiblockUtils.singleBlockMultiblock(List.of(
                0,
                0,
                1
        ));
    }

    @Override
    public Map<Integer, @Nullable Block> getDefinition() {
        return Map.of(0, IRBlocks.BLAST_FURNACE_BRICKS.get(), 1, IRBlocks.BLAST_FURNACE_HATCH.get());
    }

    @Override
    public void formBlock(Level level, MultiblockDirection direction, BlockPos blockPos, BlockPos controllerPos, int index, int indexY) {

    }

    @Override
    public void unformBlock(Level level, BlockPos blockPos, BlockPos controllerPos) {

    }

    public enum BrickStates implements StringRepresentable {
        UNFORMED("unformed"),
        FORMED("formed"),
        HATCH_LEFT("hatch_left"),
        HATCH_RIGHT("hatch_right"),
        TOP("top");

        private final String name;

        BrickStates(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return name;
        }
    }
}
