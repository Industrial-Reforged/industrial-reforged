package com.indref.industrial_reforged.content.blockentities.multiblocks.controller;

import com.google.common.collect.ImmutableMap;
import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blockentities.container.ContainerBlockEntity;
import com.indref.industrial_reforged.api.blockentities.multiblock.MultiblockEntity;
import com.indref.industrial_reforged.api.capabilities.IOActions;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.heat.IHeatStorage;
import com.indref.industrial_reforged.api.multiblocks.MultiblockData;
import com.indref.industrial_reforged.api.tiers.CrucibleTier;
import com.indref.industrial_reforged.client.renderer.item.bar.CrucibleProgressRenderer;
import com.indref.industrial_reforged.networking.CrucibleMeltingProgressPayload;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.content.blocks.multiblocks.controller.CrucibleControllerBlock;
import com.indref.industrial_reforged.util.recipes.IngredientWithCount;
import com.indref.industrial_reforged.content.recipes.CrucibleSmeltingRecipe;
import com.indref.industrial_reforged.content.gui.menus.CrucibleMenu;
import com.indref.industrial_reforged.util.capabilities.CapabilityUtils;
import com.indref.industrial_reforged.util.ItemUtils;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

// TODO: Make it so heat to main controller heats up most and the other blocks only heat up by ~60% of that
public class CrucibleBlockEntity extends ContainerBlockEntity implements MenuProvider, MultiblockEntity {
    public static final AABB AABB = new AABB(-1, 0.25, -1, 2, 1, 2);
    private final CrucibleTier tier;
    private MultiblockData multiblockData;

    // Block that can be filled when turning crucible
    public BlockCapabilityCache<IFluidHandler, Direction> fillBlockCache;

    public float independentAngle;
    public float chasingVelocity;
    public int inUse;
    public int speed;

    private boolean turnedOver;

    private int tempTimer;

