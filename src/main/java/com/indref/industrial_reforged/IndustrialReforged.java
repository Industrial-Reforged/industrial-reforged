package com.indref.industrial_reforged;

import com.google.common.base.Preconditions;
import com.indref.industrial_reforged.api.blockentities.container.ContainerBlockEntity;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.ItemEnergyWrapper;
import com.indref.industrial_reforged.api.capabilities.heat.ItemHeatWrapper;
import com.indref.industrial_reforged.api.items.bundles.AdvancedBundleContents;
import com.indref.industrial_reforged.api.items.container.IEnergyItem;
import com.indref.industrial_reforged.api.items.container.IFluidItem;
import com.indref.industrial_reforged.api.items.container.IHeatItem;
import com.indref.industrial_reforged.content.blockentities.multiblocks.part.BlastFurnacePartBlockEntity;
import com.indref.industrial_reforged.content.items.storage.ToolboxItem;
import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.data.IRDataMaps;
import com.indref.industrial_reforged.networking.ArmorActivityPayload;
import com.indref.industrial_reforged.networking.CrucibleControllerPayload;
import com.indref.industrial_reforged.networking.CrucibleMeltingProgressPayload;
import com.indref.industrial_reforged.networking.CrucibleTurnPayload;
import com.indref.industrial_reforged.registries.*;
import com.indref.industrial_reforged.util.Utils;
import com.mojang.logging.LogUtils;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.Multiblock;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.MultiblockDefinition;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.MultiblockLayer;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Mod(IndustrialReforged.MODID)
public final class IndustrialReforged {
    public static final String MODID = "indref";
    public static final Logger LOGGER = LogUtils.getLogger();

    public IndustrialReforged(IEventBus modEventBus) {
        modEventBus.addListener(this::registerRegistries);
        modEventBus.addListener(this::registerDataMaps);
        modEventBus.addListener(this::onRegister);
        modEventBus.addListener(this::registerCapabilities);
        modEventBus.addListener(this::registerPayloads);

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
    }

    private void registerRegistries(NewRegistryEvent event) {
        event.register(IRRegistries.MULTIBLOCK);
        event.register(IRRegistries.ENERGY_TIER);
    }

    private void registerDataMaps(RegisterDataMapTypesEvent event) {
        event.register(IRDataMaps.CASTING_MOLDS);
        event.register(IRDataMaps.MOLD_INGREDIENTS);
    }

    private void onRegister(RegisterEvent event) {
        Registry<Multiblock> registry = event.getRegistry(IRRegistries.MULTIBLOCK.key());
        if (registry != null) {
            for (Multiblock multiblock : registry) {
                // Check non-negative integers in definition
                MultiblockDefinition def = multiblock.getDefinition();
                for (Map.Entry<Integer, Pair<Predicate<BlockState>, Block>> entry : def.def().entrySet()) {
                    Preconditions.checkArgument(entry.getKey() >= 0, "The integer keys for multiblock blocks are not allowed to be less than zero. Affected multiblock: "
                            + multiblock + ", affected key: " + entry.getKey() + ", " + entry.getValue());
                }

                // Check that multiblock has controller
                Map<Pair<Predicate<BlockState>, Block>, Integer> revDef = Utils.reverseMap(def.def());
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
                    throw new IllegalStateException("Every multiblocks controller needs to have blockentity that keeps track of data relevant for formed multiblocks. The blockentity type however is null. Affected multiblock: " + multiblock);
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
                        (stack, ctx) -> new FluidHandlerItemStack(IRDataComponents.FLUID, stack, fluidItem.getDefaultFluidCapacity()), item);

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
            if (testBE instanceof ContainerBlockEntity containerBE) {
                if (containerBE.getEnergyStorage() != null) {
                    event.registerBlockEntity(IRCapabilities.EnergyStorage.BLOCK, (BlockEntityType<ContainerBlockEntity>) be.get(), ContainerBlockEntity::getEnergyHandlerOnSide);
                }

                if (containerBE.getHeatStorage() != null) {
                    event.registerBlockEntity(IRCapabilities.HeatStorage.BLOCK, (BlockEntityType<ContainerBlockEntity>) be.get(), ContainerBlockEntity::getHeatHandlerOnSide);
                }

                if (containerBE.getItemHandler() != null) {
                    event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, (BlockEntityType<ContainerBlockEntity>) be.get(), ContainerBlockEntity::getItemHandlerOnSide);
                }

                if (containerBE.getFluidHandler() != null) {
                    event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, (BlockEntityType<ContainerBlockEntity>) be.get(), ContainerBlockEntity::getFluidHandlerOnSide);
                }
            }
        }

        registerMBPartCaps(event);
    }

    private static void registerMBPartCaps(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, IRBlockEntityTypes.BLAST_FURNACE_PART.get(), BlastFurnacePartBlockEntity::exposeItemHandler);
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, IRBlockEntityTypes.BLAST_FURNACE_PART.get(), BlastFurnacePartBlockEntity::exposeFluidHandler);
    }

    private void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(IndustrialReforged.MODID);
        registrar.playToServer(ArmorActivityPayload.TYPE, ArmorActivityPayload.STREAM_CODEC, ArmorActivityPayload::handle);

        registrar.playToClient(CrucibleControllerPayload.TYPE, CrucibleControllerPayload.STREAM_CODEC, CrucibleControllerPayload::handle);
        registrar.playToClient(CrucibleMeltingProgressPayload.TYPE, CrucibleMeltingProgressPayload.STREAM_CODEC, CrucibleMeltingProgressPayload::handle);
        registrar.playToClient(CrucibleTurnPayload.TYPE, CrucibleTurnPayload.STREAM_CODEC, CrucibleTurnPayload::handle);
    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
