package com.indref.industrial_reforged.events;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.events.ScannerEvent;
import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.client.renderer.item.bar.CrucibleProgressRenderer;
import com.indref.industrial_reforged.networking.ArmorActivityPayload;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.content.items.armor.JetpackItem;
import com.indref.industrial_reforged.util.ItemUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

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

    @EventBusSubscriber(modid = IndustrialReforged.MODID, bus = EventBusSubscriber.Bus.GAME)
    public static final class CommonBus {
        @SubscribeEvent
        public static void playerTick(PlayerTickEvent.Pre event) {
            if (event.getEntity().level().isClientSide()) return;

            Player player = event.getEntity();
            Level level = player.level();

            NonNullList<ItemStack> items = player.getInventory().items;
            for (ItemStack item : items) {
                CompoundTag tag = ItemUtils.getImmutableTag(item).copyTag();
                int meltingType = tag.getInt(CrucibleProgressRenderer.IS_MELTING_KEY);
                if (meltingType == 1) {
                    if (level.getGameTime() % 20 == 0) {
                        tag.putFloat(CrucibleProgressRenderer.BARWIDTH_KEY, tag.getFloat(CrucibleProgressRenderer.BARWIDTH_KEY) - 1);
                        item.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                        Registry<DamageType> damageTypes = player.damageSources().damageTypes;
                        player.hurt(new DamageSource(damageTypes.getHolderOrThrow(DamageTypes.IN_FIRE)), 4);
                    }

                    if (tag.getFloat(CrucibleProgressRenderer.BARWIDTH_KEY) <= 0) {
                        item.remove(DataComponents.CUSTOM_DATA);
                    }
                } else if (meltingType == 2) {
                    item.remove(DataComponents.CUSTOM_DATA);
                    Registry<DamageType> damageTypes = player.damageSources().damageTypes;
                    player.hurt(new DamageSource(damageTypes.getHolderOrThrow(DamageTypes.IN_FIRE)), 3);
                }
            }
        }

    }
}
