package com.indref.industrial_reforged.api.tiers;

import com.indref.industrial_reforged.IRRegistries;
import com.portingdeadmods.portingdeadlibs.utils.Utils;
import net.minecraft.network.chat.Component;

public interface EnergyTier {
    int maxInput();

    int maxOutput();

    int color();

    int defaultCapacity();

    default Component getDisplayName() {
        return Utils.registryTranslation(IRRegistries.ENERGY_TIER, this);
    }

}
