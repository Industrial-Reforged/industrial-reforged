package com.indref.industrial_reforged;

import com.indref.industrial_reforged.api.items.MultiBarItem;
import com.indref.industrial_reforged.api.items.container.SimpleFluidItem;
import com.indref.industrial_reforged.api.items.tools.DisplayItem;
import com.indref.industrial_reforged.client.hud.ScannerInfoOverlay;
import com.indref.industrial_reforged.client.item.IRDisplayItems;
import com.indref.industrial_reforged.client.item.IRItemProperties;
import com.indref.industrial_reforged.client.model.BlastFurnaceItemModel;
import com.indref.industrial_reforged.client.model.CrucibleModel;
import com.indref.industrial_reforged.client.renderer.blockentity.CastingBasinRenderer;
import com.indref.industrial_reforged.client.renderer.blockentity.CrucibleRenderer;
import com.indref.industrial_reforged.client.renderer.blockentity.FaucetRenderer;
import com.indref.industrial_reforged.client.renderer.item.BlastFurnaceItemRenderer;
import com.indref.industrial_reforged.client.renderer.item.CrucibleItemRenderer;
import com.indref.industrial_reforged.client.renderer.item.bar.CrucibleProgressRenderer;
import com.indref.industrial_reforged.client.renderer.item.bar.MultiBarRenderer;
import com.indref.industrial_reforged.client.screen.*;
import com.indref.industrial_reforged.content.fluids.MoltenMetalFluid;
import com.indref.industrial_reforged.content.items.storage.BatteryItem;
import com.indref.industrial_reforged.content.items.tools.NanoSaberItem;
import com.indref.industrial_reforged.content.items.tools.ThermometerItem;
import com.indref.industrial_reforged.registries.*;
import com.indref.industrial_reforged.util.ItemUtils;
import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.portingdeadmods.portingdeadlibs.api.fluids.BaseFluidType;
import com.portingdeadmods.portingdeadlibs.api.fluids.PDLFluid;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.Vec3i;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.model.DynamicFluidContainerModel;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

@Mod(IndustrialReforgedClient.MODID)
public final class IndustrialReforgedClient {
    public static final String MODID = "indref";

    public IndustrialReforgedClient(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::registerGuiOverlays);
        modEventBus.addListener(this::registerBERenderers);
        modEventBus.addListener(this::registerClientExtensions);
        modEventBus.addListener(this::registerItemColor);
        modEventBus.addListener(this::registerModels);
        modEventBus.addListener(this::onClientSetup);
        modEventBus.addListener(this::registerItemDecorations);
        modEventBus.addListener(this::registerMenuScreens);
    }

    public static final Map<Item, DisplayItem> DISPLAY_ITEMS = new HashMap<>();

    private void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            registerItemProperties();
            registerDisplayItems();
        });
    }

    private static void registerDisplayItems() {
        DISPLAY_ITEMS.put(IRItems.SCANNER.get(), IRDisplayItems::scanner);
        DISPLAY_ITEMS.put(IRItems.THERMOMETER.get(), IRDisplayItems::thermometer);
    }

    private static void registerItemProperties() {
        ItemProperties.register(IRItems.NANO_SABER.get(), IRItemProperties.ACTIVE_KEY, IRItemProperties::isActive);
        ItemProperties.register(IRItems.THERMOMETER.get(), IRItemProperties.TEMPERATURE_KEY, IRItemProperties::getTemperature);
        ItemProperties.register(IRItems.ELECTRIC_CHAINSAW.get(), IRItemProperties.ACTIVE_KEY, IRItemProperties::isActive);
        ItemProperties.register(IRItems.ADVANCED_CHAINSAW.get(), IRItemProperties.ACTIVE_KEY, IRItemProperties::isActive);
        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof BatteryItem batteryItem) {
                ItemProperties.register(item, IRItemProperties.BATTERY_STAGE_KEY,
                        (stack, level, living, id) -> batteryItem.getEnergyStage(stack));
            }
        }
    }

    private void registerGuiOverlays(RegisterGuiLayersEvent event) {
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(MODID, "scanner_info_overlay"), ScannerInfoOverlay.HUD_SCANNER_INFO);
    }

    private static final CrucibleItemRenderer CRUCIBLE_ITEM_RENDERER = new CrucibleItemRenderer();
    private static final BlastFurnaceItemRenderer BLAST_FURNACE_ITEM_RENDERER = new BlastFurnaceItemRenderer();

    private void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new IClientItemExtensions() {
            @Override
            public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return CRUCIBLE_ITEM_RENDERER;
            }
        }, IRBlocks.CERAMIC_CRUCIBLE_CONTROLLER.asItem());

        event.registerItem(new IClientItemExtensions() {
            @Override
            public @NotNull BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return BLAST_FURNACE_ITEM_RENDERER;
            }
        }, IRBlocks.BLAST_FURNACE_CONTROLLER.asItem());
    }

    private void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(IRMenuTypes.FIREBOX_MENU.get(), FireBoxScreen::new);
        event.register(IRMenuTypes.CRUCIBLE_MENU.get(), CrucibleScreen::new);
        event.register(IRMenuTypes.CENTRIFUGE_MENU.get(), CentrifugeScreen::new);
        event.register(IRMenuTypes.BLAST_FURNACE_MENU.get(), BlastFurnaceScreen::new);
        event.register(IRMenuTypes.CRAFTING_STATION_MENU.get(), CraftingStationScreen::new);
        event.register(IRMenuTypes.BASIC_GENERATOR_MENU.get(), BasicGeneratorScreen::new);

        event.register(IRMenuTypes.BLUEPRINT_MENU.get(), BlueprintScreen::new);
    }

    private void registerItemDecorations(RegisterItemDecorationsEvent event) {
        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof MultiBarItem)
                event.register(item, new MultiBarRenderer(item));
            event.register(item, new CrucibleProgressRenderer());
        }
    }

    private void registerItemColor(RegisterColorHandlersEvent.Item event) {
        event.register(new SimpleFluidItem.Colors(), IRItems.FLUID_CELL.get());
        for (PDLFluid fluid : IRFluids.HELPER.getFluids()) {
            if (fluid instanceof MoltenMetalFluid) {
                event.register(new DynamicFluidContainerModel.Colors(), fluid.getDeferredBucket());
            }
        }
        event.register(new DynamicFluidContainerModel.Colors(), IRFluids.BIO_MASS.getDeferredBucket());
        event.register(new DynamicFluidContainerModel.Colors(), IRFluids.METHANE.getDeferredBucket());
        event.register((itemstack, index) -> index == 0 ? FastColor.ARGB32.color(255, itemstack.get(DataComponents.DYED_COLOR).rgb()) : -1, IRItems.TOOLBOX);
    }

    private void registerBERenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(IRBlockEntityTypes.CRUCIBLE.get(), CrucibleRenderer::new);
        event.registerBlockEntityRenderer(IRBlockEntityTypes.CASTING_BASIN.get(), CastingBasinRenderer::new);
        event.registerBlockEntityRenderer(IRBlockEntityTypes.FAUCET.get(), FaucetRenderer::new);
    }

    private void registerModels(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CrucibleModel.LAYER_LOCATION, CrucibleModel::createBodyLayer);
        event.registerLayerDefinition(BlastFurnaceItemModel.LAYER_LOCATION, BlastFurnaceItemModel::createBodyLayer);
    }

}
