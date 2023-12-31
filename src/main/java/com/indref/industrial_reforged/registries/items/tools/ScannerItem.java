package com.indref.industrial_reforged.registries.items.tools;

import com.indref.industrial_reforged.api.items.IToolItem;
import com.indref.industrial_reforged.api.items.SimpleElectricItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ScannerItem extends SimpleElectricItem implements IToolItem {
    public ScannerItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public int getEnergyCapacity() {
        return 69;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player player, InteractionHand p_41434_) {
        ItemStack itemStack = player.getItemInHand(p_41434_);
        setEnergyStored(itemStack, getEnergyStored(itemStack)+10);
        player.sendSystemMessage(Component.literal("Energy stored: "+getEnergyStored(itemStack)));
        return InteractionResultHolder.success(itemStack);
    }

}
