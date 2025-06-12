package com.indref.industrial_reforged.datagen.assets;

import com.indref.industrial_reforged.content.blocks.multiblocks.parts.FireboxPartBlock;
import com.indref.industrial_reforged.content.multiblocks.BlastFurnaceMultiblock;
import com.indref.industrial_reforged.content.multiblocks.FireboxMultiblock;
import com.indref.industrial_reforged.content.multiblocks.IFireboxMultiblock;
import com.indref.industrial_reforged.content.multiblocks.SmallFireboxMultiblock;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.Multiblock;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;
import org.jetbrains.annotations.NotNull;

public class IRMultiblockDataGenHelper {
    private final IRBlockStateProvider bsp;

    public IRMultiblockDataGenHelper(IRBlockStateProvider bsp) {
        this.bsp = bsp;
    }

    public void smallFirebox() {
        Block block = IRBlocks.SMALL_FIREBOX_HATCH.get();

        VariantBlockStateBuilder builder = bsp.getVariantBuilder(block);
        for (int i = 0; i < 4; i++) {
            builder.partialState().with(SmallFireboxMultiblock.FIREBOX_PART, i).with(Multiblock.FORMED, true).with(SmallFireboxMultiblock.ACTIVE, true)
                    .modelForState().modelFile(smallFireboxModel(block, i, true)).addModel()
                    .partialState().with(SmallFireboxMultiblock.FIREBOX_PART, i).with(Multiblock.FORMED, false).with(SmallFireboxMultiblock.ACTIVE, true)
                    .modelForState().modelFile(unformedSmallFireboxModel(block)).addModel()
                    .partialState().with(SmallFireboxMultiblock.FIREBOX_PART, i).with(Multiblock.FORMED, true).with(SmallFireboxMultiblock.ACTIVE, false)
                    .modelForState().modelFile(smallFireboxModel(block, i, false)).addModel()
                    .partialState().with(SmallFireboxMultiblock.FIREBOX_PART, i).with(Multiblock.FORMED, false).with(SmallFireboxMultiblock.ACTIVE, false)
                    .modelForState().modelFile(unformedSmallFireboxModel(block)).addModel();
        }
    }

    private BlockModelBuilder smallFireboxModel(Block block, int part, boolean active) {
        String suffix0 = (part == 0 || part == 3 ? "_side_1" : "_side_0") + (active ? "_active" : "");
        String suffix1 = (part == 0 || part == 3 ? "_side_0" : "_side_1") + (active ? "_active" : "");
        return bsp.models().cube(bsp.name(block) + "_formed_" + part + (active ? "_active" : ""),
                extend(multiblockLoc(block), "_top_" + part),
                extend(multiblockLoc(block), "_top_" + part),
                extend(multiblockLoc(block), suffix0),
                extend(multiblockLoc(block), suffix0),
                extend(multiblockLoc(block), suffix1),
                extend(multiblockLoc(block), suffix1)
        ).texture("particle", bsp.blockTexture(Blocks.IRON_BLOCK));
    }

    public @NotNull BlockModelBuilder unformedSmallFireboxModel(Block block) {
        return bsp.models()
                .cube(bsp.name(IRBlocks.SMALL_FIREBOX_HATCH.get()),
                        extend(multiblockLoc(block), "_unformed_top"),
                        extend(multiblockLoc(block), "_unformed_top"),
                        extend(multiblockLoc(block), "_front"),
                        extend(multiblockLoc(block), "_front"),
                        extend(multiblockLoc(block), "_front"),
                        extend(multiblockLoc(block), "_front")
                ).texture("particle", bsp.blockTexture(Blocks.IRON_BLOCK));
    }

