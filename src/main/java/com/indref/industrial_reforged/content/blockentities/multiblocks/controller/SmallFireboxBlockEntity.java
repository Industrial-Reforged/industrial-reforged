package com.indref.industrial_reforged.content.blockentities.multiblocks.controller;

import com.indref.industrial_reforged.api.blockentities.multiblock.FakeBlockEntity;
import com.indref.industrial_reforged.api.blockentities.multiblock.SavesControllerPosBlockEntity;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.tiers.FireboxTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class SmallFireboxBlockEntity extends FireboxBlockEntity implements FakeBlockEntity, SavesControllerPosBlockEntity {
    private BlockPos mainControllerPos;
    private UUID uuid;

    public SmallFireboxBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(IRBlockEntityTypes.SMALL_FIREBOX.get(), p_155229_, p_155230_, FireboxTiers.SMALL, 1800);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Small Firebox");
    }

    @Override
    public boolean actualBlockEntity() {
        return worldPosition.equals(mainControllerPos);
    }

    @Override
    public @Nullable BlockPos getActualBlockEntityPos() {
        return mainControllerPos;
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
        BlockPos controllerPos = getActualBlockEntityPos();
        if (controllerPos != null) {
            tag.putLong("mainControllerPos", controllerPos.asLong());
        }
    }

    @Override
    protected void loadData(CompoundTag tag, HolderLookup.Provider provider) {
        long mainControllerPos1 = tag.getLong("mainControllerPos");
        this.mainControllerPos = BlockPos.of(mainControllerPos1);
    }
}
