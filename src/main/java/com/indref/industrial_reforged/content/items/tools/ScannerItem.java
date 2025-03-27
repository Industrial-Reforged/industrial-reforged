package com.indref.industrial_reforged.content.items.tools;

import com.indref.industrial_reforged.api.items.container.SimpleEnergyItem;
import com.indref.industrial_reforged.api.items.tools.electric.ElectricToolItem;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntSupplier;

public class ScannerItem extends SimpleEnergyItem implements ElectricToolItem {
    private final IntSupplier energyUsage;

    public ScannerItem(Properties p_41383_, Holder<EnergyTier> energyTier, IntSupplier energyUsage, IntSupplier defaultEnergyCapacity) {
        super(p_41383_, energyTier, defaultEnergyCapacity);
        this.energyUsage = energyUsage;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public boolean requireEnergyToWork(ItemStack itemStack, @Nullable Entity entity) {
        return true;
    }

    @Override
    public int getEnergyUsage(ItemStack itemStack, @Nullable Entity entity) {
        return energyUsage.getAsInt();
    }

//    @Override
//    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
//        super.inventoryTick(stack, level, entity, slotId, isSelected);
//
//        if (entity instanceof Player player) {
//            if (player.getMainHandItem().equals(stack)) {
//                BlockHitResult playerPOVHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
//                if (playerPOVHitResult.getType() == HitResult.Type.BLOCK) {
//                    if (level.getBlockState(playerPOVHitResult.getBlockPos()).) {
//
//                    }
//                }
//            }
//        }
//    }
}
