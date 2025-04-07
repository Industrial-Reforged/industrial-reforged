package com.indref.industrial_reforged.util.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

public class IRRenderTypes extends RenderStateShard {
    public static final RenderType TEST_RENDER_TYPE;

    public IRRenderTypes(String pName, Runnable pSetupState, Runnable pClearState) {
        super(pName, pSetupState, pClearState);
    }

    private static RenderType createDefault(String name, VertexFormat format, VertexFormat.Mode mode, RenderType.CompositeState state) {
        return RenderType.create(name, format, mode, 4194304, true, true, state);
    }

    static {
        TEST_RENDER_TYPE = createDefault("industrial_reforged:cube", DefaultVertexFormat.POSITION_COLOR_NORMAL, VertexFormat.Mode.QUADS, RenderType.CompositeState.builder()
                .setShaderState(RenderStateShard.POSITION_COLOR_SHADER)
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setDepthTestState(RenderStateShard.NO_DEPTH_TEST)
                .createCompositeState(true));
    }
}

