package com.indref.industrial_reforged.compat.guideme.tags;

import com.indref.industrial_reforged.IRRegistries;
import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import guideme.color.ConstantColor;
import guideme.compiler.PageCompiler;
import guideme.compiler.tags.FlowTagCompiler;
import guideme.document.flow.LytFlowParent;
import guideme.document.flow.LytFlowText;
import guideme.libs.mdast.mdx.model.MdxJsxElementFields;
import guideme.style.TextStyle;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

import java.util.Set;

public class EnergyTierTagExtension extends FlowTagCompiler {
    @Override
    public Set<String> getTagNames() {
        return Set.of(IndustrialReforged.MODID + ":EnergyTier");
    }

    @Override
    protected void compile(PageCompiler compiler, LytFlowParent parent, MdxJsxElementFields el) {
        String id = el.getAttributeString("id", "");
        if (id.isEmpty()) {
            parent.appendError(compiler, "id is required", el);
            return;
        }

        EnergyTier energyTier = IRRegistries.ENERGY_TIER.get(ResourceLocation.parse(id));

        this.appendText(parent, energyTier.getDisplayName().copy().withColor(energyTier.color()).withStyle(style -> style.withBold(true)));
        this.appendText(parent, Component.literal(" - Max Input/Output: %s, %s - Default Capacity: %s".formatted(
             ChatFormatting.WHITE + this.intToString(energyTier.maxInput()) + " EU" + ChatFormatting.GRAY,
                ChatFormatting.WHITE + this.intToString(energyTier.maxOutput()) + " EU" + ChatFormatting.GRAY,
                ChatFormatting.WHITE + this.intToString(energyTier.defaultCapacity()) + " EU" + ChatFormatting.GRAY
        )).withStyle(ChatFormatting.GRAY));
    }

    private void appendText(LytFlowParent parent, Component component) {
        LytFlowText lytFlowText = parent.appendText(component.getString());
        lytFlowText.setStyle(this.componentStyleToTextStyle(component.getStyle()));
    }

    private static final String[] NUMBER_POSTFIXES = {"", "", "M", "B", "T"};

    private String intToString(int i) {
        if (i == Integer.MAX_VALUE) {
            return "MAX";
        }

        if (i < 1_000_000) {
            return String.valueOf(i);
        }

        double value = i;
        int index = 0;

        while (value >= 1_000_000 && index < NUMBER_POSTFIXES.length - 1) {
            value /= 1000.0;
            index++;
        }

        return String.format("%.1f%s", value, NUMBER_POSTFIXES[index])
                .replaceAll("\\.0([A-Z]*)$", "$1"); // remove trailing .0
    }

    private TextStyle componentStyleToTextStyle(Style style) {
        TextStyle.Builder builder = TextStyle.builder()
                .bold(style.isBold())
                .italic(style.isItalic())
                .underlined(style.isUnderlined())
                .strikethrough(style.isStrikethrough())
                .obfuscated(style.isObfuscated());

        if (style.getColor() != null) {
            builder.color(new ConstantColor(style.getColor().getValue()));
        }

        return builder.build();
    }

}
