package com.indref.industrial_reforged.registries.blockentities.multiblocks;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.client.renderer.CrucibleProgressRenderer;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.recipes.CrucibleCastingRecipe;
import com.indref.industrial_reforged.registries.recipes.CrucibleSmeltingRecipe;
import com.indref.industrial_reforged.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import java.util.Optional;

public class CastingTableBlockEntity extends ContainerBlockEntity {
    public static final int CAST_SLOT = 0;

    private int duration;
    private int maxDuration;

    public CastingTableBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(IRBlockEntityTypes.CASTING_TABLE.get(), p_155229_, p_155230_);
        addItemHandler(2);
        addFluidTank(1000);
    }

    public ItemStack[] getRenderStacks() {
        return new ItemStack[]{
                getItemHandler().getStackInSlot(0),
                getItemHandler().getStackInSlot(1)
        };
    }

    public void tick(BlockPos blockPos, BlockState blockState) {
        if (hasRecipe()) {
            increaseCraftingProgress();
            setChanged(level, blockPos, blockState);

            if (hasProgressFinished())
                castItem();
        } else {
            resetProgress();
        }
    }

    public void castItem() {
        Optional<RecipeHolder<CrucibleCastingRecipe>> rawRecipe = getCurrentRecipe();
        if (rawRecipe.isPresent()) {
            CrucibleCastingRecipe recipe = rawRecipe.get().value();

            getFluidTank().drain(recipe.getFluid(), IFluidHandler.FluidAction.EXECUTE);
            if (recipe.shouldConsumeCast())
                getItemHandler().setStackInSlot(CAST_SLOT, ItemStack.EMPTY);
            getItemHandler().insertItem(1, recipe.getResultItem(level.registryAccess()), false);
            resetProgress();
        }
    }

    public void increaseCraftingProgress() {
        this.duration++;
    }

    public void resetProgress() {
        this.duration = 0;
    }

    public int getDuration() {
        IndustrialReforged.LOGGER.debug("duration: {}", duration);
        return duration;
    }

    public int getMaxDuration() {
        IndustrialReforged.LOGGER.debug("maxduration: {}", maxDuration);
        return maxDuration;
    }

    public boolean hasProgressFinished() {
        return this.duration >= getCurrentRecipe().get().value().getDuration();
    }

    public boolean hasRecipe() {
        Optional<RecipeHolder<CrucibleCastingRecipe>> recipe = getCurrentRecipe();

        return recipe.filter(crucibleCastingRecipeRecipeHolder ->
                canInsertIntoOutput(crucibleCastingRecipeRecipeHolder.value().getResultItem(level.registryAccess()))).isPresent();
    }

    private Optional<RecipeHolder<CrucibleCastingRecipe>> getCurrentRecipe() {
        if (level.isClientSide()) return Optional.empty();

        SimpleContainer inventory = new SimpleContainer(1);
        ItemStack stackInSlot = this.getItemHandler().getStackInSlot(CAST_SLOT);

        if (!stackInSlot.isEmpty()) {
            inventory.setItem(CAST_SLOT, stackInSlot);
        }

        Optional<RecipeHolder<CrucibleCastingRecipe>> recipe = this.level.getRecipeManager().getRecipeFor(CrucibleCastingRecipe.Type.INSTANCE, inventory, level);

        if (recipe.isEmpty()) return Optional.empty();

        this.maxDuration = recipe.get().value().getDuration();

        IndustrialReforged.LOGGER.debug("maxduration: {}", maxDuration);

        boolean matchesFluid = recipe.get().value().matchesFluids(getFluidTank().getFluidInTank(0), level);

        return matchesFluid ? recipe : Optional.empty();
    }

    private boolean canInsertIntoOutput(ItemStack outputItem) {
        ItemStack itemStack = getItemHandler().getStackInSlot(1);
        return itemStack.isEmpty()
                || (ItemHandlerHelper.canItemStacksStack(itemStack, outputItem)
                && itemStack.getCount() + outputItem.getCount() <= outputItem.getMaxStackSize());
    }

    @Override
    protected void saveOther(CompoundTag tag) {
        tag.putInt("duration", this.duration);
        tag.putInt("maxDuration", this.maxDuration);
    }

    @Override
    protected void loadOther(CompoundTag tag) {
        this.duration = tag.getInt("duration");
        this.maxDuration = tag.getInt("maxDuration");
    }
}
