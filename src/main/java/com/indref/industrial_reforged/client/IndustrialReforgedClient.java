package com.indref.industrial_reforged.client;

import com.indref.industrial_reforged.api.items.MultiBarItem;
import com.indref.industrial_reforged.api.items.container.SimpleFluidItem;
import com.indref.industrial_reforged.api.items.tools.ClientDisplayItem;
import com.indref.industrial_reforged.client.hud.CastingMoldSelectionOverlay;
import com.indref.industrial_reforged.client.hud.ScannerInfoOverlay;
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
import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.networking.molds.SetCastingMoldPayload;
import com.indref.industrial_reforged.registries.*;
import com.indref.industrial_reforged.tags.IRTags;
import com.indref.industrial_reforged.translations.IRTranslations;
import com.indref.industrial_reforged.util.SingleFluidStack;
import com.portingdeadmods.portingdeadlibs.api.config.PDLConfigHelper;
import com.portingdeadmods.portingdeadlibs.api.fluids.PDLFluid;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.model.DynamicFluidContainerModel;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.List;
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

        NeoForge.EVENT_BUS.addListener(this::onScroll);
        NeoForge.EVENT_BUS.addListener(this::onRightClick);
        NeoForge.EVENT_BUS.addListener(this::appendTooltip);

        PDLConfigHelper.registerConfig(IRClientConfig.class, ModConfig.Type.CLIENT, modContainer);
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    public static final Map<Item, ClientDisplayItem> DISPLAY_ITEMS = new HashMap<>();

    private void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            IndustrialReforgedClient.registerItemProperties();
            IndustrialReforgedClient.registerDisplayItems();
        });
    }

    private static void registerDisplayItems() {
        //DISPLAY_ITEMS.put(IRItems.SCANNER.get(), IRDisplayItems::scanner);
        // DISPLAY_ITEMS.put(IRItems.THERMOMETER.get(), IRDisplayItems::thermometer);
    }

    @SuppressWarnings("deprecation")
    private static void registerItemProperties() {
        ItemProperties.register(IRItems.NANO_SABER.get(), IRItemProperties.ACTIVE_KEY, (ClampedItemPropertyFunction) IRItemProperties::isActive);
        ItemProperties.register(IRItems.BASIC_CHAINSAW.get(), IRItemProperties.ACTIVE_KEY, (ClampedItemPropertyFunction) IRItemProperties::isItemHeld);
        ItemProperties.register(IRItems.ADVANCED_CHAINSAW.get(), IRItemProperties.ACTIVE_KEY, (ClampedItemPropertyFunction) IRItemProperties::isItemHeld);
        ItemProperties.register(IRItems.BASIC_DRILL.get(), IRItemProperties.ACTIVE_KEY, (ClampedItemPropertyFunction) IRItemProperties::isItemHeld);
        ItemProperties.register(IRItems.ADVANCED_DRILL.get(), IRItemProperties.ACTIVE_KEY, (ClampedItemPropertyFunction) IRItemProperties::isItemHeld);
        // IMPORTANT: WE DON'T USE CLAMPED ITEM PROPERTY FUNCTION HERE CUZ IT MEANS PROPERTIES CANT GO ABOVE 1
        ItemProperties.register(IRItems.JETPACK.get(), IRItemProperties.JETPACK_STAGE_KEY, IRItemProperties::getJetpackStage);
        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof BatteryItem) {
                ItemProperties.register(item, IRItemProperties.BATTERY_STAGE_KEY, IRItemProperties::getBatteryStage);
            }
        }
    }

    private void onRightClick(InputEvent.MouseButton.Pre event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            ItemStack mainHandItem = player.getMainHandItem();
            if (event.getButton() == GLFW.GLFW_MOUSE_BUTTON_RIGHT && Screen.hasShiftDown() && mainHandItem.is(IRTags.Items.CLAY_MOLDS)) {
                List<Item> castingMoldItems = CastingMoldSelectionOverlay.CASTING_MOLD_ITEMS;
                Item item = castingMoldItems.get(CastingMoldSelectionOverlay.INDEX.get());
                if (!mainHandItem.is(item)) {
                    PacketDistributor.sendToServer(new SetCastingMoldPayload(item));
                    CastingMoldSelectionOverlay.INDEX.set(castingMoldItems.indexOf(item));
                }
            }
        }
    }

    private void onScroll(InputEvent.MouseScrollingEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (Screen.hasShiftDown()) {
            if (player.getMainHandItem().is(IRTags.Items.CLAY_MOLDS)) {
                int index = CastingMoldSelectionOverlay.INDEX.get();
                int scrollDeltaY = (int) event.getScrollDeltaY();
                if (index < CastingMoldSelectionOverlay.CASTING_MOLD_ITEMS.size() - 1 && scrollDeltaY < 0) {
                    CastingMoldSelectionOverlay.INDEX.getAndIncrement();
                } else if (index > 0 && scrollDeltaY > 0) {
                    CastingMoldSelectionOverlay.INDEX.getAndDecrement();
                }
            }
        }
    }

    private void appendTooltip(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();
        if (itemStack.is(IRTags.Items.CLAY_MOLDS)) {
            event.getToolTip().add(IRTranslations.Tooltip.CLAY_CASTING_MOLD.component().withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    private void registerGuiOverlays(RegisterGuiLayersEvent event) {
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(MODID, "scanner_info_overlay"), ScannerInfoOverlay.HUD_SCANNER_INFO);
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(MODID, "casting_mold_overlay"), CastingMoldSelectionOverlay.HUD_CASTING_MOLD_SELECTION);
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
        event.register(IRMenuTypes.BLAST_FURNACE_MENU.get(), BlastFurnaceScreen::new);
        event.register(IRMenuTypes.CRAFTING_STATION_MENU.get(), CraftingStationScreen::new);

        event.register(IRMachines.CENTRIFUGE.getMenuType(), CentrifugeScreen::new);
        event.register(IRMachines.BASIC_GENERATOR.getMenuType(), BasicGeneratorScreen::new);
        event.register(IRMachines.BATTERY_BOX.getMenuType(), BatteryBoxScreen::new);
    }

    private void registerItemDecorations(RegisterItemDecorationsEvent event) {
        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof MultiBarItem) {
                event.register(item, new MultiBarRenderer(item));
            }
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
        event.register(new DynamicFluidContainerModel.Colors(), IRFluids.STICKY_RESIN.getDeferredBucket());
        event.register((itemstack, index) -> index == 0 ? FastColor.ARGB32.color(255, itemstack.get(DataComponents.DYED_COLOR).rgb()) : -1, IRItems.TOOLBOX);
        event.register((itemstack, index) -> {
            SingleFluidStack singleFluidContent = itemstack.get(IRDataComponents.SINGLE_FLUID);
            int color = IClientFluidTypeExtensions.of(singleFluidContent.fluidStack().getFluid()).getTintColor();
            return index == 0 ? color : -1;
        }, IRItems.CASTING_SCRAPS);
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
