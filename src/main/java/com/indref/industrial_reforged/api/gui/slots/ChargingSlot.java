package com.indref.industrial_reforged.api.gui.slots;

import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class ChargingSlot extends SlotItemHandler {
    private final ChargeMode mode;

    public ChargingSlot(IItemHandler itemHandler, int index, ChargeMode mode, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.mode = mode;
    }

    public ChargeMode getMode() {
        return mode;
    }

    public enum ChargeMode {
        CHARGE,
        DECHARGE,
    }
}
