package com.indref.industrial_reforged.event;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.client.hud.ScannerInfoOverlay;
import com.indref.industrial_reforged.screen.IRMenuTypes;
import com.indref.industrial_reforged.screen.SimplePressMenu;
import com.indref.industrial_reforged.screen.SimplePressScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = IndustrialReforged.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IRClientEvents {
    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("scanner_info_overlay", ScannerInfoOverlay.HUD_SCANNER_INFO);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(IRMenuTypes.SIMPLE_PRESS_MENU.get(), SimplePressScreen::new);
    }
}
