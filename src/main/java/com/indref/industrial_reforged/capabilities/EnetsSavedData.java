package com.indref.industrial_reforged.capabilities;

import com.indref.industrial_reforged.capabilities.energy.network.EnergyNets;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

public class EnetsSavedData extends SavedData {
    private EnergyNets enets;

    public EnetsSavedData(ServerLevel level) {
        this.enets = new EnergyNets(level);
    }

    public EnetsSavedData(EnergyNets enets) {
        this.enets = enets;
    }

    public EnergyNets getEnets() {
        return enets;
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag nbt) {
        nbt.put("enets", this.enets.serializeNBT());
        return nbt;
    }

    public static EnetsSavedData load(CompoundTag nbt, ServerLevel level) {
        EnergyNets enets = new EnergyNets(level);
        enets.deserializeNBT(nbt.getCompound("enets"));
        return new EnetsSavedData(enets);
    }
}
