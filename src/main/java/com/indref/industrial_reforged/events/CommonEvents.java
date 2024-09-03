package com.indref.industrial_reforged.events;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.multiblocks.Multiblock;
import com.indref.industrial_reforged.api.multiblocks.MultiblockLayer;
import com.indref.industrial_reforged.api.util.HorizontalDirection;
import com.indref.industrial_reforged.client.renderer.item.bar.CrucibleProgressRenderer;
import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.data.components.ComponentTapeMeasure;
import com.indref.industrial_reforged.events.helper.TapeMeasureRenderer;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRMultiblocks;
import com.indref.industrial_reforged.registries.blocks.MetalStorageBlock;
import com.indref.industrial_reforged.registries.items.tools.TapeMeasureItem;
import com.indref.industrial_reforged.util.ItemUtils;
import com.indref.industrial_reforged.util.MultiblockHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
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
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.apache.commons.lang3.IntegerRange;

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

        // TODO: Rendering is temporary
        @SubscribeEvent
        public static void renderOutline(RenderHighlightEvent.Block event) {
            if (event.getCamera().getEntity() instanceof Player player) {
                ItemStack itemStack = player.getMainHandItem();

                if (!(itemStack.getItem() instanceof TapeMeasureItem)) {
                    itemStack = player.getOffhandItem();
                }

                if (itemStack.getItem() instanceof TapeMeasureItem) {
                    ComponentTapeMeasure tapeMeasureData = itemStack.get(IRDataComponents.TAPE_MEASURE_DATA);
                    if (tapeMeasureData.tapeMeasureExtended()) {
                        BlockPos firstPos = tapeMeasureData.firstPos();
                        if (firstPos != null) {
                            TapeMeasureRenderer.renderDistance(event.getPoseStack(), event.getMultiBufferSource().getBuffer(RenderType.lines()), event.getCamera().getPosition(), player, event.getTarget(), firstPos);

                            event.setCanceled(true);
                        }
                    }

                }

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

            if (itemStack.has(IRDataComponents.BLUEPRINT_POS)) {
                Multiblock multiblock = IRMultiblocks.CRUCIBLE_CERAMIC.get();
                BlockPos firstPos = itemStack.getOrDefault(IRDataComponents.BLUEPRINT_POS, BlockPos.ZERO);
                MultiblockLayer[] layout = multiblock.getLayout();
                Int2ObjectMap<Block> def = multiblock.getDefinition();

                int y = 0;
                for (MultiblockLayer layer : layout) {
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
                            renderSmallBlock(poseStack, curPos, camX, camY, camZ, blockRenderer, bufferSource, mc.level, block);
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

        private static void renderSmallBlock(PoseStack poseStack, BlockPos blockPos, double camX, double camY, double camZ, BlockRenderDispatcher blockRenderer, MultiBufferSource.BufferSource bufferSource, Level level, Block block) {
            poseStack.pushPose();
            {
                poseStack.translate(blockPos.getX() - camX, blockPos.getY() - camY, blockPos.getZ() - camZ);
                poseStack.scale(0.5f, 0.5f, 0.5f);
                poseStack.translate(0.5, 0.5, 0.5);
                blockRenderer.renderSingleBlock(block.defaultBlockState(), poseStack, bufferSource, getLightLevel(level, blockPos), OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.solid());
            }
            poseStack.popPose();
        }

        private static int getLightLevel(Level level, BlockPos pos) {
            int bLight = level.getBrightness(LightLayer.BLOCK, pos);
            int sLight = level.getBrightness(LightLayer.SKY, pos);
            return LightTexture.pack(bLight, sLight);
        }

    }
}
