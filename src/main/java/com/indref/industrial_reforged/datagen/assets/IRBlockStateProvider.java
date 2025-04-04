package com.indref.industrial_reforged.datagen.assets;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.content.blocks.machines.primitive.CastingBasinBlock;
import com.indref.industrial_reforged.content.blocks.machines.primitive.FaucetBlock;
import com.indref.industrial_reforged.content.blocks.trees.RubberTreeResinHoleBlock;
import com.indref.industrial_reforged.registries.IRBlocks;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.indref.industrial_reforged.util.Utils.ACTIVE;

public class IRBlockStateProvider extends BlockStateProvider {
    public IRBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, IndustrialReforged.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        treeStatesAndModels();
        multiblockModels();
        metalStorageBlocks();
        oreBlocks();

        blockModel(IRBlocks.BASIC_GENERATOR)
                .front(this::machineTexture, "_front")
                .horizontalFacing()
                .active()
                .create();

        blockModel(IRBlocks.CENTRIFUGE)
                .top(this::machineTexture, "_top")
                .sides(this::machineTexture, "_side")
                .active()
                .create();

        blockModel(IRBlocks.BATTERY_BOX)
                .top(this::machineTexture, "_top")
                .bottom(this::machineTexture, "_bottom")
                .defaultTexture(this::machineTexture)
                .facing()
                .create();

        blockModel(IRBlocks.CRAFTING_STATION)
                .top(this::machineTexture, "_top")
                .sides(this::machineTexture, "_side")
                .front(this::machineTexture, "_front")
                .bottom(this::machineTexture, "_bottom")
                .horizontalFacing()
                .create();

        blockModel(IRBlocks.DRAIN)
                .top(this::machineTexture, "_top")
                .sides(this::machineTexture, "_side")
                .front(this::machineTexture, "_front")
                .bottom(this::machineTexture, "_bottom")
                .horizontalFacing()
                .create();

        blockModel(IRBlocks.WOODEN_SCAFFOLDING)
                .top(this::modelTexture, "_top")
                .sides(this::modelTexture, "_side")
                .bottom(this::modelTexture, "_bottom")
                .particle(this::modelTexture, "_top")
                .cutout()
                .create();

        cableBlock(IRBlocks.TIN_CABLE.get());
        cableBlock(IRBlocks.COPPER_CABLE.get());
        cableBlock(IRBlocks.GOLD_CABLE.get());
        cableBlock(IRBlocks.STEEL_CABLE.get());

        simpleBlock(IRBlocks.BLAST_FURNACE_BRICKS.get());

        simpleBlock(IRBlocks.REFRACTORY_STONE.get());
        simpleBlock(IRBlocks.REFRACTORY_BRICK.get());
        simpleBlock(IRBlocks.BASIC_MACHINE_FRAME.get());
        simpleBlock(IRBlocks.TERRACOTTA_BRICKS.get());
        slabBlock(IRBlocks.TERRACOTTA_BRICK_SLAB.get(), blockTexture(IRBlocks.TERRACOTTA_BRICKS.get()),
                blockTexture(IRBlocks.TERRACOTTA_BRICKS.get()));

        fenceBlock(IRBlocks.IRON_FENCE.get(), blockTexture(Blocks.IRON_BLOCK));

        castingBasin(IRBlocks.CERAMIC_CASTING_BASIN, blockTexture(IRBlocks.TERRACOTTA_BRICKS.get()));
        castingBasin(IRBlocks.BLAST_FURNACE_CASTING_BASIN, blockTexture(IRBlocks.BLAST_FURNACE_BRICKS.get()));

        faucet(IRBlocks.BLAST_FURNACE_FAUCET, blockTexture(IRBlocks.BLAST_FURNACE_BRICKS.get()));

