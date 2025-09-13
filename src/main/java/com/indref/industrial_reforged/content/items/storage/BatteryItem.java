package com.indref.industrial_reforged.content.items.storage;

import com.indref.industrial_reforged.api.capabilities.energy.IEnergyHandler;
import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.api.items.container.SimpleEnergyItem;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.translations.IRTranslations;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class BatteryItem extends SimpleEnergyItem {
    private final int stages;

    public BatteryItem(Properties properties, Supplier<EnergyTier> energyTier, IntSupplier defaultEnergyCapacity, int stages) {
        super(properties, energyTier, defaultEnergyCapacity);
        this.stages = stages;
    }

    public int getStages() {
        return stages;
    }

    public float getBatteryStage(ItemStack itemStack) {
        IEnergyHandler energyStorage = getEnergyCap(itemStack);
        return ((float) energyStorage.getEnergyStored() / energyStorage.getEnergyCapacity()) * (this.stages - 1);
    }

    @Override
    public boolean isBarVisible(ItemStack p_150899_) {
        return false;
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (pEntity instanceof Player player && pStack.getOrDefault(IRDataComponents.ACTIVE, false)) {
            IEnergyHandler batteryHandler = getEnergyCap(pStack);
            for (ItemStack itemStack : player.getInventory().items) {
                if (pLevel.getGameTime() % 3 == 0) {
                    IEnergyHandler energyStorage = getEnergyCap(itemStack);
                    if (energyStorage != null && !pStack.equals(itemStack)) {
                        int drained = batteryHandler.drainEnergy(getEnergyTier().maxOutput(), false);
                        energyStorage.fillEnergy(drained, false);
                    } else {
                        IEnergyStorage feEnergyStorage = itemStack.getCapability(Capabilities.EnergyStorage.ITEM);
                        if (feEnergyStorage == null) continue;

                        feEnergyStorage.receiveEnergy(getEnergyTier().maxOutput(), false);
                    }
                }
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
        if (pPlayer.isShiftKeyDown()) {
            itemStack.set(IRDataComponents.ACTIVE, !itemStack.getOrDefault(IRDataComponents.ACTIVE, false));
            return InteractionResultHolder.success(itemStack);
        }
        return InteractionResultHolder.fail(itemStack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag p41424) {
        if (stack.getOrDefault(IRDataComponents.ACTIVE, false)) {
            tooltip.add(IRTranslations.Tooltip.ACTIVE.component().withStyle(ChatFormatting.GREEN));
        } else {
            tooltip.add(IRTranslations.Tooltip.INACTIVE.component().withStyle(ChatFormatting.RED));
        }
        super.appendHoverText(stack, ctx, tooltip, p41424);
    }
}
