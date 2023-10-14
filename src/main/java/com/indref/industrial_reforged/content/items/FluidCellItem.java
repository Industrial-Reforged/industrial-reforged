package com.indref.industrial_reforged.content.items;

import com.indref.industrial_reforged.api.items.SimpleFluidItem;
import com.indref.industrial_reforged.util.ItemUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class FluidCellItem extends SimpleFluidItem {
    private int capacity;
    public FluidCellItem(Properties properties, int capacity) {
        super(properties, capacity);
        this.capacity = capacity;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = ItemUtils.itemStackFromInteractionHand(interactionHand, player);
        if (tryFill(100, itemStack)) {
            player.sendSystemMessage(Component.literal("Filled successfully"));
        } else {
            player.sendSystemMessage(Component.literal("Failed to fill"));
        }
        return super.use(level, player, interactionHand);
    }

    @Override
    public Fluid getFluid() {
        return Fluids.WATER;
    }

}
