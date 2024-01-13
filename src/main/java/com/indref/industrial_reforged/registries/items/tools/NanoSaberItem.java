package com.indref.industrial_reforged.registries.items.tools;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.indref.industrial_reforged.api.items.BaseElectricSwordItem;
import com.indref.industrial_reforged.api.items.container.IEnergyItem;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.tiers.EnergyTiers;
import com.indref.industrial_reforged.util.ItemUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;

public class NanoSaberItem extends BaseElectricSwordItem implements IEnergyItem {
    public NanoSaberItem(Properties p_43272_) {
        super(Tiers.DIAMOND, -1, -3F, p_43272_);
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

                    if (tag.getBoolean("active")) {
                        modifiers.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Nano modifier", 19, AttributeModifier.Operation.ADDITION));
                    } else {
                        modifiers.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", getDamage() + getTier().getAttackDamageBonus(), AttributeModifier.Operation.ADDITION));
                    }

                    return modifiers;
                }
            }
        }
        return super.getAttributeModifiers(slot, stack);
    }

    @Override
    public EnergyTier getEnergyTier() {
        return EnergyTiers.HIGH;
    }
}
