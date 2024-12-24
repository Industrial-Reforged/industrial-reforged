package com.indref.industrial_reforged;

import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.data.IRDataMaps;
import com.indref.industrial_reforged.registries.*;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import org.slf4j.Logger;

@Mod(IndustrialReforged.MODID)
public final class IndustrialReforged {
    public static final String MODID = "indref";
    public static final Logger LOGGER = LogUtils.getLogger();

    public IndustrialReforged(IEventBus modEventBus) {
        modEventBus.addListener(this::registerRegistries);
        modEventBus.addListener(this::registerDataMaps);

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
    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
