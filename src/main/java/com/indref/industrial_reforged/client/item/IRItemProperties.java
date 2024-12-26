package com.indref.industrial_reforged.client.item;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.data.IRDataComponents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public final class IRItemProperties {
    public static final ResourceLocation ACTIVE_KEY = IndustrialReforged.rl("active");
    public static final ResourceLocation TEMPERATURE_KEY = IndustrialReforged.rl("temperature");
    public static final ResourceLocation BATTERY_STAGE_KEY = IndustrialReforged.rl("battery_stage");

    public static float isActive(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
        return stack.getOrDefault(IRDataComponents.ACTIVE, false) ? 1 : 0;
    }

    public static float getTemperature(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
        return stack.getOrDefault(IRDataComponents.THERMOMETER_STAGE, 0);
    }
}
