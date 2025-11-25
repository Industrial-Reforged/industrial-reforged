package com.indref.industrial_reforged.client;

import com.indref.industrial_reforged.IndustrialReforged;
import com.portingdeadmods.portingdeadlibs.api.config.ConfigValue;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class IRClientConfig {
    @ConfigValue(name = "Mold Selection Items Per Row", comment = "The amount of casting molds displayed per row, when selecting the mold", range = {1, 99})
    public static int moldSelectionItemsPerRow = 5;
}
