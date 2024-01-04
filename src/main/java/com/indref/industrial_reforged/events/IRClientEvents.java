package com.indref.industrial_reforged.events;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.items.IMultiBarItem;
import com.indref.industrial_reforged.api.items.SimpleFluidItem;
import com.indref.industrial_reforged.client.hud.ScannerInfoOverlay;
import com.indref.industrial_reforged.client.renderer.MultiBarRenderer;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.registries.IRMenuTypes;
import com.indref.industrial_reforged.registries.items.tools.TapeMeasureItem;
import com.indref.industrial_reforged.registries.screen.CrucibleScreen;
import com.indref.industrial_reforged.registries.screen.FireBoxScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiOverlaysEvent;
import net.neoforged.neoforge.client.event.RegisterItemDecorationsEvent;

@Mod.EventBusSubscriber(modid = IndustrialReforged.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IRClientEvents {
    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll(new ResourceLocation(IndustrialReforged.MODID, "scanner_info_overlay"), ScannerInfoOverlay.HUD_SCANNER_INFO);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(IRMenuTypes.FIREBOX_MENU.get(), FireBoxScreen::new);
        MenuScreens.register(IRMenuTypes.CRUCIBLE_MENU.get(), CrucibleScreen::new);
    }

    @SubscribeEvent
    public static void itemDecorationRender(RegisterItemDecorationsEvent event) {
        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof IMultiBarItem)
                event.register(item, new MultiBarRenderer(item));
        }
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
