package com.indref.industrial_reforged.content.blockentities.multiblocks.controller;

import com.google.common.collect.ImmutableMap;
import com.indref.industrial_reforged.IRConfig;
import com.indref.industrial_reforged.api.blockentities.PowerableBlockEntity;
import com.indref.industrial_reforged.api.blockentities.IRContainerBlockEntity;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.heat.IHeatStorage;
import com.indref.industrial_reforged.api.tiers.CrucibleTier;
import com.indref.industrial_reforged.client.renderer.item.bar.CrucibleProgressRenderer;
import com.indref.industrial_reforged.content.blockentities.CastingBasinBlockEntity;
import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.data.maps.CastingMoldValue;
import com.indref.industrial_reforged.networking.BasinFluidChangedPayload;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.content.blocks.multiblocks.controller.CrucibleControllerBlock;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.util.BlockUtils;
import com.indref.industrial_reforged.util.IRClientUtils;
import com.indref.industrial_reforged.translations.IRTranslations;
import com.indref.industrial_reforged.util.recipes.IngredientWithCount;
import com.indref.industrial_reforged.content.recipes.CrucibleSmeltingRecipe;
import com.indref.industrial_reforged.content.gui.menus.CrucibleMenu;
import com.indref.industrial_reforged.util.capabilities.CapabilityUtils;
import com.indref.industrial_reforged.util.items.ItemUtils;
import com.portingdeadmods.portingdeadlibs.api.blockentities.multiblocks.MultiblockEntity;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.MultiblockData;
import com.portingdeadmods.portingdeadlibs.api.utils.IOAction;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.entity.BlockEntity;
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
public class CrucibleBlockEntity extends IRContainerBlockEntity implements MenuProvider, MultiblockEntity, PowerableBlockEntity {
    public static final AABB AABB = new AABB(-1, (double) 10 / 16, -1, 2, 1, 2);
    public static final int SLOTS = 9;
    private static final int CASTING_SPEED = 3;
    private final CrucibleTier tier;

    private MultiblockData multiblockData;

    private Item expectedCast;
    // Block that can be filled when turning crucible
    public BlockCapabilityCache<IFluidHandler, Direction> basinFluidHandlerCache;
    // Recipe cache
    private final Int2ObjectMap<CrucibleSmeltingRecipe> recipeCache;

    public float independentAngle;
    public float chasingVelocity;
    public int inUse;
    public int speed;

    private boolean turnedOver;
    private int turnTimer;
    private int maxTurnTime;
    // REDSTONE CONTROL
    private boolean powered;

