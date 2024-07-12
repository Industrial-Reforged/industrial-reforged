package com.indref.industrial_reforged.client.hud;

import com.indref.industrial_reforged.api.items.DisplayItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class ScannerInfoOverlay {
    public static final LayeredDraw.Layer HUD_SCANNER_INFO = (guiGraphics, var1) -> {
        Minecraft minecraft = Minecraft.getInstance();
        int lineOffset = 0;
        int x = guiGraphics.guiWidth() / 2;
        int y = guiGraphics.guiHeight() / 2;
        Level level = minecraft.level;
        Player player = minecraft.player;
        if (minecraft.hitResult instanceof BlockHitResult blockHitResult) {
            if (minecraft.player.getMainHandItem().getItem() instanceof DisplayItem displayItem) {
                displayItem.displayOverlay(guiGraphics, x, y, lineOffset, level, player, blockHitResult.getBlockPos());
            } else if (minecraft.player.getOffhandItem().getItem() instanceof DisplayItem displayItem) {
                displayItem.displayOverlay(guiGraphics, x, y, lineOffset, level, player, blockHitResult.getBlockPos());
            }
        }
    };
}