package com.indref.industrial_reforged.content.items.armor;

import com.indref.industrial_reforged.data.IRDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class JetpackItem extends ArmorItem {
    public JetpackItem(Holder<ArmorMaterial> p_40386_, Properties p_40388_) {
        super(p_40386_, Type.CHESTPLATE, p_40388_.stacksTo(1));
    }
}
