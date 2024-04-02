package com.indref.industrial_reforged.util;

import com.indref.industrial_reforged.api.items.container.IEnergyItem;
import com.indref.industrial_reforged.api.items.container.IFluidItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

import java.util.List;

public final class ItemUtils {
    // The corresponding rgb value: rgb(77,166,255)
    public static final int ENERGY_BAR_COLOR = 0x4DA6FF;
    public static final int HEAT_BAR_COLOR = 0xFF8000;
    public static final String ACTIVE_KEY = "active";

    public enum ChargeType {
        HEAT,
        FLUID,
        ENERGY,
    }

    public static int energyForDurabilityBar(ItemStack itemStack) {
        return Math.round(13.0F - ((1 - getChargeRatio(ChargeType.ENERGY, itemStack)) * 13.0F));
    }

    public static int heatForDurabilityBar(ItemStack itemStack) {
        return Math.round(13.0F - ((1 - getChargeRatio(ChargeType.HEAT, itemStack)) * 13.0F));
    }

    public static int fluidForDurabilityBar(ItemStack itemStack) {
        return Math.round(13.0F - ((1 - getChargeRatio(ChargeType.FLUID, itemStack)) * 13.0F));
    }

    public static EquipmentSlot equipmentSlotFromIndex(int index) {
        return switch (index) {
            case 0 -> EquipmentSlot.MAINHAND;
            case 1 -> EquipmentSlot.OFFHAND;
            case 2 -> EquipmentSlot.FEET;
            case 3 -> EquipmentSlot.LEGS;
            case 4 -> EquipmentSlot.CHEST;
            case 5 -> EquipmentSlot.HEAD;
            default -> null;
        };
    }

    public static int indexFromEquipmentSlot(EquipmentSlot slot) {
        return switch (slot) {
            case MAINHAND -> 0;
            case OFFHAND -> 1;
            case FEET -> 2;
            case LEGS -> 3;
            case CHEST -> 4;
            case HEAD -> 5;
        };
    }

    public static IFluidItem getFluidItem(ItemStack itemStack) {
        if (itemStack.getItem() instanceof IFluidItem fluidItem) {
            return fluidItem;
        }
        return null;
    }

    public static IEnergyItem getEnergyItem(ItemStack itemStack) {
        if (itemStack.getItem() instanceof IEnergyItem fluidItem) {
            return fluidItem;
        }
        return null;
    }

    public static float getChargeRatio(ChargeType type, ItemStack stack) {
        return switch (type) {
            case FLUID -> (float) getFluidItem(stack).getFluidStored(stack) / getFluidItem(stack).getFluidCapacity();
            case ENERGY ->
                    (float) getEnergyItem(stack).getEnergyStored(stack) / getEnergyItem(stack).getEnergyCapacity();
            default -> 0F;
        };
    }

    public static ItemStack itemStackFromInteractionHand(InteractionHand interactionHand, Player player) {
        switch (interactionHand) {
            case MAIN_HAND -> {
                return player.getMainHandItem();
            }
            case OFF_HAND -> {
                return player.getOffhandItem();
            }
        }
        return ItemStack.EMPTY;
    }

    public static void addEnergyTooltip(List<Component> tooltip, ItemStack itemStack) {
        IEnergyItem item;
        if (itemStack.getItem() instanceof IEnergyItem iEnergyItem)
            item = iEnergyItem;
        else return;
        tooltip.add(
                Component.translatable("indref.energy.desc.stored").withStyle(ChatFormatting.GRAY)
                        .append(Component.literal(": ").withStyle(ChatFormatting.GRAY))
                        .append(Component.literal(String.format("%s / %s", item.getEnergyStored(itemStack),
                                item.getEnergyCapacity())).withStyle(ChatFormatting.AQUA))
        );
        tooltip.add(
                Component.translatable("indref.energy.desc.tier").withStyle(ChatFormatting.GRAY)
                        .append(Component.literal(": ").withStyle(ChatFormatting.GRAY))
                        .append(item.getEnergyTier().getName())
        );
    }

    public static void addFluidToolTip(List<Component> tooltip, ItemStack itemStack) {
        IFluidHandlerItem item = itemStack.getCapability(Capabilities.FluidHandler.ITEM);
        if (item == null) return;

        if (!item.getFluidInTank(0).getFluid().equals(Fluids.EMPTY)) {
            tooltip.add(Component.translatable("fluid_cell.desc.stored").append(": ")
                    .append(Component.literal(item.getFluidInTank(0).getDisplayName().getString())
                            .withStyle(ChatFormatting.AQUA)));
            tooltip.add(Component.translatable("fluid_cell.desc.amount").append(": ")
                    .append("%d/%d".formatted(
                            item.getFluidInTank(0).getAmount(),
                            getFluidItem(itemStack).getFluidCapacity()))
                    .withStyle(ChatFormatting.AQUA));
        }
    }

}
