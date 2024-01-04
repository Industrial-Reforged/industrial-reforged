package com.indref.industrial_reforged.api.capabilities;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.data.energy.IEnergyStorage;
import com.indref.industrial_reforged.api.data.heat.IHeatStorage;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;
import org.jetbrains.annotations.Nullable;

/**
 * Class used for registering and storing
 * references to all capabilities of ind-ref
 */

public final class IRCapabilities {
    public static final class EnergyStorage {
        public static final BlockCapability<IEnergyStorage, @Nullable Direction> BLOCK = BlockCapability.createSided(create("energy"), IEnergyStorage.class);
        public static final EntityCapability<IEnergyStorage, @Nullable Direction> ENTITY = EntityCapability.createSided(create("energy"), IEnergyStorage.class);
        public static final ItemCapability<IEnergyStorage, Void> ITEM = ItemCapability.createVoid(create("energy"), IEnergyStorage.class);

        private EnergyStorage() {
        }
    }

    public static final class HeatStorage {
        public static final BlockCapability<IHeatStorage, @Nullable Direction> BLOCK = BlockCapability.createSided(create("heat"), IHeatStorage.class);
        public static final EntityCapability<IHeatStorage, @Nullable Direction> ENTITY = EntityCapability.createSided(create("heat"), IHeatStorage.class);
        public static final ItemCapability<IHeatStorage, Void> ITEM = ItemCapability.createVoid(create("heat"), IHeatStorage.class);

        private HeatStorage() {
        }
    }

    private static ResourceLocation create(String path) {
        return new ResourceLocation(IndustrialReforged.MODID, path);
    }

}
