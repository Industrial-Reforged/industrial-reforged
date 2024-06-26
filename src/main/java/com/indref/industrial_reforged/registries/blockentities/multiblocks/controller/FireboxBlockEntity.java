package com.indref.industrial_reforged.registries.blockentities.multiblocks.controller;

import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.api.tiers.FireboxTier;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.screen.FireBoxMenu;
import com.indref.industrial_reforged.tiers.FireboxTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FireboxBlockEntity extends ContainerBlockEntity implements MenuProvider {
    private static final int INPUT_SLOT = 0;

    private int burnTime;
    private int maxBurnTime;
    private final ContainerData data;
    private final FireboxTier fireboxTier;

    public FireboxBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, FireboxTier fireboxTier, int heatCapacity) {
        super(blockEntityType, blockPos, blockState);
        addItemHandler(1, (slot, itemStack) -> itemStack.getBurnTime(RecipeType.SMELTING) > 0);
        addHeatStorage(heatCapacity);
        this.fireboxTier = fireboxTier;
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> burnTime;
                    case 1 -> maxBurnTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> burnTime = pValue;
                    case 1 -> maxBurnTime = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public FireboxBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(IRBlockEntityTypes.FIREBOX.get(), blockPos, blockState, FireboxTiers.REFRACTORY, 4000);
    }

    public boolean isActive() {
        return this.burnTime > 0;
    }

    @Override
    public void onItemsChanged(int slot) {
        ItemStack stack = getItemHandler().get().getStackInSlot(slot);
        int burnTime = stack.getBurnTime(RecipeType.SMELTING);
        if (burnTime > 0 && this.burnTime <= 0) {
            this.burnTime = burnTime;
            this.maxBurnTime = burnTime;
            stack.shrink(1);
        }
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Firebox");
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if (getItemHandler().isEmpty()) return;

        if (this.burnTime > 0) {
            burnTime--;
            if (burnTime % 10 == 0) {
                this.setHeatStored(getHeatStored()+1);
            }
        } else {
            this.maxBurnTime = 0;
            ItemStack stack = getItemHandler().get().getStackInSlot(INPUT_SLOT);
            int burnTime = stack.getBurnTime(RecipeType.SMELTING);
            if (burnTime > 0) {
                this.burnTime = burnTime;
                this.maxBurnTime = burnTime;
                stack.shrink(1);
            }
        }
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
        return new FireBoxMenu(containerId, inventory, this, this.data);
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
