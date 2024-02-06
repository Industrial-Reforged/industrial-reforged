package com.indref.industrial_reforged.registries.blockentities;

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
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CrucibleBlockEntity extends BlockEntity implements MenuProvider {
    private final CrucibleTier tier;
    protected final ContainerData data;

    private final ItemStackHandler itemHandler = new ItemStackHandler(9) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()) {
                level.setBlockAndUpdate(worldPosition, getBlockState());
            }
        }
    };

    private final FluidTank fluidTank = new FluidTank(9000) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if (!level.isClientSide()) {
                level.setBlockAndUpdate(worldPosition, getBlockState());
            }
        }
    };

    public CrucibleBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRBlockEntityTypes.CRUCIBLE.get(), blockPos, blockState);
        this.tier = ((CrucibleControllerBlock) blockState.getBlock()).getTier();
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

    public int getHeatCapacity() {
        return tier.heatCapacity();
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("Crucible");
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public FluidTank getFluidTank() {
        return fluidTank;
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        /*
        if (hasRecipe()) {
            increaseCraftingProgress();
            setChanged(level, blockPos, blockState);

            if (hasProgressFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
        */
    }

    public boolean hasRecipe() {
        Optional<RecipeHolder<CrucibleSmeltingRecipe>> recipe = getCurrentRecipe();

        if (recipe.isEmpty()) {
            return false;
        }

        FluidStack result = recipe.get().value().getResultFluid();

        return canInsertAmountIntoOutput(result.getAmount()) && canInsertFluidIntoOutput(result.getFluid());
    }

    private Optional<RecipeHolder<CrucibleSmeltingRecipe>> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));
        }

        return this.level.getRecipeManager().getRecipeFor(CrucibleSmeltingRecipe.Type.INSTANCE, inventory, level);
    }


    private boolean canInsertFluidIntoOutput(Fluid fluid) {
        for (int i = 0; i < fluidTank.getTanks(); i++) {
            FluidStack fluidStack = fluidTank.getFluidInTank(i);
            if (fluidStack.isEmpty() || fluidStack.getFluid().equals(fluid))
                return true;
        }
        return false;
    }

    private boolean canInsertAmountIntoOutput(int count) {
        for (int i = 0; i < fluidTank.getTanks(); i++) {
            FluidStack fluidStack = fluidTank.getFluidInTank(i);
            if (fluidStack.getAmount() + count <= fluidTank.getTankCapacity(i))
                return true;
        }
        return false;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag = fluidTank.writeToNBT(pTag);
        super.saveAdditional(pTag);
    }

    public FluidStack getFluid() {
        return fluidTank.getFluid();
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        fluidTank.readFromNBT(pTag);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
        return new CrucibleMenu(containerId, inventory, this, this.data);
    }
}