    public CrucibleBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRBlockEntityTypes.CRUCIBLE.get(), blockPos, blockState);
        this.tier = ((CrucibleControllerBlock) blockState.getBlock()).getTier();
        this.multiblockData = MultiblockData.EMPTY;
        this.recipeCache = new Int2ObjectOpenHashMap<>(SLOTS);
        addItemHandler(9, 1);
        addFluidTank(IRConfig.crucibleFluidCapacity);
        addHeatStorage(IRConfig.crucibleHeatCapacity);
    }

    public void turn() {
        if (!this.turnedOver && canTurn()) {
            BlockEntity blockEntity = level.getBlockEntity(getBasinPos());
            if (blockEntity != null) {
                IItemHandler basinItemHandler = CapabilityUtils.itemHandlerCapability(blockEntity);
                this.expectedCast = basinItemHandler.getStackInSlot(CastingBasinBlockEntity.CAST_SLOT).getItem();
                IFluidHandler basinFluidTank = CapabilityUtils.fluidHandlerCapability(blockEntity);
                IFluidHandler fluidTank = getFluidHandler();
                this.maxTurnTime = Math.min(basinFluidTank.getTankCapacity(0) - basinFluidTank.getFluidInTank(0).getAmount(), fluidTank.getFluidInTank(0).getAmount() / CASTING_SPEED);
                this.inUse = 70;
                this.speed = 70;
                this.turnedOver = true;
            }
        }
    }

    public void turnBack() {
        if (this.inUse == 0) {
            this.inUse = 70;
            this.speed = -70;
            this.turnedOver = false;
            this.turnTimer = 0;
        }
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
    public void setPowered(boolean powered) {
        this.powered = powered;
    }

    @Override
    public boolean isPowered() {
        return powered;
    }

    public boolean canTurn() {
        if (level.getBlockEntity(getBasinPos()) instanceof CastingBasinBlockEntity be) {
            return be.hasMoldAndNotFull();
        }
        return false;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        initCapCache();
    }

    @SuppressWarnings("OptionalIsPresent")
    @Override
    protected void onItemsChanged(int slot) {
        Optional<CrucibleSmeltingRecipe> _recipe = getRecipeForCache(slot);
        if (_recipe.isPresent()) {
            this.recipeCache.put(slot, _recipe.get());
        }
    }

    @Override
    public void onFluidChanged() {
        for (int slot = 0; slot < getItemHandler().getSlots(); slot++) {
            Optional<CrucibleSmeltingRecipe> _recipe = getRecipeForCache(slot);
            if (_recipe.isPresent()) {
                this.recipeCache.put(slot, _recipe.get());
            }
        }
    }

    private Optional<CrucibleSmeltingRecipe> getRecipeForCache(int slot) {
        ItemStack stackInSlot = getItemHandler().getStackInSlot(slot);

        if (stackInSlot.isEmpty()) return Optional.empty();

        if (stackInSlot.is(IRItems.CASTING_SCRAPS)) {
            return Optional.of(new CrucibleSmeltingRecipe(IngredientWithCount.of(IRItems.CASTING_SCRAPS.get(), 1), stackInSlot.get(IRDataComponents.SINGLE_FLUID).copy().fluidStack(), 200, 200));
        }

        return this.level.getRecipeManager()
                .getRecipeFor(CrucibleSmeltingRecipe.TYPE, new SingleRecipeInput(stackInSlot), level)
                .map(RecipeHolder::value);
    }

    private void initCapCache() {
        if (level instanceof ServerLevel serverLevel) {
            this.basinFluidHandlerCache = BlockCapabilityCache.create(
                    Capabilities.FluidHandler.BLOCK,
                    serverLevel,
                    getBasinPos(),
                    Direction.UP,
                    () -> !this.isRemoved(),
                    this::onInvalidateCaps
            );
        }
    }

    public @Nullable CrucibleSmeltingRecipe getCurrentRecipe(int slot) {
        return recipeCache.get(slot);
    }

    private @NotNull BlockPos getBasinPos() {
        Direction direction = getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite();
        return worldPosition.relative(direction, 2);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return IRTranslations.Menus.CRUCIBLE.component();
    }

    public void commonTick() {
        getHeatStorage().setLastHeatStored(getHeatStorage().getHeatStored());

        // Recipe
        int totalIncreasedAmount = 0;
        for (int slot = 0; slot < getItemHandler().getSlots(); slot++) {
            CrucibleSmeltingRecipe recipe = getCurrentRecipe(slot);
            if (!getItemHandler().getStackInSlot(slot).isEmpty()) {
                int increased = hasRecipe(slot, recipe, getFluidTank().getFluid().getAmount() + totalIncreasedAmount);
                if (increased != -1) {
                    totalIncreasedAmount += increased;
                    increaseCraftingProgress(slot, recipe);

                    tryMeltItem(slot, recipe);
                }
            }
        }

        // Item sucking in
        List<ItemEntity> items = getItemsInside();
        suckInItems(items);

        List<LivingEntity> entities = getEntitiesInside();
        boolean containsPlayer = false;
        for (LivingEntity entity : entities) {
            if (entity instanceof Player) {
                containsPlayer = true;
                break;
            }
        }

        if (level.isClientSide()) {
            IRClientUtils.setPlayerInCrucible(containsPlayer ? getFluidHandler().getFluidInTank(0) : null);

            if (getFluidHandler().getFluidInTank(0).getAmount() > 0 && level.random.nextInt(0, 170) == 10) {
                level.playLocalSound(worldPosition, SoundEvents.LAVA_AMBIENT, SoundSource.BLOCKS, 1.75F, 0.75F, true);
            }

        }

        meltEntities(entities);

        turnCrucible();

        fillBlock();

        tickRedstone();

        if (!level.isClientSide()) {
            tickHeat();

            // ensure that the basin has the correct cast
            if (isTurnedOver()) {
                if (level.getBlockEntity(getBasinPos()) instanceof CastingBasinBlockEntity be) {
                    ItemStack castingMold = be.getItemHandler().getStackInSlot(CastingBasinBlockEntity.CAST_SLOT);
                    if (!castingMold.is(this.expectedCast)) {
                        CastingMoldValue mold = CastingBasinBlockEntity.getMold(castingMold.getItem());
                        if (mold != null) {
                            this.maxTurnTime = mold.capacity();
                            this.expectedCast = castingMold.getItem();
                        } else {
                            this.maxTurnTime = 0;
                            this.expectedCast = null;
                        }
                    }
                }
            }
        }

    }

    private void tickRedstone() {
        if (this.isPowered() && !getFluidHandler().getFluidInTank(0).isEmpty()) {
            if (this.inUse == 0) {
                if (!this.isTurnedOver()) {
                    this.turn();
                }
            } else if (this.turnTimer >= this.maxTurnTime) {
                if (this.isTurnedOver()) {
                    this.turnBack();
                }
            }
        }
    }

    public float getHeatDecay() {
        return IRConfig.crucibleHeatDecay;
    }

    protected void tickHeat() {
        float heatDiff = getHeatStorage().getHeatStored() - getHeatStorage().getLastHeatStored();
        if (heatDiff <= 0) {
            getHeatStorage().drain(getHeatDecay() + Math.abs(getHeatDecay() * heatDiff), false);
        }
    }

    private void fillBlock() {
        if (!level.isClientSide()) {
            if (turnedOver && inUse == 0 && basinFluidHandlerCache != null) {
                IFluidHandler fluidHandler = basinFluidHandlerCache.getCapability();

                if (fluidHandler != null) {
                    FluidStack drained = getFluidHandler().drain(CASTING_SPEED, IFluidHandler.FluidAction.EXECUTE);
                    int filled = fluidHandler.fill(drained, IFluidHandler.FluidAction.EXECUTE);
                    int remaining = drained.getAmount() - filled;
                    getFluidTank().fill(getFluidTank().getFluidInTank(0).copyWithAmount(remaining), IFluidHandler.FluidAction.EXECUTE);
                    PacketDistributor.sendToAllPlayers(
                            new BasinFluidChangedPayload(getBasinPos(), fluidHandler.getFluidInTank(0).getAmount())
                    );
                }
            }
        }
    }

    private void meltEntities(List<LivingEntity> entities) {
        // TODO: Use actual values
        if (getHeatStorage().getHeatStored() > 100) {
            if (level.getGameTime() % 25 == 0) {
                for (LivingEntity entity : entities) {
                    if (getHeatStorage().getHeatStored() > 100) {
                        entity.hurt(level.damageSources().lava(), 7);
                        getHeatStorage().drain(12, false);
                    } else {
                        return;
                    }
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
                    this.chasingVelocity = 0;
                    this.independentAngle = 0;
                }
            }
        } else if (this.turnedOver) {
            if (this.turnTimer >= this.maxTurnTime) {
                turnBack();
            }
            this.turnTimer++;
        }
    }

    private void onInvalidateCaps() {
        initCapCache();
    }

    @Override
    public <T> ImmutableMap<Direction, Pair<IOAction, int[]>> getSidedInteractions(BlockCapability<T, @Nullable Direction> capability) {
        if (capability == IRCapabilities.HeatStorage.BLOCK) {
            return ImmutableMap.of(
                    Direction.DOWN, Pair.of(IOAction.INSERT, new int[]{0})
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

    private void tryMeltItem(int slot, CrucibleSmeltingRecipe recipe) {
        IItemHandler handler = getItemHandler();
        if (recipe != null && this.getHeatStorage().getHeatStored() >= recipe.heat()) {
            ItemStack itemStack = handler.getStackInSlot(slot);
            CompoundTag tag = ItemUtils.getImmutableTag(itemStack).copyTag();
            if (recipe.ingredient().test(itemStack) && tag.getInt(CrucibleProgressRenderer.BARWIDTH_KEY) >= 10) {
                handler.extractItem(slot, 1, false);
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

    private void increaseCraftingProgress(int slot, CrucibleSmeltingRecipe recipe) {
        IItemHandler itemStackHandler = getItemHandler();
        IHeatStorage heatStorage = getHeatStorage();

        if (itemStackHandler == null || heatStorage == null) return;

        // TODO: Clean this up to reduce amount of logic
        ItemStack itemStack = itemStackHandler.getStackInSlot(slot);
        CompoundTag tag = ItemUtils.getImmutableTag(itemStack).copyTag();
        if (recipe != null) {
            if (heatStorage.getHeatStored() >= recipe.heat()) {
                IngredientWithCount input = recipe.ingredient();
                if (input.test(itemStack)) {
                    if (tag.getInt(CrucibleProgressRenderer.IS_MELTING_KEY) == 0) {
                        tag.putInt(CrucibleProgressRenderer.IS_MELTING_KEY, 1);
                        itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                    }
                    float pValue = tag.getFloat(CrucibleProgressRenderer.BARWIDTH_KEY) + ((float) 1 / recipe.duration()) * 6;
                    if (pValue < 0) pValue = 0;
                    tag.putFloat(CrucibleProgressRenderer.BARWIDTH_KEY, pValue);
                    itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                }
            }
        } else if (!itemStack.isEmpty()) {
            if (tag.getInt(CrucibleProgressRenderer.IS_MELTING_KEY) == 0) {
                tag.putInt(CrucibleProgressRenderer.IS_MELTING_KEY, 2);
                itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
            }
        }
    }

    /**
     * @param slot              slot for which we need to check the recipe
     * @param recipe            the recipe in the slot
     * @param actualFluidAmount the actual amount of fluid in the fluid tank
     * @return the amount of fluid that will be added to the fluid tank, returns -1 if we cant find a recipe
     */
    public int hasRecipe(int slot, CrucibleSmeltingRecipe recipe, int actualFluidAmount) {
        if (recipe != null) {
            IFluidHandler fluidHandler = getFluidHandler();

            FluidStack result = recipe.resultFluid();
            FluidStack fluidInTank = fluidHandler.getFluidInTank(0);

            if ((fluidInTank.is(result.getFluid()) || fluidInTank.isEmpty())
                    && actualFluidAmount + result.getAmount() <= fluidHandler.getTankCapacity(0)
                    && fluidHandler.isFluidValid(0, result)) {
                return recipe.resultFluid().getAmount();
            }
        }
        return -1;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
        return new CrucibleMenu(containerId, inventory, this);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();

        if (level.isClientSide) {
            IRClientUtils.setPlayerInCrucible(null);
        }
    }

    @Override
    protected void loadData(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        super.loadData(nbt, provider);
        this.multiblockData = loadMBData(nbt.getCompound("multiblockData"));
        this.powered = nbt.getBoolean("powered");
        this.turnTimer = nbt.getInt("turnTimer");
        this.maxTurnTime = nbt.getInt("maxTurnTime");
        // TODO: Serialize animation data
    }

    @Override
    protected void saveData(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        super.saveData(nbt, provider);
        nbt.put("multiblockData", saveMBData());
        nbt.putBoolean("powered", this.powered);
        nbt.putInt("turnTimer", this.turnTimer);
        nbt.putInt("maxTurnTime", this.maxTurnTime);
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
