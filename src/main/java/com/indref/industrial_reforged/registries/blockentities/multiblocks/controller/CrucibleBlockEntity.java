package com.indref.industrial_reforged.registries.blockentities.multiblocks.controller;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.api.blocks.container.IHeatBlock;
import com.indref.industrial_reforged.api.tiers.CrucibleTier;
import com.indref.industrial_reforged.client.renderer.items.CrucibleProgressRenderer;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.blocks.multiblocks.CrucibleControllerBlock;
import com.indref.industrial_reforged.registries.recipes.CrucibleSmeltingRecipe;
import com.indref.industrial_reforged.registries.screen.CrucibleMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CrucibleBlockEntity extends ContainerBlockEntity implements MenuProvider {
    private final CrucibleTier tier;

    public CrucibleBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRBlockEntityTypes.CRUCIBLE.get(), blockPos, blockState);
        addFluidTank(9000);
        addItemHandler(9);
        addHeatStorage(2000);

        this.tier = ((CrucibleControllerBlock) blockState.getBlock()).getTier();
    }

    @Override
    public int getHeatCapacity() {
        return tier.getHeatCapacity();
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Crucible");
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if (hasRecipe()) {
            increaseCraftingProgress();
            setChanged(level, blockPos, blockState);

            tryMeltItem();
        }
    }

    private void tryMeltItem() {
        getItemHandler().ifPresent(itemStackHandler -> {
            for (int i = 0; i < itemStackHandler.getSlots(); i++) {
                Optional<RecipeHolder<CrucibleSmeltingRecipe>> recipe = getCurrentRecipe(i);
                if (recipe.isPresent()) {
                    ItemStack itemStack = itemStackHandler.getStackInSlot(i);
                    Item input = recipe.get().value().getIngredients().get(0).getItems()[0].getItem();
                    if (itemStack.is(input) && itemStack.getOrCreateTag().getFloat(CrucibleProgressRenderer.BARWIDTH_KEY) >= 10) {
                        itemStack.shrink(1);
                        itemStack.getOrCreateTag().putFloat(CrucibleProgressRenderer.BARWIDTH_KEY, 0);
                        if (getFluidTank().isPresent()) {
                            FluidStack resultFluid = recipe.get().value().getResultFluid();
                            this.getFluidTank().get().fill(resultFluid, IFluidHandler.FluidAction.EXECUTE);
                        }
                    }
                }
            }
        });
    }


    private void increaseCraftingProgress() {
        getItemHandler().ifPresent(itemStackHandler -> {
            for (int i = 0; i < itemStackHandler.getSlots(); i++) {
                Optional<RecipeHolder<CrucibleSmeltingRecipe>> recipe = getCurrentRecipe(i);
                if (recipe.isPresent()) {
                    ItemStack itemStack = itemStackHandler.getStackInSlot(i);
                    Item input = recipe.get().value().getIngredients().get(0).getItems()[0].getItem();
                    if (itemStack.is(input)) {
                        CompoundTag tag = itemStack.getOrCreateTag();
                        if (!tag.getBoolean(CrucibleProgressRenderer.IS_MELTING_KEY))
                            tag.putBoolean(CrucibleProgressRenderer.IS_MELTING_KEY, true);
                        float pValue = tag.getFloat(CrucibleProgressRenderer.BARWIDTH_KEY) + ((float) 1 / recipe.get().value().getDuration()) * 6;
                        if (pValue < 0) pValue = 0;
                        IndustrialReforged.LOGGER.info("Progress: {}", pValue);
                        tag.putFloat(CrucibleProgressRenderer.BARWIDTH_KEY, pValue);
                    }
                }
            }
        });
    }

    public boolean hasRecipe() {
        if (getItemHandler().isPresent()) {
            for (int i = 0; i < getItemHandler().get().getSlots(); i++) {
                Optional<RecipeHolder<CrucibleSmeltingRecipe>> recipe = getCurrentRecipe(i);
                if (recipe.isEmpty()) {
                    continue;
                }

                FluidStack result = recipe.get().value().getResultFluid();

                if (canInsertAmountIntoOutput(result.getAmount()) && canInsertFluidIntoOutput(result.getFluid()))
                    return true;
            }
        }

        return false;
    }

    private Optional<RecipeHolder<CrucibleSmeltingRecipe>> getCurrentRecipe(int slot) {
        if (getItemHandler().isPresent()) {
            SimpleContainer inventory = new SimpleContainer(1);
            ItemStack stackInSlot = this.getItemHandler().get().getStackInSlot(slot);
            if (!stackInSlot.isEmpty()) {
                inventory.setItem(0, stackInSlot);
            }

            return this.level.getRecipeManager().getRecipeFor(CrucibleSmeltingRecipe.Type.INSTANCE, inventory, level);
        }
        return Optional.empty();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
        return new CrucibleMenu(containerId, inventory, this);
    }

    private boolean canInsertFluidIntoOutput(Fluid fluid) {
        if (getFluidTank().isEmpty()) return false;

        for (int i = 0; i < getFluidTank().get().getTanks(); i++) {
            FluidStack fluidStack = getFluidTank().get().getFluidInTank(i);
            if (fluidStack.isEmpty() || fluidStack.getFluid().equals(fluid))
                return true;
        }
        return false;
    }

    private boolean canInsertAmountIntoOutput(int count) {
        if (getFluidTank().isEmpty()) return false;

        for (int i = 0; i < getFluidTank().get().getTanks(); i++) {
            FluidStack fluidStack = getFluidTank().get().getFluidInTank(i);
            if (fluidStack.getAmount() + count <= getFluidTank().get().getTankCapacity(i))
                return true;
        }
        return false;
    }
}