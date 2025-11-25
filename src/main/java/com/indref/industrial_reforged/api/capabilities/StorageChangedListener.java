package com.indref.industrial_reforged.api.capabilities;

import java.util.function.Consumer;

public interface StorageChangedListener {
    void setOnChangedFunction(Consumer<Integer> onChangedFunction);
}
