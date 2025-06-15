package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.IRRegistries;
import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.upgrade.Upgrade;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class IRUpgrades {
    public static final DeferredRegister<Upgrade> UPGRADES = DeferredRegister.create(IRRegistries.UPGRADE, IndustrialReforged.MODID);

    public static final Supplier<Upgrade> OVERCLOCK_UPGRADE = UPGRADES.register("overclock", () -> new Upgrade() {
    });
}
