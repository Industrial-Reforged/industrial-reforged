package com.indref.industrial_reforged.test;

import com.indref.industrial_reforged.api.items.container.IEnergyItem;
import com.indref.industrial_reforged.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.items.SimpleElectricItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class EnergyTestItem extends SimpleElectricItem {
    public EnergyTestItem(Properties properties) {
        super(properties);
    }

    @Override
    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) {
        ItemStack useItemStack = ItemStack.EMPTY;
        switch (interactionHand) {
            case MAIN_HAND -> useItemStack = player.getMainHandItem();
            case OFF_HAND -> useItemStack = player.getOffhandItem();
        }
        if (!level.isClientSide()) {
            useItemStack.getCapability(IRCapabilities.ENERGY).ifPresent((energyStorage) -> energyStorage.setEnergyStored(energyStorage.getEnergyStored()+100));
            player.sendSystemMessage(Component.literal(String.valueOf(IEnergyItem.getEnergyStorage(useItemStack).getEnergyStored())));
            return InteractionResultHolder.success(useItemStack);
        }
        return InteractionResultHolder.fail(useItemStack);
    }

    @Override
    public int getEnergyCapacity() {
        return 10000;
    }
}
