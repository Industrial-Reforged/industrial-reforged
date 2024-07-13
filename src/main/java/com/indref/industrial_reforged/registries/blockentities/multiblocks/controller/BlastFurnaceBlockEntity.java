package com.indref.industrial_reforged.registries.blockentities.multiblocks.controller;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.FakeBlockEntity;
import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.multiblocks.BlastFurnaceMultiblock;
import com.indref.industrial_reforged.registries.recipes.BlastFurnaceRecipe;
import com.indref.industrial_reforged.registries.gui.menus.BlastFurnaceMenu;
import com.indref.industrial_reforged.util.BlockUtils;
import com.indref.industrial_reforged.util.recipes.IngredientWithCount;
import com.indref.industrial_reforged.util.recipes.recipe_inputs.ItemRecipeInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * This is the blockentity for the blast furnace.
 * It is attached to the blast furnace hatch.
 * <p>
 * Since the blast furnace has 4 hatches, only one
 * is the actual blockentity that handles the
 * logic and the others just point to that block.
 */
public class BlastFurnaceBlockEntity extends ContainerBlockEntity implements MenuProvider, FakeBlockEntity {
    private boolean mainController;
    private BlockPos mainControllerPos;
    private int duration;

    public BlastFurnaceBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(IRBlockEntityTypes.BLAST_FURNACE.get(), p_155229_, p_155230_);
        addItemHandler(2);
        addFluidTank(9000);
    }

    public void setMainController(boolean mainController) {
        this.mainController = mainController;
    }

    public boolean isMainController() {
        return mainController;
    }

    public void setMainControllerPos(BlockPos mainControllerPos) {
        this.mainControllerPos = mainControllerPos;
    }

    public Optional<BlockPos> getMainControllerPos() {
        return Optional.ofNullable(mainControllerPos);
    }

    @Override
    public Optional<BlockPos> getActualBlockEntityPos() {
        return getMainControllerPos();
    }

    @Override
    protected void saveData(CompoundTag tag, HolderLookup.Provider provider) {
        tag.putBoolean("isController", isMainController());
        getMainControllerPos().ifPresent(pos -> tag.putLong("mainControllerPos", pos.asLong()));
    }

    @Override
    protected void loadData(CompoundTag tag, HolderLookup.Provider provider) {
        this.mainController = tag.getBoolean("isController");
        long mainControllerPos1 = tag.getLong("mainControllerPos");
        if (mainControllerPos1 != 0) {
            this.mainControllerPos = BlockPos.of(mainControllerPos1);
        }
    }

    public void commonTick() {
        if (isMainController()) {
            Optional<BlastFurnaceRecipe> optRecipe = getCurrentRecipe();
            if (optRecipe.isPresent()) {
                BlastFurnaceRecipe recipe = optRecipe.get();
                int maxDuration = recipe.duration();
                if (duration >= maxDuration) {
                    IndustrialReforged.LOGGER.debug("Recipe has finished");
                    ItemStackHandler itemHandler = getItemHandler();
                    IngredientWithCount ingredient0 = recipe.ingredients().get(0);
                    IngredientWithCount ingredient1 = recipe.ingredients().get(1);
                    ItemStack firstStack = itemHandler.getStackInSlot(0);
                    ItemStack secondStack = itemHandler.getStackInSlot(1);
                    if (ingredient0.test(firstStack)) {
                        // Ingredient refers to slot 0
                        firstStack.shrink(ingredient0.count());
                        secondStack.shrink(ingredient1.count());
                    } else {
                        // Ingredient refers to slot 1
                        secondStack.shrink(ingredient0.count());
                        firstStack.shrink(ingredient1.count());
                    }
                    getFluidTank().fill(recipe.resultFluid(), IFluidHandler.FluidAction.EXECUTE);
                } else {
                    duration++;
                }
            }
        }
    }

    public Optional<BlastFurnaceRecipe> getCurrentRecipe() {
        return level.getRecipeManager()
                .getRecipeFor(BlastFurnaceRecipe.TYPE, new ItemRecipeInput(List.of(getItemHandlerStacks())), level)
                .map(RecipeHolder::value);
    }

    public boolean isFormed() {
        return !level.getBlockState(worldPosition).getValue(BlastFurnaceMultiblock.BRICK_STATE).equals(BlastFurnaceMultiblock.BrickStates.UNFORMED);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Blast Furnace");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        // TODO: Replace NULL
        return new BlastFurnaceMenu(containerId, inventory, this, null);
    }
}
