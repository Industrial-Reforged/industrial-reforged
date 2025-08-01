package com.indref.industrial_reforged.content.items.tools;

import com.indref.industrial_reforged.IRConfig;
import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyHandler;
import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.api.items.tools.electric.ElectricSwordItem;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.translations.IRTranslations;
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
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class NanoSaberItem extends ElectricSwordItem {
    public NanoSaberItem(Properties properties, Tier tier, int baseAttackDamage, float baseAttackSpeed, Supplier<EnergyTier> energyTier, IntSupplier energyUsage, IntSupplier energyCapacity) {
        super(properties.component(IRDataComponents.ACTIVE, false), tier, baseAttackDamage, baseAttackSpeed, energyTier, energyUsage, energyCapacity);
    }

    public @NotNull ItemAttributeModifiers createAttributes(ItemStack stack) {
        if (stack.is(this)) {
            if (stack.has(IRDataComponents.ACTIVE) && stack.has(IRDataComponents.ENERGY)) {
                ItemAttributeModifiers.Builder modifiers = ItemAttributeModifiers.builder();

                if (stack.getOrDefault(IRDataComponents.ACTIVE, false)) {
                    modifiers.add(Attributes.ATTACK_DAMAGE, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "attack_modifier"), IRConfig.nanoSaberAttackDamage, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
                    modifiers.add(Attributes.ATTACK_SPEED, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "attack_speed"), IRConfig.nanoSaberAttackSpeed, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
                }

                return modifiers.build();
            }
        }
        return ItemAttributeModifiers.EMPTY;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) {
        ItemStack stack = player.getItemInHand(interactionHand);
        IEnergyHandler energyStorage = getEnergyCap(stack);
        if (player.isShiftKeyDown() && energyStorage.getEnergyStored() > 0) {
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

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean b) {
        IEnergyHandler energyStorage = getEnergyCap(stack);
        if (stack.getOrDefault(IRDataComponents.ACTIVE, false)) {
            if (level.getGameTime() % 20 == 0) {
                int drained = energyStorage.drainEnergy(getEnergyUsage(stack, entity), false);
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

    @Override
    public EnergyTier getEnergyTier() {
        return energyTier.get();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag p41424) {
        if (stack.getOrDefault(IRDataComponents.ACTIVE, false)) {
            tooltip.add(IRTranslations.Tooltip.ACTIVE.component().withStyle(ChatFormatting.GREEN));
        } else {
            tooltip.add(IRTranslations.Tooltip.INACTIVE.component().withStyle(ChatFormatting.RED));
        }
        super.appendHoverText(stack, ctx, tooltip, p41424);
    }
}
