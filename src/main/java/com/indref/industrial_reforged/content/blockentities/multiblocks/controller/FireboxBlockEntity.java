package com.indref.industrial_reforged.content.blockentities.multiblocks.controller;

import com.google.common.collect.ImmutableMap;
import com.indref.industrial_reforged.api.blockentities.container.IRContainerBlockEntity;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.heat.IHeatStorage;
import com.indref.industrial_reforged.api.tiers.FireboxTier;
import com.indref.industrial_reforged.content.multiblocks.IFireboxMultiblock;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.content.blocks.multiblocks.parts.FireboxPartBlock;
import com.indref.industrial_reforged.content.gui.menus.FireBoxMenu;
import com.indref.industrial_reforged.tiers.FireboxTiers;
import com.indref.industrial_reforged.util.capabilities.CapabilityUtils;
import com.portingdeadmods.portingdeadlibs.api.blockentities.multiblocks.MultiblockEntity;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.MultiblockData;
import com.portingdeadmods.portingdeadlibs.api.utils.IOAction;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class FireboxBlockEntity extends IRContainerBlockEntity implements MenuProvider, MultiblockEntity {
    private static final int INPUT_SLOT = 0;

    private int burnTime;
    private int maxBurnTime;
    protected MultiblockData multiblockData;
    private final FireboxTier fireboxTier;

    private Map<BlockPos, BlockCapabilityCache<IHeatStorage, Direction>> aboveBlockCapCache;

    public FireboxBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, FireboxTier fireboxTier, int heatCapacity) {
        super(blockEntityType, blockPos, blockState);
        addItemHandler(1, (slot, itemStack) -> itemStack.getBurnTime(RecipeType.SMELTING) > 0);
        addHeatStorage(heatCapacity);
        this.fireboxTier = fireboxTier;
        this.multiblockData = MultiblockData.EMPTY;
        this.aboveBlockCapCache = new HashMap<>();
    }

    public FireboxBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(IRBlockEntityTypes.FIREBOX.get(), blockPos, blockState, FireboxTiers.REFRACTORY, 4000);
    }

    @Override
    public void onLoad() {
        if (level instanceof ServerLevel serverLevel) {
            BlockPos[] offsets = {worldPosition, worldPosition.north(), worldPosition.south(), worldPosition.east(), worldPosition.west(),
                    worldPosition.north().east(), worldPosition.north().west(), worldPosition.south().east(), worldPosition.south().west()};
            for (BlockPos pos : offsets) {
                BlockPos above = pos.above();
                this.aboveBlockCapCache.put(above, BlockCapabilityCache.create(IRCapabilities.HeatStorage.BLOCK, serverLevel, above, Direction.DOWN));
            }
        }
        super.onLoad();
    }

    public boolean isActive() {
        return this.burnTime > 0;
    }

    public int getProductionAmount() {
        return 3;
    }

    @Override
    public void onItemsChanged(int slot) {
        IItemHandler itemHandler = getItemHandler();
        if (itemHandler != null) {
            ItemStack stack = itemHandler.getStackInSlot(slot);
            int burnTime = stack.getBurnTime(RecipeType.SMELTING);
            if (burnTime > 0 && this.burnTime <= 0) {
                this.burnTime = burnTime;
                this.maxBurnTime = burnTime;
                stack.shrink(1);
                setBlockActive(true);
            }
        }
    }

    @Override
    public <T> ImmutableMap<Direction, Pair<IOAction, int[]>> getSidedInteractions(BlockCapability<T, @Nullable Direction> capability) {
        if (capability == Capabilities.ItemHandler.BLOCK) {
            return ImmutableMap.of(
                    Direction.NORTH, Pair.of(IOAction.INSERT, new int[]{0}),
                    Direction.EAST, Pair.of(IOAction.INSERT, new int[]{0}),
                    Direction.SOUTH, Pair.of(IOAction.INSERT, new int[]{0}),
                    Direction.WEST, Pair.of(IOAction.INSERT, new int[]{0})
            );
        }
        return ImmutableMap.of();
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Firebox");
    }

    public void commonTick() {
        tickRecipe();
        tickIO();

        if (level.isClientSide() && burnTime > 0) {
            clientTick();
        }
    }

    protected void clientTick() {
        RandomSource random = level.random;
        if (random.nextInt(0, 2) == 0) {
            int i0 = random.nextInt(-1, 1);
            int i1 = random.nextInt(-1, 1);
            double xSpeed = random.nextDouble() * 0.15 * i0;
            double ySpeed = random.nextDouble() * 0.07;
            double zSpeed = random.nextDouble() * 0.15 * i1;
            int j0 = random.nextInt(1, 2);
            int j1 = random.nextInt(1, 2);
            level.addParticle(ParticleTypes.SMOKE,
                    worldPosition.getX() - xSpeed * 10 * j0, worldPosition.above().getY(), worldPosition.getZ() - zSpeed * 10 * j1,
                    xSpeed / 10, ySpeed / 10, zSpeed / 10);
            if (random.nextInt(0, 3) == 0) {
                level.addParticle(new DustParticleOptions(new Vector3f(242 / 255f, 103 / 255f, 25 / 255f), 1),
                        worldPosition.getX() - xSpeed * 10 * j0, worldPosition.above().getY(), worldPosition.getZ() - zSpeed * 10 * j1,
                        xSpeed / 10, ySpeed / 10, zSpeed / 10);
            }
        }
    }

    protected void tickIO() {
        if (!level.isClientSide()) {
            // Only export heat to block directly above
            BlockPos abovePos = worldPosition.above();
            if (aboveBlockCapCache != null) {
                BlockCapabilityCache<IHeatStorage, Direction> cache = aboveBlockCapCache.get(abovePos);
                if (cache != null) {
                    IHeatStorage aboveHeatStorage = cache.getCapability();
                    if (aboveHeatStorage != null && level != null) {
                        IHeatStorage thisHeatStorage = getHeatStorage();
                        int output = Math.min(thisHeatStorage.getMaxOutput(), aboveHeatStorage.getMaxInput());
                        int drained = thisHeatStorage.tryDrainHeat(output, true);
                        aboveHeatStorage.tryFillHeat(drained, false);
                    }
                }
            }
        }
    }

    protected void tickRecipe() {
        IItemHandler itemHandler = getItemHandler();
        IHeatStorage heatStorage = getHeatStorage();
        if (this.burnTime > 0) {
            burnTime--;
            if (burnTime % 5 == 0) {
                if (!level.isClientSide()) {
                    heatStorage.tryFillHeat(getProductionAmount(), false);
                }
            }

            if (burnTime == 0) {
                this.maxBurnTime = 0;
                ItemStack stack = itemHandler.getStackInSlot(INPUT_SLOT);
                int burnTime = stack.getBurnTime(RecipeType.SMELTING);
                if (burnTime > 0) {
                    this.burnTime = burnTime;
                    this.maxBurnTime = burnTime;
                    stack.shrink(1);
                    setBlockActive(true);
                } else {
                    setBlockActive(false);
                }
            }
        } else {
            if (level.getGameTime() % 3 == 0 && !level.isClientSide()) {
                heatStorage.tryDrainHeat((int) Math.pow(10, 0.5 - ((double) heatStorage.getHeatStored() / heatStorage.getHeatCapacity())), false);
            }
        }
    }

    public void setBlockActive(boolean value) {
        for (Direction dir : BlockStateProperties.HORIZONTAL_FACING.getPossibleValues()) {
            BlockPos pos = worldPosition.relative(dir);
            BlockState blockState = level.getBlockState(pos);
            if (blockState.hasProperty(IFireboxMultiblock.ACTIVE)) {
                level.setBlockAndUpdate(pos, blockState.setValue(IFireboxMultiblock.ACTIVE, value));
            }
        }
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
        return new FireBoxMenu(containerId, inventory, this);
    }

    @Override
    protected void saveData(CompoundTag pTag, HolderLookup.Provider provider) {
        super.saveData(pTag, provider);
        pTag.putInt("burnTime", this.burnTime);
        pTag.putInt("maxBurnTime", this.maxBurnTime);
        pTag.put("multiblockData", saveMBData());
    }

    @Override
    protected void loadData(CompoundTag pTag, HolderLookup.Provider provider) {
        super.loadData(pTag, provider);
        this.burnTime = pTag.getInt("burnTime");
        this.maxBurnTime = pTag.getInt("maxBurnTime");
        this.multiblockData = loadMBData(pTag.getCompound("multiblockData"));
    }

    public int getBurnTime() {
        return this.burnTime;
    }

    public int getMaxBurnTime() {
        return this.maxBurnTime;
    }

    public FireboxTier getFireboxTier() {
        return this.fireboxTier;
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
