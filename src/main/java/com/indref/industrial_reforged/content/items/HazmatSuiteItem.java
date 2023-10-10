package com.indref.industrial_reforged.content.items;

import net.minecraft.network.PacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HazmatSuiteItem extends ArmorItem {
    public HazmatSuiteItem(ArmorMaterial armorMaterial, Type type, Properties properties) {
        super(armorMaterial, type, properties);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        if (this.getEquipmentSlot() == EquipmentSlot.FEET) {
            if (entity instanceof Player player) {
            }
        }
    }
}
