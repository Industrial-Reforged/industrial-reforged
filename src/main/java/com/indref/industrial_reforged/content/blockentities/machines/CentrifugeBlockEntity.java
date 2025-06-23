package com.indref.industrial_reforged.content.blockentities.machines;

import com.google.common.collect.ImmutableMap;
import com.indref.industrial_reforged.IRConfig;
import com.indref.industrial_reforged.api.blockentities.MachineBlockEntity;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.content.recipes.CentrifugeRecipe;
import com.indref.industrial_reforged.content.gui.menus.CentrifugeMenu;
import com.indref.industrial_reforged.registries.IREnergyTiers;
import com.indref.industrial_reforged.registries.IRMachines;
import com.indref.industrial_reforged.translations.IRTranslations;
import com.indref.industrial_reforged.util.recipes.IngredientWithCount;
import com.portingdeadmods.portingdeadlibs.api.utils.IOAction;
import com.portingdeadmods.portingdeadlibs.utils.capabilities.SidedCapUtils;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.indref.industrial_reforged.util.Utils.ACTIVE;

// TODO: manual fluid insertion, extraction
public class CentrifugeBlockEntity extends MachineBlockEntity implements MenuProvider {
    private @Nullable CentrifugeRecipe recipe;

    public CentrifugeBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(IRMachines.CENTRIFUGE.getBlockEntityType(), p_155229_, p_155230_);
        addEuStorage(IREnergyTiers.LOW, IRConfig.centrifugeEnergyCapacity);
        addFluidTank(IRConfig.centrifugeFluidCapacity);
        addItemHandler(6, ((slot, itemStack) -> slot == 0
                || (slot == 5 && itemStack.getCapability(IRCapabilities.EnergyStorage.ITEM) != null)));
    }

    @Override
    public boolean supportsUpgrades() {
        return true;
    }

    public float getProgress() {
        return this.progress;
    }

    public float getMaxProgress() {
        return recipe != null ? recipe.duration() : 0;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return IRTranslations.Menus.CENTRIFUGE.component();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new CentrifugeMenu(i, inventory, this);
    }

    @Override
    protected void onItemsChanged(int slot) {
        super.onItemsChanged(slot);
        checkRecipe();
    }

    @Override
    public void onEuChanged(int oldAmount) {
        super.onEuChanged(oldAmount);
        if (getEnergyStorage() != null && getEnergyStorage().getEnergyStored() <= 0) {
            setActive(false);
        }
    }

    private void checkRecipe() {
        Optional<CentrifugeRecipe> currentRecipe = getCurrentRecipe();

        if (currentRecipe.isPresent()) {
            CentrifugeRecipe centrifugeRecipe = currentRecipe.get();
            List<ItemStack> results = centrifugeRecipe.results();
            int energy = centrifugeRecipe.energy();

            if (canInsertItems(results)
                    && getEuStorage().getEnergyStored() - energy >= 0
                    && forceFillTank(centrifugeRecipe.resultFluid().copy(), IFluidHandler.FluidAction.SIMULATE) == centrifugeRecipe.resultFluid().getAmount()) {
                this.recipe = centrifugeRecipe;
            }
        } else {
            this.recipe = null;
        }
    }

    @Override
    public void commonTick() {
        super.commonTick();

        if (this.getRedstoneSignalType().isActive(this.getRedstoneSignalStrength())) {
            if (recipe != null) {
                int energy = 1;
                int maxProgress = recipe.duration();
                IItemHandler itemHandler = getItemHandler();
                IEnergyStorage energyStorage = getEuStorage();

                List<ItemStack> results = recipe.results();
                IngredientWithCount ingredient = recipe.ingredient();
                setActive(true);

                if (this.progress >= maxProgress) {
                    for (ItemStack result : results) {
                        ItemStack toInsert = result.copy();
                        for (int j = 0; j < getItemHandler().getSlots(); j++) {
                            toInsert = forceInsertItem(j, toInsert, false);
                            if (toInsert.isEmpty()) {
                                break;
                            }
                        }
                    }
                    getFluidHandler().fill(recipe.resultFluid().copy(), IFluidHandler.FluidAction.EXECUTE);
                    itemHandler.extractItem(0, ingredient.count(), false);
                    resetRecipe();
                } else {
                    if (!level.isClientSide()) {
                        energyStorage.tryDrainEnergy(energy, false);
                        this.increaseProgress();
                    }
                }
            } else {
                this.resetRecipe();
            }
        } else {
            this.setActive(false);
        }
    }

    private void resetRecipe() {
        this.progress = 0;
        setActive(false);
    }

    private boolean canInsertItems(List<ItemStack> results) {
        IntList slots = new IntArrayList();
        IItemHandler itemHandler = getItemHandler();
        for (int slotIndex = 1; slotIndex < 5; slotIndex++) {
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
        IItemHandler itemHandler = getItemHandler();

        return level.getRecipeManager()
                .getRecipeFor(CentrifugeRecipe.TYPE, new SingleRecipeInput(itemHandler.getStackInSlot(0)), level)
                .map(RecipeHolder::value);
    }

    public void setActive(boolean active) {
        if (getBlockState().getValue(ACTIVE) != active) {
            level.setBlockAndUpdate(worldPosition, getBlockState().setValue(ACTIVE, active));
        }
    }

    @Override
    public int emitRedstoneLevel() {
        return ItemHandlerHelper.calcRedstoneFromInventory(this.getItemHandler());
    }

    @Override
    public void onLoad() {
        super.onLoad();

        this.checkRecipe();
    }

    @Override
    public <T> ImmutableMap<Direction, Pair<IOAction, int[]>> getSidedInteractions(BlockCapability<T, @Nullable Direction> capability) {
        if (capability == Capabilities.ItemHandler.BLOCK) {
            return ImmutableMap.of(
                    Direction.UP, Pair.of(IOAction.INSERT, new int[]{0}),
                    Direction.NORTH, Pair.of(IOAction.EXTRACT, new int[]{1, 2, 3, 4}),
                    Direction.EAST, Pair.of(IOAction.EXTRACT, new int[]{1, 2, 3, 4}),
                    Direction.SOUTH, Pair.of(IOAction.EXTRACT, new int[]{1, 2, 3, 4}),
                    Direction.WEST, Pair.of(IOAction.EXTRACT, new int[]{1, 2, 3, 4}),
                    Direction.DOWN, Pair.of(IOAction.EXTRACT, new int[]{1, 2, 3, 4})
            );
        } else if (capability == IRCapabilities.EnergyStorage.BLOCK) {
            return SidedCapUtils.allInsert(0);
        }
        return ImmutableMap.of();
    }

    @Override
    protected void loadData(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadData(tag, provider);
        this.progress = tag.getFloat("progress");
    }

    @Override
    protected void saveData(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveData(tag, provider);
        tag.putFloat("progress", this.progress);
    }

}
