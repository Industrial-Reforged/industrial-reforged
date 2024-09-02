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

    public static String axisToString(Axis axis) {
        if (axis == Axis.XN) return "xn";
        else if (axis == Axis.XP) return "xp";
        else if (axis == Axis.ZN) return "zn";
        else if (axis == Axis.ZP) return "zp";
        else if (axis == Axis.YN) return "yn";
        else if (axis == Axis.YP) return "yp";
        else throw new IllegalArgumentException("Invalid axis");
    }

}
