package com.indref.industrial_reforged.client.renderer.blockentity;

import com.indref.industrial_reforged.client.model.CrucibleModel;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.controller.CrucibleBlockEntity;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.misc.CrucibleLegsBlockEntity;
import com.indref.industrial_reforged.registries.blocks.multiblocks.misc.CrucibleLegsBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public class CrucibleLegsRenderer implements BlockEntityRenderer<CrucibleLegsBlockEntity> {
    private final CrucibleModel model;

    public CrucibleLegsRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new CrucibleModel(context.bakeLayer(CrucibleModel.LAYER_LOCATION));
    }

    @Override
    public void render(CrucibleLegsBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.mulPose(Axis.YN.rotationDegrees(blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite().toYRot()));
        poseStack.translate(-0.5, -0.5, -0.5);
        this.model.setupAnimation(partialTick);
        VertexConsumer vertexconsumer = CrucibleModel.CRUCIBLE_LOCATION.buffer(bufferSource, RenderType::entitySolid);
        this.model.leg0.render(poseStack, vertexconsumer, packedLight, packedOverlay);
        this.model.leg1.render(poseStack, vertexconsumer, packedLight, packedOverlay);
        poseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(CrucibleLegsBlockEntity blockEntity) {
        return true;
    }

    @Override
    public @NotNull AABB getRenderBoundingBox(CrucibleLegsBlockEntity blockEntity) {
        return new AABB(blockEntity.getBlockPos()).inflate(2);
    }
}
