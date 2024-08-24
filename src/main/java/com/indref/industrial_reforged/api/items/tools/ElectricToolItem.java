package com.indref.industrial_reforged.api.items.tools;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface ElectricToolItem extends IToolItem {
    boolean requireEnergyToBreak(ItemStack itemStack, Player player);
    int getEnergyUsage(ItemStack itemStack, @Nullable Player player);
}
