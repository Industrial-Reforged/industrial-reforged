package com.indref.industrial_reforged.client.hud;

import com.indref.industrial_reforged.api.items.DisplayItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

public class ScannerInfoOverlay {
    public static final LayeredDraw.Layer HUD_SCANNER_INFO = (guiGraphics, delta) -> {
        Minecraft minecraft = Minecraft.getInstance();
        int lineOffset = 0;
        int x = guiGraphics.guiWidth() / 2;
        int y = guiGraphics.guiHeight() / 2;
        Level level = minecraft.level;
        Player player = minecraft.player;
        if (minecraft.hitResult instanceof BlockHitResult blockHitResult) {
            ItemStack mainHandItem = player.getMainHandItem();
            ItemStack offhandItem = player.getOffhandItem();
            if (mainHandItem.getItem() instanceof DisplayItem displayItem) {
                displayItem.displayOverlay(guiGraphics, x, y, lineOffset, level, player, blockHitResult.getBlockPos(), mainHandItem);
            } else if (offhandItem.getItem() instanceof DisplayItem displayItem) {
                displayItem.displayOverlay(guiGraphics, x, y, lineOffset, level, player, blockHitResult.getBlockPos(), offhandItem);
            }
        }
    };
}