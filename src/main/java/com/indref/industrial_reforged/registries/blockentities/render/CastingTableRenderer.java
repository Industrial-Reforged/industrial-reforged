package com.indref.industrial_reforged.registries.blockentities.render;

import com.indref.industrial_reforged.registries.blockentities.CastingTableBlockEntity;
import com.indref.industrial_reforged.registries.items.misc.MoldItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

import java.util.Objects;

public class CastingTableRenderer implements BlockEntityRenderer<CastingTableBlockEntity> {
    public CastingTableRenderer(BlockEntityRendererProvider.Context ignored) {
    }

    @Override
    public void render(CastingTableBlockEntity castingTableBlockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack itemStack = castingTableBlockEntity.getRenderStack();
        poseStack.pushPose();
        poseStack.translate(0.5f, 0.75f, 0.5f);
        if (itemStack.getItem() instanceof MoldItem || itemStack.getItem() instanceof BlockItem) {
            poseStack.scale(0.92f, 0.92f, 0.92f);
        } else {
            poseStack.scale(0.5f, 0.92f, 0.5f);
        }
        poseStack.mulPose(Axis.XP.rotationDegrees(90));

        poseStack.mulPose(Axis.ZP.rotationDegrees(0));

        itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, getLightLevel(castingTableBlockEntity.getLevel(),
                        castingTableBlockEntity.getBlockPos()),
                OverlayTexture.NO_OVERLAY, poseStack, multiBufferSource, castingTableBlockEntity.getLevel(), 1);
        poseStack.popPose();
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
