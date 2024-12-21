package com.indref.industrial_reforged.tiers;

import com.indref.industrial_reforged.api.tiers.EnergyTier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

@SuppressWarnings("DataFlowIssue")
public enum EnergyTiers implements EnergyTier {
    // TODO: Adjust these values
    NONE("none", ChatFormatting.GRAY.getColor(), 0, 0, 0),
    LOW("low", ChatFormatting.WHITE.getColor(), 16, 16, 4_000),
    MEDIUM("medium", ChatFormatting.GOLD.getColor(),64, 64, 16_000),
    HIGH("high", ChatFormatting.BLUE.getColor(),128, 128, 32_000),
    EXTREME("extreme", ChatFormatting.GREEN.getColor(), 512, 512, 256_000),
    INSANE("insane", ChatFormatting.RED.getColor(), 1024, 1024, 512_000),
    CREATIVE("creative", ChatFormatting.LIGHT_PURPLE.getColor(), Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);

    private final int maxInput;
    private final int maxOutput;
    private final int current;
    private final int defaultCapacity;
    private final String id;
    private final int color;

    EnergyTiers(String id, int color, int throughPut, int current, int defaultCapacity) {
        this.id = id;
        this.color = color;
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
    public String getId() {
        return this.id;
    }

    @Override
    public int getColor() {
        return this.color;
    }
}
