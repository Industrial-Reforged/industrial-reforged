package com.indref.industrial_reforged.client.renderer.blockentity;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.client.model.CrucibleModel;
import com.indref.industrial_reforged.content.blockentities.multiblocks.controller.CrucibleBlockEntity;
import com.indref.industrial_reforged.util.renderer.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class CrucibleRenderer implements BlockEntityRenderer<CrucibleBlockEntity> {
    private final CrucibleModel model;

    public CrucibleRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new CrucibleModel(context.bakeLayer(CrucibleModel.LAYER_LOCATION));
    }

    @Override
    public void render(CrucibleBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Direction oppositeFacing = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite();
        FluidStack fluidInTank = blockEntity.getFluidHandler().getFluidInTank(0);

        float angle = blockEntity.getIndependentAngle(partialTick);

        poseStack.pushPose();
        {
            poseStack.translate(0.5, 0.5, 0.5);
            poseStack.mulPose(Axis.YN.rotationDegrees(oppositeFacing.toYRot()));
            poseStack.translate(-0.5, -0.5, -0.5);
            // TODO: Maybe we can move this to the constructor
            this.model.setupAnimation(partialTick);
            VertexConsumer vertexconsumer = CrucibleModel.CRUCIBLE_LOCATION.buffer(bufferSource, RenderType::entitySolid);
            // Fluid top
            poseStack.pushPose();
            {
                renderCrucibleFluidTop(poseStack, bufferSource, angle, packedLight, fluidInTank);
            }
            poseStack.popPose();

            // Crucible- and fluid body
            poseStack.pushPose();
            {
                renderCrucibleBody(poseStack, vertexconsumer, angle, packedLight, packedOverlay);

                if (!fluidInTank.isEmpty() && angle == 0) {
                    poseStack.translate(0, 0.325, 0);
                    renderFluid(poseStack, bufferSource, fluidInTank, 1, packedLight, FluidPart.BODY);
                }
            }
            poseStack.popPose();

            // Crucible legs
            this.model.renderCrucibleLegs(poseStack, vertexconsumer, packedLight, packedOverlay, -1);

            // TODO: position correctly
            if (/*blockEntity.isTurnedOver() && */blockEntity.inUse == 0 && !fluidInTank.isEmpty()) {
                poseStack.pushPose();
                {
                    poseStack.translate(0, 2.5, 2);
                    renderFluid(poseStack, bufferSource, fluidInTank, 1, packedLight, FluidPart.FAUCET);
                }
                poseStack.popPose();
            }
        }
        poseStack.popPose();
    }

    private void renderCrucibleBody(PoseStack poseStack, VertexConsumer vertexConsumer, float angle, int packedLight, int packedOverlay) {
        poseStack.translate(0.5, 2.15 + 0.1625, 0.5);
        poseStack.mulPose(Axis.XP.rotation((float) Math.toRadians(angle)));
        poseStack.translate(-0.5, -2.15 - 0.1625, -0.5);

        this.model.renderCrucibleBody(poseStack, vertexConsumer, packedLight, packedOverlay, -1);
    }

    private void renderCrucibleFluidTop(PoseStack poseStack, MultiBufferSource bufferSource, float angle, int packedLight, FluidStack fluidStack) {
        poseStack.translate(0.5, 2.15, 0.5);
        poseStack.mulPose(Axis.XP.rotation((float) Math.toRadians(angle) * 0.5f));
        poseStack.translate(-0.5, -2.15, -0.5);

        if (!fluidStack.isEmpty()) {
            poseStack.translate(0, 0.325, 0);
            renderFluid(poseStack, bufferSource, fluidStack, 1, packedLight, FluidPart.TOP);
        }
    }

    private void renderFluid(PoseStack poseStack, MultiBufferSource bufferSource, FluidStack fluidStack, float heightPercentage, int combinedLight, FluidPart renderPart) {
        VertexConsumer vertexBuilder = bufferSource.getBuffer(RenderType.translucent());
        IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidTypeExtensions.getStillTexture(fluidStack));
        int color = fluidTypeExtensions.getTintColor();
        float alpha = 1;
        alpha *= (color >> 24 & 255) / 255f;
        float red = (color >> 16 & 255) / 255f;
        float green = (color >> 8 & 255) / 255f;
        float blue = (color & 255) / 255f;

        switch (renderPart) {
            case TOP ->
                    renderTopQuads(poseStack.last().pose(), vertexBuilder, sprite, red, green, blue, alpha, heightPercentage, combinedLight);
            case BODY ->
                    renderBodyQuads(poseStack.last().pose(), vertexBuilder, sprite, red, green, blue, alpha, heightPercentage, combinedLight);
            case FAUCET ->
                    renderFaucetQuads(poseStack.last().pose(), vertexBuilder, sprite, red, green, blue, alpha, 3f, combinedLight);
        }
    }

    private static void renderTopQuads(Matrix4f matrix, VertexConsumer buffer, TextureAtlasSprite sprite, float r, float g, float b, float alpha, float heightPercentage, int light) {
        float height = 0.38f * (10 * 0.55f);
        //float minV = sprite.getV(MIN_Y), maxV = sprite.getV(height);
        // top
        buffer.addVertex(matrix, -0.9f, height, -0.9f).setColor(r, g, b, alpha).setUv(sprite.getU(0), sprite.getV(0)).setLight(light).setNormal(0, 1, 0);
        buffer.addVertex(matrix, -0.9f, height, 1.9f).setColor(r, g, b, alpha).setUv(sprite.getU(0), sprite.getV(1)).setLight(light).setNormal(0, 1, 0);
        buffer.addVertex(matrix, 1.9f, height, 1.9f).setColor(r, g, b, alpha).setUv(sprite.getU(1), sprite.getV(1)).setLight(light).setNormal(0, 1, 0);
        buffer.addVertex(matrix, 1.9f, height, -0.9f).setColor(r, g, b, alpha).setUv(sprite.getU(1), sprite.getV(0)).setLight(light).setNormal(0, 1, 0);

        buffer.addVertex(matrix, -0.9f, height, -0.9f).setColor(r, g, b, alpha).setUv(sprite.getU(0), sprite.getV(0)).setLight(light).setNormal(0, -1, 0);
        buffer.addVertex(matrix, 1.9f, height, -0.9f).setColor(r, g, b, alpha).setUv(sprite.getU(1), sprite.getV(0)).setLight(light).setNormal(0, -1, 0);
        buffer.addVertex(matrix, 1.9f, height, 1.9f).setColor(r, g, b, alpha).setUv(sprite.getU(1), sprite.getV(1)).setLight(light).setNormal(0, -1, 0);
        buffer.addVertex(matrix, -0.9f, height, 1.9f).setColor(r, g, b, alpha).setUv(sprite.getU(0), sprite.getV(1)).setLight(light).setNormal(0, -1, 0);
    }

    public static void renderBodyQuads(Matrix4f matrix, VertexConsumer buffer, TextureAtlasSprite sprite, float r, float g, float b, float alpha, float heightPercentage, int light) {
        float height = 0.38f * (10 * 0.55f);
        // sides
        // side 1
        buffer.addVertex(matrix, 1.74f, height, 1.9f).setColor(r, g, b, alpha).setUv(sprite.getU(0.25f), sprite.getV(0)).setLight(light).setNormal(-1, 0, 0);
        buffer.addVertex(matrix, 1.74f, height, -0.9f).setColor(r, g, b, alpha).setUv(sprite.getU(0.25f), sprite.getV(1)).setLight(light).setNormal(-1, 0, 0);
        buffer.addVertex(matrix, 1.74f, 0.25f, -0.9f).setColor(r, g, b, alpha).setUv(sprite.getU(1), sprite.getV(1)).setLight(light).setNormal(-1, 0, 0);
        buffer.addVertex(matrix, 1.74f, 0.25f, 1.9f).setColor(r, g, b, alpha).setUv(sprite.getU(1), sprite.getV(0)).setLight(light).setNormal(-1, 0, 0);
        // side 2
        buffer.addVertex(matrix, -0.74f, height, 1.9f).setColor(r, g, b, alpha).setUv(sprite.getU(0.25f), sprite.getV(0)).setLight(light).setNormal(1, 0, 0);
        buffer.addVertex(matrix, -0.74f, 0.25f, 1.9f).setColor(r, g, b, alpha).setUv(sprite.getU(1), sprite.getV(0)).setLight(light).setNormal(1, 0, 0);
        buffer.addVertex(matrix, -0.74f, 0.25f, -0.9f).setColor(r, g, b, alpha).setUv(sprite.getU(1), sprite.getV(1)).setLight(light).setNormal(1, 0, 0);
        buffer.addVertex(matrix, -0.74f, height, -0.9f).setColor(r, g, b, alpha).setUv(sprite.getU(0.25f), sprite.getV(1)).setLight(light).setNormal(1, 0, 0);
        // side 3
        buffer.addVertex(matrix, 1.9f, height, -0.74f).setColor(r, g, b, alpha).setUv(sprite.getU(0.25f), sprite.getV(0)).setLight(light).setNormal(-1, 0, 0);
        buffer.addVertex(matrix, -0.9f, height, -0.74f).setColor(r, g, b, alpha).setUv(sprite.getU(0.25f), sprite.getV(1)).setLight(light).setNormal(-1, 0, 0);
        buffer.addVertex(matrix, -0.9f, 0.25f, -0.74f).setColor(r, g, b, alpha).setUv(sprite.getU(1), sprite.getV(1)).setLight(light).setNormal(-1, 0, 0);
        buffer.addVertex(matrix, 1.9f, 0.25f, -0.74f).setColor(r, g, b, alpha).setUv(sprite.getU(1), sprite.getV(0)).setLight(light).setNormal(-1, 0, 0);
        // side 4
        buffer.addVertex(matrix, 1.9f, height, 1.74f).setColor(r, g, b, alpha).setUv(sprite.getU(0.25f), sprite.getV(0)).setLight(light).setNormal(1, 0, 0);
        buffer.addVertex(matrix, 1.9f, 0.25f, 1.74f).setColor(r, g, b, alpha).setUv(sprite.getU(1), sprite.getV(0)).setLight(light).setNormal(1, 0, 0);
        buffer.addVertex(matrix, -0.9f, 0.25f, 1.74f).setColor(r, g, b, alpha).setUv(sprite.getU(1), sprite.getV(1)).setLight(light).setNormal(1, 0, 0);
        buffer.addVertex(matrix, -0.9f, height, 1.74f).setColor(r, g, b, alpha).setUv(sprite.getU(0.25f), sprite.getV(1)).setLight(light).setNormal(1, 0, 0);
    }

    private static void renderFaucetQuads(Matrix4f matrix, VertexConsumer buffer, TextureAtlasSprite sprite, float r, float g, float b, float alpha, float belowMaxY, int light) {
        float frontMinU = sprite.getU(0.375f);
        float frontMaxU = sprite.getU(0.625f);
        float yOffset = 2;
        float minY = (2.1f / 16f) - 0.125f - yOffset;
        float maxY = 0.4f - minY + 0.2f + -0.125f - yOffset;
        float height = minY + (maxY - minY);
        float topMinV = sprite.getV(0);
        float topMaxV = sprite.getV(0.625f);
        float minV = sprite.getV(0);
        float maxV = sprite.getV(1);
        //float minV = sprite.getV(MIN_Y), maxV = sprite.getV(height);
        // top

        buffer.addVertex(matrix, 0.3125f, height, -0.25f).setColor(r, g, b, alpha).setUv(frontMinU, topMinV).setLight(light).setNormal(0, 1, 0);
        buffer.addVertex(matrix, 0.3125f, height, 0.375f).setColor(r, g, b, alpha).setUv(frontMinU, topMaxV).setLight(light).setNormal(0, 1, 0);
        buffer.addVertex(matrix, 0.685f, height, 0.375f).setColor(r, g, b, alpha).setUv(frontMaxU, topMaxV).setLight(light).setNormal(0, 1, 0);
        buffer.addVertex(matrix, 0.685f, height, -0.25f).setColor(r, g, b, alpha).setUv(frontMaxU, topMinV).setLight(light).setNormal(0, 1, 0);

        // back
        buffer.addVertex(matrix, 0.6875f, height - 0.25f, 0.25f).setColor(r, g, b, alpha).setUv(frontMinU, minV).setLight(light).setNormal(0, 0, -1);
        buffer.addVertex(matrix, 0.6875f, minY, 0.25f).setColor(r, g, b, alpha).setUv(frontMinU, maxV).setLight(light).setNormal(0, 0, -1);
        buffer.addVertex(matrix, 0.3125f, minY, 0.25f).setColor(r, g, b, alpha).setUv(frontMaxU, maxV).setLight(light).setNormal(0, 0, -1);
        buffer.addVertex(matrix, 0.3125f, height - 0.25f, 0.25f).setColor(r, g, b, alpha).setUv(frontMaxU, minV).setLight(light).setNormal(0, 0, -1);
        // front
        buffer.addVertex(matrix, 0.6875f, height, 0.375f).setColor(r, g, b, alpha).setUv(frontMinU, minV).setLight(light).setNormal(0, 0, 1);
        buffer.addVertex(matrix, 0.3125f, height, 0.375f).setColor(r, g, b, alpha).setUv(frontMaxU, minV).setLight(light).setNormal(0, 0, 1);
        buffer.addVertex(matrix, 0.3125f, minY, 0.375f).setColor(r, g, b, alpha).setUv(frontMaxU, maxV).setLight(light).setNormal(0, 0, 1);
        buffer.addVertex(matrix, 0.6875f, minY, 0.375f).setColor(r, g, b, alpha).setUv(frontMinU, maxV).setLight(light).setNormal(0, 0, 1);

        float sideMinU = sprite.getU(0.25f);
        float sideMaxU = sprite.getU(0.5f);
        // side 1
        buffer.addVertex(matrix, 0.3125f, height, 0.375f).setColor(r, g, b, alpha).setUv(sideMinU, minV).setLight(light).setNormal(-1, 0, 0);
        buffer.addVertex(matrix, 0.3125f, height, 0.25f).setColor(r, g, b, alpha).setUv(sideMaxU, minV).setLight(light).setNormal(-1, 0, 0);
        buffer.addVertex(matrix, 0.3125f, minY, 0.25f).setColor(r, g, b, alpha).setUv(sideMaxU, maxV).setLight(light).setNormal(-1, 0, 0);
        buffer.addVertex(matrix, 0.3125f, minY, 0.375f).setColor(r, g, b, alpha).setUv(sideMinU, maxV).setLight(light).setNormal(-1, 0, 0);
        // side 2
        buffer.addVertex(matrix, 0.6875f, height, 0.375f).setColor(r, g, b, alpha).setUv(sideMinU, minV).setLight(light).setNormal(1, 0, 0);
        buffer.addVertex(matrix, 0.6875f, minY, 0.375f).setColor(r, g, b, alpha).setUv(sideMinU, maxV).setLight(light).setNormal(1, 0, 0);
        buffer.addVertex(matrix, 0.6875f, minY, 0.25f).setColor(r, g, b, alpha).setUv(sideMaxU, maxV).setLight(light).setNormal(1, 0, 0);
        buffer.addVertex(matrix, 0.6875f, height, 0.25f).setColor(r, g, b, alpha).setUv(sideMaxU, minV).setLight(light).setNormal(1, 0, 0);

    }

    @Override
    public boolean shouldRenderOffScreen(CrucibleBlockEntity blockEntity) {
        return true;
    }

    @Override
    public @NotNull AABB getRenderBoundingBox(CrucibleBlockEntity blockEntity) {
        return new AABB(blockEntity.getBlockPos()).inflate(2);
    }

    enum FluidPart {
        TOP,
        BODY,
        FAUCET;
    }
}
