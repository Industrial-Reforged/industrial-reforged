package com.indref.industrial_reforged.client.model;

import com.indref.industrial_reforged.IndustrialReforged;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

public class BlastFurnaceItemModel extends Model {
    public static final Material BLAST_FURNACE_LOCATION = new Material(
            InventoryMenu.BLOCK_ATLAS, ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "entity/blast_furnace")
    );
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "blast_furnace"), "main");

    private final ModelPart main;

    public BlastFurnaceItemModel(ModelPart root) {
        super(RenderType::entitySolid);
        this.main = root.getChild("main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(-24.0F, -64.0F, -8.0F, 32.0F, 64.0F, 32.0F, new CubeDeformation(0.0F))
                .texOffs(0, 96).addBox(-24.0F, -68.0F, -8.0F, 32.0F, 4.0F, 32.0F, new CubeDeformation(0.0F))
                .texOffs(128, 0).addBox(-21.0F, -72.0F, -5.0F, 26.0F, 4.0F, 26.0F, new CubeDeformation(0.0F))
                .texOffs(128, 30).addBox(-18.0F, -76.0F, -2.0F, 20.0F, 4.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        this.main.render(poseStack, buffer, packedLight, packedOverlay);
    }
}
