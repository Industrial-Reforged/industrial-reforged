package com.indref.industrial_reforged.client.model;

import com.indref.industrial_reforged.IndustrialReforged;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class CrucibleModel extends Model {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "crucible"), "main");
    private final ModelPart crucible;
    private final ModelPart leg0;
    private final ModelPart leg1;
    private float rotation;

    public CrucibleModel(ModelPart root) {
        super(RenderType::entitySolid);
        this.crucible = root.getChild("crucible");
        this.leg0 = root.getChild("leg0");
        this.leg1 = root.getChild("leg1");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition crucible = partdefinition.addOrReplaceChild("crucible", CubeListBuilder.create().texOffs(44, 68).addBox(14.0F, -32.0F, -19.0F, 4.0F, 30.0F, 36.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-18.0F, -2.0F, -19.0F, 36.0F, 2.0F, 36.0F, new CubeDeformation(0.0F))
                .texOffs(0, 38).addBox(-18.0F, -32.0F, -19.0F, 4.0F, 30.0F, 36.0F, new CubeDeformation(0.0F))
                .texOffs(44, 50).addBox(18.0F, -27.0F, -3.0F, 7.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(88, 38).addBox(-14.0F, -32.0F, 13.0F, 28.0F, 30.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(108, 0).addBox(-14.0F, -32.0F, -19.0F, 28.0F, 30.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(44, 38).addBox(-25.0F, -27.0F, -3.0F, 7.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 20.0F, 0.0F));

        PartDefinition leg0 = partdefinition.addOrReplaceChild("leg0", CubeListBuilder.create().texOffs(0, 38).addBox(-24.0F, -13.0F, -6.0F, 4.0F, 25.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(70, 52).addBox(-24.0F, -21.0F, 2.0F, 4.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(64, 44).addBox(-24.0F, -21.0F, -4.0F, 4.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(64, 62).addBox(-24.0F, -21.0F, -6.0F, 4.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.0F, 1.0F));

        PartDefinition leg1 = partdefinition.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(0, 0).addBox(-24.0F, -13.0F, -6.0F, 4.0F, 25.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(18, 38).addBox(-24.0F, -21.0F, 2.0F, 4.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(44, 62).addBox(-24.0F, -21.0F, -4.0F, 4.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(18, 0).addBox(-24.0F, -21.0F, -6.0F, 4.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(44.0F, 12.0F, 1.0F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        render(poseStack, buffer, packedLight, packedOverlay, color, 0);
    }

    public void render(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color, float partialTick) {
        if (this.rotation == 90) {
            this.rotation = 0;
        } else {
            this.rotation += 0.025f;
        }
        poseStack.pushPose();
        poseStack.translate(0, 1.75, 0.125);
        poseStack.mulPose(Axis.XP.rotation(this.rotation));
        poseStack.translate(0, -1.75, -0.125);

        poseStack.translate(0, -1, 0.125);
        this.crucible.render(poseStack, buffer, packedLight, packedOverlay, color);
        poseStack.popPose();
        this.leg0.render(poseStack, buffer, packedLight, packedOverlay, color);
        this.leg1.render(poseStack, buffer, packedLight, packedOverlay, color);
    }

    public void setupAnimation(float time) {
        this.crucible.xRot = (float) Math.toRadians(180);
        this.leg0.xRot = (float) Math.toRadians(180);
        this.leg1.xRot = (float) Math.toRadians(180);
    }

}
