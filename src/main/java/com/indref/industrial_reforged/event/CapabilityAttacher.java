package com.indref.industrial_reforged.event;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.capabilities.energy.EnergyStorageCapabilityAttacher;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = IndustrialReforged.MODID, value = Dist.CLIENT)
public class CapabilityAttacher {

    @SubscribeEvent
    public static void onAttachingCapabilities(final AttachCapabilitiesEvent<BlockEntity> event) {
        IndustrialReforged.LOGGER.info("Attaching Capabilities");
        EnergyStorageCapabilityAttacher.attach(event);
    }

}
