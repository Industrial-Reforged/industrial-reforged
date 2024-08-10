package com.indref.industrial_reforged.registries.blockentities.multiblocks.controller;

import com.indref.industrial_reforged.api.multiblocks.FakeBlockEntity;
import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.api.capabilities.heat.IHeatStorage;
import com.indref.industrial_reforged.api.multiblocks.Multiblock;
import com.indref.industrial_reforged.api.tiers.CrucibleTier;
import com.indref.industrial_reforged.client.renderer.item.bar.CrucibleProgressRenderer;
import com.indref.industrial_reforged.networking.CrucibleMeltingProgressPayload;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.IRRegistries;
import com.indref.industrial_reforged.registries.blocks.multiblocks.CrucibleControllerBlock;
import com.indref.industrial_reforged.registries.multiblocks.IFireboxMultiblock;
import com.indref.industrial_reforged.util.recipes.recipeInputs.CrucibleSmeltingRecipeInput;
import com.indref.industrial_reforged.registries.recipes.CrucibleSmeltingRecipe;
import com.indref.industrial_reforged.registries.gui.menus.CrucibleMenu;
import com.indref.industrial_reforged.util.CapabilityUtils;
import com.indref.industrial_reforged.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CrucibleBlockEntity extends ContainerBlockEntity implements MenuProvider {
    private final CrucibleTier tier;

    public CrucibleBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRBlockEntityTypes.CRUCIBLE.get(), blockPos, blockState);
        addItemHandler(9, 1);
        addHeatStorage(2000);
        addFluidTank(9000);
        this.tier = ((CrucibleControllerBlock) blockState.getBlock()).getTier();
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Crucible");
    }

    public void commonTick() {
        if (hasRecipe()) {
            increaseCraftingProgress();
            setChanged(level, worldPosition, getBlockState());

            tryMeltItem();
        }

        Optional<IFireboxMultiblock> fireboxMultiblock = getFireBox();

        if (fireboxMultiblock.isPresent()) {
            BlockPos controllerPos = this.worldPosition.offset(0, -1, 0);
            BlockEntity controllerBlockEntity = level.getBlockEntity(controllerPos);
            if (controllerBlockEntity instanceof FireboxBlockEntity) {
                int output = fireboxMultiblock.get().getTier().getMaxHeatOutput();
                IHeatStorage thisHeatStorage = getHeatStorage();
                IHeatStorage fireBoxHeatStorage = CapabilityUtils.heatStorageCapability(controllerBlockEntity);
                int drained = fireBoxHeatStorage.tryDrainHeat(Math.min(output, thisHeatStorage.getHeatCapacity() - thisHeatStorage.getHeatStored()), false);
                thisHeatStorage.tryFillHeat(drained, false);
            }
        }
    }

    private void tryMeltItem() {
        IItemHandler handler = getItemHandler();
        for (int i = 0; i < handler.getSlots(); i++) {
            Optional<CrucibleSmeltingRecipe> recipeOptional = this.getCurrentRecipe(i);
            if (recipeOptional.isPresent()) {
                CrucibleSmeltingRecipe recipe = recipeOptional.get();
                ItemStack itemStack = handler.getStackInSlot(i);
                CompoundTag tag = ItemUtils.getImmutableTag(itemStack).copyTag();
                if (recipe.ingredient().test(itemStack) && tag.getInt(CrucibleProgressRenderer.BARWIDTH_KEY) >= 10) {
                    itemStack.shrink(1);
                    tag.putInt(CrucibleProgressRenderer.BARWIDTH_KEY, 0);
                    itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                    FluidStack resultFluid = recipe.resultFluid();
                    if (!level.isClientSide()) {
                        IFluidHandler fluidHandler = getFluidHandler();
                        fluidHandler.fill(resultFluid.copy(), IFluidHandler.FluidAction.EXECUTE);
                    }
                }
            }
        }
    }

    private void increaseCraftingProgress() {
        IItemHandler itemStackHandler = getItemHandler();
        IHeatStorage heatStorage = getHeatStorage();

        if (itemStackHandler == null || heatStorage == null) return;

        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            Optional<CrucibleSmeltingRecipe> recipe = getCurrentRecipe(i);
            if (recipe.isPresent() && heatStorage.getHeatStored() >= recipe.get().heat()) {
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
                    tag.putFloat(CrucibleProgressRenderer.BARWIDTH_KEY, pValue);
                    itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                    PacketDistributor.sendToAllPlayers(new CrucibleMeltingProgressPayload(worldPosition, i, pValue));
                }
            }
        }
    }

    public boolean hasRecipe() {
        for (int i = 0; i < getItemHandler().getSlots(); i++) {
            Optional<CrucibleSmeltingRecipe> recipe = getCurrentRecipe(i);

            IFluidHandler fluidHandler = CapabilityUtils.fluidHandlerCapability(this);

            if (recipe.isEmpty())
                continue;

            FluidStack result = recipe.get().resultFluid();
            FluidStack fluidInTank = fluidHandler.getFluidInTank(0);

//            if ((fluidInTank.is(result.getFluid()) || fluidInTank.isEmpty())
//                    && fluidInTank.getAmount() + result.getAmount() <= fluidHandler.getTankCapacity(0)
//                    && fluidHandler.isFluidValid(0, result))
//                return true;
        }

        return true;
    }

    private Optional<CrucibleSmeltingRecipe> getCurrentRecipe(int slot) {
        IHeatStorage heatStorage = CapabilityUtils.heatStorageCapability(this);
        IItemHandler itemHandler = CapabilityUtils.itemHandlerCapability(this);
        ItemStack stackInSlot = itemHandler.getStackInSlot(slot);

        if (stackInSlot.isEmpty() || heatStorage == null) return Optional.empty();

        return this.level.getRecipeManager()
                .getRecipeFor(CrucibleSmeltingRecipe.TYPE, new CrucibleSmeltingRecipeInput(stackInSlot, heatStorage.getHeatStored()), level)
                .map(RecipeHolder::value);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
        return new CrucibleMenu(containerId, inventory, this);
    }

    private boolean canInsertFluidIntoOutput(Fluid fluid) {
        if (getFluidTank().isEmpty()) return false;

        FluidStack fluidStack = getFluidTank().getFluidInTank(0);
        return fluidStack.isEmpty() || fluidStack.getFluid().equals(fluid);
    }

    private boolean canInsertAmountIntoOutput(int count) {
        if (getFluidTank().isEmpty()) return false;

        for (int i = 0; i < getFluidTank().getTanks(); i++) {
            FluidStack fluidStack = getFluidTank().getFluidInTank(i);
            if (fluidStack.getAmount() + count <= getFluidTank().getTankCapacity(i))
                return true;
        }
        return false;
    }

    public Optional<IFireboxMultiblock> getFireBox() {
        BlockPos fireboxPos = worldPosition.below();
        if (level.getBlockEntity(fireboxPos) instanceof FireboxBlockEntity fireBox) {
            for (Multiblock multiblock : IRRegistries.MULTIBLOCK) {
                if (multiblock instanceof IFireboxMultiblock fireboxMultiblock) {
                    BlockPos controllerPos = fireboxPos;
                    if (fireBox instanceof FakeBlockEntity fakeBlockEntity) {
                        if (fakeBlockEntity.getActualBlockEntityPos().isPresent()) {
                            controllerPos = fakeBlockEntity.getActualBlockEntityPos().get();
                        }
                    }
                    if (multiblock.isFormed(level, fireboxPos, controllerPos)) {
                        return Optional.of(fireboxMultiblock);
                    }
                }
            }
        }
        return Optional.empty();
    }
}