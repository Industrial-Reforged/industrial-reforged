package com.indref.industrial_reforged.content.blockentities;

import com.google.common.collect.ImmutableMap;
import com.indref.industrial_reforged.api.blockentities.IRContainerBlockEntity;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CastingBasinBlockEntity extends IRContainerBlockEntity {
    public static final int CAST_SLOT = 0;

    public int duration;
    public int maxDuration;

    private ResourceLocation recipeId;
    private RecipeHolder<CrucibleCastingRecipe> recipe;
    private FluidStack rememberedFluid;

    public CastingBasinBlockEntity(BlockPos pos, BlockState state) {
        super(IRBlockEntityTypes.CASTING_BASIN.get(), pos, state);
        addItemHandler(
                2,
                slot -> slot == 0 ? 1 : 64,
                (slot, item) -> slot == 0 && getMold(item.getItem()) != null
        );
        addFluidTank(0);
        this.rememberedFluid = FluidStack.EMPTY;
    }

    @Override
    protected void onItemsChanged(int slot) {
        super.onItemsChanged(slot);
        updateRecipe(true);
        if (slot == 0) {
            CastingMoldValue moldValue = getMold(getItemHandler().getStackInSlot(slot).getItem());
            DynamicFluidTank fluidTank = getFluidTank();
            if (moldValue != null) {
                fluidTank.setCapacity(moldValue.capacity());
                int filled = fluidTank.fill(this.rememberedFluid, IFluidHandler.FluidAction.EXECUTE);
                this.rememberedFluid = this.rememberedFluid.copyWithAmount(this.rememberedFluid.getAmount() - filled);
                update();
            } else {
                FluidStack fluid = fluidTank.getFluid();
                if (fluid.is(this.rememberedFluid.getFluid())) {
                    this.rememberedFluid = this.rememberedFluid.copyWithAmount(this.rememberedFluid.getAmount() + fluid.getAmount());
                } else {
                    this.rememberedFluid = fluid.copy();
                }
                fluidTank.setCapacity(0);
                update();
            }
        }
    }

    public FluidStack getRememberedFluid() {
        return rememberedFluid;
    }

    public boolean hasMold() {
        return !getItemHandler().getStackInSlot(0).isEmpty() && getItemHandler().getStackInSlot(1).isEmpty();
    }

    public boolean hasMoldAndNotFull() {
        return !getItemHandler().getStackInSlot(0).isEmpty() && getItemHandler().getStackInSlot(1).isEmpty() && getFluidTank().getFluidAmount() < getFluidTank().getCapacity();
    }

    public static @Nullable CastingMoldValue getMold(Item item) {
        return RegistryUtils.holder(BuiltInRegistries.ITEM, item).getData(IRDataMaps.CASTING_MOLDS);
    }

    @Override
    public void onFluidChanged() {
        super.onFluidChanged();
        updateRecipe(false);
    }

    public void onClientFluidChanged(int fluidAmount) {
        updateRecipe(fluidAmount);
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
            Item moldItem = this.recipe.value().moldItem();
            ItemStack resultItem1 = this.recipe.value().resultStack().copy();

            fluidTank.drain(this.recipe.value().fluidStack().getAmount(), IFluidHandler.FluidAction.EXECUTE);

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
        return this.recipe != null && this.duration >= this.recipe.value().getDuration();
    }

    public void updateRecipe(boolean itemsChanged) {
        updateRecipe(getFluidTank().getFluidAmount());
    }

    public void updateRecipe(int fluidAmount) {
        Optional<RecipeHolder<CrucibleCastingRecipe>> recipe = getCurrentRecipe(fluidAmount);

        boolean canInsert = recipe.filter(crucibleCastingRecipe ->
                canInsertIntoOutput(crucibleCastingRecipe.value().getResultItem(level.registryAccess()))).isPresent();

        if (canInsert) {
            this.recipe = recipe.get();
        } else {
            this.recipe = null;
        }
    }

    private Optional<RecipeHolder<CrucibleCastingRecipe>> getCurrentRecipe(int fluidAmount) {
        ItemStack moltItem = this.getItemHandler().getStackInSlot(CAST_SLOT);

        Optional<RecipeHolder<CrucibleCastingRecipe>> recipe = this.level.getRecipeManager()
                .getRecipeFor(CrucibleCastingRecipe.TYPE, new CrucibleCastingRecipeInput(moltItem, getFluidTank().getFluidInTank(0).copyWithAmount(fluidAmount)), level);

        if (recipe.isEmpty()) return Optional.empty();

        this.maxDuration = recipe.get().value().getDuration();

        return recipe;
    }

    private boolean canInsertIntoOutput(ItemStack outputItem) {
        return forceInsertItem(1, outputItem, true).isEmpty();
    }

    public CrucibleCastingRecipe getRecipe() {
        return this.recipe != null ? this.recipe.value() : null;
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
    public void onLoad() {
        super.onLoad();

        this.recipe = this.recipeId != null ? (RecipeHolder<CrucibleCastingRecipe>) level.getRecipeManager().byKey(this.recipeId).orElse(null) : null;
    }

    @Override
    protected void loadData(CompoundTag tag, HolderLookup.Provider provider) {
        this.duration = tag.getInt("duration");
        this.maxDuration = tag.getInt("maxDuration");
        String recipeId = tag.getString("recipe_id");
        if (!recipeId.equals("NORECIPE") && !recipeId.isEmpty()) {
            this.recipeId = ResourceLocation.parse(recipeId);
        } else {
            this.recipeId = null;
        }
        if (tag.contains("remembered_fluid")) {
            this.rememberedFluid = FluidStack.parseOptional(provider, tag.getCompound("remembered_fluid"));
        } else {
            this.rememberedFluid = FluidStack.EMPTY;
        }
    }

    @Override
    protected void saveData(CompoundTag tag, HolderLookup.Provider provider) {
        tag.putInt("duration", this.duration);
        tag.putInt("maxDuration", this.maxDuration);
        tag.putString("recipe_id", this.recipe != null ? this.recipe.id().toString() : "NORECIPE");
        if (this.rememberedFluid != null && !this.rememberedFluid.isEmpty()) {
            tag.put("remembered_fluid", this.rememberedFluid.saveOptional(provider));
        }
    }

}
