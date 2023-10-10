package com.indref.industrial_reforged.event;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.EnergyStorageProvider;
import com.indref.industrial_reforged.api.energy.blocks.IEnergyBlock;
import com.indref.industrial_reforged.api.energy.items.IEnergyItem;
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
            IndustrialReforged.LOGGER.info("Attaching capabilities to block entity");
        }
    }

    @SubscribeEvent
    public static void onCapabilityAttachItemStack(AttachCapabilitiesEvent<ItemStack> event) {
        if (event.getObject().getItem() instanceof IEnergyItem energyItem) {
            event.addCapability(EnergyStorageProvider.IDENTIFIER, new EnergyStorageProvider(event.getObject()));
            IndustrialReforged.LOGGER.info("Attaching capabilities to itemstack");
        }
    }
}
