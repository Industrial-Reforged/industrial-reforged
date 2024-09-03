package com.indref.industrial_reforged.registries.blockentities.machines;

import com.google.common.collect.ImmutableMap;
import com.indref.industrial_reforged.api.blockentities.machine.MachineBlockEntity;
import com.indref.industrial_reforged.api.capabilities.IOActions;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.blocks.machines.CentrifugeBlock;
import com.indref.industrial_reforged.registries.recipes.CentrifugeRecipe;
import com.indref.industrial_reforged.registries.gui.menus.CentrifugeMenu;
import com.indref.industrial_reforged.tiers.EnergyTiers;
import com.indref.industrial_reforged.util.capabilities.CapabilityUtils;
import com.indref.industrial_reforged.util.recipes.IngredientWithCount;
import com.indref.industrial_reforged.util.recipes.recipeInputs.ItemRecipeInput;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CentrifugeBlockEntity extends MachineBlockEntity implements MenuProvider {
    private int duration;
    private int maxDuration;

    public CentrifugeBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(IRBlockEntityTypes.CENTRIFUGE.get(), p_155229_, p_155230_);
        addEnergyStorage(EnergyTiers.LOW);
        addItemHandler(6, ((slot, itemStack) -> slot == 0
                || (slot == 5 && itemStack.getCapability(IRCapabilities.EnergyStorage.ITEM) != null)));
    }

    public int getProgress() {
        return duration;
    }

    public int getMaxProgress() {
        return maxDuration;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Centrifuge");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new CentrifugeMenu(i, inventory, this, new SimpleContainerData(6));
    }

    @Override
    public void commonTick() {
        super.commonTick();
        Optional<CentrifugeRecipe> optionalRecipe = getCurrentRecipe();

        if (optionalRecipe.isPresent()) {
            CentrifugeRecipe recipe = optionalRecipe.get();
            int energy = recipe.energy();
            int maxDuration = recipe.duration();
            this.maxDuration = maxDuration;
            IItemHandler itemHandler = CapabilityUtils.itemHandlerCapability(this);
            IEnergyStorage energyStorage = CapabilityUtils.energyStorageCapability(this);

            List<ItemStack> results = recipe.results();
            IngredientWithCount ingredient = recipe.ingredient();
            if (canInsertItems(results) && energyStorage.getEnergyStored() - energy >= 0) {
                setActive(true);
                energyStorage.tryDrainEnergy(energy, false);
                if (this.duration >= maxDuration) {
                    for (int i = 0; i < results.size(); i++) {
                        ItemStack result = results.get(i).copy();
                        ItemStack prev = itemHandler.getStackInSlot(i + 1);
                        result.grow(prev.getCount());

                        getItemStackHandler().setStackInSlot(i + 1, result);
                    }
                    itemHandler.extractItem(0, ingredient.count(), false);
                    resetRecipe();
                } else {
                    this.duration++;
                }
            } else {
                resetRecipe();
            }
        }
    }

    private void resetRecipe() {
        this.duration = 0;
        this.maxDuration = 0;
        setActive(false);
    }

    // TODO: Expensive operation, cache this
    private boolean canInsertItems(List<ItemStack> results) {
        IntList slots = new IntArrayList();
        IItemHandler itemHandler = CapabilityUtils.itemHandlerCapability(this);
        for (int slotIndex = 0; slotIndex < itemHandler.getSlots(); slotIndex++) {
            ItemStack slot = itemHandler.getStackInSlot(slotIndex);
            for (ItemStack item : results) {
                if (slot.isEmpty() || (slot.is(item.getItem()) && slot.getCount() + item.getCount() <= item.getMaxStackSize())) {
                    slots.add(slotIndex);
                }
            }
        }
        return slots.size() >= results.size();
    }

    public Optional<CentrifugeRecipe> getCurrentRecipe() {
        IItemHandler itemHandler = CapabilityUtils.itemHandlerCapability(this);

        return level.getRecipeManager()
                .getRecipeFor(CentrifugeRecipe.TYPE, new ItemRecipeInput(Collections.singletonList(itemHandler.getStackInSlot(0))), level)
                .map(RecipeHolder::value);
    }

    public void setActive(boolean active) {
        if (getBlockState().getValue(CentrifugeBlock.ACTIVE) != active) {
            level.setBlockAndUpdate(worldPosition, getBlockState().setValue(CentrifugeBlock.ACTIVE, active));
        }
    }

    @Override
    protected void saveData(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveData(tag, provider);
        tag.putInt("duration", duration);
        tag.putInt("max_duration", maxDuration);
    }

    @Override
    public <T> ImmutableMap<Direction, Pair<IOActions, int[]>> getSidedInteractions(BlockCapability<T, @Nullable Direction> capability) {
        if (capability == Capabilities.ItemHandler.BLOCK) {
            return ImmutableMap.of(
                    Direction.UP, Pair.of(IOActions.INSERT, new int[]{0}),
                    Direction.NORTH, Pair.of(IOActions.EXTRACT, new int[]{1, 2, 3, 4}),
                    Direction.EAST, Pair.of(IOActions.EXTRACT, new int[]{1, 2, 3, 4}),
                    Direction.SOUTH, Pair.of(IOActions.EXTRACT, new int[]{1, 2, 3, 4}),
                    Direction.WEST, Pair.of(IOActions.EXTRACT, new int[]{1, 2, 3, 4}),
                    Direction.DOWN, Pair.of(IOActions.EXTRACT, new int[]{1, 2, 3, 4})
            );
        }
        return ImmutableMap.of();
    }

    @Override
    protected void loadData(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadData(tag, provider);
        this.duration = tag.getInt("duration");
        this.maxDuration = tag.getInt("max_duration");
    }
}
