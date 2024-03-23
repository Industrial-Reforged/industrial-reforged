package com.indref.industrial_reforged.client.renderer;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.CastingTableBlockEntity;
import com.indref.industrial_reforged.registries.blocks.CastingTableBlock;
import com.indref.industrial_reforged.util.BlockUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.joml.Matrix4f;

import java.util.Optional;

public class CastingTableRenderer implements BlockEntityRenderer<CastingTableBlockEntity> {
    private static final float SIDE_MARGIN = (float) CastingTableBlock.SHAPE.min(Direction.Axis.X) + 0.1f,
            MIN_Y = 10 / 16f,
            MAX_Y = 1.61f - MIN_Y;

    public CastingTableRenderer(BlockEntityRendererProvider.Context ignored) {
    }

    @Override
    public void render(CastingTableBlockEntity castingTableBlockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int combinedLight, int combinedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack[] itemStacks = castingTableBlockEntity.getRenderStacks();
        for (ItemStack itemStack : itemStacks) {
            poseStack.pushPose();
            poseStack.translate(0.5f, 0.80f, 0.5f);
            poseStack.scale(0.78f, 0.78f, 0.78f);
            poseStack.mulPose(Axis.XP.rotationDegrees(90));

            poseStack.mulPose(Axis.ZP.rotationDegrees(0));

            itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, getLightLevel(castingTableBlockEntity.getLevel(),
                            castingTableBlockEntity.getBlockPos()),
                    OverlayTexture.NO_OVERLAY, poseStack, multiBufferSource, castingTableBlockEntity.getLevel(), 1);
            poseStack.popPose();
        }

        try {
            IFluidHandler fluidHandler = BlockUtils.getBlockEntityCapability(Capabilities.FluidHandler.BLOCK, castingTableBlockEntity);
            FluidStack fluidStack = fluidHandler.getFluidInTank(0);
            int fluidCapacity = fluidHandler.getTankCapacity(0);
            int alpha = 1;
            float progress = (float) castingTableBlockEntity.getDuration() / castingTableBlockEntity.getMaxDuration();

            if (fluidStack.isEmpty())
                return;

            IndustrialReforged.LOGGER.debug("Progress: {}", progress);

            float fillPercentage = Math.min(1, (float) fluidStack.getAmount() / fluidCapacity) / 2;
            if (fluidStack.getFluid().getFluidType().isLighterThanAir())
                renderFluid(poseStack, multiBufferSource, fluidStack, fillPercentage, 1, combinedLight);
            else
                renderFluid(poseStack, multiBufferSource, fluidStack, alpha, fillPercentage, combinedLight);

        } catch (Exception ignored) {
        }
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }

    private static void renderFluid(PoseStack poseStack, MultiBufferSource bufferSource, FluidStack fluidStack, float alpha, float heightPercentage, int combinedLight) {
        VertexConsumer vertexBuilder = bufferSource.getBuffer(RenderType.translucent());
        IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(fluidTypeExtensions.getStillTexture(fluidStack));
        int color = fluidTypeExtensions.getTintColor();
        alpha *= (color >> 24 & 255) / 255f;
        float red = (color >> 16 & 255) / 255f;
        float green = (color >> 8 & 255) / 255f;
        float blue = (color & 255) / 255f;

        renderQuads(poseStack.last().pose(), vertexBuilder, sprite, red, green, blue, alpha, heightPercentage, combinedLight);
    }

    private static void renderQuads(Matrix4f matrix, VertexConsumer buffer, TextureAtlasSprite sprite, float r, float g, float b, float alpha, float heightPercentage, int light) {
        float height = MIN_Y + (MAX_Y - MIN_Y) * heightPercentage;
        float minU = sprite.getU(SIDE_MARGIN), maxU = sprite.getU((1 - SIDE_MARGIN));
        float minV = sprite.getV(MIN_Y), maxV = sprite.getV(height);
        // min z
        buffer.vertex(matrix, SIDE_MARGIN, MIN_Y, SIDE_MARGIN).color(r, g, b, alpha).uv(minU, minV).uv2(light).normal(0, 0, -1).endVertex();
        buffer.vertex(matrix, SIDE_MARGIN, height, SIDE_MARGIN).color(r, g, b, alpha).uv(minU, maxV).uv2(light).normal(0, 0, -1).endVertex();
        buffer.vertex(matrix, 1 - SIDE_MARGIN, height, SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, maxV).uv2(light).normal(0, 0, -1).endVertex();
        buffer.vertex(matrix, 1 - SIDE_MARGIN, MIN_Y, SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, minV).uv2(light).normal(0, 0, -1).endVertex();
        // max z
        buffer.vertex(matrix, SIDE_MARGIN, MIN_Y, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(minU, minV).uv2(light).normal(0, 0, 1).endVertex();
        buffer.vertex(matrix, 1 - SIDE_MARGIN, MIN_Y, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, minV).uv2(light).normal(0, 0, 1).endVertex();
        buffer.vertex(matrix, 1 - SIDE_MARGIN, height, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, maxV).uv2(light).normal(0, 0, 1).endVertex();
        buffer.vertex(matrix, SIDE_MARGIN, height, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(minU, maxV).uv2(light).normal(0, 0, 1).endVertex();
        // min x
        buffer.vertex(matrix, SIDE_MARGIN, MIN_Y, SIDE_MARGIN).color(r, g, b, alpha).uv(minU, minV).uv2(light).normal(-1, 0, 0).endVertex();
        buffer.vertex(matrix, SIDE_MARGIN, MIN_Y, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, minV).uv2(light).normal(-1, 0, 0).endVertex();
        buffer.vertex(matrix, SIDE_MARGIN, height, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, maxV).uv2(light).normal(-1, 0, 0).endVertex();
        buffer.vertex(matrix, SIDE_MARGIN, height, SIDE_MARGIN).color(r, g, b, alpha).uv(minU, maxV).uv2(light).normal(-1, 0, 0).endVertex();
        // max x
        buffer.vertex(matrix, 1 - SIDE_MARGIN, MIN_Y, SIDE_MARGIN).color(r, g, b, alpha).uv(minU, minV).uv2(light).normal(1, 0, 0).endVertex();
        buffer.vertex(matrix, 1 - SIDE_MARGIN, height, SIDE_MARGIN).color(r, g, b, alpha).uv(minU, maxV).uv2(light).normal(1, 0, 0).endVertex();
        buffer.vertex(matrix, 1 - SIDE_MARGIN, height, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, maxV).uv2(light).normal(1, 0, 0).endVertex();
        buffer.vertex(matrix, 1 - SIDE_MARGIN, MIN_Y, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, minV).uv2(light).normal(1, 0, 0).endVertex();
        // top
        if (heightPercentage < 1) {
            minV = sprite.getV(SIDE_MARGIN);
            maxV = sprite.getV(1 - SIDE_MARGIN);
            buffer.vertex(matrix, SIDE_MARGIN, height, SIDE_MARGIN).color(r, g, b, alpha).uv(minU, minV).uv2(light).normal(0, 1, 0).endVertex();
            buffer.vertex(matrix, SIDE_MARGIN, height, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(minU, maxV).uv2(light).normal(0, 1, 0).endVertex();
            buffer.vertex(matrix, 1 - SIDE_MARGIN, height, 1 - SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, maxV).uv2(light).normal(0, 1, 0).endVertex();
            buffer.vertex(matrix, 1 - SIDE_MARGIN, height, SIDE_MARGIN).color(r, g, b, alpha).uv(maxU, minV).uv2(light).normal(0, 1, 0).endVertex();
        }
    }
}
