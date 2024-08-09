package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.registries.blocks.*;
import com.indref.industrial_reforged.registries.blocks.machines.BasicGeneratorBlock;
import com.indref.industrial_reforged.registries.blocks.machines.CentrifugeBlock;
import com.indref.industrial_reforged.registries.blocks.machines.CraftingStationBlock;
import com.indref.industrial_reforged.registries.blocks.machines.DrainBlock;
import com.indref.industrial_reforged.registries.blocks.multiblocks.*;
import com.indref.industrial_reforged.registries.blocks.trees.RubberTreeLeavesBlock;
import com.indref.industrial_reforged.registries.blocks.trees.RubberTreeLogBlock;
import com.indref.industrial_reforged.registries.blocks.trees.RubberTreeResinHoleBlock;
import com.indref.industrial_reforged.tiers.CrucibleTiers;
import com.indref.industrial_reforged.tiers.EnergyTiers;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class IRBlocks {
    public static final BlockSetType RUBBER_SET_TYPE = BlockSetType.register(new BlockSetType(IndustrialReforged.MODID + ":rubber"));
    public static final WoodType RUBBER_WOOD_TYPE = WoodType.register(new WoodType(IndustrialReforged.MODID + ":rubber", RUBBER_SET_TYPE));
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(IndustrialReforged.MODID);

    public static final DeferredBlock<CableBlock> TIN_CABLE = registerBlockAndItem("tin_cable",
            () -> new CableBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOL), 6, EnergyTiers.LOW));
    public static final DeferredBlock<CableBlock> COPPER_CABLE = registerBlockAndItem("copper_cable",
            () -> new CableBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOL), 6, EnergyTiers.MEDIUM));
    public static final DeferredBlock<CableBlock> GOLD_CABLE = registerBlockAndItem("gold_cable",
            () -> new CableBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOL), 6, EnergyTiers.HIGH));
    public static final DeferredBlock<CableBlock> STEEL_CABLE = registerBlockAndItem("steel_cable",
            () -> new CableBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOL), 6, EnergyTiers.EXTREME));
    public static final DeferredBlock<MiningPipeBlock> MINING_PIPE = registerBlock("mining_pipe",
            () -> new MiningPipeBlock(BlockBehaviour.Properties.of().noOcclusion()));
    public static final DeferredBlock<RefractoryBrickBlock> REFRACTORY_BRICK = registerBlockAndItem("refractory_brick",
            () -> new RefractoryBrickBlock(BlockBehaviour.Properties.of().sound(SoundType.STONE)));
    public static final DeferredBlock<BlastFurnaceBricksBlock> BLAST_FURNACE_BRICKS = registerBlockAndItem("blast_furnace_bricks",
            () -> new BlastFurnaceBricksBlock(BlockBehaviour.Properties.of().sound(SoundType.STONE)));
    public static final DeferredBlock<BlastFurnaceHatchBlock> BLAST_FURNACE_HATCH = registerBlockAndItem("blast_furnace_hatch",
            () -> new BlastFurnaceHatchBlock(BlockBehaviour.Properties.of().sound(SoundType.STONE)));
    public static final DeferredBlock<SmallFireboxHatchBlock> SMALL_FIREBOX_HATCH = registerBlockAndItem("small_firebox_hatch",
            () -> new SmallFireboxHatchBlock(BlockBehaviour.Properties.of().sound(SoundType.METAL)));
    public static final DeferredBlock<Block> REFRACTORY_STONE = registerBlockAndItem("refractory_stone",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.STONE)));
    public static final DeferredBlock<CoilBlock> COIL = registerBlockAndItem("coil",
            () -> new CoilBlock(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<MachineFrameBlock> BASIC_MACHINE_FRAME = registerBlockAndItem("basic_machine_frame",
            () -> new MachineFrameBlock(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<SlabBlock> TERRACOTTA_BRICK_SLAB = registerBlockAndItem("terracotta_brick_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<Block> TERRACOTTA_BRICK = registerBlockAndItem("terracotta_brick",
            () -> new Block(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<CrucibleControllerBlock> CERAMIC_CRUCIBLE_CONTROLLER = registerBlockAndItem("ceramic_crucible_controller",
            () -> new CrucibleControllerBlock(BlockBehaviour.Properties.of().noOcclusion(), CrucibleTiers.CERAMIC));
    public static final DeferredBlock<CrucibleWallBlock> CERAMIC_CRUCIBLE_WALL = registerBlockAndItem("ceramic_crucible_wall",
            () -> new CrucibleWallBlock(BlockBehaviour.Properties.of().noOcclusion(), CrucibleTiers.CERAMIC));
    public static final DeferredBlock<FaucetBlock> CERAMIC_FAUCET = registerBlockAndItem("ceramic_faucet",
            () -> new FaucetBlock(BlockBehaviour.Properties.of(), IRBlocks.TERRACOTTA_BRICK.get()));
    public static final DeferredBlock<FaucetBlock> BLAST_FURNACE_FAUCET = registerBlockAndItem("blast_furnace_faucet",
            () -> new FaucetBlock(BlockBehaviour.Properties.of(), IRBlocks.BLAST_FURNACE_BRICKS.get()));
    public static final DeferredBlock<CastingBasinBlock> CERAMIC_CASTING_BASIN = registerBlockAndItem("ceramic_casting_basin",
            () -> new CastingBasinBlock(BlockBehaviour.Properties.of().noOcclusion(), IRBlocks.TERRACOTTA_BRICK.get()));
    public static final DeferredBlock<CastingBasinBlock> SANDY_CASTING_BASIN = registerBlockAndItem("sandy_casting_basin",
            () -> new CastingBasinBlock(BlockBehaviour.Properties.of().noOcclusion(), IRBlocks.BLAST_FURNACE_BRICKS.get()));
    public static final DeferredBlock<CentrifugeBlock> CENTRIFUGE = registerBlockAndItem("centrifuge",
            () -> new CentrifugeBlock(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<BasicGeneratorBlock> BASIC_GENERATOR = registerBlockAndItem("basic_generator",
            () -> new BasicGeneratorBlock(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<DrainBlock> DRAIN = registerBlockAndItem("drain",
            () -> new DrainBlock(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<CraftingStationBlock> CRAFTING_STATION = registerBlockAndItem("crafting_station",
            () -> new CraftingStationBlock(BlockBehaviour.Properties.of()));

    // Rubber
    public static final DeferredBlock<RubberTreeLogBlock> RUBBER_TREE_LOG = registerBlockAndItem("rubber_tree_log",
            RubberTreeLogBlock::new);
    public static final DeferredBlock<RubberTreeLogBlock> STRIPPED_RUBBER_TREE_LOG = registerBlockAndItem("stripped_rubber_tree_log",
            RubberTreeLogBlock::new);
    public static final DeferredBlock<RubberTreeLogBlock> RUBBER_TREE_WOOD = registerBlockAndItem("rubber_tree_wood",
            RubberTreeLogBlock::new);
    public static final DeferredBlock<RubberTreeLogBlock> STRIPPED_RUBBER_TREE_WOOD = registerBlockAndItem("stripped_rubber_tree_wood",
            RubberTreeLogBlock::new);
    public static final DeferredBlock<RubberTreeLeavesBlock> RUBBER_TREE_LEAVES = registerBlockAndItem("rubber_tree_leaves",
            RubberTreeLeavesBlock::new);
    public static final DeferredBlock<SaplingBlock> RUBBER_TREE_SAPLING = registerBlockAndItem("rubber_tree_sapling",
            () -> new SaplingBlock(IRTreeGrowers.RUBBER, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)));
    public static final DeferredBlock<RubberTreeResinHoleBlock> RUBBER_TREE_RESIN_HOLE = registerBlockAndItem("rubber_tree_resin_hole",
            RubberTreeResinHoleBlock::new);
    public static final DeferredBlock<Block> RUBBER_TREE_PLANKS = registerBlockAndItem("rubber_tree_planks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final DeferredBlock<StairBlock> RUBBER_TREE_STAIRS = registerBlockAndItem("rubber_tree_stairs",
            () -> new StairBlock(RUBBER_TREE_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS)));
    public static final DeferredBlock<DoorBlock> RUBBER_TREE_DOOR = registerBlockAndItem("rubber_tree_door",
            () -> new DoorBlock(RUBBER_SET_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR)));
    public static final DeferredBlock<PressurePlateBlock> RUBBER_TREE_PRESSURE_PLATE = registerBlockAndItem("rubber_tree_pressure_plate",
            () -> new PressurePlateBlock(RUBBER_SET_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE)));
    public static final DeferredBlock<FenceBlock> RUBBER_TREE_FENCE = registerBlockAndItem("rubber_tree_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR)));
    public static final DeferredBlock<TrapDoorBlock> RUBBER_TREE_TRAPDOOR = registerBlockAndItem("rubber_tree_trapdoor",
            () -> new TrapDoorBlock(RUBBER_SET_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR)));
    public static final DeferredBlock<FenceGateBlock> RUBBER_TREE_FENCE_GATE = registerBlockAndItem("rubber_tree_fence_gate",
            () -> new FenceGateBlock(RUBBER_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE)));
    public static final DeferredBlock<ButtonBlock> RUBBER_TREE_BUTTON = registerBlockAndItem("rubber_tree_button",
            () -> new ButtonBlock(RUBBER_SET_TYPE, 30, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON)));
    public static final DeferredBlock<SlabBlock> RUBBER_TREE_SLAB = registerBlockAndItem("rubber_tree_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB)));

    // Ores
    public static final DeferredBlock<DropExperienceBlock> BAUXITE_ORE = registerBlockAndItem("bauxite_ore",
            () -> new DropExperienceBlock(ConstantInt.of(1), oreSettings(false)));
    public static final DeferredBlock<DropExperienceBlock> DEEPSLATE_BAUXITE_ORE = registerBlockAndItem("deepslate_bauxite_ore",
            () -> new DropExperienceBlock(ConstantInt.of(1), oreSettings(true)));
    public static final DeferredBlock<DropExperienceBlock> CHROMIUM_ORE = registerBlockAndItem("chromium_ore",
            () -> new DropExperienceBlock(ConstantInt.of(1), oreSettings(false)));
    public static final DeferredBlock<DropExperienceBlock> DEEPSLATE_CHROMIUM_ORE = registerBlockAndItem("deepslate_chromium_ore",
            () -> new DropExperienceBlock(ConstantInt.of(1), oreSettings(true)));
    public static final DeferredBlock<DropExperienceBlock> IRIDIUM_ORE = registerBlockAndItem("iridium_ore",
            () -> new DropExperienceBlock(ConstantInt.of(1), oreSettings(false)));
    public static final DeferredBlock<DropExperienceBlock> DEEPSLATE_IRIDIUM_ORE = registerBlockAndItem("deepslate_iridium_ore",
            () -> new DropExperienceBlock(ConstantInt.of(1), oreSettings(true)));
    public static final DeferredBlock<DropExperienceBlock> LEAD_ORE = registerBlockAndItem("lead_ore",
            () -> new DropExperienceBlock(ConstantInt.of(1), oreSettings(false)));
    public static final DeferredBlock<DropExperienceBlock> DEEPSLATE_LEAD_ORE = registerBlockAndItem("deepslate_lead_ore",
            () -> new DropExperienceBlock(ConstantInt.of(1), oreSettings(true)));
    public static final DeferredBlock<DropExperienceBlock> NICKEL_ORE = registerBlockAndItem("nickel_ore",
            () -> new DropExperienceBlock(ConstantInt.of(1), oreSettings(false)));
    public static final DeferredBlock<DropExperienceBlock> DEEPSLATE_NICKEL_ORE = registerBlockAndItem("deepslate_nickel_ore",
            () -> new DropExperienceBlock(ConstantInt.of(1), oreSettings(true)));
    public static final DeferredBlock<DropExperienceBlock> TIN_ORE = registerBlockAndItem("tin_ore",
            () -> new DropExperienceBlock(ConstantInt.of(1), oreSettings(false)));
    public static final DeferredBlock<DropExperienceBlock> DEEPSLATE_TIN_ORE = registerBlockAndItem("deepslate_tin_ore",
            () -> new DropExperienceBlock(ConstantInt.of(1), oreSettings(true)));
    public static final DeferredBlock<DropExperienceBlock> URANIUM_ORE = registerBlockAndItem("uranium_ore",
            () -> new DropExperienceBlock(ConstantInt.of(1), oreSettings(false)));
    public static final DeferredBlock<DropExperienceBlock> DEEPSLATE_URANIUM_ORE = registerBlockAndItem("deepslate_uranium_ore",
            () -> new DropExperienceBlock(ConstantInt.of(1), oreSettings(true)));

    // Metal storage blocks
    public static final DeferredBlock<MetalStorageBlock> ALUMINUM_BLOCK = registerBlockAndItem("aluminum_block", MetalStorageBlock::new);
    public static final DeferredBlock<RawOreStorageBlock> RAW_BAUXITE_BLOCK = registerBlockAndItem("raw_bauxite_block", RawOreStorageBlock::new);
    public static final DeferredBlock<MetalStorageBlock> CHROMIUM_BLOCK = registerBlockAndItem("chromium_block", MetalStorageBlock::new);
    public static final DeferredBlock<RawOreStorageBlock> RAW_CHROMIUM_BLOCK = registerBlockAndItem("raw_chromium_block", RawOreStorageBlock::new);
    public static final DeferredBlock<MetalStorageBlock> IRIDIUM_BLOCK = registerBlockAndItem("iridium_block", MetalStorageBlock::new);
    public static final DeferredBlock<RawOreStorageBlock> RAW_IRIDIUM_BLOCK = registerBlockAndItem("raw_iridium_block", RawOreStorageBlock::new);
    public static final DeferredBlock<MetalStorageBlock> LEAD_BLOCK = registerBlockAndItem("lead_block", MetalStorageBlock::new);
    public static final DeferredBlock<RawOreStorageBlock> RAW_LEAD_BLOCK = registerBlockAndItem("raw_lead_block", RawOreStorageBlock::new);
    public static final DeferredBlock<MetalStorageBlock> NICKEL_BLOCK = registerBlockAndItem("nickel_block", MetalStorageBlock::new);
    public static final DeferredBlock<RawOreStorageBlock> RAW_NICKEL_BLOCK = registerBlockAndItem("raw_nickel_block", RawOreStorageBlock::new);
    public static final DeferredBlock<MetalStorageBlock> TIN_BLOCK = registerBlockAndItem("tin_block", MetalStorageBlock::new);
    public static final DeferredBlock<RawOreStorageBlock> RAW_TIN_BLOCK = registerBlockAndItem("raw_tin_block", RawOreStorageBlock::new);
    public static final DeferredBlock<MetalStorageBlock> TITANIUM_BLOCK = registerBlockAndItem("titanium_block", MetalStorageBlock::new);
    public static final DeferredBlock<MetalStorageBlock> URANIUM_BLOCK = registerBlockAndItem("uranium_block", MetalStorageBlock::new);
    public static final DeferredBlock<RawOreStorageBlock> RAW_URANIUM_BLOCK = registerBlockAndItem("raw_uranium_block", RawOreStorageBlock::new);
    public static final DeferredBlock<MetalStorageBlock> STEEL_BLOCK = registerBlockAndItem("steel_block", MetalStorageBlock::new);

    public static final DeferredBlock<LiquidBlock> MOLTEN_STEEL_FLUID_BLOCK = BLOCKS.register("molten_steel_block",
            () -> new LiquidBlock(IRFluids.MOLTEN_STEEL_SOURCE.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.LAVA)));
    public static final DeferredBlock<LiquidBlock> OIL_FLUID_BLOCK = BLOCKS.register("oil_block",
            () -> new LiquidBlock(IRFluids.OIL_SOURCE.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.LAVA)));

    /**
     * Registers a new block and item
     *
     * @param name  name of the block
     * @param block the Blocks you want to add and configure using `new {@link net.minecraft.world.item.Item.Properties}()`
     * @return returns the block-registry-object that can then be used to refer to the block
     */
    private static <T extends Block> DeferredBlock<T> registerBlockAndItem(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerItemFromBlock(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    private static <T extends Block> void registerItemFromBlock(String name, Supplier<T> block) {
        IRItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static BlockBehaviour.Properties oreSettings(boolean deepslate) {
        if (deepslate) {
            return BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).strength(4.5F, 3.0F)
                    .requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM);
        }
        return BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(3.0F, 3.0F)
                .requiresCorrectToolForDrops().instrument(NoteBlockInstrument.BASEDRUM);
    }
}
