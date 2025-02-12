package com.indref.industrial_reforged.content.fluids;

import com.indref.industrial_reforged.IndustrialReforged;
import com.portingdeadmods.portingdeadlibs.api.fluids.FluidTemplate;
import net.minecraft.resources.ResourceLocation;

public enum FluidTemplates implements FluidTemplate {
    MOLTEN_METAL(ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "fluid/molten_fluid_still"),
            ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "fluid/molten_fluid_flow"),
            ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "fluid/molten_fluid_overlay")),
    MOLTEN_GOLD(ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "fluid/molten_gold_fluid_still"),
            ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "fluid/molten_gold_fluid_flow"),
            ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "fluid/molten_gold_fluid_overlay")),
    MOLTEN_LEAD(ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "fluid/molten_lead_fluid_still"),
            ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "fluid/molten_lead_fluid_flow"),
            ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "fluid/molten_lead_fluid_overlay")),
    OIL(ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "fluid/oil_fluid_still"),
            ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "fluid/oil_fluid_flow"),
            ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "fluid/oil_overlay")),
    WATER(ResourceLocation.parse("block/water_still"),
            ResourceLocation.parse("block/water_flow"),
            ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "misc/in_soap_water"));

    private final ResourceLocation still;
    private final ResourceLocation flowing;
    private final ResourceLocation overlay;

    FluidTemplates(ResourceLocation still, ResourceLocation flowing, ResourceLocation overlay) {
        this.still = still;
        this.flowing = flowing;
        this.overlay = overlay;
    }

    @Override
    public ResourceLocation getStillTexture() {
        return still;
    }

    @Override
    public ResourceLocation getFlowingTexture() {
        return flowing;
    }

    @Override
    public ResourceLocation getOverlayTexture() {
        return overlay;
    }
}
