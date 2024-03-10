package com.indref.industrial_reforged.events;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.networking.data.ArmorActivitySyncData;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.registries.items.armor.JetpackItem;
import com.indref.industrial_reforged.util.InputUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import static com.indref.industrial_reforged.events.IREvents.ClientBus.JETPACK_ASCEND;
import static com.indref.industrial_reforged.events.IREvents.ClientBus.JETPACK_TOGGLE;

public class NeoforgeEvents {

    @Mod.EventBusSubscriber(modid = IndustrialReforged.MODID)
    public static class Common {
        @SubscribeEvent
        public static void onLivingFall(LivingFallEvent event) {
            if (event.getEntity().getItemBySlot(EquipmentSlot.FEET).is(IRItems.HAZMAT_BOOTS.get())) {
                event.setDistance(0);
            }
        }
    }


    @Mod.EventBusSubscriber(modid = IndustrialReforged.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class CommonBus {
        @SubscribeEvent
        public static void playerTick(TickEvent.PlayerTickEvent event) {
            NonNullList<ItemStack> items = event.player.getInventory().items;
            for (ItemStack item : items) {
                CompoundTag tag = item.getTag();
                if (tag != null && tag.getBoolean("cruciblemelting"))
                    tag.putInt("barwidth", -5);
            }
        }
    }

    @Mod.EventBusSubscriber(modid = IndustrialReforged.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class Client {
        private static boolean up = false;

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            Player player = Minecraft.getInstance().player;
            if (event.phase == TickEvent.Phase.END) { // Only call code once as the tick event is called twice every tick
                while (JETPACK_TOGGLE.get().consumeClick()) {
                    ItemStack jetpackItem = Minecraft.getInstance().player.getItemBySlot(EquipmentSlot.CHEST);
                    if (jetpackItem.getItem() instanceof JetpackItem) {
                        JetpackItem.toggle(jetpackItem);
                        PacketDistributor.SERVER.noArg().send(new ArmorActivitySyncData(EquipmentSlot.CHEST,
                                player.getItemBySlot(EquipmentSlot.CHEST).getOrCreateTag().getBoolean("active")));
                    }
                }
            }

            if (event.phase == TickEvent.Phase.START) {
                if (JETPACK_ASCEND.get().isDown()) {
                    InputUtils.update(player, true, false, false, false, false, false, false);
                }
            }
        }
    }
}
