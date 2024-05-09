package com.indref.industrial_reforged.registries.items.tools;

import com.indref.industrial_reforged.api.items.DisplayItem;
import com.indref.industrial_reforged.api.items.ToolItem;
import com.indref.industrial_reforged.api.items.container.SimpleElectricItem;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.tiers.EnergyTiers;
import com.indref.industrial_reforged.util.BlockUtils;
import com.indref.industrial_reforged.util.DisplayUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ScannerItem extends SimpleElectricItem implements ToolItem, DisplayItem {
    public ScannerItem(Properties p_41383_, EnergyTier energyTier) {
        super(p_41383_, energyTier);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41432_, Player player, InteractionHand p_41434_) {
        ItemStack itemStack = player.getItemInHand(p_41434_);
        setEnergyStored(itemStack, getEnergyStored(itemStack)+10);
        player.sendSystemMessage(Component.literal("Energy stored: "+getEnergyStored(itemStack)));
        return InteractionResultHolder.success(itemStack);
    }

    @Override
    public void displayOverlay(GuiGraphics guiGraphics, int x, int y, int lineOffset, Level level, Player player, BlockPos blockPos) {
        Font font = Minecraft.getInstance().font;
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        BlockState blockstate = level.getBlockState(blockPos);
        ItemStack mainHandStack = player.getMainHandItem();
        if (BlockUtils.isEnergyBlock(blockEntity)) {
            for (Component component : DisplayUtils.displayEnergyInfo(blockEntity, blockstate)) {
                guiGraphics.drawCenteredString(font, component, x, y + lineOffset, 256);
                lineOffset += font.lineHeight + 3;
            }
            if (level.getGameTime() % 20 == 0 && !player.isCreative()) {
                this.tryDrainEnergy(mainHandStack, 1);
            }
        }
    }

}
