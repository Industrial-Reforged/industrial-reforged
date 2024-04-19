package com.indref.industrial_reforged.registries.blockentities.multiblocks;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.networking.NetworkingHelper;
import com.indref.industrial_reforged.networking.data.CastingDurationSyncData;
import com.indref.industrial_reforged.networking.data.CastingGhostItemSyncData;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.recipes.CrucibleCastingRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import java.util.Optional;

public class CastingBasinBlockEntity extends ContainerBlockEntity {
    public static final int CAST_SLOT = 0;

    public int duration;
    public int maxDuration;
    private ContainerData data;
    public ItemStack resultItem = ItemStack.EMPTY;

    public CastingBasinBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(IRBlockEntityTypes.CASTING_BASIN.get(), p_155229_, p_155230_);
        addItemHandler(2);
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
        if (getItemHandler().isPresent()) {
            ItemStack[] itemStacks = new ItemStack[2];
            itemStacks[0] = getItemHandler().get().getStackInSlot(0);

            ItemStack resultStack = getItemHandler().get().getStackInSlot(1);
            if (!resultStack.isEmpty()) {
                resetRenderedStack();
                itemStacks[1] = resultStack;
                IndustrialReforged.LOGGER.debug("real item");
            } else {
                itemStacks[1] = resultItem;
                IndustrialReforged.LOGGER.debug("Ghost item");
            }

            return itemStacks;
        }
        return new ItemStack[0];
    }

    public void tick(BlockPos blockPos, BlockState blockState) {
        if (hasRecipe()) {
            increaseCraftingProgress();
            setChanged(level, blockPos, blockState);
            updateRenderedStack();

            if (hasProgressFinished()) {
                castItem();
            }
        } else {
            resetProgress();
        }
    }

    public void updateRenderedStack() {
        IndustrialReforged.LOGGER.debug("Updating render stack");
        this.resultItem = getCurrentRecipe().get().value().getResultItem(level.registryAccess());
        NetworkingHelper.sendToClient(new CastingGhostItemSyncData(this.resultItem, getBlockPos()));
    }

    public void resetRenderedStack() {
        this.resultItem = ItemStack.EMPTY;
        NetworkingHelper.sendToClient(new CastingGhostItemSyncData(this.resultItem, getBlockPos()));
    }

    public void castItem() {
        Optional<RecipeHolder<CrucibleCastingRecipe>> rawRecipe = getCurrentRecipe();
        if (rawRecipe.isPresent() && getFluidTank().isPresent() && getItemHandler().isPresent()) {
            CrucibleCastingRecipe recipe = rawRecipe.get().value();

            getFluidTank().get().drain(recipe.getFluid(), IFluidHandler.FluidAction.EXECUTE);
            if (recipe.shouldConsumeCast())
                getItemHandler().get().setStackInSlot(CAST_SLOT, ItemStack.EMPTY);
            getItemHandler().get().insertItem(1, recipe.getResultItem(level.registryAccess()), false);
            resetProgress();
        }
    }

    public void increaseCraftingProgress() {
        this.duration++;
        NetworkingHelper.sendToClient(new CastingDurationSyncData(duration, maxDuration, worldPosition));
    }

    public void resetProgress() {
        this.duration = 0;
        this.maxDuration = 0;
        NetworkingHelper.sendToClient(new CastingDurationSyncData(duration, maxDuration, worldPosition));
    }

    // TODO: Use container data to sync these
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
        return getCurrentRecipe().isPresent() && this.duration >= getCurrentRecipe().get().value().getDuration();
    }

    public boolean hasRecipe() {
        Optional<RecipeHolder<CrucibleCastingRecipe>> recipe = getCurrentRecipe();

        return recipe.filter(crucibleCastingRecipeRecipeHolder ->
                canInsertIntoOutput(crucibleCastingRecipeRecipeHolder.value().getResultItem(level.registryAccess()))).isPresent();
    }

    private Optional<RecipeHolder<CrucibleCastingRecipe>> getCurrentRecipe() {
        if (level.isClientSide() || getFluidTank().isEmpty() || getItemHandler().isEmpty()) return Optional.empty();

        SimpleContainer inventory = new SimpleContainer(1);
        ItemStack stackInSlot = this.getItemHandler().get().getStackInSlot(CAST_SLOT);

        if (!stackInSlot.isEmpty()) {
            inventory.setItem(CAST_SLOT, stackInSlot);
        }

        Optional<RecipeHolder<CrucibleCastingRecipe>> recipe = this.level.getRecipeManager().getRecipeFor(CrucibleCastingRecipe.Type.INSTANCE, inventory, level);

        if (recipe.isEmpty()) return Optional.empty();

        this.maxDuration = recipe.get().value().getDuration();

        boolean matchesFluid = recipe.get().value().matchesFluids(getFluidTank().get().getFluidInTank(0), level);

        return matchesFluid ? recipe : Optional.empty();
    }

    private boolean canInsertIntoOutput(ItemStack outputItem) {
        if (getItemHandler().isEmpty()) return false;

        ItemStack itemStack = getItemHandler().get().getStackInSlot(1);
        return itemStack.isEmpty()
                || (ItemHandlerHelper.canItemStacksStack(itemStack, outputItem)
                && itemStack.getCount() + outputItem.getCount() <= outputItem.getMaxStackSize());
    }

    @Override
    protected void saveOther(CompoundTag tag) {
        tag.putInt("duration", this.duration);
        tag.putInt("maxDuration", this.maxDuration);
        CompoundTag itemTag = new CompoundTag();
        this.resultItem.save(itemTag);
        tag.put("resultItem", itemTag);
    }

    @Override
    protected void loadOther(CompoundTag tag) {
        this.duration = tag.getInt("duration");
        this.maxDuration = tag.getInt("maxDuration");
        this.resultItem = ItemStack.of(tag.getCompound("resultItem"));
    }
}
