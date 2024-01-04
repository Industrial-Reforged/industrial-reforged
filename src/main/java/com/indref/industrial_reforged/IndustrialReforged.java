package com.indref.industrial_reforged;

import com.indref.industrial_reforged.api.data.IRAttachmentTypes;
import com.indref.industrial_reforged.registries.IRFluidTypes;
import com.indref.industrial_reforged.registries.IRFluids;
import com.indref.industrial_reforged.events.IREvents;
import com.indref.industrial_reforged.networking.IRPackets;
import com.indref.industrial_reforged.registries.*;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import org.slf4j.Logger;

@Mod(IndustrialReforged.MODID)
public class IndustrialReforged {
    public static final String MODID = "indref";
    public static final Logger LOGGER = LogUtils.getLogger();

    public IndustrialReforged() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(NewRegistryEvent.class, event -> event.register(IRRegistries.MULTIBLOCK));

        IRItems.ITEMS.register(modEventBus);

        IRBlocks.BLOCKS.register(modEventBus);

        IRMultiblocks.MULTIBLOCKS.register(modEventBus);

        IRBlockEntityTypes.BLOCK_ENTITIES.register(modEventBus);

        IRMenuTypes.MENUS.register(modEventBus);

        IRTabs.CREATIVE_TABS.register(modEventBus);

        IRPlacerTypes.FOLIAGE_PLACERS.register(modEventBus);
        IRPlacerTypes.TRUNK_PLACERS.register(modEventBus);

        IRFluids.FLUIDS.register(modEventBus);
        IRFluidTypes.FLUID_TYPES.register(modEventBus);

        IRAttachmentTypes.ATTACHMENT_TYPES.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(IRPackets::register);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }
}
