package com.indref.industrial_reforged.client.renderer.blockentity;

import com.indref.industrial_reforged.content.blockentities.CastingBasinBlockEntity;
import com.indref.industrial_reforged.content.blocks.machines.primitive.CastingBasinBlock;
import com.indref.industrial_reforged.content.recipes.BasinCastingRecipe;
import com.indref.industrial_reforged.data.maps.CastingMoldValue;
import com.indref.industrial_reforged.util.capabilities.CapabilityUtils;
import com.indref.industrial_reforged.util.renderer.CastingItemRenderTypeBuffer;
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
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.joml.Matrix4f;

public class CastingBasinRenderer implements BlockEntityRenderer<CastingBasinBlockEntity> {
    private static final float SIDE_MARGIN = (float) CastingBasinBlock.SHAPE.min(Direction.Axis.X) + 0.1f;
    public static final float MIN_Y = 2.1f / 16f;
    public static final float MAX_Y = 0.4f - MIN_Y;

    public CastingBasinRenderer(BlockEntityRendererProvider.Context ignored) {
    }

    @Override
    public void render(CastingBasinBlockEntity blockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int combinedLight, int combinedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        IItemHandler itemHandler = blockEntity.getItemHandler();
        BasinCastingRecipe recipe = blockEntity.getRecipe();
        ItemStack moldItem = itemHandler.getStackInSlot(0);
        CastingMoldValue mold = CastingBasinBlockEntity.getMold(moldItem.getItem());
        ItemStack resultItem = recipe != null
                && ((mold != null
                && blockEntity.getFluidHandler().getFluidInTank(0).getAmount() == mold.capacity()) || CastingBasinBlockEntity.isMoldIngredient(moldItem)) ? recipe.getResultItem(null) : itemHandler.getStackInSlot(1);

        // This code for fading item and fluid texture is from Tinkers construct.
        // Thank you to the tinkers construct devs for this
        int duration = blockEntity.getDuration();
        int maxDuration = blockEntity.getMaxDuration();
        int fluidOpacity = 0xFF;
        int itemOpacity = 0;
        if (duration > 0 && maxDuration > 0) {
            int opacity = (4 * 0xFF) * duration / maxDuration;
            // fade item in
            itemOpacity = opacity / 4;

            // fade fluid and temperature out during last 10%
            if (opacity > 3 * 0xFF) {
                fluidOpacity = (4 * 0xFF) - opacity;
            }
        }

        Direction direction = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        poseStack.translate(.5f, .5f, .5f);
        poseStack.mulPose(Axis.YN.rotationDegrees(direction.toYRot()));
        poseStack.translate(-.5f, -.5f, -.5f);

        renderItem(moldItem, blockEntity, poseStack, multiBufferSource, 0, fluidOpacity, itemRenderer);
        renderItem(resultItem, blockEntity, poseStack, multiBufferSource, itemOpacity, fluidOpacity, itemRenderer);

        IFluidHandler fluidHandler = CapabilityUtils.fluidHandlerCapability(blockEntity);
        if (fluidHandler != null) {
            FluidStack fluidStack = fluidHandler.getFluidInTank(0);
            int fluidCapacity = fluidHandler.getTankCapacity(0);
            int alpha = 1;

            if (fluidStack.isEmpty()) {
                fluidStack = blockEntity.getRememberedFluid();
                if (fluidStack.isEmpty()) {
                    return;
                }
            }


            float fillPercentage = Math.min(1, (float) fluidStack.getAmount() / fluidCapacity) / 2;

            if (fluidStack.getFluid().getFluidType().isLighterThanAir())
                renderFluid(poseStack, multiBufferSource, fluidStack, fillPercentage, 1, combinedLight, fluidOpacity);
            else
                renderFluid(poseStack, multiBufferSource, fluidStack, alpha, fillPercentage, combinedLight, fluidOpacity);

        }
    }

    private void renderItem(ItemStack item, CastingBasinBlockEntity castingTableBlockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int itemOpacity, int fluidOpacity, ItemRenderer itemRenderer) {
        poseStack.pushPose();
        {
            poseStack.translate(0.5f, 0.18f, 0.5f);
            poseStack.scale(0.78f, 0.78f, 0.78f);
            poseStack.mulPose(Axis.XP.rotationDegrees(90));

            poseStack.mulPose(Axis.ZP.rotationDegrees(0));

            // This code for fading the item texture is from Tinkers construct.
            // Thank you to the tinkers construct devs for implementing this
            MultiBufferSource outputBuffer = multiBufferSource;
            if (itemOpacity > 0) {
                // apply a buffer wrapper to tint and add opacity
                outputBuffer = new CastingItemRenderTypeBuffer(multiBufferSource, itemOpacity, fluidOpacity);
            }

            itemRenderer.renderStatic(item, ItemDisplayContext.FIXED, getLightLevel(castingTableBlockEntity.getLevel(),
                            castingTableBlockEntity.getBlockPos()),
                    OverlayTexture.NO_OVERLAY, poseStack, outputBuffer, castingTableBlockEntity.getLevel(), 1);
        }
        poseStack.popPose();
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }

