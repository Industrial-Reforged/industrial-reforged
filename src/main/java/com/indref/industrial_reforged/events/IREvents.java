package com.indref.industrial_reforged.events;

import com.google.common.base.Preconditions;
import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blockentities.container.ContainerBlockEntity;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.ItemEnergyWrapper;
import com.indref.industrial_reforged.api.capabilities.heat.ItemHeatWrapper;
import com.indref.industrial_reforged.api.fluids.BaseFluidType;
import com.indref.industrial_reforged.api.multiblocks.Multiblock;
import com.indref.industrial_reforged.api.multiblocks.MultiblockLayer;
import com.indref.industrial_reforged.client.model.CrucibleModel;
import com.indref.industrial_reforged.client.renderer.blockentity.CrucibleRenderer;
import com.indref.industrial_reforged.registries.*;
import com.indref.industrial_reforged.api.items.MultiBarItem;
import com.indref.industrial_reforged.api.items.bundles.AdvancedBundleContents;
import com.indref.industrial_reforged.api.items.container.IEnergyItem;
import com.indref.industrial_reforged.api.items.container.IFluidItem;
import com.indref.industrial_reforged.api.items.container.IHeatItem;
import com.indref.industrial_reforged.api.items.container.SimpleFluidItem;
import com.indref.industrial_reforged.client.hud.ScannerInfoOverlay;
import com.indref.industrial_reforged.client.renderer.blockentity.CastingBasinRenderer;
import com.indref.industrial_reforged.client.renderer.item.bar.CrucibleProgressRenderer;
import com.indref.industrial_reforged.client.renderer.item.bar.MultiBarRenderer;
import com.indref.industrial_reforged.networking.*;
import com.indref.industrial_reforged.client.renderer.blockentity.FaucetRenderer;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.part.FireboxPartBlockEntity;
import com.indref.industrial_reforged.registries.gui.screens.*;
import com.indref.industrial_reforged.registries.items.misc.BlueprintItem;
import com.indref.industrial_reforged.registries.items.storage.BatteryItem;
import com.indref.industrial_reforged.registries.items.storage.ToolboxItem;
import com.indref.industrial_reforged.registries.items.tools.NanoSaberItem;
import com.indref.industrial_reforged.registries.items.tools.TapeMeasureItem;
import com.indref.industrial_reforged.registries.items.tools.ThermometerItem;
import com.indref.industrial_reforged.util.ItemUtils;
import com.indref.industrial_reforged.util.Utils;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Map;

public class IREvents {
    @EventBusSubscriber(modid = IndustrialReforged.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
    public static class ClientBus {

        @SubscribeEvent
        public static void registerGuiOverlays(RegisterGuiLayersEvent event) {
            event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "scanner_info_overlay"), ScannerInfoOverlay.HUD_SCANNER_INFO);
        }

