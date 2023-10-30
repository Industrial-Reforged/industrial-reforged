package com.indref.industrial_reforged.content.items.food;

import com.indref.industrial_reforged.content.items.misc.CannedFoodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;

public class EnergyDrinkItem extends CannedFoodItem {
    public EnergyDrinkItem(Properties properties) {
        super(properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack p_41452_) {
        return UseAnim.DRINK;
    }
}
