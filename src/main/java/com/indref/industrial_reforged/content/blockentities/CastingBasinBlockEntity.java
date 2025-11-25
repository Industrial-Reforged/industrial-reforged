package com.indref.industrial_reforged.content.blockentities;

import com.indref.industrial_reforged.api.blockentities.IRContainerBlockEntity;
import com.indref.industrial_reforged.content.recipes.BasinCastingRecipe;
import com.indref.industrial_reforged.data.IRDataMaps;
import com.indref.industrial_reforged.data.maps.CastingMoldValue;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.content.recipes.recipeInputs.ItemFluidRecipeInput;
import com.indref.industrial_reforged.util.IRHandlerUtils;
import com.indref.industrial_reforged.util.recipes.FluidIngredientWithAmount;
import com.portingdeadmods.portingdeadlibs.api.capabilities.DynamicFluidTank;
import com.portingdeadmods.portingdeadlibs.utils.RegistryUtils;
import com.portingdeadmods.portingdeadlibs.utils.capabilities.HandlerUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public class CastingBasinBlockEntity extends IRContainerBlockEntity {
    public static final int CAST_SLOT = 0;

    public int duration;
    public int maxDuration;

    private ResourceLocation recipeId;
    private RecipeHolder<BasinCastingRecipe> recipe;
    private FluidStack rememberedFluid;

    public CastingBasinBlockEntity(BlockPos pos, BlockState state) {
        super(IRBlockEntityTypes.CASTING_BASIN.get(), pos, state);
        addItemHandler(HandlerUtils::newItemStackHandler, builder -> builder
                .slots(2)
                .slotLimit(slot -> slot == 0 ? 1 : 64)
                .onChange(this::onItemsChanged)
                .validator((slot, item) -> slot == 0 && (getMold(item.getItem()) != null || isMoldIngredient(item)))
        );
        addFluidHandler(IRHandlerUtils::newDynamicFluidTank, builder -> builder
                .slotLimit($ -> 0)
                .onChange(this::onFluidChanged));
        this.rememberedFluid = FluidStack.EMPTY;
    }

    public static boolean isMoldIngredient(ItemStack item) {
        Map<ResourceKey<Item>, TagKey<Item>> dataMap = BuiltInRegistries.ITEM.getDataMap(IRDataMaps.MOLD_INGREDIENTS);
        for (Map.Entry<ResourceKey<Item>, TagKey<Item>> entry : dataMap.entrySet()) {
            if (item.is(entry.getValue())) {
                return true;
            }
        }
        return false;
    }

    protected void onItemsChanged(int slot) {
        this.updateData();
        this.updateRecipe(true);
        
        if (slot == 0) {
            CastingMoldValue moldValue = getMold(getItemHandler().getStackInSlot(slot).getItem());
            DynamicFluidTank fluidTank = ((DynamicFluidTank) getFluidHandler());
            if (moldValue != null) {
                fluidTank.setCapacity(moldValue.capacity());
                int filled = fluidTank.fill(this.rememberedFluid, IFluidHandler.FluidAction.EXECUTE);
                int amount = this.rememberedFluid.getAmount() - filled;
                this.rememberedFluid = amount == 0 ? FluidStack.EMPTY : this.rememberedFluid.copyWithAmount(amount);
                this.updateData();
            } else if (isMoldIngredient(getItemHandler().getStackInSlot(CAST_SLOT))) {
                fluidTank.setCapacity(333);
                int filled = fluidTank.fill(this.rememberedFluid, IFluidHandler.FluidAction.EXECUTE);
                int amount = this.rememberedFluid.getAmount() - filled;
                this.rememberedFluid = amount == 0 ? FluidStack.EMPTY : this.rememberedFluid.copyWithAmount(amount);
                this.updateData();
            } else {
                FluidStack fluid = fluidTank.getFluid();
                if (fluid.is(this.rememberedFluid.getFluid())) {
                    this.rememberedFluid = this.rememberedFluid.copyWithAmount(this.rememberedFluid.getAmount() + fluid.getAmount());
                } else {
                    this.rememberedFluid = fluid.copy();
                }
                fluidTank.setCapacity(0);
                this.updateData();
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
        DynamicFluidTank tank = ((DynamicFluidTank) this.getFluidHandler());
        return !getItemHandler().getStackInSlot(0).isEmpty() && getItemHandler().getStackInSlot(1).isEmpty() && tank.getFluidAmount() < tank.getCapacity();
    }

    public static @Nullable CastingMoldValue getMold(Item item) {
        return RegistryUtils.holder(BuiltInRegistries.ITEM, item).getData(IRDataMaps.CASTING_MOLDS);
    }

    public void onFluidChanged(int tank) {
        this.updateData();
        
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
        DynamicFluidTank fluidTank = ((DynamicFluidTank) getFluidHandler());
        ItemStackHandler itemHandler = ((ItemStackHandler) getItemHandler());

        if (this.recipe != null) {
            FluidIngredientWithAmount fluidIngredient = this.recipe.value().fluidIngredient();
            ItemStack resultItem = this.recipe.value().resultStack().copy();

            fluidTank.drain(fluidIngredient.amount(), IFluidHandler.FluidAction.EXECUTE);

            // this.recipe is null from here on
            CastingMoldValue moldValue = getMold(itemHandler.getStackInSlot(CAST_SLOT).getItem());
            if (moldValue != null && moldValue.consumeCast()) {
                itemHandler.setStackInSlot(CAST_SLOT, ItemStack.EMPTY);
            }

            forceInsertItem(((IItemHandlerModifiable) this.getItemHandler()), 1, resultItem, false, this::onItemsChanged);
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
        updateRecipe(((DynamicFluidTank) getFluidHandler()).getFluidAmount());
    }

    public void updateRecipe(int fluidAmount) {
        Optional<RecipeHolder<BasinCastingRecipe>> recipe = getCurrentRecipe(fluidAmount);

        boolean canInsert = recipe.filter(crucibleCastingRecipe ->
                canInsertIntoOutput(crucibleCastingRecipe.value().getResultItem(level.registryAccess()))).isPresent();

        if (canInsert) {
            this.recipe = recipe.get();
        } else {
            this.recipe = null;
        }
    }

    private Optional<RecipeHolder<BasinCastingRecipe>> getCurrentRecipe(int fluidAmount) {
        ItemStack moltItem = this.getItemHandler().getStackInSlot(CAST_SLOT);

        Optional<RecipeHolder<BasinCastingRecipe>> recipe = this.level.getRecipeManager()
                .getRecipeFor(BasinCastingRecipe.TYPE, new ItemFluidRecipeInput(moltItem, ((DynamicFluidTank) getFluidHandler()).getFluidInTank(0).copyWithAmount(fluidAmount)), level);

        if (recipe.isEmpty()) return Optional.empty();

        this.maxDuration = recipe.get().value().getDuration();

        return recipe;
    }

    private boolean canInsertIntoOutput(ItemStack outputItem) {
        return forceInsertItem(((IItemHandlerModifiable) this.getItemHandler()), 1, outputItem, true, this::onItemsChanged).isEmpty();
    }

    public BasinCastingRecipe getRecipe() {
        return this.recipe != null ? this.recipe.value() : null;
    }

    @Override
    public void onLoad() {
        super.onLoad();

        this.recipe = this.recipeId != null ? (RecipeHolder<BasinCastingRecipe>) level.getRecipeManager().byKey(this.recipeId).orElse(null) : null;
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
