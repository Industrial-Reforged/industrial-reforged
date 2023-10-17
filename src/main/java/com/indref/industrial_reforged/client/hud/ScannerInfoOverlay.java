package com.indref.industrial_reforged.client.hud;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.IScannable;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.content.IRBlocks;
import com.indref.industrial_reforged.content.items.ScannerItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class ScannerInfoOverlay {
    public static final IGuiOverlay HUD_SCANNER_INFO = ((gui, guiGraphics, partialTick, width, height) -> {
        int x = width / 2;
        int y = height / 2;
        Minecraft minecraft = Minecraft.getInstance();
        Level level = minecraft.level;
        HitResult.Type hitResultType = minecraft.hitResult.getType();
        Font font = Minecraft.getInstance().font;
        if (hitResultType == HitResult.Type.BLOCK && (minecraft.player.getMainHandItem().getItem() instanceof ScannerItem || minecraft.player.getOffhandItem().getItem() instanceof ScannerItem)) {
            BlockPos hitResultBlockPos = ((BlockHitResult) minecraft.hitResult).getBlockPos();
            BlockEntity blockEntity = level.getBlockEntity(hitResultBlockPos);
            BlockState blockstate = minecraft.level.getBlockState(hitResultBlockPos);
            if (blockstate.is(IRBlocks.TEST_BLOCK_ENERGY.get())) {
                if (blockEntity instanceof IScannable iScannable) {
                    guiGraphics.drawCenteredString(font,
                            iScannable.displayText(blockstate, hitResultBlockPos, level).get(0), x, y, 256);
                    guiGraphics.drawCenteredString(font,
                            iScannable.displayText(blockstate, hitResultBlockPos, level).get(1), x, y+font.lineHeight+3, 256);
                }
            }
            level.getBlockEntity(hitResultBlockPos).getCapability(IRCapabilities.ENERGY).ifPresent(energyStorage -> IndustrialReforged.LOGGER.info(String.valueOf(energyStorage.getEnergyStored())));
        }
    });
}
