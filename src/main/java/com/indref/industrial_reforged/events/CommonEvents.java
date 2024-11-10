package com.indref.industrial_reforged.events;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.multiblocks.Multiblock;
import com.indref.industrial_reforged.api.multiblocks.MultiblockLayer;
import com.indref.industrial_reforged.api.util.HorizontalDirection;
import com.indref.industrial_reforged.client.renderer.item.bar.CrucibleProgressRenderer;
import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.data.components.ComponentBlueprint;
import com.indref.industrial_reforged.data.components.ComponentTapeMeasure;
import com.indref.industrial_reforged.events.helper.MultiblockPreviewRenderer;
import com.indref.industrial_reforged.events.helper.TapeMeasureRenderer;
import com.indref.industrial_reforged.content.items.tools.TapeMeasureItem;
import com.indref.industrial_reforged.util.ItemUtils;
import com.indref.industrial_reforged.util.MultiblockHelper;
import com.indref.industrial_reforged.util.renderer.IRRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

public final class CommonEvents {
    @EventBusSubscriber(modid = IndustrialReforged.MODID, value = Dist.CLIENT)
    public static final class Client {
        @SubscribeEvent
        public static void appendTooltips(ItemTooltipEvent event) {
            ItemStack item = event.getItemStack();
            CompoundTag tag = ItemUtils.getImmutableTag(item).copyTag();
            int meltingType = tag.getInt(CrucibleProgressRenderer.IS_MELTING_KEY);
            if (meltingType == 1) {
                event.getToolTip().add(Component.translatable("*.desc.melting_progress")
                        .append(": ")
                        .append(String.format("%.1f", tag.getFloat(CrucibleProgressRenderer.BARWIDTH_KEY)))
                        .append("/10.0")
                        .withStyle(ChatFormatting.GRAY));
            } else if (meltingType == 2) {
                event.getToolTip().add(Component.translatable("*.desc.melting_not_possible").withStyle(ChatFormatting.GRAY));
            }
        }

        @SubscribeEvent
        public static void renderMultiblockPreview(RenderLevelStageEvent event) {
            if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) return;

            Vec3 cameraPos = event.getCamera().getPosition();

            double camX = cameraPos.x;
            double camY = cameraPos.y;
            double camZ = cameraPos.z;

            Minecraft mc = Minecraft.getInstance();
            BlockRenderDispatcher blockRenderer = mc.getBlockRenderer();
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
                MultiblockLayer[] layout = multiblock.getLayout();
                Int2ObjectMap<Block> def = multiblock.getDefinition();

                int y = 0;
                for (MultiblockLayer layer : layout) {
                    for (int i = 0; i < layer.range().getMaximum(); i++) {
                        // initialize/reset x and z coords for indexing
                        int x = 0;
                        int z = 0;

                        int width = multiblock.getWidths().get(y).leftInt();

                        // Iterate over blocks in a layer (X, Z)
                        for (int blockIndex : layer.layer()) {
                            // Define position-related variables
                            BlockPos curPos = MultiblockHelper.getCurPos(firstPos, new Vec3i(x, y, z), HorizontalDirection.NORTH);

                            Block block = def.get(blockIndex);
                            if (block != null) {
                                BlockState blockState = mc.level.getBlockState(curPos);
                                if (!blockState.is(block)) {
                                    if (blockState.isEmpty()) {
                                        if (i < layer.range().getMinimum()) {
                                            MultiblockPreviewRenderer.renderSmallBlock(poseStack, curPos, camX, camY, camZ, blockRenderer, bufferSource, mc.level, block);
                                        } else {
                                            MultiblockPreviewRenderer.renderSmallOptionalBlock(poseStack, curPos, camX, camY, camZ, blockRenderer, bufferSource, mc.level, block);
                                        }
                                    } else {
                                        MultiblockPreviewRenderer.renderErrorBlock(poseStack, curPos, camX, camY, camZ, bufferSource, mc.level);
                                    }
                                }
                            }
                            // Increase x and z coordinates
                            // start new x if we are done with row and increase z as another row is done
                            if (x + 1 < width) {
                                x++;
                            } else {
                                x = 0;
                                z++;
                            }
                        }

                        y++;
                    }
                }
            }
        }



    }
}
