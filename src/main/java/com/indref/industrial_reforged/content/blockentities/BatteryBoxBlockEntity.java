package com.indref.industrial_reforged.content.blockentities;

import com.indref.industrial_reforged.IRConfig;
import com.indref.industrial_reforged.api.blockentities.MachineBlockEntity;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.content.blocks.BatteryBoxBlock;
import com.indref.industrial_reforged.data.saved.EnergyNetsSavedData;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.tiers.EnergyTiers;
import com.indref.industrial_reforged.transportation.deprecated.EnergyNet;
import com.indref.industrial_reforged.util.EnergyNetUtils;
import com.indref.industrial_reforged.util.capabilities.CapabilityUtils;
import com.portingdeadmods.portingdeadlibs.api.utils.IOAction;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BatteryBoxBlockEntity extends MachineBlockEntity {
    private final Map<Direction, Pair<IOAction, int[]>> sidedInteractions;

    public BatteryBoxBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRBlockEntityTypes.BATTERY_BOX.get(), blockPos, blockState);
        addEuStorage(EnergyTiers.LOW, IRConfig.batteryBoxEnergyCapacity);

        this.sidedInteractions = new HashMap<>();
    }

    @Override
    public void commonTick() {
        super.commonTick();

        if (!level.isClientSide()) {
            IEnergyStorage thisEnergyStorage = CapabilityUtils.energyStorageCapability(this);
            if (level instanceof ServerLevel serverLevel) {
                EnergyNetsSavedData energyNets = EnergyNetUtils.getEnergyNets(serverLevel);
                Optional<EnergyNet> enet = energyNets.getEnets().getNetwork(worldPosition);
                if (enet.isPresent()) {
                    int filled = enet.get().distributeEnergy(Math.min(thisEnergyStorage.getEnergyTier().value().defaultCapacity(), thisEnergyStorage.getEnergyStored()));
                    thisEnergyStorage.tryDrainEnergy(filled, false);
                }
            }
        }
    }

    public void onBlockUpdated() {
        for (Direction direction : Direction.values()) {
            this.sidedInteractions.put(direction, Pair.of(IOAction.INSERT, new int[]{0}));
        }
        this.sidedInteractions.put(getBlockState().getValue(BatteryBoxBlock.FACING), Pair.of(IOAction.EXTRACT, new int[]{0}));
    }

    @Override
    public void onLoad() {
        super.onLoad();

        onBlockUpdated();
    }

    @Override
    public <T> Map<Direction, Pair<IOAction, int[]>> getSidedInteractions(BlockCapability<T, @Nullable Direction> blockCapability) {
        return this.sidedInteractions;
    }
}
