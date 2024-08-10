package com.indref.industrial_reforged.registries.blockentities.multiblocks.controller;

import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.api.capabilities.heat.IHeatStorage;
import com.indref.industrial_reforged.api.tiers.FireboxTier;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.blocks.multiblocks.misc.RefractoryBrickBlock;
import com.indref.industrial_reforged.registries.gui.menus.FireBoxMenu;
import com.indref.industrial_reforged.tiers.FireboxTiers;
import com.indref.industrial_reforged.util.BlockUtils;
import com.indref.industrial_reforged.util.CapabilityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FireboxBlockEntity extends ContainerBlockEntity implements MenuProvider {
    private static final int INPUT_SLOT = 0;

    private int burnTime;
    private int maxBurnTime;
    private final FireboxTier fireboxTier;

    public FireboxBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, FireboxTier fireboxTier, int heatCapacity) {
        super(blockEntityType, blockPos, blockState);
        addItemHandler(1, (slot, itemStack) -> itemStack.getBurnTime(RecipeType.SMELTING) > 0);
        addHeatStorage(heatCapacity);
        this.fireboxTier = fireboxTier;
    }

    public FireboxBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(IRBlockEntityTypes.FIREBOX.get(), blockPos, blockState, FireboxTiers.REFRACTORY, 4000);
    }

    public boolean isActive() {
        return this.burnTime > 0;
    }

    @Override
    public void onItemsChanged(int slot) {
        IItemHandler itemHandler = CapabilityUtils.itemHandlerCapability(this);
        if (itemHandler != null) {
            ItemStack stack = itemHandler.getStackInSlot(slot);
            int burnTime = stack.getBurnTime(RecipeType.SMELTING);
            if (burnTime > 0 && this.burnTime <= 0) {
                this.burnTime = burnTime;
                this.maxBurnTime = burnTime;
                stack.shrink(1);
                setBlockActive(true);
            }
        }
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Firebox");
    }

    public void commonTick() {
        IItemHandler itemHandler = CapabilityUtils.itemHandlerCapability(this);
        IHeatStorage heatStorage = CapabilityUtils.heatStorageCapability(this);
        if (heatStorage != null) {
            if (this.burnTime > 0) {
                burnTime--;
                if (burnTime % 5 == 0) {
                    heatStorage.tryFillHeat(1, false);
                }
            } else {
                this.maxBurnTime = 0;
                ItemStack stack = itemHandler.getStackInSlot(INPUT_SLOT);
                int burnTime = stack.getBurnTime(RecipeType.SMELTING);
                if (burnTime > 0) {
                    this.burnTime = burnTime;
                    this.maxBurnTime = burnTime;
                    stack.shrink(1);
                    setBlockActive(true);
                } else {
                    setBlockActive(false);
                }
            }
        }
    }

    public void setBlockActive(boolean value) {
        for (BlockPos pos : BlockUtils.getBlocksAroundSelfHorizontal(worldPosition)) {
            BlockState blockState = level.getBlockState(pos);
            if (blockState.hasProperty(RefractoryBrickBlock.HATCH_ACTIVE)) {
                level.setBlockAndUpdate(pos, blockState.setValue(RefractoryBrickBlock.HATCH_ACTIVE, value));
            }
        }
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
        return new FireBoxMenu(containerId, inventory, this);
    }

    @Override
    protected void saveData(CompoundTag pTag, HolderLookup.Provider provider) {
        pTag.putInt("burnTime", burnTime);
        pTag.putInt("maxBurnTime", maxBurnTime);
    }

    @Override
    protected void loadData(CompoundTag pTag, HolderLookup.Provider provider) {
        burnTime = pTag.getInt("burnTime");
        maxBurnTime = pTag.getInt("maxBurnTime");
    }

    public int getBurnTime() {
        return burnTime;
    }

    public int getMaxBurnTime() {
        return maxBurnTime;
    }

    public FireboxTier getFireboxTier() {
        return fireboxTier;
    }
}
