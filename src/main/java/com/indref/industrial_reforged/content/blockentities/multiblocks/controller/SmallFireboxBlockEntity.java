package com.indref.industrial_reforged.content.blockentities.multiblocks.controller;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.heat.IHeatStorage;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.tiers.FireboxTiers;
import com.portingdeadmods.portingdeadlibs.api.blockentities.multiblocks.FakeBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.blockentities.multiblocks.SavesControllerPosBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SmallFireboxBlockEntity extends FireboxBlockEntity implements FakeBlockEntity, SavesControllerPosBlockEntity {
    private BlockPos mainControllerPos;
    private BlockCapabilityCache<IHeatStorage, Direction> aboveCapCache;

    public SmallFireboxBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(IRBlockEntityTypes.SMALL_FIREBOX.get(), p_155229_, p_155230_, FireboxTiers.SMALL, 1800);
    }

    @Override
    public void onLoad() {
        if (level instanceof ServerLevel serverLevel) {
            this.aboveCapCache = BlockCapabilityCache.create(IRCapabilities.HeatStorage.BLOCK, serverLevel, worldPosition.above(), Direction.DOWN);
            IndustrialReforged.LOGGER.debug("cap cache: {}", this.aboveCapCache.getCapability());
        }
        super.onLoad();
    }

    @Override
    public void commonTick() {
        if (actualBlockEntity()) {
            tickRecipe();
        }

        tickIO();
    }

    @Override
    protected void tickIO() {
        if (!level.isClientSide()) {
            // Only export heat to block directly above
            BlockPos abovePos = worldPosition.above();
            if (aboveCapCache != null) {
                IHeatStorage aboveHeatStorage = aboveCapCache.getCapability();
                if (aboveHeatStorage != null && level != null) {
                    IHeatStorage thisHeatStorage = getHeatStorage();
                    int output = Math.min(thisHeatStorage.getMaxOutput(), aboveHeatStorage.getMaxInput());
                    int drained = thisHeatStorage.tryDrainHeat(output, true);
                    thisHeatStorage.tryDrainHeat(drained, false);
                    if (aboveHeatStorage.tryFillHeat(drained, false) != 0) {
                        level.invalidateCapabilities(abovePos);
                    }
                }
            }
        }
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
        super.saveData(tag, provider);
        BlockPos controllerPos = getActualBlockEntityPos();
        if (controllerPos != null) {
            tag.putLong("mainControllerPos", controllerPos.asLong());
        }
    }

    @Override
    protected void loadData(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadData(tag, provider);
        long mainControllerPos1 = tag.getLong("mainControllerPos");
        this.mainControllerPos = BlockPos.of(mainControllerPos1);
    }
}