        coilBlock(IRBlocks.COIL.get());
    }

    private void multiblockModels() {
        IRMultiblockDataGenHelper dataGenHelper = new IRMultiblockDataGenHelper(this);
        dataGenHelper.smallFirebox();
        dataGenHelper.firebox();
        dataGenHelper.blastFurnace();
    }

    private void oreBlocks() {
        for (DeferredBlock<?> oreBlock : IRBlocks.ORES.keySet()) {
            simpleBlock(oreBlock.get());
        }
    }

    private void simpleColumn(DeferredBlock<?> block, DeferredBlock<?> end) {
        simpleBlock(block.get(), models().cubeColumn(name(block.get()),
                blockTexture(block.get()),
                blockTexture(end.get())));
    }

    private ModelBuilder blockModel(DeferredBlock<?> block) {
        return new ModelBuilder(block);
    }

    private void metalStorageBlocks() {
        for (DeferredBlock<Block> block : IRBlocks.METAL_STORAGE_BLOCKS) {
            simpleBlock(block.get());
        }

        for (DeferredBlock<Block> block : IRBlocks.RAW_STORAGE_BLOCKS) {
            simpleBlock(block.get());
        }
    }

    private ModelFile baseCable(ResourceLocation blockLoc) {
        return models().withExistingParent(blockLoc.getPath() + "_base", modLoc("block/cable_base"))
                .texture("texture", ResourceLocation.fromNamespaceAndPath(blockLoc.getNamespace(), "block/" + blockLoc.getPath()));
    }

    private void cableBlock(Block block) {
        ResourceLocation loc = BuiltInRegistries.BLOCK.getKey(block);
        MultiPartBlockStateBuilder builder = getMultipartBuilder(block);
        cableConnection(builder, loc, Direction.DOWN, 0, 0);
        cableConnection(builder, loc, Direction.UP, 180, 0);
        cableConnection(builder, loc, Direction.NORTH, 90, 180);
        cableConnection(builder, loc, Direction.EAST, 90, 270);
        cableConnection(builder, loc, Direction.SOUTH, 90, 0);
        cableConnection(builder, loc, Direction.WEST, 90, 90);
        builder.part().modelFile(baseCable(loc)).addModel().end();
    }

    private void cableConnection(MultiPartBlockStateBuilder builder, ResourceLocation loc, Direction direction, int x, int y) {
        builder.part().modelFile(cableConnectionModel(loc)).rotationX(x).rotationY(y).addModel()
                .condition(com.indref.industrial_reforged.api.blocks.transfer.PipeBlock.CONNECTION[direction.get3DDataValue()], true).end();
    }

    private ModelFile cableConnectionModel(ResourceLocation blockLoc) {
        return models().withExistingParent(blockLoc.getPath() + "_connection", modLoc("block/cable_connection"))
                .texture("texture", ResourceLocation.fromNamespaceAndPath(blockLoc.getNamespace(), "block/" + blockLoc.getPath()));
    }

    private void treeStatesAndModels() {
        logBlock(IRBlocks.RUBBER_TREE_LOG.get(), "tree");
        logBlock(IRBlocks.STRIPPED_RUBBER_TREE_LOG.get(), "tree");
        woodBlock(IRBlocks.RUBBER_TREE_WOOD.get(), IRBlocks.RUBBER_TREE_LOG.get(), "tree");
        woodBlock(IRBlocks.STRIPPED_RUBBER_TREE_WOOD.get(), IRBlocks.STRIPPED_RUBBER_TREE_LOG.get(), "tree");
        simpleBlockParentItem(IRBlocks.RUBBER_TREE_PLANKS.get(), "cube_all", "all", "tree");
        simpleBlockParentItem(IRBlocks.RUBBER_TREE_LEAVES.get(), "leaves", "all", "tree");
        simpleBlock(IRBlocks.RUBBER_TREE_SAPLING.get(), models().singleTexture(
                name(IRBlocks.RUBBER_TREE_SAPLING.get()),
                mcLoc(ModelProvider.BLOCK_FOLDER + "/cross"),
                "cross",
                blockTexture(IRBlocks.RUBBER_TREE_SAPLING.get(), "tree")
        ).renderType("cutout"));
        rubberTreeResinHole(IRBlocks.RUBBER_TREE_RESIN_HOLE);
        buttonBlock(IRBlocks.RUBBER_TREE_BUTTON.get(), blockTexture(IRBlocks.RUBBER_TREE_PLANKS.get(), "tree"));
        inventoryModel(IRBlocks.RUBBER_TREE_BUTTON.get(), "button_inventory", blockTexture(IRBlocks.RUBBER_TREE_PLANKS.get(), "tree"));
        doorBlock(IRBlocks.RUBBER_TREE_DOOR.get(),
                blockTexture(IRBlocks.RUBBER_TREE_DOOR.get(), "tree", "_bottom"), blockTexture(IRBlocks.RUBBER_TREE_DOOR.get(), "tree", "_top"));
        fenceBlock(IRBlocks.RUBBER_TREE_FENCE.get(), blockTexture(IRBlocks.RUBBER_TREE_PLANKS.get(), "tree"));
        inventoryModel(IRBlocks.RUBBER_TREE_FENCE.get(), "fence_inventory", blockTexture(IRBlocks.RUBBER_TREE_PLANKS.get(), "tree"));
        fenceGateBlock(IRBlocks.RUBBER_TREE_FENCE_GATE.get(), blockTexture(IRBlocks.RUBBER_TREE_PLANKS.get(), "tree"));
        pressurePlateBlock(IRBlocks.RUBBER_TREE_PRESSURE_PLATE.get(), blockTexture(IRBlocks.RUBBER_TREE_PLANKS.get(), "tree"));
        trapdoorBlock(IRBlocks.RUBBER_TREE_TRAPDOOR.get(), blockTexture(IRBlocks.RUBBER_TREE_TRAPDOOR.get(), "tree"), false);
        slabBlock(IRBlocks.RUBBER_TREE_SLAB.get(), ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "block/rubber_tree_planks"),
                blockTexture(IRBlocks.RUBBER_TREE_PLANKS.get(), "tree"));
        stairsBlock(IRBlocks.RUBBER_TREE_STAIRS.get(),
                blockTexture(IRBlocks.RUBBER_TREE_PLANKS.get(), "tree"));
    }

    private void castingBasin(DeferredBlock<CastingBasinBlock> block, ResourceLocation texture) {
        simpleBlock(block.get(), models().withExistingParent(name(block.get()), modLoc("block/casting_basin_base"))
                .texture("texture", texture));
    }

    private void faucet(DeferredBlock<FaucetBlock> block, ResourceLocation texture) {
        horizontalBlock(block.get(), models().withExistingParent(name(block.get()), modLoc("block/faucet_base"))
                .texture("texture", texture));
    }

    private void rubberTreeResinHole(DeferredBlock<RubberTreeResinHoleBlock> block) {
        VariantBlockStateBuilder builder = getVariantBuilder(block.get());
        BlockModelBuilder modelBuilder = models().cube(name(block.get()) + "_full",
                        extend(blockTexture(IRBlocks.RUBBER_TREE_LOG.get(), "tree"), "_top"),
                        extend(blockTexture(IRBlocks.RUBBER_TREE_LOG.get(), "tree"), "_top"),
                        extend(blockTexture(block.get(), "tree"), "_full"),
                        blockTexture(IRBlocks.RUBBER_TREE_LOG.get(), "tree"),
                        blockTexture(IRBlocks.RUBBER_TREE_LOG.get(), "tree"),
                        blockTexture(IRBlocks.RUBBER_TREE_LOG.get(), "tree"))
                .texture("particle", blockTexture(IRBlocks.RUBBER_TREE_LOG.get(), "tree"));
        BlockModelBuilder emptyModelBuilder = models().cube(name(block.get()),
                        extend(blockTexture(IRBlocks.RUBBER_TREE_LOG.get(), "tree"), "_top"),
                        extend(blockTexture(IRBlocks.RUBBER_TREE_LOG.get(), "tree"), "_top"),
                        blockTexture(block.get(), "tree"),
                        blockTexture(IRBlocks.RUBBER_TREE_LOG.get(), "tree"),
                        blockTexture(IRBlocks.RUBBER_TREE_LOG.get(), "tree"),
                        blockTexture(IRBlocks.RUBBER_TREE_LOG.get(), "tree"))
                .texture("particle", blockTexture(IRBlocks.RUBBER_TREE_LOG.get(), "tree"));

        for (Direction dir : BlockStateProperties.HORIZONTAL_FACING.getPossibleValues()) {
            builder.partialState().with(RubberTreeResinHoleBlock.RESIN, true).with(BlockStateProperties.HORIZONTAL_FACING, dir)
                    .modelForState().modelFile(modelBuilder).rotationY(((int) dir.toYRot() + 180) % 360).addModel()
                    .partialState().with(RubberTreeResinHoleBlock.RESIN, false).with(BlockStateProperties.HORIZONTAL_FACING, dir)
                    .modelForState().modelFile(emptyModelBuilder).rotationY(((int) dir.toYRot() + 180) % 360).addModel();
        }
    }

    private void logBlock(RotatedPillarBlock block, String textureFolder) {
        ResourceLocation name = BuiltInRegistries.BLOCK.getKey(block);
        axisBlock(block, blockTexture(block, textureFolder), blockTexture(block, textureFolder, "_top"));
        simpleBlockItem(block, models().withExistingParent(name.getPath(), mcLoc("minecraft:block/cube_column")));
    }

    private void coilBlock(RotatedPillarBlock block) {
        ResourceLocation name = BuiltInRegistries.BLOCK.getKey(block);
        axisBlock(block, blockTexture(block, "", "_side"), blockTexture(block, "", "_top"));
        simpleBlockItem(block, models().withExistingParent(name.getPath(), mcLoc("minecraft:block/cube_column")));
    }

    private void woodBlock(RotatedPillarBlock block, Block blockTexture, String textureFolder) {
        ResourceLocation name = BuiltInRegistries.BLOCK.getKey(block);
        ResourceLocation texture = blockTexture(blockTexture, textureFolder);
        axisBlock(block, models().cubeColumn(name.getPath(), texture, texture),
                models().cubeColumnHorizontal(name.getPath(), texture, texture));
        simpleBlockItem(block, models().withExistingParent(name.getPath(), mcLoc("minecraft:block/cube_column")));
    }

    private void inventoryModel(Block block, String parentModel, ResourceLocation texture) {
        ResourceLocation name = BuiltInRegistries.BLOCK.getKey(block);
        models().getBuilder(name.getPath() + "_inventory").parent(models().getExistingFile(ResourceLocation.parse(parentModel))).texture("texture", texture);
    }

    private ResourceLocation machineTexture(DeferredBlock<?> block, String suffix) {
        return blockTexture(block.get(), "machine", suffix);
    }

    private ResourceLocation machineTexture(DeferredBlock<?> block) {
        return blockTexture(block.get(), "machine", "");
    }

    private ResourceLocation machineTexture(Block block, String suffix) {
        return blockTexture(block, "machine", suffix);
    }

    private ResourceLocation machineTexture(Block block) {
        return blockTexture(block, "machine", "");
    }

    private ResourceLocation modelTexture(Block block, String suffix) {
        return blockTexture(block, "", suffix);
    }

    private ResourceLocation blockTexture(Block block, String textureFolder) {
        return blockTexture(block, textureFolder, "");
    }

    private ResourceLocation blockTexture(Block block, String textureFolder, String suffix) {
        ResourceLocation name = key(block);
        if (textureFolder == null || textureFolder.trim().isEmpty())
            return ResourceLocation.fromNamespaceAndPath(name.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + name.getPath() + suffix);
        return ResourceLocation.fromNamespaceAndPath(name.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + textureFolder + "/" + name.getPath() + suffix);
    }

    private ModelFile cubeAll(Block block, String textureFolder) {
        ResourceLocation name = key(block);
        return models().singleTexture(name.getPath(), mcLoc(ModelProvider.BLOCK_FOLDER + "/cube_all"), "all", blockTexture(block, textureFolder));
    }

    public void simpleBlock(Block block, String textureFolder) {
        simpleBlock(block, cubeAll(block, textureFolder));
    }

    private void simpleBlock(Block block, String modelName, String textureKey, String textureFolder) {
        simpleBlock(block, models().singleTexture(
                key(block).getPath(), ResourceLocation.parse(ModelProvider.BLOCK_FOLDER + "/" + modelName),
                textureKey, blockTexture(block, textureFolder)));
    }

    private void simpleBlockParentItem(Block block, String modelName, String textureKey, String textureFolder) {
        simpleBlockWithItem(block, models()
                .withExistingParent(key(block).getPath(), ModelProvider.BLOCK_FOLDER + "/" + modelName)
                .texture(textureKey, blockTexture(block, textureFolder)));
    }

    public ResourceLocation key(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    public String name(Block block) {
        return key(block).getPath();
    }

    public ResourceLocation extend(ResourceLocation rl, String suffix) {
        return ResourceLocation.fromNamespaceAndPath(rl.getNamespace(), rl.getPath() + suffix);
    }

    private class ModelBuilder {
        private final Block block;

        private boolean active;
        private boolean horizontalFacing;
        private boolean facing;
        private boolean cutout;
        private ResourceLocation up;
        private ResourceLocation down;
        private ResourceLocation north;
        private ResourceLocation east;
        private ResourceLocation south;
        private ResourceLocation west;
        private ResourceLocation defaultTexture;
        private ResourceLocation particle;

        public ModelBuilder(DeferredBlock<?> block) {
            this.block = block.get();
            this.defaultTexture = blockTexture(IRBlocks.BASIC_MACHINE_FRAME.get());
        }

        public ModelBuilder cutout() {
            this.cutout = true;
            return this;
        }

        public ModelBuilder active() {
            this.active = true;
            return this;
        }

        public ModelBuilder facing() {
            this.facing = true;
            return this;
        }

        public ModelBuilder horizontalFacing() {
            this.horizontalFacing = true;
            return this;
        }

        public ModelBuilder front(ResourceLocation frontTexture) {
            this.north = frontTexture;
            return this;
        }

        public ModelBuilder front(Function<Block, ResourceLocation> frontTexture) {
            return front(frontTexture.apply(this.block));
        }

        public ModelBuilder front(BiFunction<Block, String, ResourceLocation> frontTexture, String suffix) {
            return front(frontTexture.apply(this.block, suffix));
        }

        public ModelBuilder back(ResourceLocation backTexture) {
            this.south = backTexture;
            return this;
        }

        public ModelBuilder back(Function<Block, ResourceLocation> backTexture) {
            return back(backTexture.apply(this.block));
        }

        public ModelBuilder back(BiFunction<Block, String, ResourceLocation> backTexture, String suffix) {
            return back(backTexture.apply(this.block, suffix));
        }

        public ModelBuilder sides(ResourceLocation sidesTexture) {
            this.north = sidesTexture;
            this.east = sidesTexture;
            this.south = sidesTexture;
            this.west = sidesTexture;
            return this;
        }

        public ModelBuilder sides(Function<Block, ResourceLocation> sidesTexture) {
            return sides(sidesTexture.apply(this.block));
        }

        public ModelBuilder sides(BiFunction<Block, String, ResourceLocation> sidesTexture, String suffix) {
            return sides(sidesTexture.apply(this.block, suffix));
        }

        public ModelBuilder top(ResourceLocation topTexture) {
            this.up = topTexture;
            return this;
        }

        public ModelBuilder top(Function<Block, ResourceLocation> topTexture) {
            return top(topTexture.apply(this.block));
        }

        public ModelBuilder top(BiFunction<Block, String, ResourceLocation> topTexture, String suffix) {
            return top(topTexture.apply(this.block, suffix));
        }

        public ModelBuilder bottom(ResourceLocation bottomTexture) {
            this.down = bottomTexture;
            return this;
        }

        public ModelBuilder bottom(Function<Block, ResourceLocation> bottomTexture) {
            return bottom(bottomTexture.apply(this.block));
        }

        public ModelBuilder bottom(BiFunction<Block, String, ResourceLocation> bottomTexture, String suffix) {
            return bottom(bottomTexture.apply(this.block, suffix));
        }

        public ModelBuilder defaultTexture(ResourceLocation defaultTexture) {
            this.defaultTexture = defaultTexture;
            return this;
        }

        public ModelBuilder defaultTexture(Function<Block, ResourceLocation> defaultTexture) {
            return defaultTexture(defaultTexture.apply(this.block));
        }

        public ModelBuilder defaultTexture(BiFunction<Block, String, ResourceLocation> defaultTexture, String suffix) {
            return defaultTexture(defaultTexture.apply(this.block, suffix));
        }

        public ModelBuilder particle(ResourceLocation defaultTexture) {
            this.particle = defaultTexture;
            return this;
        }

        public ModelBuilder particle(Function<Block, ResourceLocation> particleTexture) {
            return particle(particleTexture.apply(this.block));
        }

        public ModelBuilder particle(BiFunction<Block, String, ResourceLocation> particleTexture, String suffix) {
            return particle(particleTexture.apply(this.block, suffix));
        }

        public void create() {
            BlockModelBuilder activeBuilder = models().withExistingParent(name(block) + "_active", "cube");
            if (this.active) {
                activeBuilder.texture("down", activeTextureOrDefault(this.down));
                activeBuilder.texture("up", activeTextureOrDefault(this.up));
                activeBuilder.texture("north", activeTextureOrDefault(this.north));
                activeBuilder.texture("east", activeTextureOrDefault(this.east));
                activeBuilder.texture("south", activeTextureOrDefault(this.south));
                activeBuilder.texture("west", activeTextureOrDefault(this.west));
                activeBuilder.texture("particle", textureOrDefault(this.particle));
            }
            BlockModelBuilder inactiveBuilder = models().withExistingParent(name(block), "cube");
            inactiveBuilder.texture("down", textureOrDefault(this.down));
            inactiveBuilder.texture("up", textureOrDefault(this.up));
            inactiveBuilder.texture("north", textureOrDefault(this.north));
            inactiveBuilder.texture("east", textureOrDefault(this.east));
            inactiveBuilder.texture("south", textureOrDefault(this.south));
            inactiveBuilder.texture("west", textureOrDefault(this.west));
            inactiveBuilder.texture("particle", textureOrDefault(this.particle));
            if (cutout) {
                activeBuilder.renderType("cutout");
                inactiveBuilder.renderType("cutout");
            }
            createBlockState(activeBuilder, inactiveBuilder);
        }

        private ResourceLocation activeTextureOrDefault(ResourceLocation texture) {
            return texture != null ? extend(texture, "_active") : defaultTexture;
        }

        private ResourceLocation textureOrDefault(ResourceLocation texture) {
            return texture != null ? texture : defaultTexture;
        }

        private void createBlockState(BlockModelBuilder activeBuilder, BlockModelBuilder inactiveBuilder) {
            VariantBlockStateBuilder builder = getVariantBuilder(block);
            if (this.facing) {
                for (Direction dir : BlockStateProperties.FACING.getPossibleValues()) {
                    if (this.active) {
                        builder.partialState().with(ACTIVE, true).with(BlockStateProperties.FACING, dir)
                                .modelForState()
                                .modelFile(activeBuilder)
                                .rotationX(dir == Direction.DOWN ? 180 : dir.getAxis().isHorizontal() ? 90 : 0)
                                .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                                .addModel()
                                .partialState().with(ACTIVE, false).with(BlockStateProperties.FACING, dir)
                                .modelForState()
                                .modelFile(inactiveBuilder)
                                .rotationX(dir == Direction.DOWN ? 180 : dir.getAxis().isHorizontal() ? 90 : 0)
                                .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                                .addModel();
                    } else {
                        builder.partialState().with(BlockStateProperties.FACING, dir)
                                .modelForState()
                                .modelFile(inactiveBuilder)
                                .rotationX(dir == Direction.DOWN ? 180 : dir.getAxis().isHorizontal() ? 90 : 0)
                                .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                                .addModel();;
                    }
                }
            } else if (this.horizontalFacing) {
                for (Direction dir : BlockStateProperties.HORIZONTAL_FACING.getPossibleValues()) {
                    if (this.active) {
                        builder.partialState().with(ACTIVE, true).with(BlockStateProperties.HORIZONTAL_FACING, dir)
                                .modelForState().modelFile(activeBuilder).rotationY(((int) dir.toYRot() + 180) % 360).addModel()
                                .partialState().with(ACTIVE, false).with(BlockStateProperties.HORIZONTAL_FACING, dir)
                                .modelForState().modelFile(inactiveBuilder).rotationY(((int) dir.toYRot() + 180) % 360).addModel();
                    } else {
                        builder.partialState().with(BlockStateProperties.HORIZONTAL_FACING, dir)
                                .modelForState().modelFile(inactiveBuilder).rotationY(((int) dir.toYRot() + 180) % 360).addModel();
                    }
                }
            } else if (this.active) {
                builder.partialState().with(ACTIVE, true)
                        .modelForState().modelFile(activeBuilder).addModel()
                        .partialState().with(ACTIVE, false)
                        .modelForState().modelFile(inactiveBuilder).addModel();
            } else {
                builder.partialState().modelForState().modelFile(inactiveBuilder).addModel();
            }
        }
    }
}
