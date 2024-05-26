package com.indref.industrial_reforged.api.items.bundles;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.component.BundleContents;

public record AdvancedBundleTooltip(AdvancedBundleContents contents) implements TooltipComponent {
    public AdvancedBundleContents contents() {
        return this.contents;
    }
}