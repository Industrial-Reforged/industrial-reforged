package com.indref.industrial_reforged.event;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.EnergyStorageProvider;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorageExposed;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = IndustrialReforged.MODID, value = Dist.CLIENT)
public class CapabilityAttacher {
    @SubscribeEvent
    public static void onCapabilityAttach(final AttachCapabilitiesEvent<BlockEntity> event) {
        if (event.getObject() instanceof IEnergyStorageExposed) {
            if (!event.getObject().getCapability(IRCapabilities.ENERGY).isPresent()) {
                event.addCapability(EnergyStorageProvider.IDENTIFIER, new EnergyStorageProvider());
            }
        }
        IndustrialReforged.LOGGER.info("Attaching capabilities");
    }
}
