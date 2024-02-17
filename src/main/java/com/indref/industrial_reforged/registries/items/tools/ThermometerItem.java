package com.indref.industrial_reforged.registries.items.tools;

import com.indref.industrial_reforged.api.blocks.container.IEnergyBlock;
import com.indref.industrial_reforged.api.blocks.container.IHeatBlock;
import com.indref.industrial_reforged.api.items.DisplayItem;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ThermometerItem extends ToolItem implements DisplayItem {
    public ThermometerItem(Properties properties) {
        super(properties);
    }

    @Override
    public void displayOverlay(GuiGraphics guiGraphics, int x, int y, int lineOffset, Level level, Player player, BlockPos blockPos) {
        Font font = Minecraft.getInstance().font;
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        BlockState blockstate = level.getBlockState(blockPos);
        ItemStack mainHandStack = player.getMainHandItem();
        if (BlockUtils.isHeatBlock(blockEntity)) {
            for (Component component : DisplayUtils.displayHeatInfo(blockEntity, blockstate)) {
                guiGraphics.drawCenteredString(font, component, x, y + lineOffset, 256);
                lineOffset += font.lineHeight + 3;
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        CompoundTag tag = itemStack.getOrCreateTag();
        float temperature = tag.getFloat("temperature");
        tag.putFloat("temperature", temperature + 1);
        return InteractionResultHolder.success(itemStack);
    }

    public static float getTemperature(ItemStack itemStack) {
        return itemStack.getOrCreateTag().getFloat("temperature");
    }
}
