package com.indref.industrial_reforged.util.renderer;

import com.indref.industrial_reforged.IndustrialReforged;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public final class GuiUtils {
    public static final ResourceLocation BATTERY_SLOT = ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "textures/gui/tanks/battery_slot.png");

    public static void drawImg(GuiGraphics guiGraphics, ResourceLocation texturePath, int x, int y, int width, int height) {
        guiGraphics.blit(texturePath, x, y, 0, 0, 0, width, height, width, height);
    }

    public static void drawWithZ(GuiGraphics guiGraphics, ResourceLocation texturePath, int x, int y, int z, int width, int height) {
        RenderSystem.setShaderTexture(0, texturePath);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(0, 0, z);
        Matrix4f matrix4f = poseStack.last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.addVertex(matrix4f, (float) x, (float) y, (float) 0).setUv(((float) 0 + 0.0F) / (float) width, ((float) 0 + 0.0F) / (float) height);
        bufferbuilder.addVertex(matrix4f, (float) x, (float) (y + height), (float) 0).setUv(((float) 0 + 0.0F) / (float) width, ((float) 0 + (float) height) / (float) height);
        bufferbuilder.addVertex(matrix4f, (float) (x + width), (float) (y + height), (float) 0).setUv(((float) 0 + (float) width) / (float) width, ((float) 0 + (float) height) / (float) height);
        bufferbuilder.addVertex(matrix4f, (float) (x + width), (float) y, (float) 0).setUv(((float) 0 + (float) width) / (float) width, ((float) 0 + 0.0F) / (float) height);
        BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
        poseStack.popPose();
    }

    public static void renderBatterySlot(GuiGraphics guiGraphics, int x, int y) {
        drawImg(guiGraphics, BATTERY_SLOT, x, y, 18, 18);
    }

}
