package com.indref.industrial_reforged.registries.items.tools;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.DisplayBlock;
import com.indref.industrial_reforged.api.blocks.container.IHeatBlock;
import com.indref.industrial_reforged.api.items.DisplayItem;
import com.indref.industrial_reforged.api.items.container.IHeatItem;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.util.BlockUtils;
import com.indref.industrial_reforged.util.DisplayUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ThermometerItem extends ToolItem implements DisplayItem, IHeatItem {
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
            if (!displayBlock.getCompatibleItems().contains(IRItems.THERMOMETER.get())) return;

            for (Component component : displayBlock.displayOverlay(blockstate, blockPos, level)) {
                guiGraphics.drawCenteredString(font, component, x, y + lineOffset, 256);
                lineOffset += font.lineHeight + 3;
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
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
        itemStack.getOrCreateTag().putFloat("temperature", Math.round((float) getHeatStored(itemStack) / 1000));
    }

    public static float getTemperature(ItemStack itemStack) {
        return itemStack.getOrCreateTag().getFloat("temperature");
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
