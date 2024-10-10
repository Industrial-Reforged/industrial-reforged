package com.indref.industrial_reforged.registries.multiblocks;

import com.indref.industrial_reforged.api.blockentities.multiblock.MultiblockEntity;
import com.indref.industrial_reforged.api.multiblocks.MultiblockData;
import com.indref.industrial_reforged.api.multiblocks.MultiblockLayer;
import com.indref.industrial_reforged.api.util.HorizontalDirection;
import com.indref.industrial_reforged.api.tiers.FireboxTier;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.controller.FireboxBlockEntity;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.part.FireboxPartBlockEntity;
import com.indref.industrial_reforged.util.MultiblockHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record FireboxMultiblock(FireboxTier tier) implements IFireboxMultiblock {
    public static final EnumProperty<PartIndex> FIREBOX_PART = EnumProperty.create("firebox_part", PartIndex.class);

    @Override
    public Block getUnformedController() {
        return IRBlocks.COIL.get();
    }

    @Override
    public Block getFormedController() {
        return IRBlocks.FIREBOX_CONTROLLER.get();
    }

    public Block getUnformedPart() {
        return IRBlocks.REFRACTORY_BRICK.get();
    }

    public Block getFormedPart() {
        return IRBlocks.FIREBOX_PART.get();
    }

    @Override
    public MultiblockLayer[] getLayout() {
        return new MultiblockLayer[]{
                layer(
                        0, 0, 0,
                        0, 1, 0,
                        0, 0, 0
                )
        };
    }

    @Override
    public Int2ObjectMap<Block> getDefinition() {
        Int2ObjectMap<Block> def = new Int2ObjectOpenHashMap<>();
        def.put(0, getUnformedPart());
        def.put(1, getUnformedController());
        return def;
    }

    @Override
    public BlockEntityType<? extends MultiblockEntity> getMultiBlockEntityType() {
        return IRBlockEntityTypes.FIREBOX.get();
    }

    @Override
    public HorizontalDirection getFixedDirection() {
        return HorizontalDirection.NORTH;
    }

    @Override
    public boolean isFormed(Level level, BlockPos blockPos) {
        BlockState blockState = level.getBlockState(blockPos);

        if (blockState.is(getUnformedController()) || blockState.is(getUnformedPart()) || getDefinition().containsValue(blockState.getBlock())) return false;

        return blockState.hasProperty(FIREBOX_PART);
    }

    @Override
    public @Nullable BlockState formBlock(Level level, BlockPos blockPos, BlockPos controllerPos, int layerIndex, int layoutIndex, MultiblockData multiblockData, @Nullable Player player) {
        if (layerIndex == 4) {
            return getFormedController().defaultBlockState().setValue(FIREBOX_PART, PartIndex.COIL);
        } else if (layerIndex >= 0 && layerIndex <= 8) {
            return getFormedPart().defaultBlockState().setValue(FIREBOX_PART, layerIndex % 2 == 0 ? PartIndex.BRICK : PartIndex.HATCH);
        }
        return null;
    }

    @Override
    public FireboxTier getTier() {
        return tier;
    }

    @Override
    public @Nullable BlockPos getControllerPos(BlockPos multiblockPos, Level level) {
        return switch (level.getBlockEntity(multiblockPos)) {
            case FireboxBlockEntity ignored -> multiblockPos;
            case FireboxPartBlockEntity fireboxPart -> fireboxPart.getControllerPos();
            case null, default -> null;
        };
    }

    public enum PartIndex implements StringRepresentable {
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
