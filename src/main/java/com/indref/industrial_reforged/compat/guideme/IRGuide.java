package com.indref.industrial_reforged.compat.guideme;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.compat.guideme.tags.EnergyTierTagExtension;
import com.indref.industrial_reforged.compat.guideme.tags.MultiblockShapeCompiler;
import guideme.Guide;
import guideme.GuidesCommon;
import guideme.compiler.TagCompiler;
import guideme.scene.element.SceneElementTagCompiler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public final class IRGuide {
    public static final ResourceLocation ID = IndustrialReforged.rl("guide");

    public static void init() {
        Guide.builder(ID)
                .folder("ir_guidebook")
                .extension(SceneElementTagCompiler.EXTENSION_POINT, new MultiblockShapeCompiler())
                .extension(TagCompiler.EXTENSION_POINT, new EnergyTierTagExtension())
                .build();
    }

    public static void openGuide(Player player) {
        GuidesCommon.openGuide(player, ID);
    }

}
