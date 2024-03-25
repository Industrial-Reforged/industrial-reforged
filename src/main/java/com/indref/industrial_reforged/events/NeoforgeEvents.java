package com.indref.industrial_reforged.events;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.client.renderer.items.CrucibleProgressRenderer;
import com.indref.industrial_reforged.networking.NetworkingHelper;
import com.indref.industrial_reforged.networking.data.ArmorActivitySyncData;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.registries.items.armor.JetpackItem;
import com.indref.industrial_reforged.util.InputUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;

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
            if (event.player.level().isClientSide()) return;

            Player player = event.player;
            Level level = player.level();

            NonNullList<ItemStack> items = player.getInventory().items;
            for (ItemStack item : items) {
                CompoundTag tag = item.getTag();
                if (tag != null && tag.getBoolean(CrucibleProgressRenderer.IS_MELTING_KEY)) {
                    if (level.getGameTime() % 20 == 0) {
                        tag.putFloat(CrucibleProgressRenderer.BARWIDTH_KEY, tag.getFloat(CrucibleProgressRenderer.BARWIDTH_KEY) - 1);
                        Registry<DamageType> damageTypes = player.damageSources().damageTypes;
                        player.hurt(new DamageSource(damageTypes.getHolderOrThrow(DamageTypes.IN_FIRE)), 4);
                    }

                    if (tag.getFloat(CrucibleProgressRenderer.BARWIDTH_KEY) <= 0)
                        tag.putBoolean(CrucibleProgressRenderer.IS_MELTING_KEY, false);
                }
            }
        }
    }

    @Mod.EventBusSubscriber(modid = IndustrialReforged.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class Client {

        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            Player player = Minecraft.getInstance().player;
            if (event.phase == TickEvent.Phase.END) { // Only call code once as the tick event is called twice every tick
                while (JETPACK_TOGGLE.get().consumeClick()) {
                    ItemStack jetpackItem = Minecraft.getInstance().player.getItemBySlot(EquipmentSlot.CHEST);
                    if (jetpackItem.getItem() instanceof JetpackItem) {
                        JetpackItem.toggle(jetpackItem);
                        NetworkingHelper.sendToServer(new ArmorActivitySyncData(EquipmentSlot.CHEST,
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
