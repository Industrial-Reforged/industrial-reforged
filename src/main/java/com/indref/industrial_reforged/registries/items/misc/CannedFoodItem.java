package com.indref.industrial_reforged.registries.items.misc;

import com.indref.industrial_reforged.registries.IRItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CannedFoodItem extends Item {
    public CannedFoodItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        ItemStack stack = super.finishUsingItem(itemStack, level, livingEntity);
        if (livingEntity instanceof Player player && (player.getAbilities().instabuild)) {
            return stack;
        } else {
            return new ItemStack(IRItems.EMPTY_CAN.get());
        }
    }
}
