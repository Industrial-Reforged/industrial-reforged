package com.indref.industrial_reforged.registries.blockentities.renderer;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.FaucetInteractBlock;
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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Matrix4f;

public class FaucetRenderer implements BlockEntityRenderer<FaucetBlockEntity> {
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

        Level level = blockEntity.getLevel();
        BlockPos blockPos = blockEntity.getBlockPos().below();
        BlockState belowState = level.getBlockState(blockPos);

        float maxY;
        if (belowState.getBlock() instanceof FaucetInteractBlock faucetInteractBlock) {
            maxY = faucetInteractBlock.getShapeMaxY(level, blockPos);
        } else {
            maxY = (float) belowState.getShape(level, blockPos).move(0.5, 0, 0.5).max(Direction.Axis.Y);
        }

        renderFluid(poseStack, bufferSource, new FluidStack(IRFluids.MOLTEN_STEEL_FLOWING.get(), 1000), maxY, packedLight);
    }

    private static void renderFluid(PoseStack poseStack, MultiBufferSource bufferSource, FluidStack fluidStack, float belowMaxY, int combinedLight) {
        float alpha = 1;
        VertexConsumer vertexBuilder = bufferSource.getBuffer(RenderType.translucent());
        IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidTypeExtensions.getFlowingTexture(fluidStack));
        int color = fluidTypeExtensions.getTintColor();
        alpha *= (color >> 24 & 255) / 255f;
        float red = (color >> 16 & 255) / 255f;
        float green = (color >> 8 & 255) / 255f;
        float blue = (color & 255) / 255f;

        renderQuads(poseStack.last().pose(), vertexBuilder, sprite, red, green, blue, alpha, belowMaxY, combinedLight);
    }

    private static void renderQuads(Matrix4f matrix, VertexConsumer buffer, TextureAtlasSprite sprite, float r, float g, float b, float alpha, float belowMaxY, int light) {
        float frontMinU = sprite.getU(0.375f);
        float frontMaxU = sprite.getU(0.625f);
        float yOffset = belowMaxY > 0 ? 1 - belowMaxY : 0;
        float minY = (2.1f / 16f) - 0.125f - yOffset;
        float maxY = 0.4f - minY + 0.2f + - 0.125f - yOffset;
        float height = minY + (maxY - minY);
        float topMinV = sprite.getV(0);
        float topMaxV = sprite.getV(0.625f);
        float minV = sprite.getV(0);
        float maxV = sprite.getV(1);
        IndustrialReforged.LOGGER.debug("H: {}, min: {}", height, minY);
        //float minV = sprite.getV(MIN_Y), maxV = sprite.getV(height);
        // top
        buffer.addVertex(matrix, 0.375f, height, 0).setColor(r, g, b, alpha).setUv(frontMinU, topMinV).setLight(light).setNormal(0, 1, 0);
        buffer.addVertex(matrix, 0.375f, height, 0.375f).setColor(r, g, b, alpha).setUv(frontMinU, topMaxV).setLight(light).setNormal(0, 1, 0);
        buffer.addVertex(matrix, 0.625f, height, 0.375f).setColor(r, g, b, alpha).setUv(frontMaxU, topMaxV).setLight(light).setNormal(0, 1, 0);
        buffer.addVertex(matrix, 0.625f, height, 0).setColor(r, g, b, alpha).setUv(frontMaxU, topMinV).setLight(light).setNormal(0, 1, 0);

        // back
        buffer.addVertex(matrix, 0.625f, height, 0.25f).setColor(r, g, b, alpha).setUv(frontMinU, minV).setLight(light).setNormal(0, 0, -1);
        buffer.addVertex(matrix, 0.625f, minY, 0.25f).setColor(r, g, b, alpha).setUv(frontMinU, maxV).setLight(light).setNormal(0, 0, -1);
        buffer.addVertex(matrix, 0.375f, minY, 0.25f).setColor(r, g, b, alpha).setUv(frontMaxU, maxV).setLight(light).setNormal(0, 0, -1);
        buffer.addVertex(matrix, 0.375f, height, 0.25f).setColor(r, g, b, alpha).setUv(frontMaxU, minV).setLight(light).setNormal(0, 0, -1);
        // front
        buffer.addVertex(matrix, 0.625f, height, 0.375f).setColor(r, g, b, alpha).setUv(frontMinU, minV).setLight(light).setNormal(0, 0, 1);
        buffer.addVertex(matrix, 0.375f, height, 0.375f).setColor(r, g, b, alpha).setUv(frontMaxU, minV).setLight(light).setNormal(0, 0, 1);
        buffer.addVertex(matrix, 0.375f, minY, 0.375f).setColor(r, g, b, alpha).setUv(frontMaxU, maxV).setLight(light).setNormal(0, 0, 1);
        buffer.addVertex(matrix, 0.625f, minY, 0.375f).setColor(r, g, b, alpha).setUv(frontMinU, maxV).setLight(light).setNormal(0, 0, 1);

        float sideMinU = sprite.getU(0.25f);
        float sideMaxU = sprite.getU(0.5f);
        // side 1
        buffer.addVertex(matrix, 0.375f, height, 0.375f).setColor(r, g, b, alpha).setUv(sideMinU, minV).setLight(light).setNormal(-1, 0, 0);
        buffer.addVertex(matrix, 0.375f, height, 0.25f).setColor(r, g, b, alpha).setUv(sideMaxU, minV).setLight(light).setNormal(-1, 0, 0);
        buffer.addVertex(matrix, 0.375f, minY, 0.25f).setColor(r, g, b, alpha).setUv(sideMaxU, maxV).setLight(light).setNormal(-1, 0, 0);
        buffer.addVertex(matrix, 0.375f, minY, 0.375f).setColor(r, g, b, alpha).setUv(sideMinU, maxV).setLight(light).setNormal(-1, 0, 0);
        // side 2
        buffer.addVertex(matrix, 0.625f, height, 0.375f).setColor(r, g, b, alpha).setUv(sideMinU, minV).setLight(light).setNormal(1, 0, 0);
        buffer.addVertex(matrix, 0.625f, minY, 0.375f).setColor(r, g, b, alpha).setUv(sideMinU, maxV).setLight(light).setNormal(1, 0, 0);
        buffer.addVertex(matrix, 0.625f, minY, 0.25f).setColor(r, g, b, alpha).setUv(sideMaxU, maxV).setLight(light).setNormal(1, 0, 0);
        buffer.addVertex(matrix, 0.625f, height, 0.25f).setColor(r, g, b, alpha).setUv(sideMaxU, minV).setLight(light).setNormal(1, 0, 0);
    }
}
