package com.indref.industrial_reforged.util;

import com.indref.industrial_reforged.api.capabilities.energy.network.EnergyNet;
import com.indref.industrial_reforged.api.capabilities.energy.network.EnetsSavedData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public final class EnergyNetUtils {

    public static EnetsSavedData getEnergyNets(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(() -> new EnetsSavedData(level),
                        (CompoundTag nbt) -> EnetsSavedData.load(nbt, level)),
                "enets");
    }

    public static void resetNets(ServerLevel level) {
        EnergyNetUtils.getEnergyNets(level).getEnets().resetNets();
        setDirty(level);
    }

    private static void setDirty(ServerLevel level) {
        EnergyNetUtils.getEnergyNets(level).setDirty();
    }
}
