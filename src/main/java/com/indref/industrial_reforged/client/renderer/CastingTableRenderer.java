package com.indref.industrial_reforged.client.renderer;

import com.indref.industrial_reforged.registries.blockentities.CastingTableBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class CastingTableRenderer implements BlockEntityRenderer<CastingTableBlockEntity> {
    public CastingTableRenderer(BlockEntityRendererProvider.Context ignored) {
    }

    @Override
    public void render(CastingTableBlockEntity castingTableBlockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack[] itemStacks = castingTableBlockEntity.getRenderStacks();
        for (int j = 0; j < itemStacks.length; j++) {
            poseStack.pushPose();
            poseStack.translate(0.5f, 0.75f, 0.5f);
            poseStack.scale(0.78f, 0.78f, 0.78f);
            poseStack.mulPose(Axis.XP.rotationDegrees(90));

            poseStack.mulPose(Axis.ZP.rotationDegrees(0));

            itemRenderer.renderStatic(itemStacks[j], ItemDisplayContext.FIXED, getLightLevel(castingTableBlockEntity.getLevel(),
                            castingTableBlockEntity.getBlockPos()),
                    OverlayTexture.NO_OVERLAY, poseStack, multiBufferSource, castingTableBlockEntity.getLevel(), 1);
            poseStack.popPose();
        }
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
