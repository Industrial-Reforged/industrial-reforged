package com.indref.industrial_reforged.impl.energy;

import com.indref.industrial_reforged.api.capabilities.energy.EnergyHandler;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.impl.tiers.EnergyTierImpl;

import java.util.function.Supplier;

public class EnergyHandlerWrapper {
    protected final EnergyHandler handler;

    public EnergyHandlerWrapper(EnergyHandler handler) {
        this.handler = handler;
    }

    public static final class NoDrain extends EnergyHandlerWrapper implements EnergyHandler.NoDrain {
        public NoDrain(EnergyHandler handler) {
            super(handler);
        }

        @Override
        public EnergyTier getEnergyTier() {
            return this.handler.getEnergyTier();
        }

        @Override
        public int getEnergyStored() {
            return this.handler.getEnergyStored();
        }

        @Override
        public void setEnergyStored(int value) {
            this.handler.setEnergyStored(value);
        }

        @Override
        public int getEnergyCapacity() {
            return this.handler.getEnergyCapacity();
        }

        @Override
        public void setEnergyCapacity(int value) {
            this.handler.setEnergyCapacity(value);
        }
    }

    public static final class NoFill extends EnergyHandlerWrapper implements EnergyHandler.NoFill {
        public NoFill(EnergyHandler handler) {
            super(handler);
        }

        @Override
        public EnergyTier getEnergyTier() {
            return this.handler.getEnergyTier();
        }

        @Override
        public int getEnergyStored() {
            return this.handler.getEnergyStored();
        }

        @Override
        public void setEnergyStored(int value) {
            this.handler.setEnergyStored(value);
        }

        @Override
        public int getEnergyCapacity() {
            return this.handler.getEnergyCapacity();
        }

        @Override
        public void setEnergyCapacity(int value) {
            this.handler.setEnergyCapacity(value);
        }
    }

}
