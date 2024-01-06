package com.indref.industrial_reforged.events;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.storage.EnergyWrapper;
import com.indref.industrial_reforged.api.capabilities.heat.storage.HeatWrapper;
import com.indref.industrial_reforged.api.items.IMultiBarItem;
import com.indref.industrial_reforged.api.items.SimpleFluidItem;
import com.indref.industrial_reforged.api.items.container.IEnergyItem;
import com.indref.industrial_reforged.api.items.container.IFluidItem;
import com.indref.industrial_reforged.api.items.container.IHeatItem;
import com.indref.industrial_reforged.client.hud.ScannerInfoOverlay;
import com.indref.industrial_reforged.client.renderer.MultiBarRenderer;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.registries.IRMenuTypes;
import com.indref.industrial_reforged.registries.items.tools.TapeMeasureItem;
import com.indref.industrial_reforged.registries.screen.CraftingStationScreen;
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
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiOverlaysEvent;
import net.neoforged.neoforge.client.event.RegisterItemDecorationsEvent;
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack;

@SuppressWarnings("unused")
public class IREvents {
    @Mod.EventBusSubscriber(modid = IndustrialReforged.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Client {
        @SubscribeEvent
        public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
            event.registerAboveAll(new ResourceLocation(IndustrialReforged.MODID, "scanner_info_overlay"), ScannerInfoOverlay.HUD_SCANNER_INFO);
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            MenuScreens.register(IRMenuTypes.FIREBOX_MENU.get(), FireBoxScreen::new);
            MenuScreens.register(IRMenuTypes.CRUCIBLE_MENU.get(), CrucibleScreen::new);
            MenuScreens.register(IRMenuTypes.CRAFTING_STATION_MENU.get(), CraftingStationScreen::new);
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
            event.enqueueWork(() -> ItemProperties.register(IRItems.TAPE_MEASURE.get(), new ResourceLocation(IndustrialReforged.MODID, "extended"),
                    (stack, level, living, id) -> TapeMeasureItem.isExtended(stack)));
        }
    }


    @Mod.EventBusSubscriber(modid = IndustrialReforged.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Server {
        @SubscribeEvent
        public static void registerCapabilities(RegisterCapabilitiesEvent event) {
            for (Item item : BuiltInRegistries.ITEM) {
                if (item instanceof IEnergyItem)
                    event.registerItem(IRCapabilities.EnergyStorage.ITEM, (stack, ctx) -> new EnergyWrapper.Item(stack), item);

                if (item instanceof IHeatItem)
                    event.registerItem(IRCapabilities.HeatStorage.ITEM, (stack, ctx) -> new HeatWrapper.Item(stack), item);

                if (item instanceof IFluidItem fluidItem)
                    event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ctx) -> new FluidHandlerItemStack(stack, fluidItem.getFluidCapacity()), item);
            }

            // Register all your block entity capabilities manually
            event.registerBlockEntity(IRCapabilities.EnergyStorage.BLOCK, IRBlockEntityTypes.TEST_GEN.get(), (blockEntity, ctx) -> new EnergyWrapper.Block(blockEntity));
            event.registerBlockEntity(IRCapabilities.EnergyStorage.BLOCK, IRBlockEntityTypes.BASIC_GENERATOR.get(), (blockEntity, ctx) -> new EnergyWrapper.Block(blockEntity));
            event.registerBlockEntity(IRCapabilities.EnergyStorage.BLOCK, IRBlockEntityTypes.TEST_BLOCK.get(), (blockEntity, ctx) -> new EnergyWrapper.Block(blockEntity));
            event.registerBlockEntity(IRCapabilities.EnergyStorage.BLOCK, IRBlockEntityTypes.CENTRIFUGE.get(), (blockEntity, ctx) -> new EnergyWrapper.Block(blockEntity));

            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, IRBlockEntityTypes.FIREBOX.get(), (blockEntity, ctx) -> blockEntity.getItemHandler());
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, IRBlockEntityTypes.CRUCIBLE.get(), (blockEntity, ctx) -> blockEntity.getItemHandler());
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, IRBlockEntityTypes.CRAFTING_STATION.get(), (blockEntity, ctx) -> blockEntity.getItemHandler());

            event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, IRBlockEntityTypes.CRUCIBLE.get(), (blockEntity, ctx) -> blockEntity.getFluidTank());
        }
    }
}
