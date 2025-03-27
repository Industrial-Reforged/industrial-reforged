package com.indref.industrial_reforged.client.hud;

import com.indref.industrial_reforged.IndustrialReforgedClient;
import com.indref.industrial_reforged.api.items.tools.ClientDisplayItem;
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
            ItemStack item = player.getMainHandItem();
            if (!IndustrialReforgedClient.DISPLAY_ITEMS.containsKey(item.getItem())) {
                ItemStack offhandItem = player.getOffhandItem();
                if (IndustrialReforgedClient.DISPLAY_ITEMS.containsKey(offhandItem.getItem())) {
                    item = offhandItem;
                } else {
                    item = ItemStack.EMPTY;
                }
            }
            if (!item.isEmpty()) {
                ClientDisplayItem clientDisplayItem = IndustrialReforgedClient.DISPLAY_ITEMS.get(item.getItem());
                clientDisplayItem.displayOverlay(guiGraphics, x, y, lineOffset, level, player, blockHitResult.getBlockPos(), item);
            }
        }
    };
}