package com.indref.industrial_reforged.registries.items.tools;

import com.indref.industrial_reforged.api.blocks.DisplayBlock;
import com.indref.industrial_reforged.api.blocks.container.IHeatBlock;
import com.indref.industrial_reforged.api.items.DisplayItem;
import com.indref.industrial_reforged.api.items.container.IHeatItem;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.util.BlockUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ThermometerItem extends ToolItem implements DisplayItem, IHeatItem {
    public static final String DISPLAY_TEMPERATURE_KEY = "thermometer_temperature";

    public ThermometerItem(Properties properties) {
        super(properties);
    }

    @Override
    public void displayOverlay(GuiGraphics guiGraphics, int x, int y, int lineOffset, Level level, Player player, BlockPos blockPos) {
        Font font = Minecraft.getInstance().font;
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        BlockState blockstate = level.getBlockState(blockPos);
        ItemStack mainHandStack = player.getMainHandItem();
        if (blockstate.getBlock() instanceof DisplayBlock displayBlock) {
            if (!displayBlock.getCompatibleItems().contains((DisplayItem) IRItems.THERMOMETER.get())) return;

            for (Component component : displayBlock.displayOverlay(blockstate, blockPos, level)) {
                guiGraphics.drawCenteredString(font, component, x, y + lineOffset, 256);
                lineOffset += font.lineHeight + 3;
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        if (!(entity instanceof Player player)) return;

        HitResult hitResult = Minecraft.getInstance().hitResult;
        if (hitResult instanceof BlockHitResult blockHitResult) {
            BlockPos blockPos = blockHitResult.getBlockPos();
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            IHeatBlock heatBlock = BlockUtils.getHeatBlock(blockEntity);
            if (heatBlock != null) {
                setHeatStored(itemStack, Math.min(getHeatStored(itemStack) + 16, heatBlock.getHeatStored(blockEntity)));
            } else {
                setHeatStored(itemStack, Math.max(getHeatStored(itemStack) - 16, 0));
            }
        } else {
            setHeatStored(itemStack, Math.max(getHeatStored(itemStack) - 16, 0));
        }
        itemStack.getOrCreateTag().putFloat(DISPLAY_TEMPERATURE_KEY, Math.round((float) getHeatStored(itemStack) / 1000));

        if (getHeatStored(itemStack) >= getHeatCapacity()) {
            explodeThermometer(player, itemStack);
        }
    }

    private static void explodeThermometer(Player player, ItemStack itemStack) {
        player.playSound(SoundEvents.GLASS_BREAK);
        Registry<DamageType> damageTypes = player.damageSources().damageTypes;
        player.hurt(new DamageSource(damageTypes.getHolderOrThrow(DamageTypes.ON_FIRE)), 4);
        itemStack.setCount(0);
        ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Items.GLASS_PANE, 3), player.getInventory().selected);
    }

    public static float getTemperature(ItemStack itemStack) {
        return itemStack.getOrCreateTag().getFloat(DISPLAY_TEMPERATURE_KEY);
    }

    @Override
    public int getHeatCapacity() {
        return 7000;
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> tooltip, TooltipFlag p_41424_) {
        tooltip.add(Component.literal("Heat Stored: ").append("%d/%d".formatted(getHeatStored(p_41421_), getHeatCapacity())));
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }
}
