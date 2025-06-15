package com.indref.industrial_reforged.client.renderer.blockentity;

import com.indref.industrial_reforged.content.blockentities.WoodenBasinBlockEntity;
import com.indref.industrial_reforged.content.blocks.machines.primitive.CastingBasinBlock;
import com.indref.industrial_reforged.content.recipes.WoodenBasinRecipe;
import com.indref.industrial_reforged.util.renderer.IRRenderTypes;
import com.mojang.blaze3d.systems.RenderSystem;
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

import static net.minecraft.client.renderer.LightTexture.FULL_BRIGHT;

public class WoodenBasinRenderer implements BlockEntityRenderer<WoodenBasinBlockEntity> {
    private static final float SIDE_MARGIN = (float) CastingBasinBlock.SHAPE.min(Direction.Axis.X) + 0.1f;
    public static final float MIN_Y = 2.1f / 16f;
    public static final float MAX_Y = 0.4f - MIN_Y;

    public WoodenBasinRenderer(BlockEntityRendererProvider.Context ignored) {
    }

    @Override
    public void render(WoodenBasinBlockEntity blockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int combinedLight, int combinedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        IItemHandler itemHandler = blockEntity.getItemHandler();
        IFluidHandler fluidHandler = blockEntity.getFluidHandler();
        WoodenBasinRecipe recipe = blockEntity.getRecipe();
        ItemStack ingredientItem = itemHandler.getStackInSlot(0);
        FluidStack inputFluidStack = fluidHandler.getFluidInTank(0);
        FluidStack resultFluid = recipe != null ? recipe.resultFluid() : fluidHandler.getFluidInTank(1);

        // This code for fading item and fluid texture is from Tinkers construct.
        // Thank you to the tinkers construct devs for this
        float duration = blockEntity.getDuration();
        float maxDuration = blockEntity.getMaxDuration();
        int fluidOpacity = 0xFF;
        int resultFluidOpactiy = 0;
        if (duration > 0 && maxDuration > 0) {
            int opacity = (int) ((4 * 0xFF) * duration / maxDuration);
            // fade result fluid in
            resultFluidOpactiy = opacity / 4;

            // fade fluid and temperature out during last 10%
            if (opacity > 3 * 0xFF) {
                fluidOpacity = (4 * 0xFF) - opacity;
            }
        }

        Direction direction = blockEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
        poseStack.translate(.5f, .5f, .5f);
        poseStack.mulPose(Axis.YN.rotationDegrees(direction.toYRot()));
        poseStack.translate(-.5f, -.5f, -.5f);

        int alpha = 1;

        renderItem(ingredientItem, blockEntity, poseStack, multiBufferSource, itemRenderer);

        if (!inputFluidStack.isEmpty()) {
            int fluidCapacity = fluidHandler.getTankCapacity(0);
            float fillPercentage = Math.min(1, (float) inputFluidStack.getAmount() / fluidCapacity) / 2;

            renderFluid(poseStack, multiBufferSource, inputFluidStack, alpha, fillPercentage, combinedLight, fluidOpacity);
        }

        if (!resultFluid.isEmpty()) {
            int fluidCapacity = fluidHandler.getTankCapacity(1);
            float fillPercentage = Math.min(1, (float) resultFluid.getAmount() / fluidCapacity) / 2;

            renderFluid(poseStack, multiBufferSource, resultFluid, alpha, fillPercentage, combinedLight, resultFluidOpactiy);
        }


    }

    private void renderItem(ItemStack item, WoodenBasinBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, ItemRenderer itemRenderer) {
        poseStack.pushPose();
        {
            poseStack.translate(0.5f, 0.24f, 0.5f);
            poseStack.scale(0.78f, 0.78f, 0.78f);
            poseStack.mulPose(Axis.XP.rotationDegrees(90));

            poseStack.mulPose(Axis.ZP.rotationDegrees(0));

            itemRenderer.renderStatic(item, ItemDisplayContext.FIXED, FULL_BRIGHT,
                    OverlayTexture.NO_OVERLAY, poseStack, multiBufferSource, blockEntity.getLevel(), 1);
        }
        poseStack.popPose();
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }

    private static void renderFluid(PoseStack poseStack, MultiBufferSource bufferSource, FluidStack fluidStack, float alpha, float heightPercentage, int combinedLight, int opacity) {
        VertexConsumer vertexBuilder = bufferSource.getBuffer(IRRenderTypes.FLUID);
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
        float height = (2f / 16f) + (heightPercentage * (7f / 16f));
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
        minV = sprite.getV(SIDE_MARGIN);
        maxV = sprite.getV(1 - SIDE_MARGIN);
        buffer.addVertex(matrix, SIDE_MARGIN, height, SIDE_MARGIN).setColor(r, g, b, alpha).setUv(minU, minV).setLight(light).setNormal(0, 1, 0);
        buffer.addVertex(matrix, SIDE_MARGIN, height, 1 - SIDE_MARGIN).setColor(r, g, b, alpha).setUv(minU, maxV).setLight(light).setNormal(0, 1, 0);
        buffer.addVertex(matrix, 1 - SIDE_MARGIN, height, 1 - SIDE_MARGIN).setColor(r, g, b, alpha).setUv(maxU, maxV).setLight(light).setNormal(0, 1, 0);
        buffer.addVertex(matrix, 1 - SIDE_MARGIN, height, SIDE_MARGIN).setColor(r, g, b, alpha).setUv(maxU, minV).setLight(light).setNormal(0, 1, 0);

    }
}
