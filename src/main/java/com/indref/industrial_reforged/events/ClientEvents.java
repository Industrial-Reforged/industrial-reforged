package com.indref.industrial_reforged.events;

import com.indref.industrial_reforged.IRRegistries;
import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.transportation.NetworkNode;
import com.indref.industrial_reforged.api.transportation.TransportNetwork;
import com.indref.industrial_reforged.client.renderer.debug.NetworkNodeRenderer;
import com.indref.industrial_reforged.client.transportation.ClientNodes;
import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.data.components.ComponentBlueprint;
import com.mojang.blaze3d.vertex.PoseStack;
import com.portingdeadmods.portingdeadlibs.api.client.renderers.multiblocks.MultiblockPreviewRenderer;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.Multiblock;
import com.portingdeadmods.portingdeadlibs.api.utils.HorizontalDirection;
import com.portingdeadmods.portingdeadlibs.utils.MultiblockHelper;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Vector3f;
import org.lwjgl.system.linux.liburing.IOURingRecvmsgOut;

import java.util.Collections;

@EventBusSubscriber(modid = IndustrialReforged.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public final class ClientEvents {
    public static FluidStack playerInCrucibleFluid = null;

    @SubscribeEvent
    public static void renderLevel(RenderLevelStageEvent event) {
        //renderMultiblockPreview(event);
        //renderNetworkNodes(event);
    }

    @SubscribeEvent
    public static void renderFog(ViewportEvent.RenderFog event) {
        Camera camera = event.getCamera();
        if (playerInCrucibleFluid != null)
            IClientFluidTypeExtensions.of(playerInCrucibleFluid.getFluid().defaultFluidState()).modifyFogRender(camera, event.getMode(), event.getRenderer().getRenderDistance(), (float) event.getPartialTick(), event.getNearPlaneDistance(), event.getFarPlaneDistance(), event.getFogShape());
    }

    @SubscribeEvent
    public static void renderFog(ViewportEvent.ComputeFogColor event) {
        Camera camera = event.getCamera();
        if (playerInCrucibleFluid != null) {
            Vector3f vector3f = IClientFluidTypeExtensions.of(playerInCrucibleFluid.getFluid().defaultFluidState()).modifyFogColor(camera, (float) event.getPartialTick(), Minecraft.getInstance().level, (int) event.getRenderer().getRenderDistance(), (float) event.getRenderer().getDarkenWorldAmount((float) event.getPartialTick()), new Vector3f(event.getRed(), event.getGreen(), event.getBlue()));
            event.setRed(vector3f.x);
            event.setGreen(vector3f.y);
            event.setBlue(vector3f.z);
        }
    }

    public static void renderMultiblockPreview(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            Vec3 cameraPos = event.getCamera().getPosition();

            Minecraft mc = Minecraft.getInstance();
            PoseStack poseStack = event.getPoseStack();
            MultiBufferSource.BufferSource bufferSource = event.getLevelRenderer().renderBuffers.bufferSource();

            ItemStack itemStack = mc.player.getMainHandItem();

            if (!itemStack.has(IRDataComponents.BLUEPRINT)) {
                itemStack = mc.player.getOffhandItem();
            }

            if (itemStack.has(IRDataComponents.BLUEPRINT)) {
                ComponentBlueprint blueprint = itemStack.getOrDefault(IRDataComponents.BLUEPRINT, ComponentBlueprint.EMPTY);
                if (blueprint.controllerPos() == null || blueprint.direction() == null || blueprint.multiblock() == null)
                    return;

                Multiblock multiblock = blueprint.multiblock();
                Vec3i relativeControllerPos = MultiblockHelper.getRelativeControllerPos(multiblock);
                BlockPos firstPos = MultiblockHelper.getFirstBlockPos(blueprint.direction(), blueprint.controllerPos(), relativeControllerPos);

                MultiblockPreviewRenderer.renderPreview(multiblock, firstPos, mc.level, HorizontalDirection.NORTH, poseStack, bufferSource, cameraPos);
            }
        }
    }

    private static void renderNetworkNodes(RenderLevelStageEvent event) {
        Vec3 cameraPos = event.getCamera().getPosition();
        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource bufferSource = event.getLevelRenderer().renderBuffers.bufferSource();

        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            for (TransportNetwork<?> network : IRRegistries.NETWORK) {
                for (NetworkNode<?> node : ClientNodes.NODES.getOrDefault(network, Collections.emptyMap()).values()) {
                    NetworkNodeRenderer.render(node, poseStack, bufferSource, cameraPos);
                }

                for (BlockPos interactor : ClientNodes.INTERACTORS.getOrDefault(network, Collections.emptySet())) {
                    NetworkNodeRenderer.renderInteractor(interactor, poseStack, bufferSource, cameraPos);
                }
            }

            if (NetworkNodeRenderer.selectedNode != null) {
                NetworkNodeRenderer.render(NetworkNodeRenderer.selectedNode, poseStack, bufferSource, cameraPos);
            }
        }
    }
}
