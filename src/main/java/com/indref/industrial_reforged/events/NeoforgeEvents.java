package com.indref.industrial_reforged.events;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.registries.IRItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;

@Mod.EventBusSubscriber(modid = IndustrialReforged.MODID)
public class NeoforgeEvents {
    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        if (event.getEntity().getItemBySlot(EquipmentSlot.FEET).is(IRItems.HAZMAT_BOOTS.get())) {
            event.setDistance(0);
        }
    }
}
