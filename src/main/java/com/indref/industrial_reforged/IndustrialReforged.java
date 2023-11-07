package com.indref.industrial_reforged;

import com.indref.industrial_reforged.content.IRBlockEntityTypes;
import com.indref.industrial_reforged.content.IRBlocks;
import com.indref.industrial_reforged.content.IRCreativeTab;
import com.indref.industrial_reforged.content.IRItems;
import com.indref.industrial_reforged.events.IREvents;
import com.indref.industrial_reforged.networking.IRPackets;
import com.indref.industrial_reforged.screen.IRMenuTypes;
import com.indref.industrial_reforged.worldgen.IRPlacerTypes;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(IndustrialReforged.MODID)
public class IndustrialReforged {
    public static final String MODID = "indref";
    public static final Logger LOGGER = LogUtils.getLogger();

    public IndustrialReforged() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        IRItems.ITEMS.register(modEventBus);

        IRBlocks.BLOCKS.register(modEventBus);

        IRBlockEntityTypes.BLOCK_ENTITIES.register(modEventBus);

        IRMenuTypes.MENUS.register(modEventBus);

        IRCreativeTab.CREATIVE_TABS.register(modEventBus);

        IRPlacerTypes.FOLIAGE_PLACERS.register(modEventBus);
        IRPlacerTypes.TRUNK_PLACERS.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(IREvents::setup);
        event.enqueueWork(IRPackets::register);
        LOGGER.info("HELLO FROM COMMON SETUP");
        test("");
    }

    void test(String sus) {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
