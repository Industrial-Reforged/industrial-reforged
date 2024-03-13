package com.indref.industrial_reforged.registries.items.armor;

import com.indref.industrial_reforged.util.InputUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class JetpackItem extends ArmorItem {
    public JetpackItem(ArmorMaterial p_40386_, Properties p_40388_) {
        super(p_40386_, Type.CHESTPLATE, p_40388_.stacksTo(1));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        Player player;
        if (pEntity instanceof Player player1)
            player = player1;
        else return;

        ItemStack item = player.getInventory().getItem(38);
        CompoundTag tag = stack.getOrCreateTag();
        if (item.is(this) && tag.getBoolean("active")) {
            if (InputUtils.isHoldingDown(player)) {
                Vec3 motion = player.getDeltaMovement();
                player.setDeltaMovement(motion.x(), 0.5, motion.z());
            }
        }
    }

    public static void toggle(ItemStack itemStack) {
        CompoundTag tag = itemStack.getOrCreateTag();
        if (itemStack.hasTag()) {
            tag.putBoolean("active", !tag.getBoolean("active"));
        } else {
            tag.putBoolean("active", true);
        }
    }
}
