package com.indref.industrial_reforged.compat.guideme.tags;

import com.indref.industrial_reforged.registries.IRMultiblocks;
import com.portingdeadmods.portingdeadlibs.PDLRegistries;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.Multiblock;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.MultiblockDefinition;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.MultiblockLayer;
import com.portingdeadmods.portingdeadlibs.api.utils.HorizontalDirection;
import com.portingdeadmods.portingdeadlibs.utils.MultiblockHelper;
import guideme.color.SymbolicColor;
import guideme.compiler.PageCompiler;
import guideme.compiler.tags.MdxAttrs;
import guideme.document.LytErrorSink;
import guideme.libs.mdast.mdx.model.MdxJsxElementFields;
import guideme.scene.GuidebookScene;
import guideme.scene.annotation.InWorldBoxAnnotation;
import guideme.scene.element.SceneElementTagCompiler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.Set;

public class MultiblockShapeCompiler implements SceneElementTagCompiler {
    @Override
    public Set<String> getTagNames() {
        return Set.of("MultiblockShape");
    }

    @Override
    public void compile(GuidebookScene scene, PageCompiler compiler, LytErrorSink errorSink, MdxJsxElementFields el) {
        String multiblockId = MdxAttrs.getString(compiler, errorSink, el, "multiblock", null);
        boolean formed = MdxAttrs.getBoolean(compiler, errorSink, el, "formed", true);
        boolean unformed = MdxAttrs.getBoolean(compiler, errorSink, el, "unformed", true);
        boolean showController = MdxAttrs.getBoolean(compiler, errorSink, el, "showController", true);
        String directionLiteral = MdxAttrs.getString(compiler, errorSink, el, "direction", HorizontalDirection.WEST.toRegularDirection().getSerializedName());
        HorizontalDirection direction = null;
        for (Direction directionValue : Direction.values()) {
            if (directionValue.getSerializedName().equals(directionLiteral)) {
                direction = HorizontalDirection.fromRegularDirection(directionValue);
                break;
            }
        }

        if (direction == null) {
            errorSink.appendError(compiler, "Invalid direction: " + directionLiteral, el);
            return;
        }

        if (multiblockId == null) {
            errorSink.appendError(compiler, "Missing 'multiblock' attribute in MultiblockShape tag", el);
            return;
        }

        ResourceLocation location = ResourceLocation.parse(multiblockId);
        Multiblock multiblock = PDLRegistries.MULTIBLOCK.get(location);

        if (multiblock == null) {
            errorSink.appendError(compiler, "Invalid multiblock: " + multiblockId, el);
            return;
        }

        BlockPos controllerPos = MdxAttrs.getPos(compiler, errorSink, el);
        MultiblockLayer bottomLayer = multiblock.getLayout()[0];
        int distance = bottomLayer.widths().firstInt();

        if (unformed) {
            if (!formed) {
                distance = -1;
            }
            setMultiblock(scene.getLevel(), multiblock, direction, controllerPos.north(-distance - 1));
        }

        if (formed) {
            setMultiblock(scene.getLevel(), multiblock, direction, controllerPos);
            multiblock.form(scene.getLevel(), controllerPos);
            fixControllerRotation(scene.getLevel(), multiblock, direction, controllerPos);
        }

        if (showController) {
            InWorldBoxAnnotation annotation = InWorldBoxAnnotation.forBlock(controllerPos, SymbolicColor.GREEN);

            if (unformed) {
                InWorldBoxAnnotation annotation1 = InWorldBoxAnnotation.forBlock(controllerPos.north(-distance - 1), SymbolicColor.GREEN);
                scene.addAnnotation(annotation1);
            }

            scene.addAnnotation(annotation);
        }

    }

    // TODO: Implement a method that allows forming the multiblock in a specific direction so we can remove this
    private static void fixControllerRotation(Level level, Multiblock multiblock, HorizontalDirection direction, BlockPos controllerPos) {
        BlockState blockState = level.getBlockState(controllerPos);
        if (blockState.hasProperty(BlockStateProperties.HORIZONTAL_FACING) && multiblock == IRMultiblocks.CRUCIBLE_CERAMIC.get()) {
            level.setBlockAndUpdate(controllerPos, blockState.setValue(BlockStateProperties.HORIZONTAL_FACING, direction.toRegularDirection()));
        }
    }

    private static void setMultiblock(Level level, Multiblock multiblock, HorizontalDirection direction, BlockPos controllerPos) {
        Vec3i relativeControllerPos = MultiblockHelper.getRelativeControllerPos(multiblock);
        BlockPos firstPos = MultiblockHelper.getFirstBlockPos(direction, controllerPos, relativeControllerPos);
        MultiblockLayer[] layout = multiblock.getLayout();
        MultiblockDefinition definition = multiblock.getDefinition();

        int y = 0;
        for (MultiblockLayer layer : layout) {
            for (int i = 0; i < layer.range().getMax(); i++) {
                int x = 0;
                int z = 0;

                int width = multiblock.getWidths().get(y).leftInt();

                for (int blockIndex : layer.layer()) {
                    BlockPos curPos = MultiblockHelper.getCurPos(firstPos, new Vec3i(x, y, z), direction);

                    Block block = definition.getDefaultBlock(blockIndex);
                    if (block != null) {
                        BlockState state = block.defaultBlockState();
                        level.setBlockAndUpdate(curPos, state);
                    }

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
