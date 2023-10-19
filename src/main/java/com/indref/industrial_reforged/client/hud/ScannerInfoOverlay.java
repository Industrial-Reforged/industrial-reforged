package com.indref.industrial_reforged.client.hud;

import com.indref.industrial_reforged.api.blocks.IScannable;
import com.indref.industrial_reforged.api.items.container.IEnergyItem;
import com.indref.industrial_reforged.content.items.ScannerItem;
import com.indref.industrial_reforged.networking.IRPackets;
import com.indref.industrial_reforged.networking.packets.C2SEnergySync;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class ScannerInfoOverlay {
    // TODO: 10/17/2023 implement off-hand 
    public static final IGuiOverlay HUD_SCANNER_INFO = ((gui, guiGraphics, partialTick, width, height) -> {
        int lineOffset = 0;
        int x = width / 2;
        int y = height / 2;
        Minecraft minecraft = Minecraft.getInstance();
        Level level = minecraft.level;
        HitResult.Type hitResultType = minecraft.hitResult.getType();
        Font font = Minecraft.getInstance().font;
        ItemStack mainHandStack = minecraft.player.getMainHandItem();
        if (hitResultType == HitResult.Type.BLOCK && (minecraft.player.getMainHandItem().getItem() instanceof ScannerItem || minecraft.player.getOffhandItem().getItem() instanceof ScannerItem)) {
            IEnergyItem mainHandEnergyItem = (IEnergyItem) minecraft.player.getMainHandItem().getItem();
            BlockPos hitResultBlockPos = ((BlockHitResult) minecraft.hitResult).getBlockPos();
            BlockEntity blockEntity = level.getBlockEntity(hitResultBlockPos);
            BlockState blockstate = minecraft.level.getBlockState(hitResultBlockPos);
            if (blockEntity instanceof IScannable iScannable) {
                for (Component component : iScannable.displayText(blockstate, hitResultBlockPos, level)) {
                    guiGraphics.drawCenteredString(font, component, x, y + lineOffset, 256);
                    lineOffset += font.lineHeight + 3;
                }
                mainHandEnergyItem.setStored(mainHandStack, mainHandEnergyItem.getStored(mainHandStack) - 1);
                IRPackets.sendToServer(new C2SEnergySync());
            }
        }
    });
}
