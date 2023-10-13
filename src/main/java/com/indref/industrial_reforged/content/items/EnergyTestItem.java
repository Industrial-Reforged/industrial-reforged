package com.indref.industrial_reforged.content.items;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.api.items.SimpleElectricItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

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
            player.sendSystemMessage(Component.literal(String.valueOf(this.getEnergyStorage(useItemStack).getEnergyStored())));
            return InteractionResultHolder.success(useItemStack);
        }
        return InteractionResultHolder.fail(useItemStack);
    }

    @Override
    public int getMaxEnergy() {
        return 10000;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level p41422, List<Component> tooltip, TooltipFlag p41424) {
        super.appendHoverText(stack, p41422, tooltip, p41424);
        Optional<IEnergyStorage> capability = stack.getCapability(IRCapabilities.ENERGY).resolve();
        if (capability.isPresent()) {
            IEnergyStorage storage = capability.get();
            tooltip.add(Component.literal(String.format("%s / %s", storage.getEnergyStored(), storage.getMaxEnergy())).withStyle(ChatFormatting.AQUA));
        } else {
            tooltip.add(Component.literal("0 / 0"));
        }
    }
}
