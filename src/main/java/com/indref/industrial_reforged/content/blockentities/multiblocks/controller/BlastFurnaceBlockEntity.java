package com.indref.industrial_reforged.content.blockentities.multiblocks.controller;

import com.google.common.collect.ImmutableMap;
import com.indref.industrial_reforged.IRConfig;
import com.indref.industrial_reforged.api.blockentities.IRContainerBlockEntity;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.content.multiblocks.BlastFurnaceMultiblock;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.content.recipes.BlastFurnaceRecipe;
import com.indref.industrial_reforged.content.menus.BlastFurnaceMenu;
import com.indref.industrial_reforged.translations.IRTranslations;
import com.indref.industrial_reforged.util.recipes.IngredientWithCount;
import com.indref.industrial_reforged.content.recipes.recipeInputs.ItemRecipeInput;
import com.portingdeadmods.portingdeadlibs.api.blockentities.multiblocks.FakeBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.blockentities.multiblocks.MultiblockEntity;
import com.portingdeadmods.portingdeadlibs.api.blockentities.multiblocks.SavesControllerPosBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.MultiblockData;
import com.portingdeadmods.portingdeadlibs.api.utils.IOAction;
import com.portingdeadmods.portingdeadlibs.utils.BlockUtils;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
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
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the blockentity for the blast furnace.
 * It is attached to the blast furnace hatch.
 * <p>
 * Since the blast furnace has 4 hatches, only one
 * is the actual blockentity that handles the
 * logic and the others just point to that block.
 */

// TODO: Make blast furnace produce slag
public class BlastFurnaceBlockEntity extends IRContainerBlockEntity implements MenuProvider, FakeBlockEntity, SavesControllerPosBlockEntity, MultiblockEntity {
    private BlockPos mainControllerPos;
    private float duration;
    private boolean active;
    private MultiblockData multiblockData;
    private BlastFurnaceRecipe recipe;

    public BlastFurnaceBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRBlockEntityTypes.BLAST_FURNACE.get(), blockPos, blockState);
        addItemHandler(2);
        addFluidTank(IRConfig.blastFurnaceFluidCapacity);
        addHeatStorage(IRConfig.blastFurnaceHeatCapacity);
        this.multiblockData = MultiblockData.EMPTY;
    }

    @Override
    protected void onItemsChanged(int slot) {
        super.onItemsChanged(slot);

        this.recipe = getRecipeForCache();
    }

    @Override
    public void onFluidChanged() {
        super.onFluidChanged();

        this.recipe = getRecipeForCache();
    }

    @Override
    public void onLoad() {
        super.onLoad();

        this.recipe = getRecipeForCache();
    }

    private BlastFurnaceRecipe getRecipeForCache() {
        BlastFurnaceRecipe blastFurnaceRecipe = level.getRecipeManager().getRecipeFor(BlastFurnaceRecipe.TYPE, new ItemRecipeInput(getNonEmptyStacks()), level)
                .map(RecipeHolder::value)
                .orElse(null);
        if (blastFurnaceRecipe != null) {
            FluidStack resource = blastFurnaceRecipe.resultFluid();
            if (getFluidHandler().fill(resource, IFluidHandler.FluidAction.SIMULATE) == resource.getAmount()) {
                return blastFurnaceRecipe;
            }
        }
        return null;
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
        return recipe != null ? recipe.duration() : 0;
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

    private boolean isMainController() {
        return worldPosition.equals(mainControllerPos);
    }

    public void setBlockActive(boolean value) {
        level.setBlockAndUpdate(worldPosition, getBlockState().setValue(BlastFurnaceMultiblock.ACTIVE, value));
        for (BlockPos pos : BlockUtils.getBlocksAroundSelf3x3(worldPosition)) {
            if (level.getBlockEntity(pos) instanceof BlastFurnaceBlockEntity be) {
                if (be.getActualBlockEntityPos() != null && be.getActualBlockEntityPos().equals(worldPosition)) {
                    level.setBlockAndUpdate(pos, level.getBlockState(pos).setValue(BlastFurnaceMultiblock.ACTIVE, value));
                }
            }
        }
    }

    public void commonTick() {
        getHeatStorage().setLastHeatStored(getHeatStorage().getHeatStored());

        if (isMainController()) {
            if (recipe != null && getHeatStorage().getHeatStored() >= recipe.heat()) {
                boolean nextActive = true;
                if (this.active != nextActive) {
                    setBlockActive(nextActive);
                }
                this.active = nextActive;
                if (duration >= recipe.duration()) {
                    IFluidHandler fluidHandler = getFluidHandler();
                    IItemHandler itemHandler = getItemHandler();

                    NonNullList<IngredientWithCount> ingredients1 = NonNullList.copyOf(recipe.ingredients());
                    List<IngredientWithCount> ingredients = new ArrayList<>(ingredients1);

                    FluidStack resultFluid = recipe.resultFluid().copy();

                    for (IngredientWithCount ingredient : ingredients1) {
                        for (int i = 0; i < itemHandler.getSlots(); i++) {
                            ItemStack itemStack = itemHandler.getStackInSlot(i);
                            if (!itemStack.isEmpty()) {
                                if (ingredient.test(itemStack) && ingredients.contains(ingredient)) {
                                    itemHandler.extractItem(i, ingredient.count(), false);
                                    ingredients.remove(ingredient);
                                    break;
                                }
                            }
                        }
                    }

                    fluidHandler.fill(resultFluid, IFluidHandler.FluidAction.EXECUTE);
                    this.duration = 0;
                } else {
                    float progress = (float) getHeight() / getBaseHeight();
                    duration += progress;
                }
            } else {
                boolean nextActive = false;
                if (this.active != nextActive) {
                    setBlockActive(nextActive);
                }
                this.active = nextActive;
                this.duration = 0;
            }

            if (!level.isClientSide()) {
                tickHeat();
            }
        }
    }

    public float getHeatDecay() {
        return IRConfig.blastFurnaceHeatDecay;
    }

    protected void tickHeat() {
        float heatDiff = getHeatStorage().getHeatStored() - getHeatStorage().getLastHeatStored();
        if (heatDiff <= 0) {
            getHeatStorage().drain(getHeatDecay() + Math.abs(getHeatDecay() * heatDiff), false);
        }
    }

    @Override
    public @NotNull Component getDisplayName() {
        return IRTranslations.Menus.BLAST_FURNACE.component();
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

    @Override
    protected void loadData(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadData(tag, provider);
        this.multiblockData = loadMBData(tag.getCompound("multiblockData"));
        long mainControllerPos1 = tag.getLong("mainControllerPos");
        if (tag.getBoolean("hasControllerPos")) {
            this.mainControllerPos = BlockPos.of(mainControllerPos1);
        }
        this.duration = tag.getFloat("duration");
        this.active = tag.getBoolean("active");
    }

    @Override
    protected void saveData(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveData(tag, provider);
        tag.put("multiblockData", saveMBData());
        BlockPos actualBlockEntityPos = getActualBlockEntityPos();
        if (actualBlockEntityPos != null) {
            tag.putLong("mainControllerPos", actualBlockEntityPos.asLong());
        }
        tag.putBoolean("hasControllerPos", actualBlockEntityPos != null);
        tag.putFloat("duration", this.duration);
        tag.putBoolean("active", this.active);
    }

}
