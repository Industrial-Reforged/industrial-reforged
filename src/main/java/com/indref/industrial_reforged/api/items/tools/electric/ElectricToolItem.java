package com.indref.industrial_reforged.api.items.tools.electric;

import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface ElectricToolItem {
    default boolean requireEnergyToWork(ItemStack itemStack) {
        return requireEnergyToWork(itemStack, null);
    }

    boolean requireEnergyToWork(ItemStack itemStack, @Nullable Entity entity);

    default int getEnergyUsage(ItemStack itemStack) {
        return getEnergyUsage(itemStack, null);
    }

    int getEnergyUsage(ItemStack itemStack, @Nullable Entity entity);

    default boolean canWork(ItemStack stack) {
        return canWork(stack, null);
    }

    default boolean canWork(ItemStack stack, @Nullable Entity entity) {
        IEnergyStorage energyStorage = stack.getCapability(IRCapabilities.EnergyStorage.ITEM);
        if (energyStorage != null && requireEnergyToWork(stack, entity)) {
            return energyStorage.getEnergyStored() >= getEnergyUsage(stack, entity);
        }
        return false;
    }

    default void consumeEnergy(ItemStack stack) {
        consumeEnergy(stack, null);
    }

    default void consumeEnergy(ItemStack stack, @Nullable Entity entity) {
        IEnergyStorage energyStorage = stack.getCapability(IRCapabilities.EnergyStorage.ITEM);
        if (requireEnergyToWork(stack, entity) && canWork(stack, entity)) {
            energyStorage.tryDrainEnergy(getEnergyUsage(stack, entity), false);
        }
    }

}
