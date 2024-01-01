package com.indref.industrial_reforged.events;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.registries.IRItems;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;

import java.util.HashSet;
import java.util.Set;

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
        if (expectArmorItem(EquipmentSlot.FEET, IRItems.HAZMAT_BOOTS.get(), event.getEntity())) {
            if (event.getSource().is(DamageTypeTags.IS_FIRE)) {
                event.setAmount(0);
                event.getEntity().clearFire();
                event.setCanceled(true);
            }
        }
    }

    private static boolean expectArmorItem(EquipmentSlot slot, Item item, Entity entity) {
        if (entity instanceof Player player) {
            return player.getItemBySlot(slot).is(item);
        }
        return false;
    }

    private static final Set<String> STING_DAMAGE_TYPES = new HashSet<>();

    public static void setup() {
        STING_DAMAGE_TYPES.add("sting");
        STING_DAMAGE_TYPES.add("cactus");
        STING_DAMAGE_TYPES.add("sweetBerryBush");
    }
}
