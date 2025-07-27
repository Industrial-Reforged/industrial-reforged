package com.indref.industrial_reforged.util;

import net.neoforged.fml.ModList;

public final class CompatUtils {
    public static boolean isGuideMELoaded() {
        return ModList.get().isLoaded("guideme");
    }
}
