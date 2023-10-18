package com.indref.industrial_reforged.api.items;

import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.api.items.container.IEnergyItem;
import com.indref.industrial_reforged.util.ItemUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public abstract class SimpleElectricItem extends Item implements IEnergyItem {
    public SimpleElectricItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getBarWidth(ItemStack itemStack) {
        return ItemUtils.energyForDurabilityBar(itemStack);
    }

    @Override
    public int getBarColor(ItemStack itemStack) {
        return ItemUtils.ENERGY_BAR_COLOR;
    }

    @Override
    public boolean isBarVisible(ItemStack p_150899_) {
        return true;
    }

    @Override
    public int getUseDuration(ItemStack p_41454_) {
        return 1;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level p41422, List<Component> tooltip, TooltipFlag p41424) {
        super.appendHoverText(stack, p41422, tooltip, p41424);
        Optional<IEnergyStorage> capability = stack.getCapability(IRCapabilities.ENERGY).resolve();
        if (capability.isPresent()) {
            IEnergyStorage storage = capability.get();
            tooltip.add(Component.literal(String.format("%s / %s", storage.getEnergyStored(), storage.getEnergyCapacity())).withStyle(ChatFormatting.AQUA));
        } else {
            tooltip.add(Component.literal("0 / 0"));
        }
    }
}
