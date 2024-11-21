package com.indref.industrial_reforged.client.renderer.item;

import com.indref.industrial_reforged.client.model.BlastFurnaceItemModel;
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

public class BlastFurnaceItemRenderer extends BlockEntityWithoutLevelRenderer {
    private BlastFurnaceItemModel model;

    public BlastFurnaceItemRenderer() {
        super(null, null);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        EntityModelSet entityModelSet = Minecraft.getInstance().getEntityModels();

        if (this.model == null) {
            this.model = new BlastFurnaceItemModel(entityModelSet.bakeLayer(BlastFurnaceItemModel.LAYER_LOCATION));
        }

        VertexConsumer vertexConsumer = BlastFurnaceItemModel.BLAST_FURNACE_LOCATION.buffer(buffer, RenderType::entitySolid);

        poseStack.pushPose();
        {
            poseStack.translate(0.5, 1.25, 0.5);
            poseStack.mulPose(Axis.XP.rotationDegrees(180));
            this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay);
        }
        poseStack.popPose();
    }
}
