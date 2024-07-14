package com.indref.industrial_reforged.util;

import com.indref.industrial_reforged.transportation.energy.EnetsSavedData;
import net.minecraft.server.level.ServerLevel;

public final class EnergyNetUtils {

    public static EnetsSavedData getEnergyNets(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(EnetsSavedData.factory(level), "enets");
    }

    public static void resetNets(ServerLevel level) {
        EnergyNetUtils.getEnergyNets(level).getEnets().resetNets();
        setDirty(level);
    }

    private static void setDirty(ServerLevel level) {
        EnergyNetUtils.getEnergyNets(level).setDirty();
    }
}
