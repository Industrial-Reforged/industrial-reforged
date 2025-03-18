package com.indref.industrial_reforged.content.blockentities;

import com.google.common.collect.ImmutableMap;
import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blockentities.container.IRContainerBlockEntity;
import com.indref.industrial_reforged.data.IRDataMaps;
import com.indref.industrial_reforged.data.maps.CastingMoldValue;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.content.recipes.CrucibleCastingRecipe;
import com.indref.industrial_reforged.util.recipes.recipeInputs.CrucibleCastingRecipeInput;
import com.portingdeadmods.portingdeadlibs.api.capabilities.DynamicFluidTank;
import com.portingdeadmods.portingdeadlibs.api.utils.IOAction;
import com.portingdeadmods.portingdeadlibs.utils.RegistryUtils;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CastingBasinBlockEntity extends IRContainerBlockEntity {
    public static final int CAST_SLOT = 0;

    public int duration;
    public int maxDuration;

    private CrucibleCastingRecipe recipe;

    public CastingBasinBlockEntity(BlockPos pos, BlockState state) {
        super(IRBlockEntityTypes.CASTING_BASIN.get(), pos, state);
        addItemHandler(
                2,
                slot -> slot == 0 ? 1 : getItemHandler().getStackInSlot(0).getMaxStackSize(),
                (slot, item) -> slot == 0 && getMold(item.getItem()) != null
        );
        addFluidTank(0);
    }

    @Override
    protected void onItemsChanged(int slot) {
        super.onItemsChanged(slot);
        updateRecipe(true);
        if (slot == 0) {
            CastingMoldValue moldValue = getMold(getItemHandler().getStackInSlot(slot).getItem());
            if (moldValue != null) {
                getFluidTank().setCapacity(moldValue.capacity());
                update();
            } else {
                getFluidTank().setCapacity(0);
                update();
            }
        }
    }

    public boolean hasMoldAndEmpty() {
        return !getItemHandler().getStackInSlot(0).isEmpty() && getItemHandler().getStackInSlot(1).isEmpty() && getFluidTank().getFluid().isEmpty();
    }

    public @Nullable CastingMoldValue getMold(Item item) {
        return RegistryUtils.holder(BuiltInRegistries.ITEM, item).getData(IRDataMaps.CASTING_MOLDS);
    }

    @Override
    public void onFluidChanged() {
        super.onFluidChanged();
        updateRecipe(false);
    }

    public void onClientFluidChanged(int fluidAmount) {
        updateRecipe(false, fluidAmount);
    }

    public void commonTick() {
        if (recipe != null) {
            increaseCraftingProgress();
            setChanged();

            if (hasProgressFinished()) {
                castItem();
            }
        } else {
            resetProgress();
        }
    }

    public void castItem() {
        DynamicFluidTank fluidTank = getFluidTank();
        ItemStackHandler itemHandler = getItemStackHandler();

        if (this.recipe != null) {
            Item moldItem = this.recipe.moldItem();
            ItemStack resultItem1 = this.recipe.resultStack().copy();

            fluidTank.drain(this.recipe.fluidStack().getAmount(), IFluidHandler.FluidAction.EXECUTE);

            // this.recipe is null from here on
            CastingMoldValue moldValue = getMold(moldItem);
            if (moldValue.consumeCast()) {
                itemHandler.setStackInSlot(CAST_SLOT, ItemStack.EMPTY);
            }

            forceInsertItem(1, resultItem1, false);
            resetProgress();
        }
    }

    public void increaseCraftingProgress() {
        this.duration++;
    }

    public void resetProgress() {
        this.duration = 0;
        this.maxDuration = 0;
    }

    public int getDuration() {
        return duration;
    }

    public int getMaxDuration() {
        return maxDuration;
    }

    public boolean hasProgressFinished() {
        return this.recipe != null && this.duration >= this.recipe.getDuration();
    }

    public void updateRecipe(boolean itemsChanged) {
        updateRecipe(itemsChanged, getFluidTank().getFluidAmount());
    }

    public void updateRecipe(boolean itemsChanged, int fluidAmount) {
        Optional<CrucibleCastingRecipe> recipe = getCurrentRecipe(fluidAmount);
        if (itemsChanged) {
            IndustrialReforged.LOGGER.debug("client: {}, recipe present: {}", level.isClientSide(), recipe.isPresent());
        }

        boolean canInsert = recipe.filter(crucibleCastingRecipe ->
                canInsertIntoOutput(crucibleCastingRecipe.getResultItem(level.registryAccess()))).isPresent();

        if (canInsert) {
            this.recipe = recipe.get();
            if (itemsChanged) {
                int capacity = this.recipe.fluidStack().getAmount();
                IndustrialReforged.LOGGER.debug("Set Capacity: {}", capacity);
            }
        } else {
            this.recipe = null;
        }
    }

    private Optional<CrucibleCastingRecipe> getCurrentRecipe(int fluidAmount) {
        ItemStack moltItem = this.getItemHandler().getStackInSlot(CAST_SLOT);

        Optional<CrucibleCastingRecipe> recipe = this.level.getRecipeManager()
                .getRecipeFor(CrucibleCastingRecipe.TYPE, new CrucibleCastingRecipeInput(moltItem, getFluidTank().getFluidInTank(0).copyWithAmount(fluidAmount)), level)
                .map(RecipeHolder::value);

        if (recipe.isEmpty()) return Optional.empty();

        this.maxDuration = recipe.get().getDuration();

        return recipe;
    }

    private boolean canInsertIntoOutput(ItemStack outputItem) {
        return forceInsertItem(1, outputItem, true).isEmpty();
    }

    public CrucibleCastingRecipe getRecipe() {
        return this.recipe;
    }

    @Override
    protected void saveData(CompoundTag tag, HolderLookup.Provider provider) {
        tag.putInt("duration", this.duration);
        tag.putInt("maxDuration", this.maxDuration);
    }

    @Override
    public <T> ImmutableMap<Direction, Pair<IOAction, int[]>> getSidedInteractions(BlockCapability<T, @Nullable Direction> capability) {
        if (capability == Capabilities.FluidHandler.BLOCK) {
            return ImmutableMap.of(
                    Direction.UP, Pair.of(IOAction.INSERT, new int[]{0})
            );
        } else if (capability == Capabilities.ItemHandler.BLOCK) {
            return ImmutableMap.of(
                    Direction.DOWN, Pair.of(IOAction.EXTRACT, new int[]{1}),
                    Direction.NORTH, Pair.of(IOAction.BOTH, new int[]{0}),
                    Direction.EAST, Pair.of(IOAction.BOTH, new int[]{0}),
                    Direction.SOUTH, Pair.of(IOAction.BOTH, new int[]{0}),
                    Direction.WEST, Pair.of(IOAction.BOTH, new int[]{0})
            );
        }
        return ImmutableMap.of();
    }

    @Override
    protected void loadData(CompoundTag tag, HolderLookup.Provider provider) {
        this.duration = tag.getInt("duration");
        this.maxDuration = tag.getInt("maxDuration");
    }
}
