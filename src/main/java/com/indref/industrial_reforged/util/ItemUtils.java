package com.indref.industrial_reforged.util;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.api.capabilities.heat.IHeatStorage;
import com.indref.industrial_reforged.api.items.container.IEnergyItem;
import com.indref.industrial_reforged.api.items.container.IFluidItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

import java.util.List;

public final class ItemUtils {
    // The corresponding rgb value: rgb(77,166,255)
    public static final int ENERGY_BAR_COLOR = 0x4DA6FF;
    public static final int HEAT_BAR_COLOR = 0xFF8000;
    public static final String ACTIVE_KEY = "active";

    public static int energyForDurabilityBar(ItemStack stack) {
        IEnergyStorage energyStorage = stack.getCapability(IRCapabilities.EnergyStorage.ITEM);
        float ratio = (float) energyStorage.getEnergyStored() / energyStorage.getEnergyCapacity();
        return Math.round(13.0F - ((1 - ratio) * 13.0F));
    }

    public static int heatForDurabilityBar(ItemStack stack) {
        IHeatStorage heatStorage = stack.getCapability(IRCapabilities.HeatStorage.ITEM);
        float ratio = (float) heatStorage.getHeatStored() / heatStorage.getHeatCapacity();
        return Math.round(13.0F - ((1 - ratio) * 13.0F));
    }

    public static ItemStack itemStackFromInteractionHand(InteractionHand interactionHand, Player player) {
        return switch (interactionHand) {
            case MAIN_HAND -> player.getMainHandItem();
            case OFF_HAND -> player.getOffhandItem();
            case null -> ItemStack.EMPTY;
        };
    }

    public static void addEnergyTooltip(List<Component> tooltip, ItemStack itemStack) {
        IEnergyStorage energyStorage = itemStack.getCapability(IRCapabilities.EnergyStorage.ITEM);
        if (energyStorage != null) {
            tooltip.add(
                    IRTranslations.Tooltip.ENERGY_STORED.component().withStyle(ChatFormatting.GOLD)
                            .append(Component.literal(": "))
                            .append(Component.literal(String.format("%s / %s", energyStorage.getEnergyStored(),
                                    energyStorage.getEnergyCapacity())).withColor(FastColor.ARGB32.color(196, 196, 196)))
            );
            tooltip.add(
                    IRTranslations.Tooltip.ENERGY_TIER.component().withStyle(ChatFormatting.GOLD)
                            .append(Component.literal(": "))
                            .append(energyStorage.getEnergyTier().value().getTranslation())
            );
        }
    }

    public static void addFluidToolTip(List<Component> tooltip, ItemStack itemStack) {
        IFluidHandlerItem item = itemStack.getCapability(Capabilities.FluidHandler.ITEM);
        if (item == null) return;

        if (!item.getFluidInTank(0).getFluid().equals(Fluids.EMPTY)) {
            tooltip.add(IRTranslations.Tooltip.FLUID_STORED.component()
                    .append(": ")
                    .append(Component.literal(item.getFluidInTank(0).getHoverName().getString())
                            .withStyle(ChatFormatting.AQUA)));
            tooltip.add(IRTranslations.Tooltip.FLUID_AMOUNT.component()
                    .append(": ")
                    .append("%d/%d".formatted(
                            item.getFluidInTank(0).getAmount(),
                            item.getTankCapacity(0)))
                    .withStyle(ChatFormatting.AQUA));
        }
    }

    public static CustomData getImmutableTag(ItemStack itemStack) {
        return itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
    }
}
