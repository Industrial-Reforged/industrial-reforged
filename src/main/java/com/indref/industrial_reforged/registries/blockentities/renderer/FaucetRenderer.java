package com.indref.industrial_reforged.registries.blockentities.renderer;

import com.indref.industrial_reforged.registries.IRFluids;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.FaucetBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Matrix4f;

public class FaucetRenderer implements BlockEntityRenderer<FaucetBlockEntity> {
    private static final float SIDE_MARGIN = 0.375f;
    public static final float MIN_Y = 2.1f / 16f;
    public static final float MAX_Y = 0.4f - MIN_Y + 0.2f;

    public FaucetRenderer(BlockEntityRendererProvider.Context ignored) {
    }

    @Override
    public void render(FaucetBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Direction direction = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        poseStack.translate(.5f, .5f, .5f);
        poseStack.mulPose(Axis.YN.rotationDegrees(direction.toYRot()));
        poseStack.translate(-.5f, -.5f, -.5f);
        renderFluid(poseStack, bufferSource, new FluidStack(IRFluids.MOLTEN_STEEL_FLOWING.get(), 1000), 1, 1, packedLight);
    }

    private static void renderFluid(PoseStack poseStack, MultiBufferSource bufferSource, FluidStack fluidStack, float alpha, float heightPercentage, int combinedLight) {
        VertexConsumer vertexBuilder = bufferSource.getBuffer(RenderType.translucent());
        IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(fluidTypeExtensions.getFlowingTexture(fluidStack));
        int color = fluidTypeExtensions.getTintColor();
        alpha *= (color >> 24 & 255) / 255f;
        float red = (color >> 16 & 255) / 255f;
        float green = (color >> 8 & 255) / 255f;
        float blue = (color & 255) / 255f;

        renderQuads(poseStack.last().pose(), vertexBuilder, sprite, red, green, blue, alpha, heightPercentage, combinedLight);
    }

    private static void renderQuads(Matrix4f matrix, VertexConsumer buffer, TextureAtlasSprite sprite, float r, float g, float b, float alpha, float heightPercentage, int light) {
        float height = MIN_Y + (MAX_Y - MIN_Y) * heightPercentage;
        float minU = sprite.getU(0.375f);
        float maxU = sprite.getU(0.625f);
        float minV = sprite.getV(0.375f);
        float maxV = sprite.getV(0.625f);
        //float minV = sprite.getV(MIN_Y), maxV = sprite.getV(height);
        // top
        buffer.addVertex(matrix, 0.375f, height, 0).setColor(r, g, b, alpha).setUv(minU, minV).setLight(light).setNormal(0, 1, 0);
        buffer.addVertex(matrix, 0.375f, height, 0.5f).setColor(r, g, b, alpha).setUv(minU, maxV).setLight(light).setNormal(0, 1, 0);
        buffer.addVertex(matrix, 0.625f, height, 0.5f).setColor(r, g, b, alpha).setUv(maxU, maxV).setLight(light).setNormal(0, 1, 0);
        buffer.addVertex(matrix, 0.625f, height, 0).setColor(r, g, b, alpha).setUv(maxU, minV).setLight(light).setNormal(0, 1, 0);

        // min z (done)
        buffer.addVertex(matrix, 0.625f, height, 0.25f).setColor(r, g, b, alpha).setUv(minU, minV).setLight(light).setNormal(0, 0, -1);
        buffer.addVertex(matrix, 0.625f, MIN_Y, 0.25f).setColor(r, g, b, alpha).setUv(minU, maxV).setLight(light).setNormal(0, 0, -1);
        buffer.addVertex(matrix, 0.375f, MIN_Y, 0.25f).setColor(r, g, b, alpha).setUv(maxU, maxV).setLight(light).setNormal(0, 0, -1);
        buffer.addVertex(matrix, 0.375f, height, 0.25f).setColor(r, g, b, alpha).setUv(maxU, minV).setLight(light).setNormal(0, 0, -1);
        // max z (done)
        buffer.addVertex(matrix, 0.625f, height, 0.5f).setColor(r, g, b, alpha).setUv(minU, minV).setLight(light).setNormal(0, 0, 1);
        buffer.addVertex(matrix, 0.375f, height, 0.5f).setColor(r, g, b, alpha).setUv(maxU, minV).setLight(light).setNormal(0, 0, 1);
        buffer.addVertex(matrix, 0.375f, MIN_Y, 0.5f).setColor(r, g, b, alpha).setUv(maxU, maxV).setLight(light).setNormal(0, 0, 1);
        buffer.addVertex(matrix, 0.625f, MIN_Y, 0.5f).setColor(r, g, b, alpha).setUv(minU, maxV).setLight(light).setNormal(0, 0, 1);
        // min x (done)
        buffer.addVertex(matrix, 0.375f, height, 0.5f).setColor(r, g, b, alpha).setUv(minU, minV).setLight(light).setNormal(-1, 0, 0);
        buffer.addVertex(matrix, 0.375f, height, 0.25f).setColor(r, g, b, alpha).setUv(maxU, minV).setLight(light).setNormal(-1, 0, 0);
        buffer.addVertex(matrix, 0.375f, MIN_Y, 0.25f).setColor(r, g, b, alpha).setUv(maxU, maxV).setLight(light).setNormal(-1, 0, 0);
        buffer.addVertex(matrix, 0.375f, MIN_Y, 0.5f).setColor(r, g, b, alpha).setUv(minU, maxV).setLight(light).setNormal(-1, 0, 0);
        // max x
        buffer.addVertex(matrix, 0.625f, height, 0.5f).setColor(r, g, b, alpha).setUv(minU, minV).setLight(light).setNormal(1, 0, 0);
        buffer.addVertex(matrix, 0.625f, MIN_Y, 0.5f).setColor(r, g, b, alpha).setUv(minU, maxV).setLight(light).setNormal(1, 0, 0);
        buffer.addVertex(matrix, 0.625f, MIN_Y, 0.25f).setColor(r, g, b, alpha).setUv(maxU, maxV).setLight(light).setNormal(1, 0, 0);
        buffer.addVertex(matrix, 0.625f, height, 0.25f).setColor(r, g, b, alpha).setUv(maxU, minV).setLight(light).setNormal(1, 0, 0);
    }
}
