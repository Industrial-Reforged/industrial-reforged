package com.indref.industrial_reforged.client.renderer.item;

import com.indref.industrial_reforged.client.model.CrucibleModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class CrucibleLegsItemRenderer extends BlockEntityWithoutLevelRenderer {
    public CrucibleLegsItemRenderer() {
        super(null, null);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        CrucibleModel model = new CrucibleModel(Minecraft.getInstance().getEntityModels().bakeLayer(CrucibleModel.LAYER_LOCATION));
        model.setupAnimation(0);
        model.leg1.render(poseStack, CrucibleModel.CRUCIBLE_LOCATION.buffer(buffer, RenderType::entitySolid), packedLight, packedOverlay, -1);
        model.leg0.render(poseStack, CrucibleModel.CRUCIBLE_LOCATION.buffer(buffer, RenderType::entitySolid), packedLight, packedOverlay, -1);
    }
}
