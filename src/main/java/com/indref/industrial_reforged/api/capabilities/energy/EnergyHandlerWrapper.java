package com.indref.industrial_reforged.api.capabilities.energy;

import com.indref.industrial_reforged.api.tiers.EnergyTier;

import java.util.function.Supplier;

public class EnergyHandlerWrapper {
    protected final IEnergyHandler handler;

    public EnergyHandlerWrapper(IEnergyHandler handler) {
        this.handler = handler;
    }

    public static final class NoDrain extends EnergyHandlerWrapper implements IEnergyHandler.NoDrain {
        public NoDrain(IEnergyHandler handler) {
            super(handler);
        }

        @Override
        public Supplier<EnergyTier> getEnergyTier() {
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

    public static final class NoFill extends EnergyHandlerWrapper implements IEnergyHandler.NoFill {
        public NoFill(IEnergyHandler handler) {
            super(handler);
        }

        @Override
        public Supplier<EnergyTier> getEnergyTier() {
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
