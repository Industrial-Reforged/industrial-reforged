package com.indref.industrial_reforged.events;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.items.SimpleFluidItem;
import com.indref.industrial_reforged.client.hud.ScannerInfoOverlay;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.registries.items.tools.TapeMeasureItem;
import com.indref.industrial_reforged.registries.screen.FireBoxScreen;
import com.indref.industrial_reforged.registries.IRMenuTypes;
import com.indref.industrial_reforged.registries.screen.SimplePressScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiOverlaysEvent;

@Mod.EventBusSubscriber(modid = IndustrialReforged.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IRClientEvents {
    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("scanner_info_overlay", ScannerInfoOverlay.HUD_SCANNER_INFO);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(IRMenuTypes.SIMPLE_PRESS_MENU.get(), SimplePressScreen::new);
        MenuScreens.register(IRMenuTypes.FIREBOX_MENU.get(), FireBoxScreen::new);
    }

    @SubscribeEvent
    public static void registerItemColor(RegisterColorHandlersEvent.Item event) {
        event.register(new SimpleFluidItem.Colors(), IRItems.FLUID_CELL.get());
    }

    @SubscribeEvent
    public static void onFMLClientSetupEvent(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(IRItems.TAPE_MEASURE.get(), new ResourceLocation(IndustrialReforged.MODID, "extended"),
                    (stack, level, living, id) -> TapeMeasureItem.isExtended(stack));
        });
    }
}
