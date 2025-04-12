package com.indref.industrial_reforged.data.saved.deprecated;

import com.indref.industrial_reforged.transportation.deprecated.EnergyNets;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

public class EnergyNetsSavedData extends SavedData {
    private final EnergyNets enets;

    public EnergyNetsSavedData(ServerLevel level) {
        this.enets = new EnergyNets(level);
    }

    public static SavedData.Factory<EnergyNetsSavedData> factory(ServerLevel pLevel) {
        return new SavedData.Factory<>(() -> new EnergyNetsSavedData(pLevel), (tag, provider) -> load(tag, pLevel));
    }

    public EnergyNetsSavedData(EnergyNets enets) {
        this.enets = enets;
    }

    public EnergyNets getEnets() {
        return enets;
    }

    public static EnergyNetsSavedData load(CompoundTag nbt, ServerLevel level) {
        EnergyNets enets = new EnergyNets(level);
        enets.deserializeNBT(nbt.getCompound("enets"));
        return new EnergyNetsSavedData(enets);
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        compoundTag.put("enets", this.enets.serializeNBT());
        return compoundTag;
    }
}
