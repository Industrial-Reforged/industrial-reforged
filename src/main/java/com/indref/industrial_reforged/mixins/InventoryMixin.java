package com.indref.industrial_reforged.mixins;

import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.tags.IRTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Inventory.class)
public class InventoryMixin {
    @Shadow
    @Final
    public Player player;

    @Inject(method = "swapPaint", at = @At("HEAD"), cancellable = true)
    private void inventoryMixin$swapPaint(double direction, CallbackInfo ci) {
        if (Screen.hasShiftDown() && (player.getMainHandItem().is(IRTags.Items.CLAY_MOLDS))) {
            ci.cancel();
        }
    }
}
