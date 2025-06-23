package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.IRConfig;
import com.indref.industrial_reforged.IRRegistries;
import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blockentities.MachineBlockEntity;
import com.indref.industrial_reforged.api.blockentities.UpgradeBlockEntity;
import com.indref.industrial_reforged.api.items.UpgradeItem;
import com.indref.industrial_reforged.api.upgrade.Upgrade;
import com.indref.industrial_reforged.content.upgrades.OverclockerUpgrade;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class IRUpgrades {
    public static final DeferredRegister<Upgrade> UPGRADES = DeferredRegister.create(IRRegistries.UPGRADE, IndustrialReforged.MODID);

    public static final Supplier<Upgrade> OVERCLOCKER_UPGRADE = UPGRADES.register("overclocker", OverclockerUpgrade::new);
}
