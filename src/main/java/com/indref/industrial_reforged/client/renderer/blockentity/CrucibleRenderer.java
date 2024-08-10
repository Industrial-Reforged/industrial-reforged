package com.indref.industrial_reforged.client.renderer.blockentity;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.client.model.CrucibleModel;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.controller.CrucibleBlockEntity;
import com.indref.industrial_reforged.registries.blocks.multiblocks.CrucibleWallBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public class CrucibleRenderer implements BlockEntityRenderer<CrucibleBlockEntity> {
    private final CrucibleModel model;

    public CrucibleRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new CrucibleModel(context.bakeLayer(CrucibleModel.LAYER_LOCATION));
    }

    @Override
    public void render(CrucibleBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.mulPose(Axis.YN.rotationDegrees(blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite().toYRot()));
        poseStack.translate(-0.5, -0.5, -0.5);
        this.model.setupAnimation(partialTick);
        VertexConsumer vertexconsumer = CrucibleModel.CRUCIBLE_LOCATION.buffer(bufferSource, RenderType::entitySolid);
        this.model.render(poseStack, vertexconsumer, packedLight, packedOverlay, -1, partialTick);
        poseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(CrucibleBlockEntity blockEntity) {
        return true;
    }

    @Override
    public @NotNull AABB getRenderBoundingBox(CrucibleBlockEntity blockEntity) {
        return new AABB(blockEntity.getBlockPos()).inflate(2);
    }
}
