package com.indref.industrial_reforged.client.hud;

import com.indref.industrial_reforged.client.IRClientConfig;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.tags.IRTags;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class CastingMoldSelectionOverlay {
    public static final List<Item> CASTING_MOLD_ITEMS = new ArrayList<>();
    public static final AtomicInteger INDEX = new AtomicInteger(0);
    private static final int ITEM_SIZE = 18;

    public static final LayeredDraw.Layer HUD_CASTING_MOLD_SELECTION = (guiGraphics, delta) -> {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        ItemStack mainHandItem = player.getMainHandItem();
        if (mainHandItem.is(IRTags.Items.CLAY_MOLDS) && Screen.hasShiftDown()) {
            int x = guiGraphics.guiWidth() / 2;
            int y = guiGraphics.guiHeight() / 2;
            int length = CASTING_MOLD_ITEMS.size();
            float startX = x - (length * 18) / 2f;
            int scrollIndex = INDEX.get();

            int itemsPerRow = IRClientConfig.moldSelectionItemsPerRow;
            int index = 0;

            for (Item item : CASTING_MOLD_ITEMS) {
                PoseStack poseStack = guiGraphics.pose();
                poseStack.pushPose();

                float highlightPadding = 0;

                int row = index / itemsPerRow;
                int col = index % itemsPerRow;

                float mod = 1f;
                float offset = 0f;
                boolean highlighted = index == scrollIndex;

                int scrollRow = scrollIndex / itemsPerRow;
                if (row == scrollRow) {
                    if (index < scrollIndex) {
                        highlightPadding = -4f;
                    } else if (index > scrollIndex) {
                        highlightPadding = 4f;
                    }
                }

                float itemX = startX + col * ITEM_SIZE;
                float itemY = y + row * (ITEM_SIZE + 3);

                if (highlighted) {
                    poseStack.scale(1.5f, 1.5f, 1f);
                    mod = 1.5f;
                    offset = ITEM_SIZE / 4f;
                }

                guiGraphics.renderItem(
                        item.getDefaultInstance(),
                        (int) ((itemX - offset + highlightPadding) / mod),
                        (int) ((itemY - offset) / mod)
                );

                poseStack.popPose();
                index++;
            }
        }
    };
}