    public void firebox() {
        Block controller = IRBlocks.FIREBOX_CONTROLLER.get();
        Block part = IRBlocks.FIREBOX_PART.get();

        // Controller
        bsp.getVariantBuilder(controller).partialState().modelForState().modelFile(bsp.models()
                .cube(bsp.name(controller),
                        bsp.blockTexture(IRBlocks.REFRACTORY_BRICK.get()),
                        bsp.modLoc("block/multiblock/firebox_coil"),
                        bsp.blockTexture(IRBlocks.REFRACTORY_BRICK.get()),
                        bsp.blockTexture(IRBlocks.REFRACTORY_BRICK.get()),
                        bsp.blockTexture(IRBlocks.REFRACTORY_BRICK.get()),
                        bsp.blockTexture(IRBlocks.REFRACTORY_BRICK.get())
                ).texture("particle", bsp.blockTexture(IRBlocks.REFRACTORY_BRICK.get()))).addModel();

        bsp.getVariantBuilder(part)
                .partialState().with(FireboxMultiblock.FIREBOX_PART, FireboxMultiblock.PartIndex.COIL).with(IFireboxMultiblock.ACTIVE, true)
                .modelForState().modelFile(bsp.models().cubeAll(bsp.name(part), bsp.blockTexture(IRBlocks.REFRACTORY_BRICK.get()))).addModel()
                .partialState().with(FireboxMultiblock.FIREBOX_PART, FireboxMultiblock.PartIndex.BRICK).with(IFireboxMultiblock.ACTIVE, true)
                .modelForState().modelFile(bsp.models().cubeAll(bsp.name(part), bsp.blockTexture(IRBlocks.REFRACTORY_BRICK.get()))).addModel()
                .partialState().with(FireboxMultiblock.FIREBOX_PART, FireboxMultiblock.PartIndex.HATCH).with(IFireboxMultiblock.ACTIVE, true)
                .modelForState().modelFile(fireboxHatchModel(part, "_active")).addModel()
                .partialState().with(FireboxMultiblock.FIREBOX_PART, FireboxMultiblock.PartIndex.COIL).with(IFireboxMultiblock.ACTIVE, false)
                .modelForState().modelFile(bsp.models().cubeAll(bsp.name(part), bsp.blockTexture(IRBlocks.REFRACTORY_BRICK.get()))).addModel()
                .partialState().with(FireboxMultiblock.FIREBOX_PART, FireboxMultiblock.PartIndex.BRICK).with(IFireboxMultiblock.ACTIVE, false)
                .modelForState().modelFile(bsp.models().cubeAll(bsp.name(part), bsp.blockTexture(IRBlocks.REFRACTORY_BRICK.get()))).addModel()
                .partialState().with(FireboxMultiblock.FIREBOX_PART, FireboxMultiblock.PartIndex.HATCH).with(IFireboxMultiblock.ACTIVE, false)
                .modelForState().modelFile(fireboxHatchModel(part, "")).addModel();
    }

    private @NotNull BlockModelBuilder fireboxHatchModel(Block part, String suffix) {
        return bsp.models().cube(bsp.name(part) + "_hatch" + suffix,
                bsp.blockTexture(IRBlocks.REFRACTORY_BRICK.get()),
                bsp.blockTexture(IRBlocks.REFRACTORY_BRICK.get()),
                bsp.modLoc("block/multiblock/firebox_hatch" + suffix),
                bsp.modLoc("block/multiblock/firebox_hatch" + suffix),
                bsp.modLoc("block/multiblock/firebox_hatch" + suffix),
                bsp.modLoc("block/multiblock/firebox_hatch" + suffix)
        ).texture("particle", bsp.blockTexture(IRBlocks.REFRACTORY_BRICK.get()));
    }

