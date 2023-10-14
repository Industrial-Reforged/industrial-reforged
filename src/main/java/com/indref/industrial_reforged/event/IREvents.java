package com.indref.industrial_reforged.event;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.content.IRItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = IndustrialReforged.MODID)
public class IREvents {
    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        if (event.getEntity().getItemBySlot(EquipmentSlot.FEET).is(IRItems.HAZMAT_BOOTS.get())) {
            event.setDistance(0);
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
    }
}
