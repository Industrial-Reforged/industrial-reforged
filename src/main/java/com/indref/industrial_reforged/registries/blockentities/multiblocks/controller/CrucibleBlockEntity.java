package com.indref.industrial_reforged.registries.blockentities.multiblocks.controller;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.api.blocks.container.IHeatBlock;
import com.indref.industrial_reforged.api.tiers.CrucibleTier;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.blocks.multiblocks.CrucibleControllerBlock;
import com.indref.industrial_reforged.registries.recipes.CrucibleSmeltingRecipe;
import com.indref.industrial_reforged.registries.screen.CrucibleMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CrucibleBlockEntity extends ContainerBlockEntity implements MenuProvider, IHeatBlock {
    private final CrucibleTier tier;
    private final ContainerData data;
    private int duration;

    public CrucibleBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRBlockEntityTypes.CRUCIBLE.get(), blockPos, blockState);
        addFluidTank(9000);
        addItemHandler(9);
        this.duration = 0;
        this.tier = ((CrucibleControllerBlock) blockState.getBlock()).getTier();
        // TODO: Serialize duration
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return 0;
            }

            @Override
            public void set(int pIndex, int pValue) {
            }

            @Override
            public int getCount() {
                return 1;
            }
        };
    }

    @Override
    public int getHeatCapacity() {
        return tier.heatCapacity();
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Crucible");
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if (hasRecipe()) {
            increaseCraftingProgress();
            setChanged(level, blockPos, blockState);

            if (hasProgressFinished()) {
                meltItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    private void meltItem() {
        CrucibleSmeltingRecipe recipe = getCurrentRecipe().get().value();
        for (int i = 0; i < getItemHandler().getSlots(); i++) {
            ItemStack itemStack = this.getItemHandler().getStackInSlot(i);
            Item input = recipe.getIngredients().get(0).getItems()[0].getItem();
            if (itemStack.is(input)) {
                itemStack.shrink(1);
                IndustrialReforged.LOGGER.debug("Slot: {}, item: {}", i, input);
                break;
            }
        }
        FluidStack resultFluid = recipe.getResultFluid();
        this.getFluidTank().fill(resultFluid, IFluidHandler.FluidAction.EXECUTE);
    }

    private void resetProgress() {
        this.duration = 0;
    }

    private void increaseCraftingProgress() {
        this.duration++;
    }

    private boolean hasProgressFinished() {
        int maxDuration = getCurrentRecipe().get().value().getDuration();
        return this.duration >= maxDuration;
    }

    public boolean hasRecipe() {

        Optional<RecipeHolder<CrucibleSmeltingRecipe>> recipe = getCurrentRecipe();

        if (recipe.isEmpty()) {
            return false;
        }

        FluidStack result = recipe.get().value().getResultFluid();

        boolean canInsert = canInsertAmountIntoOutput(result.getAmount()) && canInsertFluidIntoOutput(result.getFluid());
        IndustrialReforged.LOGGER.debug("Has recipe");
        return canInsert;
    }

    private Optional<RecipeHolder<CrucibleSmeltingRecipe>> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(this.getItemHandler().getSlots());
        int j = 0;
        for (int i = 0; i < getItemHandler().getSlots(); i++) {
            ItemStack stackInSlot = this.getItemHandler().getStackInSlot(i);
            if (!stackInSlot.isEmpty()) {
                inventory.setItem(j, stackInSlot);
                j++;
            }
        }

        return this.level.getRecipeManager().getRecipeFor(CrucibleSmeltingRecipe.Type.INSTANCE, inventory, level);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
        return new CrucibleMenu(containerId, inventory, this, this.data);
    }

    private boolean canInsertFluidIntoOutput(Fluid fluid) {
        for (int i = 0; i < getFluidTank().getTanks(); i++) {
            FluidStack fluidStack = getFluidTank().getFluidInTank(i);
            if (fluidStack.isEmpty() || fluidStack.getFluid().equals(fluid))
                return true;
        }
        return false;
    }

    private boolean canInsertAmountIntoOutput(int count) {
        for (int i = 0; i < getFluidTank().getTanks(); i++) {
            FluidStack fluidStack = getFluidTank().getFluidInTank(i);
            if (fluidStack.getAmount() + count <= getFluidTank().getTankCapacity(i))
                return true;
        }
        return false;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("duration", duration);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        duration = tag.getInt("duration");
    }
}