        @SubscribeEvent
        public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
            // Fluid renderers
            for (FluidType fluidType : NeoForgeRegistries.FLUID_TYPES) {
                if (fluidType instanceof BaseFluidType baseFluidType) {
                    event.registerFluidType(new IClientFluidTypeExtensions() {
                        @Override
                        public @NotNull ResourceLocation getStillTexture() {
                            return baseFluidType.getStillTexture();
                        }

                        @Override
                        public @NotNull ResourceLocation getFlowingTexture() {
                            return baseFluidType.getFlowingTexture();
                        }

                        @Override
                        public @Nullable ResourceLocation getOverlayTexture() {
                            return baseFluidType.getOverlayTexture();
                        }

                        @Override
                        public int getTintColor() {
                            Vec3i color = baseFluidType.getColor();
                            return FastColor.ARGB32.color(color.getX(), color.getY(), color.getZ());
                        }

                        @Override
                        public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                            Vec3i color = baseFluidType.getColor();
                            return new Vector3f(color.getX() / 255f, color.getY() / 255f, color.getZ() / 255f);
                        }

                        @Override
                        public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {
                            RenderSystem.setShaderFogStart(1f);
                            RenderSystem.setShaderFogEnd(6f); // distance when the fog starts
                        }
                    }, baseFluidType);
                }
            }
        }

        @SubscribeEvent
        public static void onClientSetup(RegisterMenuScreensEvent event) {
            event.register(IRMenuTypes.FIREBOX_MENU.get(), FireBoxScreen::new);
            event.register(IRMenuTypes.CRUCIBLE_MENU.get(), CrucibleScreen::new);
            event.register(IRMenuTypes.CENTRIFUGE_MENU.get(), CentrifugeScreen::new);
            event.register(IRMenuTypes.BLAST_FURNACE_MENU.get(), BlastFurnaceScreen::new);
            event.register(IRMenuTypes.CRAFTING_STATION_MENU.get(), CraftingStationScreen::new);
            event.register(IRMenuTypes.BASIC_GENERATOR_MENU.get(), BasicGeneratorScreen::new);
        }

        @SubscribeEvent
        public static void itemDecorationRender(RegisterItemDecorationsEvent event) {
            for (Item item : BuiltInRegistries.ITEM) {
                if (item instanceof MultiBarItem)
                    event.register(item, new MultiBarRenderer(item));
                event.register(item, new CrucibleProgressRenderer());
            }
        }

        @SubscribeEvent
        public static void registerItemColor(RegisterColorHandlersEvent.Item event) {
            event.register(new SimpleFluidItem.Colors(), IRItems.FLUID_CELL.get());
        }

        @SubscribeEvent
        public static void onFMLClientSetupEvent(final FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                ItemProperties.register(IRItems.TAPE_MEASURE.get(), ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, TapeMeasureItem.EXTENDED_KEY),
                        (stack, level, living, id) -> TapeMeasureItem.isExtended(stack));
                ItemProperties.register(IRItems.NANO_SABER.get(), ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, ItemUtils.ACTIVE_KEY),
                        (stack, level, living, id) -> NanoSaberItem.isActive(stack));
                ItemProperties.register(IRItems.BLUEPRINT.get(), ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, BlueprintItem.HAS_RECIPE_KEY),
                        (stack, level, living, id) -> BlueprintItem.hasRecipe(stack));
                ItemProperties.register(IRItems.THERMOMETER.get(), ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, ThermometerItem.DISPLAY_TEMPERATURE_KEY),
                        (stack, level, living, id) -> ThermometerItem.getTemperature(stack));
                for (Item item : BuiltInRegistries.ITEM) {
                    if (item instanceof BatteryItem) {
                        ItemProperties.register(item, ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, BatteryItem.ENERGY_STAGE_KEY),
                                (stack, level, living, id) -> BatteryItem.getEnergyStage(stack));
                    }
                }
            });
        }

        public static final Lazy<KeyMapping> JETPACK_TOGGLE = Lazy.of(() -> new KeyMapping(
                "key.indref.toggle_jetpack",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_J,
                "key.categories.indref"
        ));
        public static final Lazy<KeyMapping> JETPACK_ASCEND = Lazy.of(() -> new KeyMapping(
                "key.indref.ascend_jetpack",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_SPACE,
                "key.categories.indref"
        ));

        @SubscribeEvent
        public static void registerBindings(RegisterKeyMappingsEvent event) {
            event.register(JETPACK_TOGGLE.get());
            event.register(JETPACK_ASCEND.get());
        }

        @SubscribeEvent
        public static void registerBERenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(IRBlockEntityTypes.CRUCIBLE.get(), CrucibleRenderer::new);
            event.registerBlockEntityRenderer(IRBlockEntityTypes.CASTING_BASIN.get(), CastingBasinRenderer::new);
            event.registerBlockEntityRenderer(IRBlockEntityTypes.FAUCET.get(), FaucetRenderer::new);
        }

        @SubscribeEvent
        public static void registerModels(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(CrucibleModel.LAYER_LOCATION, CrucibleModel::createBodyLayer);
        }
    }

    @EventBusSubscriber(modid = IndustrialReforged.MODID, value = Dist.CLIENT)
    public static class Client {
        @SubscribeEvent
        public static void appendTooltips(ItemTooltipEvent event) {
            ItemStack item = event.getItemStack();
            CompoundTag tag = ItemUtils.getImmutableTag(item).copyTag();
            int meltingType = tag.getInt(CrucibleProgressRenderer.IS_MELTING_KEY);
            if (meltingType == 1) {
                event.getToolTip().add(Component.translatable("*.desc.melting_progress")
                        .append(": ")
                        .append(String.format("%.1f", tag.getFloat(CrucibleProgressRenderer.BARWIDTH_KEY)))
                        .append("/10.0")
                        .withStyle(ChatFormatting.GRAY));
            } else if (meltingType == 2) {
                event.getToolTip().add(Component.translatable("*.desc.melting_not_possible").withStyle(ChatFormatting.GRAY));
            }
        }
    }

    @EventBusSubscriber(modid = IndustrialReforged.MODID, bus = EventBusSubscriber.Bus.MOD)
    public static class CommonBus {
        @SubscribeEvent
        public static void onRegister(RegisterEvent event) {
            Registry<Multiblock> registry = event.getRegistry(IRRegistries.MULTIBLOCK.key());
            if (registry != null) {
                for (Multiblock multiblock : registry) {
                    // Check non-negative integers in definition
                    Int2ObjectMap<@Nullable Block> def = multiblock.getDefinition();
                    for (Map.Entry<Integer, @Nullable Block> entry : def.int2ObjectEntrySet()) {
                        Preconditions.checkArgument(entry.getKey() >= 0, "The integer keys for multiblock blocks are not allowed to be less than zero. Affected multiblock: "
                                + multiblock + ", affected key: " + entry.getKey() + ", " + entry.getValue());
                    }

                    // Check that multiblock has controller
                    Map<@Nullable Block, Integer> revDef = Utils.reverseMap(def);
                    int controllerKey = revDef.get(multiblock.getUnformedController());
                    boolean hasController = false;
                    for (MultiblockLayer layer : multiblock.getLayout()) {
                        for (int i : layer.layer()) {
                            if (i == controllerKey) {
                                hasController = true;
                                break;
                            }
                        }
                    }
                    if (!hasController) {
                        throw new IllegalStateException("Every multiblock needs to have at least one controller in it's layout. The controller is the block returned by Multiblock#getUnformedController(). Affected multiblock: "
                                + multiblock);
                    }

                    // Check that multiblocks have block entity types
                    if (multiblock.getMultiBlockEntityType() == null) {
                        throw new IllegalStateException("Every multiblocks controller needs to have blockentity that keeps track of data relevant for formed multiblocks. The blockentity type however is null. Affected multiblock: "+multiblock);
                    }
                }
            }
        }

        @SubscribeEvent
        public static void registerCapabilities(RegisterCapabilitiesEvent event) {
            for (Item item : BuiltInRegistries.ITEM) {
                if (item instanceof IEnergyItem energyItem)
                    event.registerItem(IRCapabilities.EnergyStorage.ITEM,
                            (stack, ctx) -> new ItemEnergyWrapper(stack, energyItem.getEnergyTier(), energyItem.getEnergyCapacity(stack)), item);

                if (item instanceof IHeatItem heatItem)
                    event.registerItem(IRCapabilities.HeatStorage.ITEM,
                            (stack, ctx) -> new ItemHeatWrapper(stack, heatItem.getHeatCapacity(stack)), item);

                if (item instanceof IFluidItem fluidItem)
                    event.registerItem(Capabilities.FluidHandler.ITEM,
                            (stack, ctx) -> new FluidHandlerItemStack(IRDataComponents.FLUID, stack, fluidItem.getFluidCapacity(stack)), item);

                if (item instanceof ToolboxItem) {
                    event.registerItem(Capabilities.ItemHandler.ITEM,
                            (stack, ctx) -> {
                                List<ItemStack> toolBoxItems = stack.getOrDefault(IRDataComponents.ADVANCED_BUNDLE_CONTENTS, AdvancedBundleContents.EMPTY).items();
                                ItemStack[] items = new ItemStack[toolBoxItems.size()];
                                return new InvWrapper(new SimpleContainer(toolBoxItems.toArray(items)));
                            }, item);
                }
            }

            // Register all your block entity capabilities manually
            event.registerBlockEntity(IRCapabilities.EnergyStorage.BLOCK, IRBlockEntityTypes.BASIC_GENERATOR.get(), (blockEntity, ctx) -> blockEntity.getEnergyStorage());
            event.registerBlockEntity(IRCapabilities.EnergyStorage.BLOCK, IRBlockEntityTypes.CENTRIFUGE.get(), (blockEntity, ctx) -> blockEntity.getEnergyStorage());

            event.registerBlockEntity(IRCapabilities.HeatStorage.BLOCK, IRBlockEntityTypes.FIREBOX.get(), ContainerBlockEntity::getHeatHandlerOnSide);
            event.registerBlockEntity(IRCapabilities.HeatStorage.BLOCK, IRBlockEntityTypes.SMALL_FIREBOX.get(), ContainerBlockEntity::getHeatHandlerOnSide);
            event.registerBlockEntity(IRCapabilities.HeatStorage.BLOCK, IRBlockEntityTypes.BLAST_FURNACE.get(), ContainerBlockEntity::getHeatHandlerOnSide);
            event.registerBlockEntity(IRCapabilities.HeatStorage.BLOCK, IRBlockEntityTypes.CRUCIBLE.get(), ContainerBlockEntity::getHeatHandlerOnSide);

            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, IRBlockEntityTypes.FIREBOX.get(), ContainerBlockEntity::getItemHandlerOnSide);
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, IRBlockEntityTypes.FIREBOX_PART.get(), FireboxPartBlockEntity::getItemHandler);
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, IRBlockEntityTypes.CRUCIBLE.get(), ContainerBlockEntity::getItemHandlerOnSide);
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, IRBlockEntityTypes.CASTING_BASIN.get(), ContainerBlockEntity::getItemHandlerOnSide);
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, IRBlockEntityTypes.CRAFTING_STATION.get(), ContainerBlockEntity::getItemHandlerOnSide);
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, IRBlockEntityTypes.BLAST_FURNACE.get(), ContainerBlockEntity::getItemHandlerOnSide);
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, IRBlockEntityTypes.SMALL_FIREBOX.get(), ContainerBlockEntity::getItemHandlerOnSide);
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, IRBlockEntityTypes.BASIC_GENERATOR.get(), ContainerBlockEntity::getItemHandlerOnSide);
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, IRBlockEntityTypes.CENTRIFUGE.get(), ContainerBlockEntity::getItemHandlerOnSide);

            event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, IRBlockEntityTypes.CRUCIBLE.get(), ContainerBlockEntity::getFluidHandlerOnSide);
            event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, IRBlockEntityTypes.DRAIN.get(), ContainerBlockEntity::getFluidHandlerOnSide);
            event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, IRBlockEntityTypes.FAUCET.get(), ContainerBlockEntity::getFluidHandlerOnSide);
            event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, IRBlockEntityTypes.CASTING_BASIN.get(), ContainerBlockEntity::getFluidHandlerOnSide);
            event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, IRBlockEntityTypes.BLAST_FURNACE.get(), ContainerBlockEntity::getFluidHandlerOnSide);
        }

        @SubscribeEvent
        public static void registerPayloads(RegisterPayloadHandlersEvent event) {
            PayloadRegistrar registrar = event.registrar(IndustrialReforged.MODID);
            registrar.playToServer(BlueprintHasRecipePayload.TYPE, BlueprintHasRecipePayload.STREAM_CODEC, PayloadActions::blueprintHasRecipe);
            registrar.playToServer(BlueprintStoredRecipePayload.TYPE, BlueprintStoredRecipePayload.STREAM_CODEC, PayloadActions::blueprintStoredRecipe);
            registrar.playToServer(ItemActivityPayload.TYPE, ItemActivityPayload.STREAM_CODEC, PayloadActions::itemActivitySync);
            registrar.playToServer(ArmorActivityPayload.TYPE, ArmorActivityPayload.STREAM_CODEC, PayloadActions::armorActivitySync);

            registrar.playToClient(CrucibleControllerPayload.TYPE, CrucibleControllerPayload.STREAM_CODEC, PayloadActions::crucibleControllerSync);
            registrar.playToClient(CrucibleMeltingProgressPayload.TYPE, CrucibleMeltingProgressPayload.STREAM_CODEC, PayloadActions::crucibleMeltingProgressSync);
        }
    }
}
