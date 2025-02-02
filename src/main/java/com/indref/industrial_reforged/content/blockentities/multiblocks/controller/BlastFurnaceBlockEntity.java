package com.indref.industrial_reforged.content.blockentities.multiblocks.controller;

import com.google.common.collect.ImmutableMap;
import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blockentities.container.IRContainerBlockEntity;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.content.recipes.BlastFurnaceRecipe;
import com.indref.industrial_reforged.content.gui.menus.BlastFurnaceMenu;
import com.indref.industrial_reforged.util.recipes.IngredientWithCount;
import com.indref.industrial_reforged.util.recipes.recipeInputs.ItemRecipeInput;
import com.portingdeadmods.portingdeadlibs.api.blockentities.multiblocks.FakeBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.blockentities.multiblocks.MultiblockEntity;
import com.portingdeadmods.portingdeadlibs.api.blockentities.multiblocks.SavesControllerPosBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.MultiblockData;
import com.portingdeadmods.portingdeadlibs.api.utils.IOAction;
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

import java.util.ArrayList;
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
public class BlastFurnaceBlockEntity extends IRContainerBlockEntity implements MenuProvider, FakeBlockEntity, SavesControllerPosBlockEntity, MultiblockEntity {
    public static final int HEAT_USAGE = 500;

    private BlockPos mainControllerPos;
    private float duration;
    private int maxDuration;
    private MultiblockData multiblockData;

    public BlastFurnaceBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(IRBlockEntityTypes.BLAST_FURNACE.get(), p_155229_, p_155230_);
        addItemHandler(2);
        addFluidTank(9000);
        addHeatStorage(2000);
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
        return (int) duration;
    }

    public int getMaxProgress() {
        return maxDuration;
    }

    @Override
    public void setControllerPos(BlockPos blockPos) {
        this.mainControllerPos = blockPos;
    }

    public int getHeight() {
        return multiblockData.layers().length;
    }

    public int getBaseHeight() {
        return 4;
    }

    @Override
    protected void loadData(CompoundTag tag, HolderLookup.Provider provider) {
        this.multiblockData = loadMBData(tag.getCompound("multiblockData"));
        long mainControllerPos1 = tag.getLong("mainControllerPos");
        this.mainControllerPos = BlockPos.of(mainControllerPos1);
        this.duration = tag.getFloat("duration");
    }

    @Override
    protected void saveData(CompoundTag tag, HolderLookup.Provider provider) {
        tag.put("multiblockData", saveMBData());
        BlockPos actualBlockEntityPos = getActualBlockEntityPos();
        if (actualBlockEntityPos != null) {
            tag.putLong("mainControllerPos", actualBlockEntityPos.asLong());
        }
        tag.putFloat("duration", this.duration);
    }

    @Override
    public IFluidHandler getFluidHandlerOnSide(Direction direction) {
        if (!isMainController() && mainControllerPos != null) {
            BlockEntity be = level.getBlockEntity(mainControllerPos);
            if (be instanceof BlastFurnaceBlockEntity blastFurnaceBlockEntity) {
                return blastFurnaceBlockEntity.getFluidHandlerOnSide(direction);
            }
        }
        return super.getFluidHandlerOnSide(direction);
    }

    @Override
    public IItemHandler getItemHandlerOnSide(Direction direction) {
        if (!isMainController() && mainControllerPos != null) {
            BlockEntity be = level.getBlockEntity(mainControllerPos);
            if (be instanceof BlastFurnaceBlockEntity blastFurnaceBlockEntity) {
                return blastFurnaceBlockEntity.getItemHandlerOnSide(direction);
            }
        }
        return super.getItemHandlerOnSide(direction);
    }

    @Override
    public <T> ImmutableMap<Direction, Pair<IOAction, int[]>> getSidedInteractions(BlockCapability<T, @Nullable Direction> capability) {
        if (capability == Capabilities.ItemHandler.BLOCK) {
            return ImmutableMap.of(Direction.UP, Pair.of(IOAction.INSERT, new int[]{0, 1}));
        } else if (capability == Capabilities.FluidHandler.BLOCK) {
            return ImmutableMap.of(
                    Direction.NORTH, Pair.of(IOAction.EXTRACT, new int[]{0}),
                    Direction.EAST, Pair.of(IOAction.EXTRACT, new int[]{0}),
                    Direction.SOUTH, Pair.of(IOAction.EXTRACT, new int[]{0}),
                    Direction.WEST, Pair.of(IOAction.EXTRACT, new int[]{0})
            );
        } else if (capability == IRCapabilities.HeatStorage.BLOCK) {
            return ImmutableMap.of(
                    Direction.DOWN, Pair.of(IOAction.INSERT, new int[]{0})
            );
        }
        return ImmutableMap.of();
    }

    private boolean isMainController() {
        return worldPosition.equals(mainControllerPos);
    }

    public void commonTick() {
        if (isMainController()) {
            Optional<BlastFurnaceRecipe> optRecipe = getCurrentRecipe();
            if (optRecipe.isPresent() && getHeatStorage().getHeatStored() > HEAT_USAGE) {
                BlastFurnaceRecipe recipe = optRecipe.get();
                int maxDuration = recipe.duration();
                this.maxDuration = maxDuration;
                if (duration >= maxDuration) {
                    IFluidHandler fluidHandler = getFluidHandler();
                    IItemHandler itemHandler = getItemHandler();

                    List<IngredientWithCount> ingredients = new ArrayList<>(recipe.ingredients());

                    for (IngredientWithCount ingredient : recipe.ingredients()) {
                        for (int i = 0; i < itemHandler.getSlots(); i++) {
                            ItemStack itemStack = itemHandler.getStackInSlot(i);
                            if (!itemStack.isEmpty()){
                                if (ingredient.test(itemStack) && ingredients.contains(ingredient)) {
                                    itemHandler.extractItem(i, ingredient.count(), false);
                                    ingredients.remove(ingredient);
                                    break;
                                }
                            }
                        }
                    }

                    fluidHandler.fill(recipe.resultFluid(), IFluidHandler.FluidAction.EXECUTE);
                    this.duration = 0;
                    this.maxDuration = 0;
                } else {
                    float progress = (float) getHeight() / getBaseHeight();
                    duration += progress;
                }
            }
        }
    }

    public Optional<BlastFurnaceRecipe> getCurrentRecipe() {
        return level.getRecipeManager()
                .getRecipeFor(BlastFurnaceRecipe.TYPE, new ItemRecipeInput(getNonEmptyStacks()), level)
                .map(RecipeHolder::value);
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