    public CrucibleBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRBlockEntityTypes.CRUCIBLE.get(), blockPos, blockState);
        this.tier = ((CrucibleControllerBlock) blockState.getBlock()).getTier();
        this.multiblockData = MultiblockData.EMPTY;
        addItemHandler(9, 1);
        addFluidTank(9000);
        addHeatStorage(tier.getHeatCapacity());
    }

    public void turn() {
        if (!this.turnedOver) {
            this.inUse = 70;
            this.speed = 70;
            this.turnedOver = true;
        }
    }

    public void turnBack() {
        if (this.inUse == 0) {
            this.inUse = 70;
            this.speed = -70;
            this.turnedOver = false;
            this.tempTimer = 0;
        }
    }

    public void reset() {
        this.inUse = 0;
        this.speed = 0;
        this.independentAngle = 0;
        this.chasingVelocity = 0;
        this.turnedOver = false;
    }

    private float getSpeed() {
        return speed;
    }

    public float getIndependentAngle(float partialTicks) {
        return (independentAngle + partialTicks * chasingVelocity) / 360;
    }

    public boolean isTurnedOver() {
        return turnedOver;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        initCapCache();
    }

    private void initCapCache() {
        if (level instanceof ServerLevel serverLevel) {
            Direction direction = getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite();
            BlockPos pos = worldPosition.relative(direction, 2);
            this.fillBlockCache = BlockCapabilityCache.create(
                    Capabilities.FluidHandler.BLOCK,
                    serverLevel,
                    pos,
                    Direction.UP,
                    () -> !this.isRemoved(),
                    this::onInvalidateCaps
            );
            IndustrialReforged.LOGGER.debug("created cache at: {}", pos);
        }
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Crucible");
    }

    public void commonTick() {
        // Recipe
        if (hasRecipe()) {
            increaseCraftingProgress();
            setChanged(level, worldPosition, getBlockState());

            tryMeltItem();
        }

        // Item sucking in
        List<ItemEntity> items = getItemsInside();
        suckInItems(items);

        List<LivingEntity> entities = getEntitiesInside();
        meltEntities(entities);

        turnCrucible();

        fillBlock();
    }

    private void fillBlock() {
        if (!level.isClientSide()) {
            if (turnedOver && fillBlockCache != null) {
                IFluidHandler fluidHandler = fillBlockCache.getCapability();

                if (fluidHandler != null) {
                    FluidStack drained = getFluidHandler().drain(20, IFluidHandler.FluidAction.EXECUTE);
                    fluidHandler.fill(drained, IFluidHandler.FluidAction.EXECUTE);
                }
            }
        }
    }

    private void meltEntities(List<LivingEntity> entities) {
        // TODO: Use actual values
        if (getHeatStorage().getHeatStored() > 100) {
            for (LivingEntity entity : entities) {
                if (getHeatStorage().getHeatStored() > 100) {
                    entity.hurt(level.damageSources().lava(), 7);
                    getHeatStorage().tryDrainHeat(12, false);
                } else {
                    return;
                }
            }
        }
    }

    private void suckInItems(List<ItemEntity> items) {
        ItemStackHandler handler = getItemStackHandler();
        for (ItemEntity itemEntity : items) {
            ItemStack itemStack = itemEntity.getItem();
            for (int i = 0; i < handler.getSlots(); i++) {
                if (handler.getStackInSlot(i).isEmpty()) {
                    handler.setStackInSlot(i, itemStack.copyWithCount(1));
                    itemStack.shrink(1);
                }
            }
        }
    }

    private void turnCrucible() {
        float actualSpeed = getSpeed();
        chasingVelocity += ((actualSpeed * 10 / 3f) - chasingVelocity) * .25f;
        independentAngle += chasingVelocity;

        if (inUse > 0) {
            inUse--;

            if (inUse == 0) {
                this.speed = 0;
                if (!turnedOver) {
                    this.independentAngle = 0;
                }
            }
        } else if (this.turnedOver) {
            if (this.tempTimer >= 300) {
                turnBack();
            }
            this.tempTimer++;
        }
    }

    private void onInvalidateCaps() {
        initCapCache();
    }

    @Override
    public <T> ImmutableMap<Direction, Pair<IOActions, int[]>> getSidedInteractions(BlockCapability<T, @Nullable Direction> capability) {
        if (capability == IRCapabilities.HeatStorage.BLOCK) {
            return ImmutableMap.of(
                    Direction.DOWN, Pair.of(IOActions.INSERT, new int[0])
            );
        }
        return ImmutableMap.of();
    }

    private List<ItemEntity> getItemsInside() {
        AABB area = AABB.move(worldPosition);
        return level != null ? level.getEntitiesOfClass(ItemEntity.class, area) : Collections.emptyList();
    }

    private List<LivingEntity> getEntitiesInside() {
        AABB area = AABB.move(worldPosition);
        return level != null ? level.getEntitiesOfClass(LivingEntity.class, area) : Collections.emptyList();
    }

    private void tryMeltItem() {
        IItemHandler handler = getItemHandler();
        for (int i = 0; i < handler.getSlots(); i++) {
            Optional<CrucibleSmeltingRecipe> recipeOptional = this.getCurrentRecipe(i);
            if (recipeOptional.isPresent() && this.getHeatStorage().getHeatStored() >= recipeOptional.get().heat()) {
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

        // TODO: Clean this up to reduce amount of logic
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            Optional<CrucibleSmeltingRecipe> recipe = getCurrentRecipe(i);
            ItemStack itemStack = itemStackHandler.getStackInSlot(i);
            CompoundTag tag = ItemUtils.getImmutableTag(itemStack).copyTag();
            if (recipe.isPresent()) {
                if (heatStorage.getHeatStored() >= recipe.get().heat()) {
                    IngredientWithCount input = recipe.get().ingredient();
                    if (input.test(itemStack)) {
                        if (tag.getInt(CrucibleProgressRenderer.IS_MELTING_KEY) == 0) {
                            tag.putInt(CrucibleProgressRenderer.IS_MELTING_KEY, 1);
                            itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                        }
                        float pValue = tag.getFloat(CrucibleProgressRenderer.BARWIDTH_KEY) + ((float) 1 / recipe.get().duration()) * 6;
                        if (pValue < 0) pValue = 0;
                        tag.putFloat(CrucibleProgressRenderer.BARWIDTH_KEY, pValue);
                        itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                        PacketDistributor.sendToAllPlayers(new CrucibleMeltingProgressPayload(worldPosition, i, pValue));
                    }
                }
            } else if (!itemStack.isEmpty()) {
                if (tag.getInt(CrucibleProgressRenderer.IS_MELTING_KEY) == 0) {
                    tag.putInt(CrucibleProgressRenderer.IS_MELTING_KEY, 2);
                    itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                }
            }
        }
    }

    public boolean hasRecipe() {
        for (int i = 0; i < getItemHandler().getSlots(); i++) {
            Optional<CrucibleSmeltingRecipe> recipe = getCurrentRecipe(i);

            if (recipe.isEmpty())
                continue;

            IFluidHandler fluidHandler = CapabilityUtils.fluidHandlerCapability(this);

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
                .getRecipeFor(CrucibleSmeltingRecipe.TYPE, new SingleRecipeInput(stackInSlot), level)
                .map(RecipeHolder::value);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
        return new CrucibleMenu(containerId, inventory, this);
    }

    // TODO: Refactor these two methods using FluidHandler extract methods
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

    @Override
    protected void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        super.loadAdditional(nbt, provider);
        this.multiblockData = loadMBData(nbt.getCompound("multiblockData"));
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        super.saveAdditional(nbt, provider);
        nbt.put("multiblockData", saveMBData());
    }

    @Override
    public MultiblockData getMultiblockData() {
        return multiblockData;
    }

    @Override
    public void setMultiblockData(MultiblockData data) {
        this.multiblockData = data;
    }
}