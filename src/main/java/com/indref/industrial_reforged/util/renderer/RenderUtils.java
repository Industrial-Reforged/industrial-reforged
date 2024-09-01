package com.indref.industrial_reforged.util.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.core.Direction;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class RenderUtils {
    public static void rotateCentered(PoseStack poseStack, Axis axis, float radians) {
        poseStack.translate(.5f, .5f, .5f);
        poseStack.mulPose(axis.rotationDegrees(radians));
        poseStack.translate(-.5f, -.5f, -.5f);
    }

    public static void rotateCentered(PoseStack poseStack, Direction direction, float degrees) {
        Vector3f step = direction.step();
        poseStack.translate(.5f, .5f, .5f);
        poseStack.mulPose(new Quaternionf().setAngleAxis(Math.toRadians(degrees), step.x(), step.y(), step.z()));
        poseStack.translate(-.5f, -.5f, -.5f);
    }

    public static Axis rotAxisFromDir(Direction dir) {
        return switch (dir) {
            case DOWN -> Axis.YN;
            case UP -> Axis.YP;
            case NORTH -> Axis.ZN;
            case EAST -> Axis.ZP;
            case SOUTH -> Axis.XN;
            case WEST -> Axis.XP;
        };
    }
}
