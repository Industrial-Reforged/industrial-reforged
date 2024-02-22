package com.indref.industrial_reforged.client.hud;

import com.indref.industrial_reforged.api.blocks.container.IEnergyBlock;
import com.indref.industrial_reforged.api.items.DisplayItem;
import com.indref.industrial_reforged.api.items.container.IEnergyItem;
import com.indref.industrial_reforged.registries.items.tools.ScannerItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.client.gui.overlay.IGuiOverlay;

public class ScannerInfoOverlay {
    // TODO: 10/17/2023 implement off-hand

    public static final IGuiOverlay HUD_SCANNER_INFO = (gui, guiGraphics, partialTick, width, height) -> {
        Minecraft minecraft = Minecraft.getInstance();
        int lineOffset = 0;
        int x = width / 2;
        int y = height / 2;
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