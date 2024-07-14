package com.indref.industrial_reforged.registries.items.tools;

import com.indref.industrial_reforged.api.blocks.DisplayBlock;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.api.items.DisplayItem;
import com.indref.industrial_reforged.api.items.ToolItem;
import com.indref.industrial_reforged.api.items.container.SimpleElectricItem;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.util.BlockUtils;
import com.indref.industrial_reforged.util.CapabilityUtils;
import com.indref.industrial_reforged.util.DisplayUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class ScannerItem extends SimpleElectricItem implements ToolItem, DisplayItem {
    public ScannerItem(Properties p_41383_, EnergyTier energyTier) {
        super(p_41383_, energyTier);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public void displayOverlay(GuiGraphics guiGraphics, int x, int y, int lineOffset, Level level, Player player, BlockPos blockPos) {
        Font font = Minecraft.getInstance().font;
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        BlockState blockstate = level.getBlockState(blockPos);
        ItemStack mainHandStack = player.getMainHandItem();
        if (blockEntity != null) {
            IEnergyStorage energyStorage = CapabilityUtils.energyStorageCapability(blockEntity);
            if (energyStorage != null) {
                List<Component> components = new ArrayList<>();
                // Is block display block
                if (blockstate.getBlock() instanceof DisplayBlock displayBlock && displayBlock.getCompatibleItems().contains(this)) {
                    displayBlock.displayOverlay(components, blockstate, blockPos, level);
                }
                // Render display if there is one
                if (!components.isEmpty()) {
                    for (Component component : components) {
                        guiGraphics.drawCenteredString(font, component, x, y + lineOffset, 256);
                        lineOffset += font.lineHeight + 3;
                    }
                }
                // Drain energy
                if (level.getGameTime() % 20 == 0 && !player.isCreative()) {
                    // TODO: Make scanner only work when it has enough energy
                    int drained = this.tryDrainEnergy(mainHandStack, 1);
                }
            }
        }
    }

}
