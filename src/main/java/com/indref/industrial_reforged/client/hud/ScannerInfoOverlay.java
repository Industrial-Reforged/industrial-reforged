package com.indref.industrial_reforged.client.hud;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.IScannable;
import com.indref.industrial_reforged.api.energy.blocks.IEnergyBlock;
import com.indref.industrial_reforged.content.IRBlocks;
import com.indref.industrial_reforged.content.items.ScannerItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class ScannerInfoOverlay {
    public static final IGuiOverlay HUD_SCANNER_INFO = ((gui, guiGraphics, partialTick, width, height) -> {
        int x = width / 2;
        int y = height / 2;
        Minecraft minecraft = Minecraft.getInstance();
        HitResult.Type hitResultType = minecraft.hitResult.getType();
        if (hitResultType == HitResult.Type.BLOCK && (minecraft.player.getMainHandItem().getItem() instanceof ScannerItem || minecraft.player.getOffhandItem().getItem() instanceof ScannerItem)) {
            BlockPos hitResultBlockPos = ((BlockHitResult) minecraft.hitResult).getBlockPos();
            BlockState blockstate = minecraft.level.getBlockState(hitResultBlockPos);
            if (blockstate.is(IRBlocks.TEST_BLOCK.get())) {
                if (blockstate.getBlock() instanceof IScannable iScannable) {
                    minecraft.player.sendSystemMessage(blockstate.getBlock().getName());
                    minecraft.player.sendSystemMessage(Component.literal("BlockState"));
                    guiGraphics.drawCenteredString(Minecraft.getInstance().font,
                            iScannable.displayText(blockstate).get(0).get(0), x, y, 256);
                }
            }
        }
    });
}
