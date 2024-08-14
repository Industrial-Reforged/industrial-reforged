package com.indref.industrial_reforged.util;

import com.indref.industrial_reforged.registries.data.saved.EnergyNetsSavedData;
import net.minecraft.server.level.ServerLevel;

public final class EnergyNetUtils {

    public static EnergyNetsSavedData getEnergyNets(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(EnergyNetsSavedData.factory(level), "enets");
    }

    public static void resetNets(ServerLevel level) {
        EnergyNetUtils.getEnergyNets(level).getEnets().resetNets();
        setDirty(level);
    }

    private static void setDirty(ServerLevel level) {
        EnergyNetUtils.getEnergyNets(level).setDirty();
    }
}
