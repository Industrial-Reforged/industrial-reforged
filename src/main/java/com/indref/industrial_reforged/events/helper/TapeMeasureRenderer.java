package com.indref.industrial_reforged.events.helper;

import com.indref.industrial_reforged.IndustrialReforged;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;
import org.joml.Matrix4f;

import java.util.List;

public class TapeMeasureRenderer {

    public static void renderDistance(PoseStack poseStack, VertexConsumer vertexConsumer, Vec3 renderView, Player player,
                                      BlockHitResult blockHitResult, BlockPos firstPos) {
        // This code is mostly from quark's abacus renderer.
        // Credits to vazkii, the violetmoon team and all contributors
        VoxelShape shape = Shapes.create(new AABB(firstPos));
        BlockPos curPos = blockHitResult.getBlockPos();

        int xDistance = curPos.getX() - firstPos.getX();
        int yDistance = curPos.getY() - firstPos.getY();
        int zDistance = curPos.getZ() - firstPos.getZ();

        if (xDistance != 0)
            shape = Shapes.or(shape, Shapes.create(new AABB(firstPos).expandTowards(xDistance, 0, 0)));
        if (yDistance != 0)
            shape = Shapes.or(shape, Shapes.create(new AABB(firstPos.offset(xDistance, 0, 0)).expandTowards(0, yDistance, 0)));
        if (zDistance != 0)
            shape = Shapes.or(shape, Shapes.create(new AABB(firstPos.offset(xDistance, yDistance, 0)).expandTowards(0, 0, zDistance)));

        List<AABB> list = shape.toAabbs();

        double xIn = -renderView.x;
        double yIn = -renderView.y;
        double zIn = -renderView.z;

        player.displayClientMessage(
                Component.literal(String.format("%d, %d, %d",
                        Math.abs(firstPos.getX()) - Math.abs(curPos.getX()),
                        -(Math.abs(firstPos.getY()) - Math.abs(curPos.getY())),
                        Math.abs(firstPos.getZ()) - Math.abs(curPos.getZ()))
                ),
                true
        );

        renderAABBs(list, poseStack, vertexConsumer, xIn, yIn, zIn);
    }

    private static void renderAABBs(List<AABB> aabbs, PoseStack poseStack, VertexConsumer consumer, double xIn, double yIn, double zIn) {
        for (AABB aabb : aabbs) {
            float r = 0;
            float g = 1;
            float b = 0;
            float a = 0.4f;

            VoxelShape individual = Shapes.create(aabb.move(0.0D, 0.0D, 0.0D));
            PoseStack.Pose pose = poseStack.last();
            Matrix4f matrix4f = pose.pose();
            individual.forAllEdges((minX, minY, minZ, maxX, maxY, maxZ) -> {
                float f = (float) (maxX - minX);
                float f1 = (float) (maxY - minY);
                float f2 = (float) (maxZ - minZ);
                float f3 = Mth.sqrt(f * f + f1 * f1 + f2 * f2);
                f /= f3;
                f1 /= f3;
                f2 /= f3;

                consumer.addVertex(matrix4f, (float) (minX + xIn), (float) (minY + yIn), (float) (minZ + zIn)).setColor(r, g, b, a).setNormal(pose, f, f1, f2);
                consumer.addVertex(matrix4f, (float) (maxX + xIn), (float) (maxY + yIn), (float) (maxZ + zIn)).setColor(r, g, b, a).setNormal(pose, f, f1, f2);
            });
        }
    }
}
