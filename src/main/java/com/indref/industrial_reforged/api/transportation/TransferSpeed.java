package com.indref.industrial_reforged.api.transportation;

public interface TransferSpeed {
    static TransferSpeed speed(float speed) {
        return new TransferSpeed() {
            @Override
            public float speed() {
                return speed;
            }

            @Override
            public boolean isInstant() {
                return false;
            }
        };
    }

    static TransferSpeed instant() {
        return new TransferSpeed() {
            @Override
            public float speed() {
                return -1;
            }

            @Override
            public boolean isInstant() {
                return true;
            }
        };
    }

    float speed();

    boolean isInstant();
}
