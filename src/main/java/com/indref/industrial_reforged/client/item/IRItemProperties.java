package com.indref.industrial_reforged.client.item;

import com.indref.industrial_reforged.IRConfig;
import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.content.items.storage.BatteryItem;
import com.indref.industrial_reforged.content.items.tools.ElectricChainsawItem;
import com.indref.industrial_reforged.content.items.tools.ElectricDrillItem;
import com.indref.industrial_reforged.data.IRDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;

import javax.annotation.Nullable;

public final class IRItemProperties {
    public static final ResourceLocation ACTIVE_KEY = IndustrialReforged.rl("active");
    public static final ResourceLocation TEMPERATURE_KEY = IndustrialReforged.rl("temperature");
    public static final ResourceLocation BATTERY_STAGE_KEY = IndustrialReforged.rl("battery_stage");

    public static float isActive(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
        return stack.getOrDefault(IRDataComponents.ACTIVE, false) ? 1 : 0;
    }

    public static float isItemHeld(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
        if (entity != null) {
            ItemStack mainHandItem = entity.getMainHandItem();
            @Nullable IEnergyStorage capability = mainHandItem.getCapability(IRCapabilities.EnergyStorage.ITEM);
            boolean runAnimation = true;
            if (stack.getItem() instanceof ElectricChainsawItem) {
                runAnimation = IRConfig.chainsawItemAnimation;
            } else if (stack.getItem() instanceof ElectricDrillItem) {
                runAnimation = IRConfig.drillItemAnimation;
            }
            return runAnimation
                    && mainHandItem.equals(stack)
                    && capability != null
                    && capability.getEnergyStored() > 0 ? 1 : 0;
        }
        return 0;
    }

    public static float getBatteryStage(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
        return ((BatteryItem) stack.getItem()).getBatteryStage(stack);
    }

    public static float getTemperature(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
        return stack.getOrDefault(IRDataComponents.THERMOMETER_STAGE, 0);
    }
}
