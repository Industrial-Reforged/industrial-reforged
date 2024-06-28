/*
 * This class is from the Tinkers Construct repository
 * and the code here was written by the TC devs. Credits
 * for this (and item/fluid fading in the casting basin)
 * go to them. Tinkers Construct is licensed under the MIT
 * license
 */

package com.indref.industrial_reforged.util.renderer;

import com.mojang.blaze3d.vertex.VertexConsumer;
import org.jetbrains.annotations.NotNull;

public class TintedVertexBuilder implements VertexConsumer {
    /** Base vertex builder */
    private final VertexConsumer inner;
    /** Tint color from 0-255 */
    private final int tintRed, tintGreen, tintBlue, tintAlpha;

    public TintedVertexBuilder(VertexConsumer inner, int tintRed, int tintGreen, int tintBlue, int tintAlpha) {
        this.inner = inner;
        this.tintRed = tintRed;
        this.tintGreen = tintGreen;
        this.tintBlue = tintBlue;
        this.tintAlpha = tintAlpha;
    }

    @Override
    public @NotNull VertexConsumer addVertex(float v, float v1, float v2) {
        return inner.addVertex(v, v1, v2);
    }

    @Override
    public @NotNull VertexConsumer setColor(int red, int green, int blue, int alpha) {
        return inner.setColor((red * tintRed) / 0xFF, (green * tintGreen) / 0xFF, (blue * tintBlue) / 0xFF, (alpha * tintAlpha) / 0xFF);
    }

    @Override
    public @NotNull VertexConsumer setUv(float u, float v) {
        return inner.setUv(u, v);
    }

    @Override
    public @NotNull VertexConsumer setUv1(int i, int i1) {
        return inner.setUv1(i, i1);
    }

    @Override
    public @NotNull VertexConsumer setUv2(int i, int i1) {
        return inner.setUv2(i, i1);
    }

    @Override
    public @NotNull VertexConsumer setNormal(float v, float v1, float v2) {
        return inner.setNormal(v, v1, v2);
    }
}