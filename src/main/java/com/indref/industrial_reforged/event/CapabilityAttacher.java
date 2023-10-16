package com.indref.industrial_reforged.event;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.IHeatBlock;
import com.indref.industrial_reforged.api.capabilities.energy.EnergyStorageProvider;
import com.indref.industrial_reforged.api.blocks.IEnergyBlock;
import com.indref.industrial_reforged.api.capabilities.heat.HeatStorageProvider;
import com.indref.industrial_reforged.api.items.container.IEnergyItem;
import com.indref.industrial_reforged.api.items.container.IHeatItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = IndustrialReforged.MODID)
public class CapabilityAttacher {
    @SubscribeEvent
    public static void onCapabilityAttachBlockEntity(AttachCapabilitiesEvent<BlockEntity> event) {
        if (event.getObject() instanceof IEnergyBlock iEnergyBlock) {
            event.addCapability(EnergyStorageProvider.IDENTIFIER, iEnergyBlock.getEnergyStorage());
            IndustrialReforged.LOGGER.info("Attaching energy capability to block entity");
        }

        if (event.getObject() instanceof IHeatBlock heatBlock) {
            event.addCapability(HeatStorageProvider.IDENTIFIER, heatBlock.getHeatStorage());
            IndustrialReforged.LOGGER.info("Attaching heat capability to block entity");
        }
    }

    @SubscribeEvent
    public static void onCapabilityAttachItemStack(AttachCapabilitiesEvent<ItemStack> event) {
        if (event.getObject().getItem() instanceof IEnergyItem) {
            event.addCapability(EnergyStorageProvider.IDENTIFIER, new EnergyStorageProvider(event.getObject()));
        }

        if (event.getObject().getItem() instanceof IHeatItem) {
            event.addCapability(HeatStorageProvider.IDENTIFIER, new HeatStorageProvider(event.getObject()));
        }
    }
}
