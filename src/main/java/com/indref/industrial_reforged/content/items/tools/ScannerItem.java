package com.indref.industrial_reforged.content.items.tools;

import com.indref.industrial_reforged.api.blocks.DisplayBlock;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.api.items.tools.DisplayItem;
import com.indref.industrial_reforged.api.items.container.SimpleEnergyItem;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.util.IRHooks;
import com.indref.industrial_reforged.util.capabilities.CapabilityUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class ScannerItem extends SimpleEnergyItem {
    private final int energyUsage;

    public ScannerItem(Properties p_41383_, Holder<EnergyTier> energyTier, int energyUsage, int defaultEnergyCapacity) {
        super(p_41383_, energyTier, defaultEnergyCapacity);
        this.energyUsage = energyUsage;
    }

    public int getEnergyUsage() {
        return energyUsage;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

}
