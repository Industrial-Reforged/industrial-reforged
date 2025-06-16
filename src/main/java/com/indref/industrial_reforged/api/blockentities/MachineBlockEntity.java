package com.indref.industrial_reforged.api.blockentities;

import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.api.gui.slots.ChargingSlot;
import com.indref.industrial_reforged.api.upgrade.Upgrade;
import com.indref.industrial_reforged.registries.IRUpgrades;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class MachineBlockEntity extends IRContainerBlockEntity implements WrenchListenerBlockEntity {
    private @Nullable ChargingSlot batterySlot;
    private final List<BlockCapabilityCache<IEnergyStorage, Direction>> caches;
    private boolean removedUsingWrench;
    private final Set<Upgrade> defaultSupportedUpgrades;

    public MachineBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        this.caches = new ArrayList<>();
        this.defaultSupportedUpgrades = Set.of(IRUpgrades.OVERCLOCK_UPGRADE.get());
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
    public void onLoad() {
        super.onLoad();

        initCapCache();
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

    public Set<Upgrade> getSupportedUpgrades() {
        return this.defaultSupportedUpgrades;
    }

    @Override
    public void drop() {
        if (!this.removedUsingWrench) {
            super.drop();
        }
    }
}