    public void blastFurnace() {
        Block hatch = IRBlocks.BLAST_FURNACE_HATCH.get();
        Block part = IRBlocks.BLAST_FURNACE_PART.get();
        Block bricks = IRBlocks.BLAST_FURNACE_BRICKS.get();

        bsp.getVariantBuilder(hatch)
                .partialState().with(BlastFurnaceMultiblock.BRICK_STATE, BlastFurnaceMultiblock.BrickStates.FORMED).with(BlastFurnaceMultiblock.ACTIVE, false)
                .modelForState().modelFile(blastFurnaceHatchModel(hatch, true, false)).addModel()
                .partialState().with(BlastFurnaceMultiblock.BRICK_STATE, BlastFurnaceMultiblock.BrickStates.FORMED).with(BlastFurnaceMultiblock.ACTIVE, true)
                .modelForState().modelFile(blastFurnaceHatchModel(hatch, true, true)).addModel()
                .partialState().with(BlastFurnaceMultiblock.BRICK_STATE, BlastFurnaceMultiblock.BrickStates.UNFORMED).with(BlastFurnaceMultiblock.ACTIVE, false)
                .modelForState().modelFile(blastFurnaceHatchModel(hatch, false, false)).addModel()
                .partialState().with(BlastFurnaceMultiblock.BRICK_STATE, BlastFurnaceMultiblock.BrickStates.UNFORMED).with(BlastFurnaceMultiblock.ACTIVE, true)
                .modelForState().modelFile(blastFurnaceHatchModel(hatch, false, true)).addModel()
                .partialState().with(BlastFurnaceMultiblock.BRICK_STATE, BlastFurnaceMultiblock.BrickStates.TOP).with(BlastFurnaceMultiblock.ACTIVE, false)
                .modelForState().modelFile(blastFurnaceHatchModel(hatch, false, false)).addModel()
                .partialState().with(BlastFurnaceMultiblock.BRICK_STATE, BlastFurnaceMultiblock.BrickStates.TOP).with(BlastFurnaceMultiblock.ACTIVE, true)
                .modelForState().modelFile(blastFurnaceHatchModel(hatch, false, true)).addModel();

        VariantBlockStateBuilder builder = bsp.getVariantBuilder(part);
        for (Direction dir : BlockStateProperties.HORIZONTAL_FACING.getPossibleValues()) {
            builder
                    .partialState().with(BlastFurnaceMultiblock.BRICK_STATE, BlastFurnaceMultiblock.BrickStates.FORMED).with(BlockStateProperties.HORIZONTAL_FACING, dir.getOpposite())
                    .modelForState().modelFile(bsp.models().cubeAll(bsp.name(part) + "_formed", bsp.blockTexture(bricks))).rotationY(((int) dir.toYRot() + 180) % 360).addModel()
                    .partialState().with(BlastFurnaceMultiblock.BRICK_STATE, BlastFurnaceMultiblock.BrickStates.UNFORMED).with(BlockStateProperties.HORIZONTAL_FACING, dir.getOpposite())
                    .modelForState().modelFile(bsp.models().cubeAll(bsp.name(part), bsp.blockTexture(bricks))).rotationY(((int) dir.toYRot() + 180) % 360).addModel()
                    .partialState().with(BlastFurnaceMultiblock.BRICK_STATE, BlastFurnaceMultiblock.BrickStates.TOP).with(BlockStateProperties.HORIZONTAL_FACING, dir.getOpposite())
                    .modelForState().modelFile(bsp.models().getExistingFile(bsp.modLoc("block/blast_furnace_top"))).rotationY(((int) dir.toYRot() + 180) % 360).addModel();
        }
    }

    private @NotNull BlockModelBuilder blastFurnaceHatchModel(Block hatch, boolean formed, boolean active) {
        String suffix = formed ? "_formed" + (active ? "_active" : "") : "";
        return bsp.models().cubeColumn(bsp.name(hatch) + suffix,
                extend(multiblockLoc(hatch), suffix),
                bsp.blockTexture(IRBlocks.BLAST_FURNACE_BRICKS.get())
        ).texture("particle", bsp.blockTexture(IRBlocks.BLAST_FURNACE_BRICKS.get()));
    }

    public ResourceLocation extend(ResourceLocation rl, String suffix) {
        return ResourceLocation.fromNamespaceAndPath(rl.getNamespace(), rl.getPath() + suffix);
    }

    private ResourceLocation multiblockLoc(Block block) {
        ResourceLocation resourceLocation = bsp.key(block);
        String namespace = resourceLocation.getNamespace();
        String path = resourceLocation.getPath();

        return ResourceLocation.fromNamespaceAndPath(namespace, "block/multiblock/" + path);
    }
}
