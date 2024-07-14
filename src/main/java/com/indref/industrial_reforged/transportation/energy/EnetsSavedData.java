package com.indref.industrial_reforged.transportation.energy;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

public class EnetsSavedData extends SavedData {
    private EnergyNets enets;

    public EnetsSavedData(ServerLevel level) {
        this.enets = new EnergyNets(level);
    }

    public static SavedData.Factory<EnetsSavedData> factory(ServerLevel pLevel) {
        return new SavedData.Factory<>(() -> new EnetsSavedData(pLevel), (tag, provider) -> load(tag, pLevel));
    }

    public EnetsSavedData(EnergyNets enets) {
        this.enets = enets;
    }

    public EnergyNets getEnets() {
        return enets;
    }

    public static EnetsSavedData load(CompoundTag nbt, ServerLevel level) {
        EnergyNets enets = new EnergyNets(level);
        enets.deserializeNBT(nbt.getCompound("enets"));
        return new EnetsSavedData(enets);
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        compoundTag.put("enets", this.enets.serializeNBT());
        return compoundTag;
    }
}
