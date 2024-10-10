package com.indref.industrial_reforged.content.items.storage;

import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.api.items.container.IEnergyItem;
import com.indref.industrial_reforged.api.items.container.SimpleElectricItem;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
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

import java.util.List;

public class BatteryItem extends SimpleElectricItem {
    public static final String ENERGY_STAGE_KEY = "energy_stage";
    private final int capacity;

    public BatteryItem(Properties properties, EnergyTier energyTier) {
        this(properties, energyTier, energyTier.getDefaultCapacity());
    }

    public BatteryItem(Properties properties, EnergyTier energyTier, int capacity) {
        super(properties.component(IRDataComponents.BATTERY_STAGE, 0),energyTier);
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public void onEnergyChanged(ItemStack itemStack) {
        float energyPercentage = (float) getEnergyStored(itemStack) / getCapacity();
        itemStack.set(IRDataComponents.BATTERY_STAGE, (int) (energyPercentage * 5));
    }

    public static float getEnergyStage(ItemStack itemStack) {
        return itemStack.getOrDefault(IRDataComponents.BATTERY_STAGE, 0);
    }

    @Override
    public boolean isBarVisible(ItemStack p_150899_) {
        return false;
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (pEntity instanceof Player player && pStack.getOrDefault(IRDataComponents.ACTIVE, false)) {
            for (ItemStack itemStack : player.getInventory().items) {
                if (pLevel.getGameTime() % 3 == 0) {
                    if (itemStack.getItem() instanceof IEnergyItem item) {
                        // TODO: Possibly round robin this?
                        int drained = this.tryDrainEnergy(pStack, getEnergyTier().getMaxOutput());
                        item.tryFillEnergy(itemStack, drained);
                    } else {
                        IEnergyStorage energyStorage = itemStack.getCapability(Capabilities.EnergyStorage.ITEM);
                        if (energyStorage == null) continue;

                        energyStorage.receiveEnergy(getEnergyTier().getMaxOutput(), false);
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
            tooltip.add(Component.translatable("nano_saber.desc.active").withStyle(ChatFormatting.GREEN));
        } else {
            tooltip.add(Component.translatable("nano_saber.desc.inactive").withStyle(ChatFormatting.RED));
        }
        super.appendHoverText(stack, ctx, tooltip, p41424);
    }
}
