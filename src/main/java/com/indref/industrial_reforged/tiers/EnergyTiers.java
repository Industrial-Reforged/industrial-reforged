package com.indref.industrial_reforged.tiers;

import com.indref.industrial_reforged.api.tiers.EnergyTier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public enum EnergyTiers implements EnergyTier {
    LOW(Component.translatable("indref.energy.tier.low").withStyle(ChatFormatting.WHITE), 32, 32, 32_000),
    MEDIUM(Component.translatable("indref.energy.tier.medium").withStyle(ChatFormatting.GOLD),64, 64, 64_000),
    HIGH(Component.translatable("indref.energy.tier.high").withStyle(ChatFormatting.BLUE),128, 128, 128_000),
    EXTREME(Component.translatable("indref.energy.tier.extreme").withStyle(ChatFormatting.GREEN), 512, 512, 512_000),
    INSANE(Component.translatable("indref.energy.tier.insane").withStyle(ChatFormatting.RED), 1024, 1024, 1_024_000),
    CREATIVE(Component.translatable("indref.energy.tier.creative").withStyle(ChatFormatting.LIGHT_PURPLE), Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);

    private final int maxInput;
    private final int maxOutput;
    private final int current;
    private final int defaultCapacity;
    private final Component name;

    EnergyTiers(Component name, int throughPut, int current, int defaultCapacity) {
        this.name = name;
        this.maxInput = throughPut;
        this.maxOutput = throughPut;
        this.current = current;
        this.defaultCapacity = defaultCapacity;
    }

    @Override
    public int getMaxInput() {
        return this.maxInput;
    }

    @Override
    public int getMaxOutput() {
        return this.maxOutput;
    }

    @Override
    public int getCurrent() {
        return this.current;
    }

    @Override
    public int getDefaultCapacity() {
        return this.defaultCapacity;
    }

    @Override
    public Component getName() {
        return this.name;
    }
}
