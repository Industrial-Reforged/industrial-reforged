package com.indref.industrial_reforged.client;

import com.indref.industrial_reforged.IndustrialReforged;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = IndustrialReforged.MODID)
public final class IRClientConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.IntValue MOLD_SELECTION_ITEMS_PER_ROW = BUILDER
            .comment("The amount of casting molds per row, when selecting the mold")
            .defineInRange("mold_selection_items_per_row", 5, 1, 99);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static int moldSelectionItemsPerRow;

    @SubscribeEvent
    public static void onConfigReload(ModConfigEvent event) {
        if (event.getConfig().getSpec() != SPEC) return;

        moldSelectionItemsPerRow = MOLD_SELECTION_ITEMS_PER_ROW.getAsInt();
    }
}
