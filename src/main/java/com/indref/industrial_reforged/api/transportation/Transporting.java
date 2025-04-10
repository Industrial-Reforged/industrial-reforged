package com.indref.industrial_reforged.api.transportation;

import org.jetbrains.annotations.Nullable;

public class Transporting<T> {
    private @Nullable T value;

    public void setValue(@Nullable T value) {
        this.value = value;
    }

    public @Nullable T getValue() {
        return value;
    }
}
