package com.indref.industrial_reforged.datagen.assets;

import com.indref.industrial_reforged.content.blocks.multiblocks.parts.FireboxPartBlock;
import com.indref.industrial_reforged.content.multiblocks.BlastFurnaceMultiblock;
import com.indref.industrial_reforged.content.multiblocks.FireboxMultiblock;
import com.indref.industrial_reforged.content.multiblocks.SmallFireboxMultiblock;
import com.indref.industrial_reforged.registries.IRBlocks;
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
        for (Direction dir : BlockStateProperties.HORIZONTAL_FACING.getPossibleValues()) {
            builder.partialState().with(SmallFireboxMultiblock.FIREBOX_STATE, SmallFireboxMultiblock.FireboxState.FORMED).with(BlockStateProperties.HORIZONTAL_FACING, dir.getOpposite())
                    .modelForState().modelFile(smallFireboxModel(block, true)).rotationY(((int) dir.toYRot() + 180) % 360).addModel()
                    .partialState().with(SmallFireboxMultiblock.FIREBOX_STATE, SmallFireboxMultiblock.FireboxState.UNFORMED).with(BlockStateProperties.HORIZONTAL_FACING, dir.getOpposite())
                    .modelForState().modelFile(smallFireboxModel(block, false)).rotationY(((int) dir.toYRot() + 180) % 360).addModel();
        }
    }

    private BlockModelBuilder smallFireboxModel(Block block, boolean formed) {
        if (formed) {
            return bsp.models().cube(bsp.name(block) + "_formed",
                    extend(multiblockLoc(block), "_top"),
                    extend(multiblockLoc(block), "_top"),
                    extend(multiblockLoc(block), "_right"),
                    extend(multiblockLoc(block), "_left"),
                    extend(multiblockLoc(block), "_right"),
                    extend(multiblockLoc(block), "_left")
            ).texture("particle", bsp.blockTexture(Blocks.IRON_BLOCK));
        }
        return bsp.models()
                .cube(bsp.name(IRBlocks.SMALL_FIREBOX_HATCH.get()),
                        bsp.blockTexture(Blocks.IRON_BLOCK),
                        bsp.blockTexture(Blocks.IRON_BLOCK),
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
                .partialState().with(FireboxMultiblock.FIREBOX_PART, FireboxMultiblock.PartIndex.COIL).with(FireboxPartBlock.HATCH_ACTIVE, true)
                .modelForState().modelFile(bsp.models().cubeAll(bsp.name(part), bsp.blockTexture(IRBlocks.REFRACTORY_BRICK.get()))).addModel()
                .partialState().with(FireboxMultiblock.FIREBOX_PART, FireboxMultiblock.PartIndex.BRICK).with(FireboxPartBlock.HATCH_ACTIVE, true)
                .modelForState().modelFile(bsp.models().cubeAll(bsp.name(part), bsp.blockTexture(IRBlocks.REFRACTORY_BRICK.get()))).addModel()
                .partialState().with(FireboxMultiblock.FIREBOX_PART, FireboxMultiblock.PartIndex.HATCH).with(FireboxPartBlock.HATCH_ACTIVE, true)
                .modelForState().modelFile(fireboxHatchModel(part, "_active")).addModel()
                .partialState().with(FireboxMultiblock.FIREBOX_PART, FireboxMultiblock.PartIndex.COIL).with(FireboxPartBlock.HATCH_ACTIVE, false)
                .modelForState().modelFile(bsp.models().cubeAll(bsp.name(part), bsp.blockTexture(IRBlocks.REFRACTORY_BRICK.get()))).addModel()
                .partialState().with(FireboxMultiblock.FIREBOX_PART, FireboxMultiblock.PartIndex.BRICK).with(FireboxPartBlock.HATCH_ACTIVE, false)
                .modelForState().modelFile(bsp.models().cubeAll(bsp.name(part), bsp.blockTexture(IRBlocks.REFRACTORY_BRICK.get()))).addModel()
                .partialState().with(FireboxMultiblock.FIREBOX_PART, FireboxMultiblock.PartIndex.HATCH).with(FireboxPartBlock.HATCH_ACTIVE, false)
                .modelForState().modelFile(fireboxHatchModel(part, "")).addModel();
    }

    private @NotNull BlockModelBuilder fireboxHatchModel(Block part, String suffix) {
        return bsp.models().cube(bsp.name(part) + "_hatch" + suffix,
                bsp.blockTexture(IRBlocks.REFRACTORY_BRICK.get()),
                bsp.blockTexture(IRBlocks.REFRACTORY_BRICK.get()),
                bsp.modLoc("block/multiblock/firebox_hatch"),
                bsp.modLoc("block/multiblock/firebox_hatch"),
                bsp.modLoc("block/multiblock/firebox_hatch"),
                bsp.modLoc("block/multiblock/firebox_hatch")
        ).texture("particle", bsp.blockTexture(IRBlocks.REFRACTORY_BRICK.get()));
    }

    public void blastFurnace() {
        Block hatch = IRBlocks.BLAST_FURNACE_HATCH.get();
        Block part = IRBlocks.BLAST_FURNACE_PART.get();
        Block bricks = IRBlocks.BLAST_FURNACE_BRICKS.get();

        bsp.getVariantBuilder(hatch)
                .partialState().with(BlastFurnaceMultiblock.BRICK_STATE, BlastFurnaceMultiblock.BrickStates.FORMED)
                .modelForState().modelFile(blastFurnaceHatchModel(hatch, true)).addModel()
                .partialState().with(BlastFurnaceMultiblock.BRICK_STATE, BlastFurnaceMultiblock.BrickStates.UNFORMED)
                .modelForState().modelFile(blastFurnaceHatchModel(hatch, false)).addModel()
                .partialState().with(BlastFurnaceMultiblock.BRICK_STATE, BlastFurnaceMultiblock.BrickStates.TOP)
                .modelForState().modelFile(blastFurnaceHatchModel(hatch, false)).addModel();

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

    private @NotNull BlockModelBuilder blastFurnaceHatchModel(Block hatch, boolean formed) {
        return bsp.models().cubeColumn(bsp.name(hatch) + (formed ? "_formed" : ""),
                extend(multiblockLoc(hatch), formed ? "_formed" : ""),
                bsp.blockTexture(IRBlocks.BLAST_FURNACE_BRICKS.get())
        ).texture("particle", bsp.blockTexture(IRBlocks.REFRACTORY_BRICK.get()));
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
