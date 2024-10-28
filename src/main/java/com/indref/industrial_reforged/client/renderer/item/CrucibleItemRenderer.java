package com.indref.industrial_reforged.client.renderer.item;

import com.indref.industrial_reforged.client.model.CrucibleModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class CrucibleItemRenderer extends BlockEntityWithoutLevelRenderer {
    public CrucibleItemRenderer() {
        super(null, null);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        EntityModelSet entityModelSet = Minecraft.getInstance().getEntityModels();

        CrucibleModel model = new CrucibleModel(entityModelSet.bakeLayer(CrucibleModel.LAYER_LOCATION));
        VertexConsumer vertexConsumer = CrucibleModel.CRUCIBLE_LOCATION.buffer(buffer, RenderType::entitySolid);

        poseStack.pushPose();
        {
            poseStack.mulPose(Axis.XP.rotationDegrees(180));
            poseStack.mulPose(Axis.YP.rotationDegrees(180));
            model.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay);
        }
        poseStack.popPose();
    }
}
