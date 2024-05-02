package com.indref.industrial_reforged.registries.blockentities.multiblocks.controller;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.FakeBlockEntity;
import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.multiblocks.BlastFurnaceMultiblock;
import com.indref.industrial_reforged.registries.recipes.BlastFurnaceRecipe;
import com.indref.industrial_reforged.registries.screen.BlastFurnaceMenu;
import com.indref.industrial_reforged.util.BlockUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
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
    private Optional<BlockPos> mainControllerPos;

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
        this.mainControllerPos = Optional.of(mainControllerPos);
    }

    public Optional<BlockPos> getMainControllerPos() {
        return mainControllerPos;
    }

    @Override
    public Optional<BlockEntity> getActualBlockEntity() {
        return BlockUtils.blockEntityAt(level, worldPosition);
    }

    @Override
    protected void saveData(CompoundTag tag, HolderLookup.Provider provider) {
        tag.putBoolean("isController", isMainController());
        mainControllerPos.ifPresent(pos -> tag.putLong("mainControllerPos", pos.asLong()));
    }

    @Override
    protected void loadData(CompoundTag tag, HolderLookup.Provider provider) {
        this.mainController = tag.getBoolean("isController");
        long mainControllerPos1 = tag.getLong("mainControllerPos");
        IndustrialReforged.LOGGER.debug("Controller pos long: {}", mainControllerPos1);
        if (mainControllerPos1 != 0) {
            this.mainControllerPos = Optional.of(BlockPos.of(mainControllerPos1));
        } else {
            this.mainControllerPos = Optional.empty();
        }
    }

    @Override
    public void tick() {
        if (isMainController()) {
            List<RecipeHolder<BlastFurnaceRecipe>> recipes = level.getRecipeManager()
                    .getRecipesFor(BlastFurnaceRecipe.Type.INSTANCE, new SimpleContainer(getItemHandlerStacks().orElse(new ItemStack[0])), level);
            IndustrialReforged.LOGGER.debug("Recipes: {}", recipes);
        }
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
