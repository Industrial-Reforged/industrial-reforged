package com.indref.industrial_reforged.registries.items.tools;

import com.indref.industrial_reforged.api.data.IRDataComponents;
import com.indref.industrial_reforged.api.items.electric.ElectricSwordItem;
import com.indref.industrial_reforged.api.items.container.IEnergyItem;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.tiers.EnergyTiers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NanoSaberItem extends ElectricSwordItem {
    public NanoSaberItem(Properties p_43272_, EnergyTier energyTier) {
        super(energyTier, Tiers.DIAMOND, -1, -3F, p_43272_);
    }

    @Override
    public @NotNull ItemAttributeModifiers getAttributeModifiers(ItemStack stack) {
        EquipmentSlot slot = stack.getEquipmentSlot();
        ItemAttributeModifiers.Builder modifiers = ItemAttributeModifiers.builder();
        if (stack.is(this)) {
            if (slot == EquipmentSlot.MAINHAND) {
                if (stack.getOrDefault(IRDataComponents.ACTIVE, false)) {
                    modifiers.add(
                            Attributes.ATTACK_DAMAGE,
                            new AttributeModifier(
                                    BASE_ATTACK_DAMAGE_UUID,
                                    "Nano Saber damage modifier",
                                    19,
                                    AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.MAINHAND
                    );
                    modifiers.add(
                            Attributes.ATTACK_SPEED,
                            new AttributeModifier(
                                    BASE_ATTACK_SPEED_UUID,
                                    "Nano Saber speed modifier",
                                    -2.8F,
                                    AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.MAINHAND
                    );

                    return modifiers.build();
                }
            }
        }
        return super.getAttributeModifiers(stack);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) {
        ItemStack stack = player.getItemInHand(interactionHand);
        if (player.isShiftKeyDown() && getEnergyStored(stack) > 0) {
            stack.set(IRDataComponents.ACTIVE, !stack.getOrDefault(IRDataComponents.ACTIVE, false));
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
        if (stack.getOrDefault(IRDataComponents.ACTIVE, false)) {
            if (level.getGameTime() % 20 == 0) {
                setEnergyStored(stack, getEnergyStored(stack) - 1);
            }
        }
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        item.set(IRDataComponents.ACTIVE, false);
        return super.onDroppedByPlayer(item, player);
    }

    public static float isActive(ItemStack stack) {
        return stack.getOrDefault(IRDataComponents.ACTIVE, false) ? 1 : 0;
    }

    @Override
    public EnergyTier getEnergyTier() {
        return EnergyTiers.HIGH;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag p41424) {
        if (stack.getOrDefault(IRDataComponents.ACTIVE, false)) {
            tooltip.add(Component.translatable("nano_saber.desc.active").withStyle(ChatFormatting.GREEN));
        } else {
            tooltip.add(Component.translatable("nano_saber.desc.inactive").withStyle(ChatFormatting.RED));
        }
        super.appendHoverText(stack, ctx, tooltip, p41424);
    }
}
