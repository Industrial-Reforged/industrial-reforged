package com.indref.industrial_reforged.registries.items.tools;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.registries.IRDataComponents;
import com.indref.industrial_reforged.api.items.electric.ElectricSwordItem;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.tiers.EnergyTiers;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
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
    public NanoSaberItem(Properties properties, EnergyTier energyTier) {
        super(energyTier, Tiers.DIAMOND, -1, -3F, properties.component(IRDataComponents.ACTIVE, false));
    }

    public @NotNull ItemAttributeModifiers createAttributes(ItemStack stack) {
        IndustrialReforged.LOGGER.debug("Get attr mods");
        if (stack.is(this)) {
            if (stack.has(IRDataComponents.ACTIVE) && stack.has(IRDataComponents.ENERGY)) {
                ItemAttributeModifiers.Builder modifiers = ItemAttributeModifiers.builder();

                if (stack.getOrDefault(IRDataComponents.ACTIVE, false)) {
                    modifiers.add(Attributes.ATTACK_DAMAGE, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "attack_modifier"), 19, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
                    modifiers.add(Attributes.ATTACK_SPEED, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "attack_speed"), 1.2F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
                }

                return modifiers.build();
            }
        }
        return ItemAttributeModifiers.EMPTY;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) {
        ItemStack stack = player.getItemInHand(interactionHand);
        if (player.isShiftKeyDown() && getEnergyStored(stack) > 0) {
            stack.set(IRDataComponents.ACTIVE, !stack.getOrDefault(IRDataComponents.ACTIVE, false));
            stack.set(DataComponents.ATTRIBUTE_MODIFIERS, createAttributes(stack));
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.fail(stack);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    public int getUsageAmount(ItemStack itemStack) {
        return 25;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean b) {
        if (stack.getOrDefault(IRDataComponents.ACTIVE, false)) {
            if (level.getGameTime() % 20 == 0) {
                int drained = tryDrainEnergy(stack, getUsageAmount(stack));
                if (drained == 0) {
                    stack.set(IRDataComponents.ACTIVE, false);
                }
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
