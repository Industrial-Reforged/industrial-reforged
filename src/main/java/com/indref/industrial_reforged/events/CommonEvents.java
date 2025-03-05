package com.indref.industrial_reforged.events;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.client.renderer.item.bar.CrucibleProgressRenderer;
import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.data.components.ComponentBlueprint;
import com.indref.industrial_reforged.util.IRTranslations;
import com.indref.industrial_reforged.util.ItemUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.portingdeadmods.portingdeadlibs.api.client.renderers.multiblocks.MultiblockPreviewRenderer;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.Multiblock;
import com.portingdeadmods.portingdeadlibs.api.utils.HorizontalDirection;
import com.portingdeadmods.portingdeadlibs.utils.MultiblockHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Vector3f;

public final class CommonEvents {
    @EventBusSubscriber(modid = IndustrialReforged.MODID, value = Dist.CLIENT)
    public static final class Client {
        public static FluidStack playerInCrucibleFluid = null;

        @SubscribeEvent
        public static void appendTooltips(ItemTooltipEvent event) {
            ItemStack item = event.getItemStack();
            CompoundTag tag = ItemUtils.getImmutableTag(item).copyTag();
            int meltingType = tag.getInt(CrucibleProgressRenderer.IS_MELTING_KEY);
            if (meltingType == 1) {
                event.getToolTip().add(IRTranslations.Tooltip.MELTING_PROGRESS
                        .component(tag.getFloat(CrucibleProgressRenderer.BARWIDTH_KEY))
                        .withStyle(ChatFormatting.GRAY));
            } else if (meltingType == 2) {
                event.getToolTip().add(IRTranslations.Tooltip.MELTING_PROGRESS.component().withStyle(ChatFormatting.GRAY));
            }
        }

        @SubscribeEvent
        public static void renderLevel(RenderLevelStageEvent event) {
            renderMultiblockPreview(event);
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
            if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) return;

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
}
