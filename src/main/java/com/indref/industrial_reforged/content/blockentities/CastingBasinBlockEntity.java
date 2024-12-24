package com.indref.industrial_reforged.content.blockentities;

import com.google.common.collect.ImmutableMap;
import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blockentities.container.ContainerBlockEntity;
import com.indref.industrial_reforged.api.capabilities.IOActions;
import com.indref.industrial_reforged.api.capabilities.fluid.DynamicFluidTank;
import com.indref.industrial_reforged.data.IRDataMaps;
import com.indref.industrial_reforged.data.maps.CastingMoldValue;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.content.recipes.CrucibleCastingRecipe;
import com.indref.industrial_reforged.util.RegistryUtils;
import com.indref.industrial_reforged.util.recipes.recipeInputs.CrucibleCastingRecipeInput;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CastingBasinBlockEntity extends ContainerBlockEntity {
    public static final int CAST_SLOT = 0;

    public int duration;
    public int maxDuration;
    public ItemStack resultItem = ItemStack.EMPTY;

    private CrucibleCastingRecipe recipe;

    public CastingBasinBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(IRBlockEntityTypes.CASTING_BASIN.get(), p_155229_, p_155230_);
        addItemHandler(2, (slot, item) -> slot == 0 && getMold(item.getItem()) != null);
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
            }
        }

        updateRenderedStack();
    }

    private @Nullable CastingMoldValue getMold(Item Item) {
        return RegistryUtils.holder(BuiltInRegistries.ITEM, Item).getData(IRDataMaps.CASTING_MOLDS);
    }

    @Override
    protected void onFluidChanged() {
        super.onFluidChanged();
        updateRecipe(false);
    }

    public ItemStack[] getRenderStacks() {
        ItemStack[] itemStacks = new ItemStack[2];
        itemStacks[0] = getItemHandler().getStackInSlot(0);

        ItemStack resultStack = getItemHandler().getStackInSlot(1);
        if (!resultStack.isEmpty()) {
            resetRenderedStack();
            itemStacks[1] = resultStack;
        } else {
            itemStacks[1] = resultItem;
        }

        return itemStacks;
    }

    public void commonTick() {
        if (recipe != null) {
            increaseCraftingProgress();
            setChanged();
            updateRenderedStack();

            if (hasProgressFinished()) {
                castItem();
            }
        } else {
            resetProgress();
        }
    }

    public void updateRenderedStack() {
        this.resultItem = this.recipe != null ? this.recipe.getResultItem(level.registryAccess()) : ItemStack.EMPTY;
    }

    public void resetRenderedStack() {
        this.resultItem = ItemStack.EMPTY;
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
        Optional<CrucibleCastingRecipe> recipe = getCurrentRecipe();

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

    private Optional<CrucibleCastingRecipe> getCurrentRecipe() {
        List<ItemStack> itemStacks = new ArrayList<>();
        ItemStack stackInSlot = this.getItemHandler().getStackInSlot(CAST_SLOT);

        if (!stackInSlot.isEmpty()) {
            itemStacks.add(CAST_SLOT, stackInSlot);
        }

        Optional<CrucibleCastingRecipe> recipe = this.level.getRecipeManager()
                .getRecipeFor(CrucibleCastingRecipe.TYPE, new CrucibleCastingRecipeInput(itemStacks, getFluidTank().getFluidInTank(0)), level)
                .map(RecipeHolder::value);

        if (recipe.isEmpty()) return Optional.empty();

        this.maxDuration = recipe.get().getDuration();

        return recipe;
    }

    private boolean canInsertIntoOutput(ItemStack outputItem) {
        ItemStack itemStack = getItemHandler().getStackInSlot(1);
        return itemStack.isEmpty()
                || (ItemStack.isSameItemSameComponents(itemStack, outputItem)
                && itemStack.getCount() + outputItem.getCount() <= outputItem.getMaxStackSize());
    }

    @Override
    protected void saveData(CompoundTag tag, HolderLookup.Provider provider) {
        tag.putInt("duration", this.duration);
        tag.putInt("maxDuration", this.maxDuration);
        if (!this.resultItem.isEmpty()) {
            tag.put("resultItem", this.resultItem.save(provider));
        }
    }

    @Override
    public <T> ImmutableMap<Direction, Pair<IOActions, int[]>> getSidedInteractions(BlockCapability<T, @Nullable Direction> capability) {
        if (capability == Capabilities.FluidHandler.BLOCK) {
            return ImmutableMap.of(
                    Direction.UP, Pair.of(IOActions.INSERT, new int[]{0})
            );
        } else if (capability == Capabilities.ItemHandler.BLOCK) {
            return ImmutableMap.of(
                    Direction.DOWN, Pair.of(IOActions.EXTRACT, new int[]{1}),
                    Direction.NORTH, Pair.of(IOActions.BOTH, new int[]{0}),
                    Direction.EAST, Pair.of(IOActions.BOTH, new int[]{0}),
                    Direction.SOUTH, Pair.of(IOActions.BOTH, new int[]{0}),
                    Direction.WEST, Pair.of(IOActions.BOTH, new int[]{0})
            );
        }
        return ImmutableMap.of();
    }

    @Override
    protected void loadData(CompoundTag tag, HolderLookup.Provider provider) {
        this.duration = tag.getInt("duration");
        this.maxDuration = tag.getInt("maxDuration");
        if (tag.contains("resultItem")) {
            Optional<ItemStack> resultItem1 = ItemStack.parse(provider, tag.getCompound("resultItem"));
            this.resultItem = resultItem1.orElse(ItemStack.EMPTY);
        }
    }
}
