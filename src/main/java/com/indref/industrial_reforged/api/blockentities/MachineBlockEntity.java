package com.indref.industrial_reforged.api.blockentities;

import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.api.capabilities.item.UpgradeItemHandler;
import com.indref.industrial_reforged.api.gui.slots.ChargingSlot;
import com.indref.industrial_reforged.api.items.UpgradeItem;
import com.indref.industrial_reforged.api.upgrade.Upgrade;
import com.indref.industrial_reforged.registries.IRUpgrades;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public abstract class MachineBlockEntity extends IRContainerBlockEntity implements WrenchListenerBlockEntity, RedstoneBlockEntity, UpgradeBlockEntity {
    private @Nullable ChargingSlot batterySlot;
    private final List<BlockCapabilityCache<IEnergyStorage, Direction>> caches;
    private boolean removedUsingWrench;
    private final UpgradeItemHandler upgradeItemHandler;
    private final Set<Supplier<Upgrade>> defaultSupportedUpgrades;
    private RedstoneSignalType redstoneSignalType;
    private int redstoneSignalStrength;
    protected float progress;
    private float progressIncrement;
    private float energyDecrement;

    public MachineBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        this.caches = new ArrayList<>();
        this.defaultSupportedUpgrades = Set.of(IRUpgrades.OVERCLOCKER_UPGRADE);
        if (this.supportsUpgrades()) {
            this.upgradeItemHandler = new UpgradeItemHandler(this.defaultSupportedUpgrades) {
                @Override
                protected void onContentsChanged(int slot) {
                    super.onContentsChanged(slot);

                    update();
                }

                @Override
                public void onUpgradeAdded(Supplier<Upgrade> upgrade) {
                    super.onUpgradeAdded(upgrade);

                    MachineBlockEntity.this.onUpgradeAdded(upgrade);
                }

                @Override
                public void onUpgradeRemoved(Supplier<Upgrade> upgrade) {
                    super.onUpgradeRemoved(upgrade);

                    MachineBlockEntity.this.onUpgradeRemoved(upgrade);
                }
            };
        } else {
            this.upgradeItemHandler = null;
        }
        this.redstoneSignalType = RedstoneSignalType.IGNORED;
        this.progressIncrement = 1;
    }

    public void setRedstoneSignalStrength(int redstoneSignalStrength) {
        this.redstoneSignalStrength = redstoneSignalStrength;
    }

    public int getRedstoneSignalStrength() {
        return redstoneSignalStrength;
    }

    public void addBatterySlot(ChargingSlot chargingSlot) {
        this.batterySlot = chargingSlot;
    }

    @Override
    public void commonTick() {
        super.commonTick();
        if (this.batterySlot != null) {
            tickBatterySlot();
        }

        if (spreadEnergy() && !level.isClientSide()) {
            int amountPerBlock = getAmountPerBlock();
            for (BlockCapabilityCache<IEnergyStorage, Direction> cache : this.caches) {
                IEnergyStorage energyStorage = cache.getCapability();
                if (energyStorage != null) {
                    int filled = energyStorage.tryFillEnergy(amountPerBlock, false);
                    getEuStorage().tryDrainEnergy(filled, false);
                }
            }
        }
    }

    private int getAmountPerBlock() {
        int blocks = 0;
        for (BlockCapabilityCache<IEnergyStorage, Direction> cache : this.caches) {
            if (cache.getCapability() != null) {
                blocks++;
            }
        }
        int amountPerBlock;
        if (getEuStorage().getEnergyStored() >= getEuStorage().getMaxOutput() * blocks) {
            amountPerBlock = getEuStorage().getMaxOutput();
        } else {
            amountPerBlock = getEuStorage().getEnergyStored() / blocks;
        }
        return amountPerBlock;
    }

    private void tickBatterySlot() {
        ItemStack itemStack = this.batterySlot.getItem();
        IEnergyStorage energyStorage = getEuStorage();
        IEnergyStorage itemEnergyStorage = itemStack.getCapability(IRCapabilities.EnergyStorage.ITEM);
        if (itemEnergyStorage != null && !level.isClientSide()) {
            if (batterySlot.getMode() == ChargingSlot.ChargeMode.CHARGE) {
                int filled = itemEnergyStorage.tryFillEnergy(Math.min(itemEnergyStorage.getMaxInput(), energyStorage.getMaxOutput()), true);
                int drained = energyStorage.tryDrainEnergy(filled, true);
                int newFilled = itemEnergyStorage.tryFillEnergy(drained, false);
                energyStorage.tryDrainEnergy(newFilled, false);
            } else {
                int drained = itemEnergyStorage.tryDrainEnergy(Math.min(itemEnergyStorage.getMaxOutput(), energyStorage.getMaxInput()), true);
                int filled = energyStorage.tryFillEnergy(drained, true);
                int newDrained = itemEnergyStorage.tryDrainEnergy(filled, false);
                energyStorage.tryFillEnergy(newDrained, false);
            }
        }
    }

    public boolean spreadEnergy() {
        return false;
    }

    @Override
    public UpgradeItemHandler getUpgradeItemHandler() {
        return upgradeItemHandler;
    }

    @Override
    public boolean hasUpgrade(Supplier<Upgrade> upgrade) {
        for (int i = 0; i < this.getUpgradeItemHandler().getSlots(); i++) {
            if (this.getUpgradeItemHandler().getStackInSlot(i).is(upgrade.get().getUpgradeItem().asItem())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onUpgradeAdded(Supplier<Upgrade> upgrade) {
        upgrade.get().init(this);
    }

    @Override
    public void onUpgradeRemoved(Supplier<Upgrade> upgrade) {

    }

    public void setEnergyDecrement(float energyDecrement) {
        this.energyDecrement = energyDecrement;
    }

    public void setProgressIncrement(float progressIncrement) {
        this.progressIncrement = progressIncrement;
    }

    @Override
    public int getUpgradeAmount(Supplier<Upgrade> upgrade) {
        int amount = 0;
        Upgrade upgradeInstance = upgrade.get();
        Item upgradeItem = upgradeInstance.getUpgradeItem().asItem();

        for (int i = 0; i < this.getUpgradeItemHandler().getSlots(); i++) {
            ItemStack stackInSlot = this.getUpgradeItemHandler().getStackInSlot(i);
            if (stackInSlot.is(upgradeItem)) {
                amount += stackInSlot.getCount();
            }
        }

        return amount;
    }


    @Override
    public void onLoad() {
        super.onLoad();

        initCapCache();

        if (this.supportsUpgrades()) {
            for (int i = 0; i < this.getUpgradeItemHandler().getSlots(); i++) {
                if (!this.getUpgradeItemHandler().getStackInSlot(i).isEmpty()) {
                    UpgradeItem item = ((UpgradeItem) this.getUpgradeItemHandler().getStackInSlot(i).getItem());
                    Supplier<Upgrade> upgrade = item.getUpgrade();
                    if (this.hasUpgrade(upgrade)) {
                        upgrade.get().init(this);
                    }
                }
            }
        }

    }

    public void increaseProgress() {
        this.progress += this.progressIncrement;
    }

    public void useEnergy() {
        this.getEuStorage().tryDrainEnergy((int) this.energyDecrement, false);
    }

    public void initCapCache() {
        if (level instanceof ServerLevel serverLevel) {
            for (Direction direction : Direction.values()) {
                this.caches.add(BlockCapabilityCache.create(IRCapabilities.EnergyStorage.BLOCK, serverLevel, worldPosition.relative(direction), direction));
            }
        }
    }

    @Override
    public void beforeRemoveByWrench(Player player) {
        this.removedUsingWrench = true;
    }

    public abstract boolean supportsUpgrades();

    public Set<Supplier<Upgrade>> getSupportedUpgrades() {
        return this.defaultSupportedUpgrades;
    }

    @Override
    public void drop() {
        if (!this.removedUsingWrench) {
            super.drop();
        } else {
            this.removedUsingWrench = false;
        }
    }

    @Override
    public int emitRedstoneLevel() {
        return 0;
    }

    @Override
    public RedstoneSignalType getRedstoneSignalType() {
        return redstoneSignalType;
    }

    @Override
    public void setRedstoneSignalType(RedstoneSignalType redstoneSignalType) {
        this.redstoneSignalType = redstoneSignalType;
        update();
    }

    @Override
    protected void loadData(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadData(tag, provider);
        if (this.upgradeItemHandler != null)
            this.upgradeItemHandler.deserializeNBT(provider, tag.getCompound("upgrade_item_handler"));
        this.redstoneSignalStrength = tag.getInt("signal_strength");
        this.redstoneSignalType = RedstoneSignalType.CODEC.decode(NbtOps.INSTANCE, tag.get("redstone_signal")).result().orElse(Pair.of(RedstoneSignalType.IGNORED, new CompoundTag())).getFirst();
    }

    @Override
    protected void saveData(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveData(tag, provider);
        if (this.upgradeItemHandler != null)
            tag.put("upgrade_item_handler", this.upgradeItemHandler.serializeNBT(provider));
        tag.putInt("signal_strength", this.redstoneSignalStrength);
        Optional<Tag> tag1 = RedstoneSignalType.CODEC.encodeStart(NbtOps.INSTANCE, this.redstoneSignalType).result();
        tag1.ifPresent(value -> {
            tag.put("redstone_signal", value);
        });
    }

}
