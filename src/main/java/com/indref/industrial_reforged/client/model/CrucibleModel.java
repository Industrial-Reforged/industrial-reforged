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
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

public class CrucibleModel extends Model {
    public static final Material CRUCIBLE_LOCATION = new Material(
            InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "entity/crucible")
    );
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "crucible"), "main");
    public final ModelPart crucible;
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

        PartDefinition crucible = partdefinition.addOrReplaceChild("crucible", CubeListBuilder.create().texOffs(38, 64).addBox(13.0F, -32.0F, -17.0F, 3.0F, 30.0F, 32.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-16.0F, -2.0F, -17.0F, 32.0F, 2.0F, 32.0F, new CubeDeformation(0.0F))
                .texOffs(0, 34).addBox(-16.0F, -32.0F, -17.0F, 3.0F, 30.0F, 32.0F, new CubeDeformation(0.0F))
                .texOffs(0, 12).addBox(16.0F, -27.0F, -4.0F, 8.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(76, 34).addBox(-13.0F, -32.0F, 12.0F, 26.0F, 30.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(108, 67).addBox(-13.0F, -32.0F, -17.0F, 26.0F, 30.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-24.0F, -27.0F, -4.0F, 8.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 20.0F, 1.0F));

        PartDefinition leg0 = partdefinition.addOrReplaceChild("leg0", CubeListBuilder.create().texOffs(108, 100).addBox(-24.0F, -13.0F, -6.0F, 4.0F, 25.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(12, 44).addBox(-24.0F, -21.0F, 2.0F, 4.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 34).addBox(-24.0F, -21.0F, -4.0F, 4.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 42).addBox(-24.0F, -21.0F, -6.0F, 4.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 12.0F, 1.0F));

        PartDefinition leg1 = partdefinition.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(0, 96).addBox(-26.0F, -13.0F, -6.0F, 4.0F, 25.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(38, 34).addBox(-26.0F, -21.0F, 2.0F, 4.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 24).addBox(-26.0F, -21.0F, -4.0F, 4.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(20, 34).addBox(-26.0F, -21.0F, -6.0F, 4.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(44.0F, 12.0F, 1.0F));

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
            //this.rotation += 0.025f;
        }

        poseStack.pushPose();
        {
            poseStack.translate(0, 1.75, 0.875);
            poseStack.mulPose(Axis.XP.rotation(this.rotation));
            poseStack.translate(0, -1.75, -0.875);

            poseStack.translate(0, -1, 0.875);
            this.crucible.render(poseStack, buffer, packedLight, packedOverlay, color);
        }
        poseStack.popPose();

        poseStack.pushPose();
        {
            poseStack.translate(0, 0, 0.875);
            this.leg0.render(poseStack, buffer, packedLight, packedOverlay, color);
        }
        poseStack.popPose();

        poseStack.pushPose();
        {
            poseStack.translate(0, 0, 0.875);
            this.leg1.render(poseStack, buffer, packedLight, packedOverlay, color);
        }
        poseStack.popPose();
    }

    public void setupAnimation(float time) {
        this.crucible.xRot = (float) Math.toRadians(180);
        this.leg0.xRot = (float) Math.toRadians(180);
        this.leg1.xRot = (float) Math.toRadians(180);
    }

}
