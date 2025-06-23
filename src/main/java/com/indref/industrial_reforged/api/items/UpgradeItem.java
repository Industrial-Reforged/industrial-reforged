package com.indref.industrial_reforged.api.items;

import com.indref.industrial_reforged.api.upgrade.Upgrade;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

public interface UpgradeItem extends ItemLike {
    Supplier<Upgrade> getUpgrade();
}
