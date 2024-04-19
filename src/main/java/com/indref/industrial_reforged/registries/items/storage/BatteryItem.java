package com.indref.industrial_reforged.registries.items.storage;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.items.SimpleElectricItem;
import com.indref.industrial_reforged.api.items.container.IEnergyItem;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.util.ItemUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.List;

public class BatteryItem extends SimpleElectricItem {
    public static final String ENERGY_STAGE_KEY = "energy_stage";

    private final EnergyTier energyTier;
    private final int capacity;

    public BatteryItem(Properties properties, EnergyTier energyTier) {
        super(properties);
        this.energyTier = energyTier;
        this.capacity = energyTier.getDefaultCapacity();
    }

    public BatteryItem(Properties properties, EnergyTier energyTier, int capacity) {
        super(properties);
        this.energyTier = energyTier;
        this.capacity = capacity;
    }

    @Override
    public EnergyTier getEnergyTier() {
        return this.energyTier;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public void onEnergyChanged(ItemStack itemStack) {
        float energyPercentage = (float) getEnergyStored(itemStack) / getCapacity();
        itemStack.getOrCreateTag().putFloat(ENERGY_STAGE_KEY, (int) (energyPercentage * 5));
    }

    public static float getEnergyStage(ItemStack itemStack) {
        return itemStack.getOrCreateTag().getFloat(ENERGY_STAGE_KEY);
    }

    @Override
    public boolean isBarVisible(ItemStack p_150899_) {
        return false;
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (pEntity instanceof Player player && pStack.getOrCreateTag().getBoolean("active")) {
            for (ItemStack itemStack : player.getInventory().items) {
                if (pLevel.getGameTime() % 10 == 0) {
                    if (itemStack.getItem() instanceof IEnergyItem item) {
                        if (this.tryDrainEnergy(pStack, getEnergyTier().getMaxOutput())) {
                            item.tryFillEnergy(itemStack, getEnergyTier().getMaxOutput());
                        }
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
            itemStack.getOrCreateTag().putBoolean("active", !itemStack.getOrCreateTag().getBoolean("active"));
            return InteractionResultHolder.success(itemStack);
        }
        return InteractionResultHolder.fail(itemStack);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level p41422, List<Component> tooltip, TooltipFlag p41424) {
        if (stack.hasTag() && stack.getOrCreateTag().getBoolean("active")) {
            tooltip.add(Component.translatable("nano_saber.desc.active").withStyle(ChatFormatting.GREEN));
        } else {
            tooltip.add(Component.translatable("nano_saber.desc.inactive").withStyle(ChatFormatting.RED));
        }
        super.appendHoverText(stack, p41422, tooltip, p41424);
    }
}
