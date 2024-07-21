package com.indref.industrial_reforged.registries.blockentities.multiblocks.controller;

import com.indref.industrial_reforged.api.multiblocks.FakeBlockEntity;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.tiers.FireboxTiers;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class SmallFireboxBlockEntity extends FireboxBlockEntity implements FakeBlockEntity {
    public SmallFireboxBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(IRBlockEntityTypes.SMALL_FIREBOX.get(), p_155229_, p_155230_, FireboxTiers.SMALL, 1800);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Small Firebox");
    }

    @Override
    public Optional<BlockPos> getActualBlockEntityPos() {
        return Optional.empty();
    }

    @Override
    public ObjectSet<BlockPos> getFakePositions() {
        return ObjectSet.of();
    }

    @Override
    public void setBlockActive(boolean value) {
    }
}
