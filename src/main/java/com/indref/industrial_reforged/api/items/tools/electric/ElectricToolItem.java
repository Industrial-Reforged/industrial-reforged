package com.indref.industrial_reforged.api.items.tools.electric;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface ElectricToolItem {
    boolean requireEnergyToWork(ItemStack itemStack, Player player);
    int getEnergyUsage(ItemStack itemStack, @Nullable Entity entity);
}
