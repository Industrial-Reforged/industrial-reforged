package com.indref.industrial_reforged.api.fluids;

import net.minecraft.resources.ResourceLocation;

public interface FluidTemplate {
    ResourceLocation getStillTexture();

    ResourceLocation getFlowingTexture();

    ResourceLocation getOverlayTexture();
}
