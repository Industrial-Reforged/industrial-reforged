package com.indref.industrial_reforged.client.renderer.blockentity;

import com.indref.industrial_reforged.content.blockentities.WoodenBasinBlockEntity;
import com.indref.industrial_reforged.content.blocks.machines.primitive.CastingBasinBlock;
import com.indref.industrial_reforged.content.blocks.machines.primitive.WoodenBasinBlock;
import com.indref.industrial_reforged.content.recipes.WoodenBasinRecipe;
import com.indref.industrial_reforged.util.renderer.IRRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.joml.Matrix4f;

import java.util.Random;

import static net.minecraft.client.renderer.LightTexture.FULL_BRIGHT;

public class WoodenBasinRenderer implements BlockEntityRenderer<WoodenBasinBlockEntity> {
    private static final float SIDE_MARGIN = (float) CastingBasinBlock.SHAPE.min(Direction.Axis.X) + 0.1f;

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
        poseStack.pushPose();
        {
            poseStack.translate(.5f, .5f, .5f);
            poseStack.mulPose(Axis.YN.rotationDegrees(direction.toYRot()));
            poseStack.translate(-.5f, -.5f, -.5f);

            int alpha = 1;

            float baseFluidHeight = 0.025f; // default when no fluid
            float surfaceHeight = baseFluidHeight;
            float floatOffset = 0;

            FluidStack renderedFluid = getRenderedFluid(blockEntity);
            float fluidHeight = getFluidHeight(blockEntity);

            if (!renderedFluid.isEmpty()) {
                poseStack.pushPose();
                {
                    poseStack.translate(0, (2 / 16f) - 0.01f, 0);
                    renderFluid(blockEntity, poseStack, multiBufferSource, renderedFluid, alpha, fluidHeight, combinedLight, fluidOpacity);
                }
                poseStack.popPose();
            }

// --- Floating Offset (sine wave) ---
            if (!inputFluidStack.isEmpty() || !resultFluid.isEmpty()) {
                float time = (blockEntity.getLevel().getGameTime() + v) % 360;
                floatOffset = (float) Math.sin(time * 0.1f) * 0.015f;
                surfaceHeight = Math.max(baseFluidHeight / 2, surfaceHeight - 7f / 16f);
            } else {
                surfaceHeight -= 2f / 16f;
            }

// --- Item Rendering ---
            if (!ingredientItem.isEmpty()) {
                int maxItems = 3;
                int itemCount = ingredientItem.getCount();
                int stackSize = Math.max(ingredientItem.getMaxStackSize(), 16);
                int renderedItems = Math.max(1, (int) Math.min(((double) itemCount / stackSize + 0.3) * 3, maxItems));

                Random rand = new Random(blockEntity.getBlockPos().asLong()); // consistent randomness

                for (int i = 0; i < renderedItems; i++) {
                    poseStack.pushPose();
                    {
                        // Base position
                        poseStack.translate(0.5, 0.5, 0.5);
                        poseStack.mulPose(Axis.XN.rotationDegrees(2));
                        poseStack.mulPose(Axis.ZN.rotationDegrees(2));
                        poseStack.scale(0.85f, 0.85f, 0.85f);
                        poseStack.translate(-0.5, -0.5, -0.5);

                        // Y translation with item "submersion"
                        float submergeOffset = i * 0.075f; // deeper per item
                        float y = surfaceHeight + floatOffset + submergeOffset;
                        poseStack.translate(0.05, y, 0);

                        // Directional offset
                        if (direction == Direction.WEST) {
                            poseStack.translate(-0.125, 0, 0);
                        } else if (direction == Direction.SOUTH) {
                            poseStack.translate(-0.045, 0, 0);
                        }

                        // Random jitter
                        poseStack.translate((rand.nextFloat() - 0.5f) / 8f, 0, (rand.nextFloat() - 0.5f) / 8f);

                        renderItem(ingredientItem, blockEntity, poseStack, multiBufferSource, itemRenderer);
                    }
                    poseStack.popPose();
                }
            }
        }
        poseStack.popPose();
    }

    private float getFluidHeight(WoodenBasinBlockEntity be) {
        FluidStack renderedFluid = getRenderedFluid(be);

        int tankCapacity = be.getFluidHandler().getTankCapacity(0);

        return ((float) renderedFluid.getAmount() / tankCapacity) * (4f / 16f);
    }

    private FluidStack getRenderedFluid(WoodenBasinBlockEntity be) {
        IFluidHandler fluidHandler = be.getFluidHandler();
        WoodenBasinRecipe recipe = be.getRecipe();

        FluidStack inputFluidStack = fluidHandler.getFluidInTank(0);
        FluidStack resultFluid = recipe != null ? recipe.resultFluid().copyWithAmount(fluidHandler.getFluidInTank(1).getAmount()) : fluidHandler.getFluidInTank(1);
        if (inputFluidStack.getAmount() > resultFluid.getAmount()) {
            return inputFluidStack;
        }
        return resultFluid;
    }

    private void renderItem(ItemStack item, WoodenBasinBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, ItemRenderer itemRenderer) {
        poseStack.pushPose();
        {
            poseStack.translate(0.5f, 0.18f, 0.5f);
            poseStack.scale(0.78f, 0.78f, 0.78f);
            poseStack.mulPose(Axis.XP.rotationDegrees(90));

            poseStack.mulPose(Axis.ZP.rotationDegrees(0));

            itemRenderer.renderStatic(item, ItemDisplayContext.FIXED, FULL_BRIGHT,
                    OverlayTexture.NO_OVERLAY, poseStack, multiBufferSource, blockEntity.getLevel(), 1);
        }
        poseStack.popPose();
    }

    private static void renderFluid(WoodenBasinBlockEntity be, PoseStack poseStack, MultiBufferSource bufferSource, FluidStack fluidStack, float alpha, float fluidHeight, int combinedLight, int opacity) {
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

        renderQuads(be, poseStack.last().pose(), vertexBuilder, sprite, red, green, blue, alpha, fluidHeight, combinedLight);
    }

    private static void renderQuads(WoodenBasinBlockEntity be, Matrix4f matrix, VertexConsumer buffer, TextureAtlasSprite sprite, float r, float g, float b, float alpha, float height, int light) {
        float minU = sprite.getU(SIDE_MARGIN), maxU = sprite.getU((1 - SIDE_MARGIN));
        int blockLight = be.getLevel().getBrightness(LightLayer.BLOCK, be.getBlockPos()); // 0–15
        int skyLight = be.getLevel().getBrightness(LightLayer.SKY, be.getBlockPos());     // 0–15

        int u = blockLight * 16;
        int v = skyLight * 16;
        // top
        float minV = sprite.getV(SIDE_MARGIN);
        float maxV = sprite.getV(1 - SIDE_MARGIN);
        buffer.addVertex(matrix, SIDE_MARGIN, height, SIDE_MARGIN).setColor(r, g, b, alpha).setUv(minU, minV).setUv1(u, v).setLight(light).setNormal(0, 1, 0);
        buffer.addVertex(matrix, SIDE_MARGIN, height, 1 - SIDE_MARGIN).setColor(r, g, b, alpha).setUv(minU, maxV).setUv1(u, v).setLight(light).setNormal(0, 1, 0);
        buffer.addVertex(matrix, 1 - SIDE_MARGIN, height, 1 - SIDE_MARGIN).setColor(r, g, b, alpha).setUv(maxU, maxV).setUv1(u, v).setLight(light).setNormal(0, 1, 0);
        buffer.addVertex(matrix, 1 - SIDE_MARGIN, height, SIDE_MARGIN).setColor(r, g, b, alpha).setUv(maxU, minV).setUv1(u, v).setLight(light).setNormal(0, 1, 0);
    }

}
