package com.indref.industrial_reforged;

import com.google.common.base.Preconditions;
import com.indref.industrial_reforged.api.blockentities.IRContainerBlockEntity;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.ItemEnergyWrapper;
import com.indref.industrial_reforged.api.capabilities.heat.ItemHeatWrapper;
import com.indref.industrial_reforged.api.items.bundles.AdvancedBundleContents;
import com.indref.industrial_reforged.api.items.container.IEnergyItem;
import com.indref.industrial_reforged.api.items.container.IHeatItem;
import com.indref.industrial_reforged.api.transportation.TransportNetwork;
import com.indref.industrial_reforged.content.blockentities.multiblocks.part.BlastFurnacePartBlockEntity;
import com.indref.industrial_reforged.content.blockentities.multiblocks.part.CruciblePartBlockEntity;
import com.indref.industrial_reforged.content.items.storage.ToolboxItem;
import com.indref.industrial_reforged.data.IRAttachmentTypes;
import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.data.IRDataMaps;
import com.indref.industrial_reforged.networking.*;
import com.indref.industrial_reforged.networking.crucible.CrucibleControllerPayload;
import com.indref.industrial_reforged.networking.crucible.CrucibleMeltingProgressPayload;
import com.indref.industrial_reforged.networking.crucible.CrucibleTurnPayload;
import com.indref.industrial_reforged.networking.crucible.EmptyCruciblePayload;
import com.indref.industrial_reforged.networking.molds.SetCastingMoldPayload;
import com.indref.industrial_reforged.networking.molds.SyncCastingMoldsPayload;
import com.indref.industrial_reforged.networking.transportation.*;
import com.indref.industrial_reforged.registries.*;
import com.indref.industrial_reforged.tags.IRTags;
import com.indref.industrial_reforged.util.Utils;
import com.mojang.logging.LogUtils;
import com.portingdeadmods.portingdeadlibs.PDLRegistries;
import com.portingdeadmods.portingdeadlibs.api.items.IFluidItem;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.Multiblock;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.MultiblockDefinition;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.MultiblockLayer;
import com.portingdeadmods.portingdeadlibs.utils.capabilities.CapabilityRegistrationHelper;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import org.slf4j.Logger;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Mod(IndustrialReforged.MODID)
public final class IndustrialReforged {
    public static final String MODID = "indref";
    public static final Logger LOGGER = LogUtils.getLogger();

    public IndustrialReforged(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::registerRegistries);
        modEventBus.addListener(this::registerDataMaps);
        modEventBus.addListener(this::onRegister);
        modEventBus.addListener(this::registerCapabilities);
        modEventBus.addListener(this::registerPayloads);
        modEventBus.addListener(this::addFeaturePacks);

        NeoForge.EVENT_BUS.addListener(this::onDatapackReload);

