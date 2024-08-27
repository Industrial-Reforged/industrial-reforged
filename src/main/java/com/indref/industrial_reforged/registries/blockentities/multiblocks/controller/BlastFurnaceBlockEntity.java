package com.indref.industrial_reforged.registries.blockentities.multiblocks.controller;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blockentities.multiblock.MultiblockEntity;
import com.indref.industrial_reforged.api.capabilities.IOActions;
import com.indref.industrial_reforged.api.capabilities.heat.IHeatStorage;
import com.indref.industrial_reforged.api.capabilities.item.SidedItemHandler;
import com.indref.industrial_reforged.api.multiblocks.MultiblockData;
import com.indref.industrial_reforged.api.multiblocks.MultiblockLayer;
import com.indref.industrial_reforged.api.blockentities.multiblock.FakeBlockEntity;
import com.indref.industrial_reforged.api.blockentities.container.ContainerBlockEntity;
import com.indref.industrial_reforged.api.blockentities.multiblock.SavesControllerPosBlockEntity;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.multiblocks.BlastFurnaceMultiblock;
import com.indref.industrial_reforged.registries.recipes.BlastFurnaceRecipe;
import com.indref.industrial_reforged.registries.gui.menus.BlastFurnaceMenu;
import com.indref.industrial_reforged.util.capabilities.CapabilityUtils;
import com.indref.industrial_reforged.util.recipes.IngredientWithCount;
import com.indref.industrial_reforged.util.recipes.recipeInputs.ItemRecipeInput;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * This is the blockentity for the blast furnace.
 * It is attached to the blast furnace hatch.
 * <p>
 * Since the blast furnace has 4 hatches, only one
 * is the actual blockentity that handles the
 * logic and the others just point to that block.
 */
public class BlastFurnaceBlockEntity extends ContainerBlockEntity implements MenuProvider, FakeBlockEntity, SavesControllerPosBlockEntity, MultiblockEntity {
    private BlockPos mainControllerPos;
    private int duration;
    private int maxDuration;
    private MultiblockData multiblockData;

    public BlastFurnaceBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(IRBlockEntityTypes.BLAST_FURNACE.get(), p_155229_, p_155230_);
        addItemHandler(2);
        addFluidTank(9000);
        this.multiblockData = MultiblockData.EMPTY;
    }

    @Override
    public BlockPos getActualBlockEntityPos() {
        return mainControllerPos;
    }

    @Override
    public boolean actualBlockEntity() {
        return worldPosition.equals(mainControllerPos);
    }

    public int getProgress() {
        return duration;
    }

    public int getMaxProgress() {
        return maxDuration;
    }

    @Override
    public void setControllerPos(BlockPos blockPos) {
        this.mainControllerPos = blockPos;
    }

    @Override
    protected void saveData(CompoundTag tag, HolderLookup.Provider provider) {
        tag.put("multiblockData", saveMBData());
        BlockPos actualBlockEntityPos = getActualBlockEntityPos();
        if (actualBlockEntityPos != null) {
            tag.putLong("mainControllerPos", actualBlockEntityPos.asLong());
        }
        tag.putInt("duration", this.duration);
    }

    @Override
    public IFluidHandler getFluidHandlerOnSide(Direction direction) {
        if (!isMainController()) {
            BlockEntity be = level.getBlockEntity(mainControllerPos);
            if (be instanceof BlastFurnaceBlockEntity blastFurnaceBlockEntity) {
                return blastFurnaceBlockEntity.getFluidHandlerOnSide(direction);
            }
        }
        return super.getFluidHandlerOnSide(direction);
    }

    // TODO: Export this through the bricks block
    @Override
    public <T> Map<Direction, Pair<IOActions, int[]>> getSidedInteractions(BlockCapability<T, @Nullable Direction> capability) {
        IndustrialReforged.LOGGER.debug("getting sided interaction");
        if (capability == Capabilities.ItemHandler.BLOCK) {
            return Map.of(Direction.UP, Pair.of(IOActions.INSERT, new int[]{0, 1}));
        } else if (capability == Capabilities.FluidHandler.BLOCK) {
            return Map.of(
                    Direction.NORTH, Pair.of(IOActions.EXTRACT, new int[]{0}),
                    Direction.EAST, Pair.of(IOActions.EXTRACT, new int[]{0}),
                    Direction.SOUTH, Pair.of(IOActions.EXTRACT, new int[]{0}),
                    Direction.WEST, Pair.of(IOActions.EXTRACT, new int[]{0})
            );
        }
        return Map.of();
    }

    private boolean isMainController() {
        return worldPosition.equals(mainControllerPos);
    }

    @Override
    protected void loadData(CompoundTag tag, HolderLookup.Provider provider) {
        this.multiblockData = loadMBData(tag);
        long mainControllerPos1 = tag.getLong("mainControllerPos");
        this.mainControllerPos = BlockPos.of(mainControllerPos1);
        this.duration = tag.getInt("duration");
    }

    public void commonTick() {
        if (isMainController()) {
            Optional<BlastFurnaceRecipe> optRecipe = getCurrentRecipe();
            if (optRecipe.isPresent()) {
                BlastFurnaceRecipe recipe = optRecipe.get();
                int maxDuration = recipe.duration();
                this.maxDuration = maxDuration;
                if (duration >= maxDuration) {
                    IFluidHandler fluidHandler = CapabilityUtils.fluidHandlerCapability(this);
                    IItemHandler itemHandler = CapabilityUtils.itemHandlerCapability(this);
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
                    fluidHandler.fill(recipe.resultFluid(), IFluidHandler.FluidAction.EXECUTE);
                    this.duration = 0;
                    this.maxDuration = 0;
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
        return new BlastFurnaceMenu(containerId, inventory, this);
    }

    @Override
    public MultiblockData getMultiblockData() {
        return this.multiblockData;
    }

    @Override
    public void setMultiblockData(MultiblockData data) {
        this.multiblockData = data;
    }
}
