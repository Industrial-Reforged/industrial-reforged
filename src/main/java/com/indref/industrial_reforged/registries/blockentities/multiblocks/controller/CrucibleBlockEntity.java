package com.indref.industrial_reforged.registries.blockentities.multiblocks.controller;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.FakeBlockEntity;
import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.api.multiblocks.Multiblock;
import com.indref.industrial_reforged.api.tiers.CrucibleTier;
import com.indref.industrial_reforged.client.renderer.items.CrucibleProgressRenderer;
import com.indref.industrial_reforged.networking.CrucibleMeltingProgressPayload;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.IRRegistries;
import com.indref.industrial_reforged.registries.blocks.multiblocks.CrucibleControllerBlock;
import com.indref.industrial_reforged.registries.multiblocks.IFireboxMultiblock;
import com.indref.industrial_reforged.registries.recipes.CrucibleSmeltingRecipe;
import com.indref.industrial_reforged.registries.screen.CrucibleMenu;
import com.indref.industrial_reforged.util.ItemUtils;
import com.indref.industrial_reforged.util.recipes.ItemRecipeInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
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

        getFireBox().ifPresent(iFireboxMultiblock -> {
            BlockPos controllerPos = this.worldPosition.offset(0, -1, 0);
            BlockEntity controllerBlockEntity = level.getBlockEntity(controllerPos);
            if (controllerBlockEntity instanceof FireboxBlockEntity fireboxBlockEntity) {
                int output = iFireboxMultiblock.getTier().getMaxHeatOutput();
                if (fireboxBlockEntity.tryDrainHeat(output) == 0) {
                    this.tryFillHeat(output);
                }
            }
        });
    }

    private void tryMeltItem() {
        getItemHandler().ifPresent(itemStackHandler -> {
            for (int i = 0; i < itemStackHandler.getSlots(); i++) {
                Optional<CrucibleSmeltingRecipe> recipeOptional = getCurrentRecipe(i);
                if (recipeOptional.isPresent()) {
                    CrucibleSmeltingRecipe recipe = recipeOptional.get();
                    IndustrialReforged.LOGGER.debug("Recipe: {}", recipe);
                    ItemStack itemStack = itemStackHandler.getStackInSlot(i);
                    Item input = recipe.ingredient().getItems()[0].getItem();
                    CompoundTag tag = ItemUtils.getImmutableTag(itemStack).copyTag();
                    if (itemStack.is(input) && tag.getInt(CrucibleProgressRenderer.BARWIDTH_KEY) >= 10) {
                        itemStack.shrink(1);
                        tag.putInt(CrucibleProgressRenderer.BARWIDTH_KEY, 0);
                        itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                        if (getFluidTank().isPresent()) {
                            FluidStack resultFluid = recipe.resultFluid();
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
                Optional<CrucibleSmeltingRecipe> recipe = getCurrentRecipe(i);
                if (recipe.isPresent() && getHeatStored() >= recipe.get().heat()) {
                    ItemStack itemStack = itemStackHandler.getStackInSlot(i);
                    Item input = recipe.get().getIngredients().get(0).getItems()[0].getItem();
                    if (itemStack.is(input)) {
                        CompoundTag tag = ItemUtils.getImmutableTag(itemStack).copyTag();
                        if (!tag.getBoolean(CrucibleProgressRenderer.IS_MELTING_KEY)) {
                            tag.putBoolean(CrucibleProgressRenderer.IS_MELTING_KEY, true);
                            itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                        }
                        float pValue = tag.getFloat(CrucibleProgressRenderer.BARWIDTH_KEY) + ((float) 1 / recipe.get().duration()) * 6;
                        if (pValue < 0) pValue = 0;
                        IndustrialReforged.LOGGER.info("Progress: {}", pValue);
                        tag.putFloat(CrucibleProgressRenderer.BARWIDTH_KEY, pValue);
                        itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                        PacketDistributor.sendToAllPlayers(new CrucibleMeltingProgressPayload(worldPosition, i, pValue));
                    }
                }
            }
        });
    }

    public boolean hasRecipe() {
        if (getItemHandler().isPresent()) {
            for (int i = 0; i < getItemHandler().get().getSlots(); i++) {
                Optional<CrucibleSmeltingRecipe> recipe = getCurrentRecipe(i);
                if (recipe.isEmpty())
                    continue;

                FluidStack result = recipe.get().resultFluid();

                if (canInsertAmountIntoOutput(result.getAmount()) && canInsertFluidIntoOutput(result.getFluid()))
                    return true;
            }
        }

        return false;
    }

    private Optional<CrucibleSmeltingRecipe> getCurrentRecipe(int slot) {
        if (getItemHandler().isPresent()) {
            List<ItemStack> itemStacks = new ArrayList<>();
            ItemStack stackInSlot = this.getItemHandler().get().getStackInSlot(slot);
            if (!stackInSlot.isEmpty())
                itemStacks.addFirst(stackInSlot);

            return this.level.getRecipeManager().getRecipeFor(CrucibleSmeltingRecipe.TYPE, new ItemRecipeInput(itemStacks), level).map(RecipeHolder::value);
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

    public Optional<IFireboxMultiblock> getFireBox() {
        BlockPos fireboxPos = worldPosition.offset(0, -1, 0);
        for (Multiblock multiblock : IRRegistries.MULTIBLOCK) {
            if (multiblock instanceof IFireboxMultiblock fireboxMultiblock) {
                try {
                    BlockPos controllerPos = fireboxPos;
                    if (level.getBlockEntity(fireboxPos) instanceof FakeBlockEntity fakeBlockEntity) {
                        IndustrialReforged.LOGGER.debug("Fake be found");
                        if (fakeBlockEntity.getActualBlockEntity().isPresent()) {
                            controllerPos = fakeBlockEntity.getActualBlockEntity().get().getBlockPos();
                        }
                    }
                    if (multiblock.isFormed(level, fireboxPos, controllerPos)) {
                        return Optional.of(fireboxMultiblock);
                    }
                } catch (Exception ignored) {
                    return Optional.empty();
                }
            }
        }
        return Optional.empty();
    }
}