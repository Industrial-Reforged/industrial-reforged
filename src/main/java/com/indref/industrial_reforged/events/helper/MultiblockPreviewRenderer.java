package com.indref.industrial_reforged.events.helper;

import com.indref.industrial_reforged.util.renderer.CastingItemRenderTypeBuffer;
import com.indref.industrial_reforged.util.renderer.IRRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.joml.Matrix4f;

public final class MultiblockPreviewRenderer {
    public static void renderSmallOptionalBlock(PoseStack poseStack, BlockPos blockPos, double camX, double camY, double camZ, BlockRenderDispatcher blockRenderer, MultiBufferSource.BufferSource bufferSource, Level level, Block block) {
        poseStack.pushPose();
        {
            poseStack.translate(blockPos.getX() - camX, blockPos.getY() - camY, blockPos.getZ() - camZ);
            poseStack.scale(0.5f, 0.5f, 0.5f);
            poseStack.translate(0.5, 0.5, 0.5);
            blockRenderer.renderSingleBlock(block.defaultBlockState(), poseStack, new CastingItemRenderTypeBuffer(bufferSource, 120, 0), getLightLevel(level, blockPos), OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.translucent());
        }
        poseStack.popPose();
    }

    public static void renderSmallBlock(PoseStack poseStack, BlockPos blockPos, double camX, double camY, double camZ, BlockRenderDispatcher blockRenderer, MultiBufferSource.BufferSource bufferSource, Level level, Block block) {
        poseStack.pushPose();
        {
            poseStack.translate(blockPos.getX() - camX, blockPos.getY() - camY, blockPos.getZ() - camZ);
            poseStack.scale(0.5f, 0.5f, 0.5f);
            poseStack.translate(0.5, 0.5, 0.5);
            blockRenderer.renderSingleBlock(block.defaultBlockState(), poseStack, bufferSource, getLightLevel(level, blockPos), OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.solid());
        }
        poseStack.popPose();
    }

    public static void renderErrorBlock(PoseStack poseStack, BlockPos blockPos, double camX, double camY, double camZ, MultiBufferSource.BufferSource bufferSource, Level level) {
        poseStack.pushPose();
        {
            poseStack.translate(blockPos.getX() - camX, blockPos.getY() - camY, blockPos.getZ() - camZ);
            int r = 255;
            int g = 0;
            int b = 0;
            int a = 100;
            VertexConsumer consumer = bufferSource.getBuffer(IRRenderTypes.SIMPLE_SOLID);
            Matrix4f matrix = poseStack.last().pose();

            renderCube(consumer, matrix, r, g, b, a);
        }
        poseStack.popPose();
    }

    public static void renderCube(VertexConsumer consumer, Matrix4f matrix, int r, int g, int b, int a) {
        // Top side
        consumer.addVertex(matrix, -0.1f, 1.1f, -0.1f).setColor(r, g, b, a).setNormal(0, 1, 0);
        consumer.addVertex(matrix, -0.1f, 1.1f, 1.1f).setColor(r, g, b, a).setNormal(0, 1, 0);
        consumer.addVertex(matrix, 1.1f, 1.1f, 1.1f).setColor(r, g, b, a).setNormal(0, 1, 0);
        consumer.addVertex(matrix, 1.1f, 1.1f, -0.1f).setColor(r, g, b, a).setNormal(0, 1, 0);

        // Bottom side
        consumer.addVertex(matrix, -0.1f, -0.1f, -0.1f).setColor(r, g, b, a).setNormal(0, -1, 0);
        consumer.addVertex(matrix, 1.1f, -0.1f, -0.1f).setColor(r, g, b, a).setNormal(0, -1, 0);
        consumer.addVertex(matrix, 1.1f, -0.1f, 1.1f).setColor(r, g, b, a).setNormal(0, -1, 0);
        consumer.addVertex(matrix, -0.1f, -0.1f, 1.1f).setColor(r, g, b, a).setNormal(0, -1, 0);

        // Front side
        consumer.addVertex(matrix, -0.1f, -0.1f, 1.1f).setColor(r, g, b, a).setNormal(0, 0, 1);
        consumer.addVertex(matrix, 1.1f, -0.1f, 1.1f).setColor(r, g, b, a).setNormal(0, 0, 1);
        consumer.addVertex(matrix, 1.1f, 1.1f, 1.1f).setColor(r, g, b, a).setNormal(0, 0, 1);
        consumer.addVertex(matrix, -0.1f, 1.1f, 1.1f).setColor(r, g, b, a).setNormal(0, 0, 1);

        // Back side
        consumer.addVertex(matrix, -0.1f, -0.1f, -0.1f).setColor(r, g, b, a).setNormal(0, 0, -1);
        consumer.addVertex(matrix, -0.1f, 1.1f, -0.1f).setColor(r, g, b, a).setNormal(0, 0, -1);
        consumer.addVertex(matrix, 1.1f, 1.1f, -0.1f).setColor(r, g, b, a).setNormal(0, 0, -1);
        consumer.addVertex(matrix, 1.1f, -0.1f, -0.1f).setColor(r, g, b, a).setNormal(0, 0, -1);

        // Left side
        consumer.addVertex(matrix, -0.1f, -0.1f, -0.1f).setColor(r, g, b, a).setNormal(-1, 0, 0);
        consumer.addVertex(matrix, -0.1f, -0.1f, 1.1f).setColor(r, g, b, a).setNormal(-1, 0, 0);
        consumer.addVertex(matrix, -0.1f, 1.1f, 1.1f).setColor(r, g, b, a).setNormal(-1, 0, 0);
        consumer.addVertex(matrix, -0.1f, 1.1f, -0.1f).setColor(r, g, b, a).setNormal(-1, 0, 0);

        // Right side
        consumer.addVertex(matrix, 1.1f, -0.1f, -0.1f).setColor(r, g, b, a).setNormal(1, 0, 0);
        consumer.addVertex(matrix, 1.1f, 1.1f, -0.1f).setColor(r, g, b, a).setNormal(1, 0, 0);
        consumer.addVertex(matrix, 1.1f, 1.1f, 1.1f).setColor(r, g, b, a).setNormal(1, 0, 0);
        consumer.addVertex(matrix, 1.1f, -0.1f, 1.1f).setColor(r, g, b, a).setNormal(1, 0, 0);
    }

    private static int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
