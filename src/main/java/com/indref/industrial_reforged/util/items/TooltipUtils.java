package com.indref.industrial_reforged.util.items;

import com.indref.industrial_reforged.IRRegistries;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyHandler;
import com.indref.industrial_reforged.api.capabilities.heat.IHeatStorage;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.translations.IRTranslations;
import com.portingdeadmods.portingdeadlibs.utils.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

import java.util.List;

public final class TooltipUtils {

    public static void addEnergyTooltip(List<Component> tooltip, ItemStack itemStack) {
        IEnergyHandler energyStorage = itemStack.getCapability(IRCapabilities.EnergyStorage.ITEM);
        if (energyStorage != null) {
            tooltip.add(
                    IRTranslations.Tooltip.ENERGY_STORED.component()
                            .withStyle(ChatFormatting.GRAY)
                            .append(IRTranslations.Tooltip.ENERGY_AMOUNT_WITH_CAPACITY.component(energyStorage.getEnergyStored(), energyStorage.getEnergyCapacity())
                                    .withColor(FastColor.ARGB32.color(255, 245, 192, 89)))
                            .append(" ")
                            .append(IRTranslations.General.ENERGY_UNIT.component()
                                    .withColor(FastColor.ARGB32.color(255, 245, 192, 89)))
            );
            EnergyTier tier = energyStorage.getEnergyTier().get();
            addEnergyTierTooltip(tooltip, tier);
        }
    }

    public static void addEnergyTierTooltip(List<Component> tooltip, EnergyTier tier) {
        tooltip.add(
                IRTranslations.Tooltip.ENERGY_TIER.component()
                        .withStyle(ChatFormatting.GRAY)
                        .append(Utils.registryTranslation(IRRegistries.ENERGY_TIER, tier).copy().withColor(tier.color()))
        );
    }

    public static void addFluidToolTip(List<Component> tooltip, ItemStack itemStack) {
        IFluidHandlerItem item = itemStack.getCapability(Capabilities.FluidHandler.ITEM);

        if (item != null && !item.getFluidInTank(0).isEmpty()) {
            tooltip.add(IRTranslations.Tooltip.FLUID_TYPE.component()
                    .withStyle(ChatFormatting.GRAY)
                    .append(item.getFluidInTank(0).getHoverName().copy()
                            .withColor(-1)));
            tooltip.add(IRTranslations.Tooltip.FLUID_STORED.component()
                    .withStyle(ChatFormatting.GRAY)
                    .append(IRTranslations.Tooltip.FLUID_AMOUNT_WITH_CAPACITY.component(item.getFluidInTank(0).getAmount(), item.getTankCapacity(0))
                            .withStyle(ChatFormatting.WHITE))
                    .append(" ")
                    .append(IRTranslations.General.FLUID_UNIT.component()
                            .withStyle(ChatFormatting.WHITE)));
        }
    }

    public static void addHeatToolTip(List<Component> tooltip, ItemStack itemStack) {
        IHeatStorage heatStorage = itemStack.getCapability(IRCapabilities.HeatStorage.ITEM);

        if (heatStorage != null) {
            tooltip.add(
                    IRTranslations.Tooltip.HEAT_STORED.component()
                            .withStyle(ChatFormatting.GRAY)
                            .append(IRTranslations.Tooltip.HEAT_AMOUNT_WITH_CAPACITY.component(heatStorage.getHeatStored(), heatStorage.getHeatCapacity())
                                    .withColor(FastColor.ARGB32.color(255, 245, 192, 89)))
                            .append(" ")
                            .append(IRTranslations.General.HEAT_UNIT.component()
                                    .withColor(FastColor.ARGB32.color(255, 245, 192, 89)))
            );
        }
    }
}
