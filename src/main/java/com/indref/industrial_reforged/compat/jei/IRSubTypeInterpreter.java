package com.indref.industrial_reforged.compat.jei;

import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IRSubTypeInterpreter<T> extends ISubtypeInterpreter<T> {
    @Override
    default @NotNull String getLegacyStringSubtypeInfo(@NotNull T ingredient, @NotNull UidContext context) {
        return "";
    }
}
