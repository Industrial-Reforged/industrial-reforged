package com.indref.industrial_reforged.content.blocks;

import com.indref.industrial_reforged.translations.IRTranslations;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class BlastFurnaceMultiblockBlock extends Block {
    public BlastFurnaceMultiblockBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(IRTranslations.Tooltip.MULTIBLOCK_HINT.component().withStyle(ChatFormatting.DARK_GRAY));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
