package com.indref.industrial_reforged.registries.blockentities.multiblocks;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.networking.CastingDurationPayload;
import com.indref.industrial_reforged.networking.CastingGhostItemPayload;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.recipes.CrucibleCastingRecipe;
import com.indref.industrial_reforged.util.recipes.ItemAndFluidRecipeInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.network.PacketDistributor;

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
        this.resultItem = getCurrentRecipe().get().getResultItem(level.registryAccess());
        PacketDistributor.sendToAllPlayers(new CastingGhostItemPayload(this.resultItem, getBlockPos()));
    }

    public void resetRenderedStack() {
        this.resultItem = ItemStack.EMPTY;
        PacketDistributor.sendToAllPlayers(new CastingGhostItemPayload(this.resultItem, getBlockPos()));
    }

    public void castItem() {
        Optional<CrucibleCastingRecipe> optionalRecipe = getCurrentRecipe();
        if (optionalRecipe.isPresent() && getFluidTank().isPresent() && getItemHandler().isPresent()) {
            CrucibleCastingRecipe recipe = optionalRecipe.get();

            getFluidTank().get().drain(recipe.fluidStack(), IFluidHandler.FluidAction.EXECUTE);
            if (recipe.shouldConsumeCast())
                getItemHandler().get().setStackInSlot(CAST_SLOT, ItemStack.EMPTY);
            getItemHandler().get().insertItem(1, recipe.getResultItem(level.registryAccess()), false);
            resetProgress();
        }
    }

    public void increaseCraftingProgress() {
        this.duration++;
        PacketDistributor.sendToAllPlayers(new CastingDurationPayload(duration, maxDuration, worldPosition));
    }

    public void resetProgress() {
        this.duration = 0;
        this.maxDuration = 0;
        PacketDistributor.sendToAllPlayers(new CastingDurationPayload(duration, maxDuration, worldPosition));
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
        return getCurrentRecipe().isPresent() && this.duration >= getCurrentRecipe().get().getDuration();
    }

    public boolean hasRecipe() {
        Optional<CrucibleCastingRecipe> recipe = getCurrentRecipe();

        return recipe.filter(crucibleCastingRecipeRecipeHolder ->
                canInsertIntoOutput(crucibleCastingRecipeRecipeHolder.getResultItem(level.registryAccess()))).isPresent();
    }

    private Optional<CrucibleCastingRecipe> getCurrentRecipe() {
        if (level.isClientSide() || getFluidTank().isEmpty() || getItemHandler().isEmpty()) return Optional.empty();

        List<ItemStack> itemStacks = new ArrayList<>();
        ItemStack stackInSlot = this.getItemHandler().get().getStackInSlot(CAST_SLOT);

        if (!stackInSlot.isEmpty()) {
            itemStacks.add(CAST_SLOT, stackInSlot);
        }

        Optional<RecipeHolder<CrucibleCastingRecipe>> recipe = this.level.getRecipeManager().getRecipeFor(CrucibleCastingRecipe.TYPE,
                new ItemAndFluidRecipeInput(itemStacks, getFluidTank().get().getFluidInTank(0)),
                level);

        if (recipe.isEmpty()) return Optional.empty();

        this.maxDuration = recipe.get().value().getDuration();

        return recipe.map(RecipeHolder::value);
    }

    private boolean canInsertIntoOutput(ItemStack outputItem) {
        if (getItemHandler().isEmpty()) return false;

        ItemStack itemStack = getItemHandler().get().getStackInSlot(1);
        return itemStack.isEmpty()
                || (ItemStack.isSameItemSameComponents(itemStack, outputItem)
                && itemStack.getCount() + outputItem.getCount() <= outputItem.getMaxStackSize());
    }

    @Override
    protected void saveData(CompoundTag tag, HolderLookup.Provider provider) {
        tag.putInt("duration", this.duration);
        tag.putInt("maxDuration", this.maxDuration);
        CompoundTag itemTag = new CompoundTag();
        if (!this.resultItem.isEmpty()) {
            this.resultItem.save(provider);
        }
        tag.put("resultItem", itemTag);
    }

    @Override
    protected void loadData(CompoundTag tag, HolderLookup.Provider provider) {
        this.duration = tag.getInt("duration");
        this.maxDuration = tag.getInt("maxDuration");
        Optional<ItemStack> resultItem1 = ItemStack.parse(provider, tag.getCompound("resultItem"));
        this.resultItem = resultItem1.orElse(ItemStack.EMPTY);
    }
}