        IRFluids.HELPER.register(modEventBus);
        IRItems.ITEMS.register(modEventBus);
        IRBlocks.BLOCKS.register(modEventBus);
        IRMultiblocks.MULTIBLOCKS.register(modEventBus);
        IRBlockEntityTypes.BLOCK_ENTITIES.register(modEventBus);
        IRMenuTypes.MENUS.register(modEventBus);
        IRTabs.CREATIVE_TABS.register(modEventBus);
        IRRecipes.SERIALIZERS.register(modEventBus);
        IRDataComponents.DATA_COMPONENT_TYPES.register(modEventBus);
        IRPlacerTypes.FOLIAGE_PLACERS.register(modEventBus);
        IRPlacerTypes.TRUNK_PLACERS.register(modEventBus);
        IRAttachmentTypes.ATTACHMENTS.register(modEventBus);
        IRSoundEvents.SOUND_EVENTS.register(modEventBus);
        IRNetworks.NETWORKS.register(modEventBus);
        IREnergyTiers.ENERGY_TIERS.register(modEventBus);
        IRArmorMaterials.ARMOR_MATERIALS.register(modEventBus);
        IRUpgrades.UPGRADES.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, IRConfig.SPEC);
    }

    private void addFeaturePacks(AddPackFindersEvent event) {
        event.addPackFinders(rl("data/indref/datapacks/ir_wip_features"), PackType.SERVER_DATA, Component.literal("Industrial Reforged: WIP Content"), PackSource.FEATURE, false, Pack.Position.TOP);
    }

    private void onDatapackReload(OnDatapackSyncEvent event) {
        Stream<ServerPlayer> relevantPlayers = event.getRelevantPlayers();
        relevantPlayers.forEach(player -> {
            Optional<HolderSet.Named<Item>> named = BuiltInRegistries.ITEM.getTag(IRTags.Items.CLAY_MOLDS);
            named.ifPresent(holders -> {
                List<Item> list = holders.stream().map(Holder::value).toList();
                PacketDistributor.sendToPlayer(player, new SyncCastingMoldsPayload(list));
            });
        });
    }

    private void registerRegistries(NewRegistryEvent event) {
        event.register(IRRegistries.ENERGY_TIER);
        event.register(IRRegistries.UPGRADE);
        event.register(IRRegistries.NETWORK);
    }

    private void registerDataMaps(RegisterDataMapTypesEvent event) {
        event.register(IRDataMaps.CASTING_MOLDS);
        event.register(IRDataMaps.MOLD_INGREDIENTS);
    }

    private void onRegister(RegisterEvent event) {
        Registry<Multiblock> registry = event.getRegistry(PDLRegistries.MULTIBLOCK.key());
        if (registry != null) {
            for (Multiblock multiblock : registry) {
                // Check non-negative integers in definition
                MultiblockDefinition def = multiblock.getDefinition();
                for (Map.Entry<Integer, Pair<Predicate<BlockState>, Block>> entry : def.def().entrySet()) {
                    Preconditions.checkArgument(entry.getKey() >= 0, "The integer keys for multiblock blocks are not allowed to be less than zero. Affected multiblock: "
                            + multiblock + ", affected key: " + entry.getKey() + ", " + entry.getValue());
                }

                // Check that multiblock has controller
                OptionalInt controllerKey = OptionalInt.empty();
                for (Map.Entry<Integer, Pair<Predicate<BlockState>, Block>> entry : def.def().entrySet()) {
                    boolean hasController = entry.getValue().first().test(multiblock.getUnformedController().defaultBlockState());
                    if (hasController) {
                        controllerKey = OptionalInt.of(entry.getKey());
                        break;
                    }
                }

                if (controllerKey.isPresent()) {
                    boolean hasController = false;
                    for (MultiblockLayer layer : multiblock.getLayout()) {
                        for (int i : layer.layer()) {
                            if (i == controllerKey.getAsInt()) {
                                hasController = true;
                                break;
                            }
                        }
                    }

                    if (!hasController) {
                        controllerKey = OptionalInt.empty();
                    }

                    // Check that multiblocks have block entity types
                    if (multiblock.getMultiBlockEntityType() == null) {
                        throw new IllegalStateException("Every multiblocks controller needs to have blockentity that keeps track of data relevant for formed multiblocks. The blockentity type however is null. Affected multiblock: " + multiblock);
                    }
                }

                if (controllerKey.isEmpty()) {
                    throw new IllegalStateException("Every multiblock needs to have at least one controller in it's layout. The controller is the block returned by Multiblock#getUnformedController(). Affected multiblock: "
                            + multiblock);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof IEnergyItem energyItem)
                event.registerItem(IRCapabilities.EnergyStorage.ITEM,
                        (stack, ctx) -> new ItemEnergyWrapper(stack, energyItem.getEnergyTier(), energyItem.getDefaultEnergyCapacity()), item);

            if (item instanceof IHeatItem heatItem)
                event.registerItem(IRCapabilities.HeatStorage.ITEM,
                        (stack, ctx) -> new ItemHeatWrapper(stack, heatItem.getDefaultHeatCapacity()), item);

            if (item instanceof IFluidItem fluidItem)
                event.registerItem(Capabilities.FluidHandler.ITEM,
                        (stack, ctx) -> new FluidHandlerItemStack(IRDataComponents.FLUID, stack, fluidItem.getFluidCapacity()), item);

            if (item instanceof ToolboxItem) {
                event.registerItem(Capabilities.ItemHandler.ITEM,
                        (stack, ctx) -> {
                            List<ItemStack> toolBoxItems = stack.getOrDefault(IRDataComponents.ADVANCED_BUNDLE_CONTENTS, AdvancedBundleContents.EMPTY).items();
                            ItemStack[] items = new ItemStack[toolBoxItems.size()];
                            return new InvWrapper(new SimpleContainer(toolBoxItems.toArray(items)));
                        }, item);
            }
        }

        for (DeferredHolder<BlockEntityType<?>, ? extends BlockEntityType<?>> be : IRBlockEntityTypes.BLOCK_ENTITIES.getEntries()) {
            Block validBlock = be.get().getValidBlocks().stream().iterator().next();
            BlockEntity testBE = be.get().create(BlockPos.ZERO, validBlock.defaultBlockState());
            if (testBE instanceof IRContainerBlockEntity machineBE) {
                if (machineBE.getHeatStorage() != null) {
                    event.registerBlockEntity(IRCapabilities.HeatStorage.BLOCK, (BlockEntityType<IRContainerBlockEntity>) be.get(), IRContainerBlockEntity::getHeatHandlerOnSide);
                }

                if (machineBE.getEuStorage() != null) {
                    event.registerBlockEntity(IRCapabilities.EnergyStorage.BLOCK, (BlockEntityType<IRContainerBlockEntity>) be.get(), IRContainerBlockEntity::getEuHandlerOnSide);
                }
            }
            CapabilityRegistrationHelper.registerBECaps(event, IRBlockEntityTypes.BLOCK_ENTITIES);
        }

        registerMBPartCaps(event);
    }

    private static void registerMBPartCaps(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, IRBlockEntityTypes.BLAST_FURNACE_PART.get(), BlastFurnacePartBlockEntity::exposeItemHandler);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, IRBlockEntityTypes.BLAST_FURNACE_PART.get(), BlastFurnacePartBlockEntity::exposeFluidHandler);

        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, IRBlockEntityTypes.CRUCIBLE_PART.get(), CruciblePartBlockEntity::exposeItemHandler);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, IRBlockEntityTypes.CRUCIBLE_PART.get(), CruciblePartBlockEntity::exposeFluidHandler);
    }

    private void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(IndustrialReforged.MODID);
        registrar.playToServer(ArmorActivityPayload.TYPE, ArmorActivityPayload.STREAM_CODEC, ArmorActivityPayload::handle);
        registrar.playToServer(EmptyCruciblePayload.TYPE, EmptyCruciblePayload.STREAM_CODEC, EmptyCruciblePayload::handle);
        registrar.playToServer(RedstoneSignalTypeSyncPayload.TYPE, RedstoneSignalTypeSyncPayload.STREAM_CODEC, RedstoneSignalTypeSyncPayload::handle);
        registrar.playToServer(UpgradeWidgetOpenClosePayload.TYPE, UpgradeWidgetOpenClosePayload.STREAM_CODEC, UpgradeWidgetOpenClosePayload::handle);

        registrar.playToClient(CrucibleControllerPayload.TYPE, CrucibleControllerPayload.STREAM_CODEC, CrucibleControllerPayload::handle);
        registrar.playToClient(CrucibleMeltingProgressPayload.TYPE, CrucibleMeltingProgressPayload.STREAM_CODEC, CrucibleMeltingProgressPayload::handle);
        registrar.playToClient(CrucibleTurnPayload.TYPE, CrucibleTurnPayload.STREAM_CODEC, CrucibleTurnPayload::handle);
        registrar.playToClient(PowerBlockEntityPayload.TYPE, PowerBlockEntityPayload.STREAM_CODEC, PowerBlockEntityPayload::handle);
        registrar.playToClient(BasinFluidChangedPayload.TYPE, BasinFluidChangedPayload.STREAM_CODEC, BasinFluidChangedPayload::handle);
        registrar.playToClient(FaucetSetRenderStack.TYPE, FaucetSetRenderStack.STREAM_CODEC, FaucetSetRenderStack::handle);

        // Transport network
        for (TransportNetwork<?> network : IRRegistries.NETWORK) {
            registrar.playToClient(AddNetworkNodePayload.type(network), AddNetworkNodePayload.streamCodec(network), AddNetworkNodePayload::handle);
            registrar.playToClient(RemoveNetworkNodePayload.type(network), RemoveNetworkNodePayload.streamCodec(network), RemoveNetworkNodePayload::handle);
            registrar.playToClient(SyncNetworkNodePayload.type(network), SyncNetworkNodePayload.streamCodec(network), SyncNetworkNodePayload::handle);
        }

        registrar.playToClient(AddNextNodePayload.TYPE, AddNextNodePayload.STREAM_CODEC, AddNextNodePayload::handle);
        registrar.playToClient(RemoveNextNodePayload.TYPE, RemoveNextNodePayload.STREAM_CODEC, RemoveNextNodePayload::handle);
        registrar.playToClient(SyncNextNodePayload.TYPE, SyncNextNodePayload.STREAM_CODEC, SyncNextNodePayload::handle);

        registrar.playToClient(AddInteractorPayload.TYPE, AddInteractorPayload.STREAM_CODEC, AddInteractorPayload::handle);
        registrar.playToClient(RemoveInteractorPayload.TYPE, RemoveInteractorPayload.STREAM_CODEC, RemoveInteractorPayload::handle);
        registrar.playToClient(SyncInteractorPayload.TYPE, SyncInteractorPayload.STREAM_CODEC, SyncInteractorPayload::handle);

        // Casting molds
        registrar.playToClient(SyncCastingMoldsPayload.TYPE, SyncCastingMoldsPayload.STREAM_CODEC, SyncCastingMoldsPayload::handle);
        registrar.playToServer(SetCastingMoldPayload.TYPE, SetCastingMoldPayload.STREAM_CODEC, SetCastingMoldPayload::handle);
    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