    private static void renderFluid(PoseStack poseStack, MultiBufferSource bufferSource, FluidStack fluidStack, float alpha, float heightPercentage, int combinedLight, int opacity) {
        VertexConsumer vertexBuilder = bufferSource.getBuffer(RenderType.translucent());
        IClientFluidTypeExtensions fluidTypeExtensions = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidTypeExtensions.getStillTexture(fluidStack));
        int color = fluidTypeExtensions.getTintColor();
        if (opacity < 0xFF) {
            // alpha is top 8 bits, multiply by opacity and divide out remainder
            int alpha1 = ((color >> 24) & 0xFF) * opacity / 0xFF;
            // clear bits in color and or in the new alpha
            color = (color & 0xFFFFFF) | (alpha1 << 24);
        }
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
        buffer.addVertex(matrix, SIDE_MARGIN, MIN_Y, SIDE_MARGIN).setColor(r, g, b, alpha).setUv(minU, minV).setLight(light).setNormal(0, 0, -1);
        buffer.addVertex(matrix, SIDE_MARGIN, height, SIDE_MARGIN).setColor(r, g, b, alpha).setUv(minU, maxV).setLight(light).setNormal(0, 0, -1);
        buffer.addVertex(matrix, 1 - SIDE_MARGIN, height, SIDE_MARGIN).setColor(r, g, b, alpha).setUv(maxU, maxV).setLight(light).setNormal(0, 0, -1);
        buffer.addVertex(matrix, 1 - SIDE_MARGIN, MIN_Y, SIDE_MARGIN).setColor(r, g, b, alpha).setUv(maxU, minV).setLight(light).setNormal(0, 0, -1);
        // max z
        buffer.addVertex(matrix, SIDE_MARGIN, MIN_Y, 1 - SIDE_MARGIN).setColor(r, g, b, alpha).setUv(minU, minV).setLight(light).setNormal(0, 0, 1);
        buffer.addVertex(matrix, 1 - SIDE_MARGIN, MIN_Y, 1 - SIDE_MARGIN).setColor(r, g, b, alpha).setUv(maxU, minV).setLight(light).setNormal(0, 0, 1);
        buffer.addVertex(matrix, 1 - SIDE_MARGIN, height, 1 - SIDE_MARGIN).setColor(r, g, b, alpha).setUv(maxU, maxV).setLight(light).setNormal(0, 0, 1);
        buffer.addVertex(matrix, SIDE_MARGIN, height, 1 - SIDE_MARGIN).setColor(r, g, b, alpha).setUv(minU, maxV).setLight(light).setNormal(0, 0, 1);
        // min x
        buffer.addVertex(matrix, SIDE_MARGIN, MIN_Y, SIDE_MARGIN).setColor(r, g, b, alpha).setUv(minU, minV).setLight(light).setNormal(-1, 0, 0);
        buffer.addVertex(matrix, SIDE_MARGIN, MIN_Y, 1 - SIDE_MARGIN).setColor(r, g, b, alpha).setUv(maxU, minV).setLight(light).setNormal(-1, 0, 0);
        buffer.addVertex(matrix, SIDE_MARGIN, height, 1 - SIDE_MARGIN).setColor(r, g, b, alpha).setUv(maxU, maxV).setLight(light).setNormal(-1, 0, 0);
        buffer.addVertex(matrix, SIDE_MARGIN, height, SIDE_MARGIN).setColor(r, g, b, alpha).setUv(minU, maxV).setLight(light).setNormal(-1, 0, 0);
        // max x
        buffer.addVertex(matrix, 1 - SIDE_MARGIN, MIN_Y, SIDE_MARGIN).setColor(r, g, b, alpha).setUv(minU, minV).setLight(light).setNormal(1, 0, 0);
        buffer.addVertex(matrix, 1 - SIDE_MARGIN, height, SIDE_MARGIN).setColor(r, g, b, alpha).setUv(minU, maxV).setLight(light).setNormal(1, 0, 0);
        buffer.addVertex(matrix, 1 - SIDE_MARGIN, height, 1 - SIDE_MARGIN).setColor(r, g, b, alpha).setUv(maxU, maxV).setLight(light).setNormal(1, 0, 0);
        buffer.addVertex(matrix, 1 - SIDE_MARGIN, MIN_Y, 1 - SIDE_MARGIN).setColor(r, g, b, alpha).setUv(maxU, minV).setLight(light).setNormal(1, 0, 0);
        // top
        if (heightPercentage < 1) {
            minV = sprite.getV(SIDE_MARGIN);
            maxV = sprite.getV(1 - SIDE_MARGIN);
            buffer.addVertex(matrix, SIDE_MARGIN, height, SIDE_MARGIN).setColor(r, g, b, alpha).setUv(minU, minV).setLight(light).setNormal(0, 1, 0);
            buffer.addVertex(matrix, SIDE_MARGIN, height, 1 - SIDE_MARGIN).setColor(r, g, b, alpha).setUv(minU, maxV).setLight(light).setNormal(0, 1, 0);
            buffer.addVertex(matrix, 1 - SIDE_MARGIN, height, 1 - SIDE_MARGIN).setColor(r, g, b, alpha).setUv(maxU, maxV).setLight(light).setNormal(0, 1, 0);
            buffer.addVertex(matrix, 1 - SIDE_MARGIN, height, SIDE_MARGIN).setColor(r, g, b, alpha).setUv(maxU, minV).setLight(light).setNormal(0, 1, 0);
        }
    }
}
