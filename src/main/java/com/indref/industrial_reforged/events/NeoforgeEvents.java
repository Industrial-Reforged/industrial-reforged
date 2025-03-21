package com.indref.industrial_reforged.events;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.client.renderer.item.bar.CrucibleProgressRenderer;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.util.items.ItemUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public final class NeoforgeEvents {
    @EventBusSubscriber(modid = IndustrialReforged.MODID)
    public static final class Common {
        @SubscribeEvent
        public static void onLivingFall(LivingFallEvent event) {
            if (event.getEntity().getItemBySlot(EquipmentSlot.FEET).is(IRItems.HAZMAT_BOOTS.get())) {
                event.setDistance(0);
            }
        }
    }
}
