package com.indref.industrial_reforged.content.items;

import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.translations.IRTranslations;
import com.indref.industrial_reforged.util.SingleFluidStack;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public class CastingScrapsItem extends Item {
    public CastingScrapsItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        SingleFluidStack fluidStack = stack.get(IRDataComponents.SINGLE_FLUID);
        FluidStack fluidStack1 = fluidStack.fluidStack();

        if (!fluidStack1.isEmpty()) {
            tooltipComponents.add(IRTranslations.Tooltip.FLUID_TYPE.component()
                    .withStyle(ChatFormatting.GRAY)
                    .append(fluidStack1.getHoverName().copy()
                            .withColor(-1)));
            tooltipComponents.add(IRTranslations.Tooltip.FLUID_STORED.component()
                    .withStyle(ChatFormatting.GRAY)
                    .append(IRTranslations.Tooltip.FLUID_AMOUNT.component(fluidStack1.getAmount())
                            .withStyle(ChatFormatting.WHITE))
                    .append(" ")
                    .append(IRTranslations.General.FLUID_UNIT.component()
                            .withStyle(ChatFormatting.WHITE)));
        }
    }
}
