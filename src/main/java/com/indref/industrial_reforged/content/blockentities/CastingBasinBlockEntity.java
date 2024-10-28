package com.indref.industrial_reforged.content.blockentities;

import com.google.common.collect.ImmutableMap;
import com.indref.industrial_reforged.api.blockentities.container.ContainerBlockEntity;
import com.indref.industrial_reforged.api.capabilities.IOActions;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.content.recipes.CrucibleCastingRecipe;
import com.indref.industrial_reforged.util.recipes.recipeInputs.CrucibleCastingRecipeInput;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CastingBasinBlockEntity extends ContainerBlockEntity {
    public static final int CAST_SLOT = 0;

    public int duration;
    public int maxDuration;
    private ContainerData data;
    public ItemStack resultItem = ItemStack.EMPTY;

    public CastingBasinBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(IRBlockEntityTypes.CASTING_BASIN.get(), p_155229_, p_155230_);
        addItemHandler(2, (slot, item) -> slot == 0);
        addFluidTank(1000);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> CastingBasinBlockEntity.this.duration;
                    case 1 -> CastingBasinBlockEntity.this.maxDuration;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int val) {
                switch (index) {
                    case 0 -> CastingBasinBlockEntity.this.duration = val;
                    case 1 -> CastingBasinBlockEntity.this.maxDuration = val;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
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
        if (hasRecipe()) {
            increaseCraftingProgress();
            setChanged(level, worldPosition, getBlockState());
            updateRenderedStack();

            if (hasProgressFinished()) {
                castItem();
            }
        } else {
            resetProgress();
        }
    }

    public void updateRenderedStack() {
        this.resultItem = getCurrentRecipe().get().getResultItem(level.registryAccess());
    }

    public void resetRenderedStack() {
        this.resultItem = ItemStack.EMPTY;
    }

    public void castItem() {
        Optional<CrucibleCastingRecipe> optionalRecipe = getCurrentRecipe();
        FluidTank fluidTank = getFluidTank();
        ItemStackHandler itemHandler = getItemStackHandler();

        if (optionalRecipe.isPresent()) {
            CrucibleCastingRecipe recipe = optionalRecipe.get();
            fluidTank.drain(recipe.fluidStack(), IFluidHandler.FluidAction.EXECUTE);

            if (recipe.shouldConsumeCast()) {
                itemHandler.setStackInSlot(CAST_SLOT, ItemStack.EMPTY);
            }

            ItemStack resultItem1 = recipe.getResultItem(level.registryAccess());

            resultItem1.grow(itemHandler.getStackInSlot(1).getCount());

            itemHandler.setStackInSlot(1, resultItem1);
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

    public ContainerData getData() {
        return data;
    }

    public boolean hasProgressFinished() {
        return getCurrentRecipe().isPresent() && this.duration >= getCurrentRecipe().get().getDuration();
    }

    public boolean hasRecipe() {
        Optional<CrucibleCastingRecipe> recipe = getCurrentRecipe();

        return recipe.filter(crucibleCastingRecipeRecipeHolder ->
                canInsertIntoOutput(crucibleCastingRecipeRecipeHolder.getResultItem(level.registryAccess()))).isPresent();
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
