package com.indref.industrial_reforged.content.items.tools;

import com.indref.industrial_reforged.api.blocks.DisplayBlock;
import com.indref.industrial_reforged.api.blockentities.multiblock.FakeBlockEntity;
import com.indref.industrial_reforged.api.capabilities.heat.IHeatStorage;
import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.api.items.tools.DisplayItem;
import com.indref.industrial_reforged.api.items.container.SimpleHeatItem;
import com.indref.industrial_reforged.util.IRTranslations;
import com.indref.industrial_reforged.util.capabilities.CapabilityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import java.util.ArrayList;
import java.util.List;

public class ThermometerItem extends SimpleHeatItem {
    public ThermometerItem(Properties properties) {
        super(properties.component(IRDataComponents.THERMOMETER_STAGE, 0));
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        if (!(entity instanceof Player player)) return;
        BlockHitResult result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
        IHeatStorage heatStorage = getHeatCap(itemStack);

        if (result.getType() != HitResult.Type.MISS) {
            BlockPos blockPos = result.getBlockPos();
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof FakeBlockEntity fakeBlockEntity) {
                if (fakeBlockEntity.getActualBlockEntityPos() != null) {
                    blockEntity = level.getBlockEntity(fakeBlockEntity.getActualBlockEntityPos());
                }
            }
            if (blockEntity != null) {
                IHeatStorage blockHeatStorage = CapabilityUtils.heatStorageCapability(blockEntity);
                if (blockHeatStorage != null) {
                    heatStorage.setHeatStored(Math.min(heatStorage.getHeatStored() + 16, blockHeatStorage.getHeatStored()));
                } else {
                    heatStorage.setHeatStored(Math.max(heatStorage.getHeatStored() - 16, 0));
                }
            }
        } else {
            heatStorage.setHeatStored(Math.max(heatStorage.getHeatStored() - 16, 0));
        }
        itemStack.set(IRDataComponents.THERMOMETER_STAGE, Math.round(heatStorage.getHeatStored() / 1000f));

        if (heatStorage.getHeatStored() >= heatStorage.getHeatCapacity()) {
            explodeThermometer(player, itemStack);
        }
    }

    private static void explodeThermometer(Player player, ItemStack itemStack) {
        player.playSound(SoundEvents.GLASS_BREAK);
        Registry<DamageType> damageTypes = player.damageSources().damageTypes;
        player.hurt(new DamageSource(damageTypes.getHolderOrThrow(DamageTypes.ON_FIRE)), 4);
        itemStack.shrink(1);
        ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Items.GLASS_PANE, 3), player.getInventory().selected);
    }

    @Override
    public int getDefaultHeatCapacity() {
        return 7000;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag p_41424_) {
        IHeatStorage heatStorage = getHeatCap(stack);
        tooltip.add(IRTranslations.Tooltip.HEAT_STORED.component(heatStorage.getHeatStored(), heatStorage.getHeatCapacity()));
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public boolean isBarVisible(ItemStack p_150899_) {
        return false;
    }
}
