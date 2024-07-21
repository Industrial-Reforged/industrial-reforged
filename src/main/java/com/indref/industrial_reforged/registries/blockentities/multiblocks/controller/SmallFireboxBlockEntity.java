package com.indref.industrial_reforged.registries.blockentities.multiblocks.controller;

import com.indref.industrial_reforged.api.multiblocks.FakeBlockEntity;
import com.indref.industrial_reforged.api.multiblocks.SavesControllerPos;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.tiers.FireboxTiers;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class SmallFireboxBlockEntity extends FireboxBlockEntity implements FakeBlockEntity, SavesControllerPos {
    private BlockPos mainControllerPos;

    public SmallFireboxBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(IRBlockEntityTypes.SMALL_FIREBOX.get(), p_155229_, p_155230_, FireboxTiers.SMALL, 1800);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Small Firebox");
    }

    public boolean isMainController() {
        return worldPosition.equals(mainControllerPos);
    }

    @Override
    public Optional<BlockPos> getActualBlockEntityPos() {
        return Optional.ofNullable(mainControllerPos);
    }

    @Override
    public void setBlockActive(boolean value) {
    }

    @Override
    public void setControllerPos(BlockPos blockPos) {
        this.mainControllerPos = blockPos;
    }

    @Override
    protected void saveData(CompoundTag tag, HolderLookup.Provider provider) {
        getActualBlockEntityPos().ifPresent(pos -> tag.putLong("mainControllerPos", pos.asLong()));
    }

    @Override
    protected void loadData(CompoundTag tag, HolderLookup.Provider provider) {
        this.mainControllerPos = BlockPos.of(tag.getLong("mainControllerPos"));
    }
}
