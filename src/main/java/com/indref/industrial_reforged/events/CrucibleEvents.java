package com.indref.industrial_reforged.events;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.client.renderer.item.bar.CrucibleProgressRenderer;
import com.indref.industrial_reforged.translations.IRTranslations;
import com.indref.industrial_reforged.util.items.ItemUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = IndustrialReforged.MODID, bus = EventBusSubscriber.Bus.GAME)
public final class CrucibleEvents {
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

    @SubscribeEvent
    public static void appendTooltips(ItemTooltipEvent event) {
        ItemStack item = event.getItemStack();
        CompoundTag tag = ItemUtils.getImmutableTag(item).copyTag();
        int meltingType = tag.getInt(CrucibleProgressRenderer.IS_MELTING_KEY);
        if (meltingType == 1) {
            float roundedBarWidth = Math.round(tag.getFloat(CrucibleProgressRenderer.BARWIDTH_KEY) * 10f) / 10f;
            event.getToolTip().add(IRTranslations.Tooltip.MELTING_PROGRESS
                    .component(roundedBarWidth, 10.0f)
                    .withStyle(ChatFormatting.GRAY));
        } else if (meltingType == 2) {
            event.getToolTip().add(IRTranslations.Tooltip.MELTING_NOT_POSSIBLE.component().withStyle(ChatFormatting.GRAY));
        }
    }

}

