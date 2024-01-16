package com.indref.industrial_reforged.registries.items.tools;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.indref.industrial_reforged.api.items.BaseElectricSwordItem;
import com.indref.industrial_reforged.api.items.container.IEnergyItem;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NanoSaberItem extends BaseElectricSwordItem implements IEnergyItem {
    final EnergyTier energyTier;

    @Deprecated
    // deprecated cuz need to remove energy tier parameter and hardcode it
    public NanoSaberItem(Properties p_43272_, EnergyTier energyTier) {
        super(Tiers.DIAMOND, -1, -3F, p_43272_);
        this.energyTier = energyTier;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (stack.getItem().equals(this)) {
            if (slot == EquipmentSlot.MAINHAND) {
                CompoundTag tag = stack.getTag();
                if (tag != null) {
                    Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();
                    modifiers.putAll(super.getAttributeModifiers(slot, stack));

                    modifiers.removeAll(Attributes.ATTACK_DAMAGE);
                    modifiers.removeAll(Attributes.ATTACK_SPEED);

                    if (tag.getBoolean("active")) {
                        modifiers.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Nano modifier", 19, AttributeModifier.Operation.ADDITION));
                        modifiers.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Speed modifier", -2.8F, AttributeModifier.Operation.ADDITION));
                    }

                    return modifiers;
                }
            }
        }
        return super.getAttributeModifiers(slot, stack);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) {
        ItemStack stack = player.getItemInHand(interactionHand);
        CompoundTag tag = stack.getOrCreateTag();
        if (player.isShiftKeyDown() && getEnergyStored(stack) > 0) {
            if (stack.hasTag()) {
                tag.putBoolean("active", !tag.getBoolean("active"));
            } else {
                tag.putBoolean("active", true);
            }
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.fail(stack);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean b) {
        if (stack.hasTag() && stack.getOrCreateTag().getBoolean("active")) {
            if (level.getGameTime() % 20 == 0) {
                setEnergyStored(stack, getEnergyStored(stack) - 1);
            }
        }
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        if (item.hasTag()) {
            item.getOrCreateTag().putBoolean("active", false);
        }
        return super.onDroppedByPlayer(item, player);
    }

    public static float isActive(ItemStack stack) {
        if (stack.hasTag()) {
            CompoundTag tag = stack.getOrCreateTag();
            return tag.getBoolean("active") ? 1 : 0;
        }
        return 0;
    }

    @Override
    public EnergyTier getEnergyTier() {
        return energyTier;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level p41422, List<Component> tooltip, TooltipFlag p41424) {
        if (stack.hasTag() && stack.getOrCreateTag().getBoolean("active")) {
            tooltip.add(Component.translatable("nano_saber.desc.active").withStyle(ChatFormatting.GREEN));
        } else {
            tooltip.add(Component.translatable("nano_saber.desc.inactive").withStyle(ChatFormatting.RED));
        }
        super.appendHoverText(stack, p41422, tooltip, p41424);
    }
}
