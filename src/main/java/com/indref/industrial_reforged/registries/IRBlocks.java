package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.registries.blocks.*;
import com.indref.industrial_reforged.registries.blocks.machines.BasicGeneratorBlock;
import com.indref.industrial_reforged.registries.blocks.machines.CentrifugeMachineBlock;
import com.indref.industrial_reforged.registries.blocks.machines.CraftingStationBlock;
import com.indref.industrial_reforged.registries.blocks.machines.DrainBlock;
import com.indref.industrial_reforged.registries.blocks.multiblocks.*;
import com.indref.industrial_reforged.registries.blocks.trees.RubberTreeLeavesBlock;
import com.indref.industrial_reforged.registries.blocks.trees.RubberTreeLogBlock;
import com.indref.industrial_reforged.registries.blocks.trees.RubberTreeResinHoleBlock;
import com.indref.industrial_reforged.tiers.CrucibleTiers;
import com.indref.industrial_reforged.tiers.EnergyTiers;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class IRBlocks {
	public static final BlockSetType RUBBER_SET_TYPE = BlockSetType.register(new BlockSetType(IndustrialReforged.MODID + ":rubber"));
	public static final WoodType RUBBER_WOOD_TYPE = WoodType.register(new WoodType(IndustrialReforged.MODID + ":rubber", RUBBER_SET_TYPE));
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, IndustrialReforged.MODID);

    public static final Supplier<Block> TIN_CABLE = registerBlockAndItem("tin_cable",
            () -> new CableBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOL), 6, EnergyTiers.LOW));
    public static final Supplier<Block> COPPER_CABLE = registerBlockAndItem("copper_cable",
            () -> new CableBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOL), 6, EnergyTiers.MEDIUM));
    public static final Supplier<Block> GOLD_CABLE = registerBlockAndItem("gold_cable",
            () -> new CableBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOL), 6, EnergyTiers.HIGH));
    public static final Supplier<Block> STEEL_CABLE = registerBlockAndItem("steel_cable",
            () -> new CableBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOL), 6, EnergyTiers.EXTREME));
    public static final Supplier<Block> MINING_PIPE = registerBlock("mining_pipe",
            () -> new MiningPipeBlock(BlockBehaviour.Properties.of().noOcclusion()));
    public static final Supplier<Block> REFRACTORY_BRICK = registerBlockAndItem("refractory_brick",
            () -> new RefractoryBrickBlock(BlockBehaviour.Properties.of().sound(SoundType.STONE)));
    public static final Supplier<Block> REFRACTORY_STONE = registerBlockAndItem("refractory_stone",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.STONE)));
    public static final Supplier<Block> COIL = registerBlockAndItem("coil",
            () -> new CoilBlock(BlockBehaviour.Properties.of()));
    public static final Supplier<Block> BASIC_MACHINE_FRAME = registerBlockAndItem("basic_machine_frame",
            () -> new MachineFrameBlock(BlockBehaviour.Properties.of()));
    public static final Supplier<Block> TERRACOTTA_BRICK_SLAB = registerBlockAndItem("terracotta_brick_slab",
            () -> new TerracottaSlabBlock(BlockBehaviour.Properties.of()));
    public static final Supplier<Block> TERRACOTTA_BRICK = registerBlockAndItem("terracotta_brick",
            () -> new Block(BlockBehaviour.Properties.of()));
    public static final Supplier<Block> CERAMIC_CRUCIBLE_CONTROLLER = registerBlockAndItem("ceramic_crucible_controller",
            () -> new CrucibleControllerBlock(BlockBehaviour.Properties.of(), CrucibleTiers.CERAMIC));
    public static final Supplier<Block> CERAMIC_CRUCIBLE_WALL= registerBlockAndItem("ceramic_crucible_wall",
            () -> new CrucibleWallBlock(BlockBehaviour.Properties.of().noOcclusion(), CrucibleTiers.CERAMIC));
    public static final Supplier<Block> CERAMIC_FAUCET= registerBlockAndItem("ceramic_faucet",
            () -> new FaucetBlock(BlockBehaviour.Properties.of().noOcclusion()));
    public static final Supplier<Block> CERAMIC_CASTING_TABLE = registerBlockAndItem("ceramic_casting_table",
            () -> new CastingTableBlock(BlockBehaviour.Properties.of().noOcclusion()));
    public static final Supplier<Block> TEST_GENERATOR = registerBlockAndItem("test_generator",
            () -> new TestGeneratorBlock(BlockBehaviour.Properties.of()));
    public static final Supplier<Block> CENTRIFUGE = registerBlockAndItem("centrifuge",
            () -> new CentrifugeMachineBlock(BlockBehaviour.Properties.of()));
    public static final Supplier<Block> BASIC_GENERATOR = registerBlockAndItem("basic_generator",
            () -> new BasicGeneratorBlock(BlockBehaviour.Properties.of()));
    public static final Supplier<Block> DRAIN = registerBlockAndItem("drain",
            () -> new DrainBlock(BlockBehaviour.Properties.of()));
    public static final Supplier<Block> CRAFTING_STATION = registerBlockAndItem("crafting_station",
            () -> new CraftingStationBlock(BlockBehaviour.Properties.of()));

    // Rubber
    public static final Supplier<Block> RUBBER_TREE_LOG = registerBlockAndItem("rubber_tree_log",
            RubberTreeLogBlock::new);
    public static final Supplier<Block> STRIPPED_RUBBER_TREE_LOG = registerBlockAndItem("stripped_rubber_tree_log",
            RubberTreeLogBlock::new);
    public static final Supplier<Block> RUBBER_TREE_WOOD = registerBlockAndItem("rubber_tree_wood",
            RubberTreeLogBlock::new);
    public static final Supplier<Block> STRIPPED_RUBBER_TREE_WOOD = registerBlockAndItem("stripped_rubber_tree_wood",
            RubberTreeLogBlock::new);
    public static final Supplier<Block> RUBBER_TREE_LEAVES = registerBlockAndItem("rubber_tree_leaves",
            RubberTreeLeavesBlock::new);
    public static final Supplier<Block> RUBBER_TREE_SAPLING = registerBlockAndItem("rubber_tree_sapling",
            () -> new SaplingBlock(IRTreeGrowers.RUBBER, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)));
    public static final Supplier<Block> RUBBER_TREE_RESIN_HOLE = registerBlockAndItem("rubber_tree_resin_hole",
            RubberTreeResinHoleBlock::new);
    public static final Supplier<Block> RUBBER_TREE_PLANKS = registerBlockAndItem("rubber_tree_planks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    @SuppressWarnings("deprecation")
	public static final Supplier<Block> RUBBER_TREE_STAIRS = registerBlockAndItem("rubber_tree_stairs",
    		() -> new StairBlock(RUBBER_TREE_PLANKS.get().defaultBlockState(),BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS)));
    public static final Supplier<Block> RUBBER_TREE_DOOR = registerBlockAndItem("rubber_tree_door",
    		() -> new DoorBlock(RUBBER_SET_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR)));
    public static final Supplier<Block> RUBBER_TREE_PRESSURE_PLATE = registerBlockAndItem("rubber_tree_pressure_plate",
    		() -> new PressurePlateBlock(RUBBER_SET_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE)));
    public static final Supplier<Block> RUBBER_TREE_FENCE = registerBlockAndItem("rubber_tree_fence",
    		() -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR)));
    public static final Supplier<Block> RUBBER_TREE_TRAPDOOR = registerBlockAndItem("rubber_tree_trapdoor",
    		() -> new TrapDoorBlock(RUBBER_SET_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR)));
    public static final Supplier<Block> RUBBER_TREE_FENCE_GATE = registerBlockAndItem("rubber_tree_fence_gate",
    		() -> new FenceGateBlock(RUBBER_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE)));
    public static final Supplier<Block> RUBBER_TREE_BUTTON = registerBlockAndItem("rubber_tree_button",
    		() -> new ButtonBlock(RUBBER_SET_TYPE, 30, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON)));
    public static final Supplier<Block> RUBBER_TREE_SLAB = registerBlockAndItem("rubber_tree_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB)));

    // Ores
    public static final Supplier<Block> BAUXITE_ORE = registerBlockAndItem("bauxite_ore",
            () -> new DropExperienceBlock(ConstantInt.of(1), oreSettings(false)));
    public static final Supplier<Block> DEEPSLATE_BAUXITE_ORE = registerBlockAndItem("deepslate_bauxite_ore",
            () -> new DropExperienceBlock(ConstantInt.of(1), oreSettings(true)));
    public static final Supplier<Block> CHROMIUM_ORE = registerBlockAndItem("chromium_ore",
            () -> new DropExperienceBlock(ConstantInt.of(1), oreSettings(false)));
    public static final Supplier<Block> DEEPSLATE_CHROMIUM_ORE = registerBlockAndItem("deepslate_chromium_ore",
            () -> new DropExperienceBlock(ConstantInt.of(1), oreSettings(true)));
    public static final Supplier<Block> IRIDIUM_ORE = registerBlockAndItem("iridium_ore",
            () -> new DropExperienceBlock(ConstantInt.of(1), oreSettings(false)));
    public static final Supplier<Block> DEEPSLATE_IRIDIUM_ORE = registerBlockAndItem("deepslate_iridium_ore",
            () -> new DropExperienceBlock(ConstantInt.of(1), oreSettings(true)));
    public static final Supplier<Block> LEAD_ORE = registerBlockAndItem("lead_ore",
            () -> new DropExperienceBlock(ConstantInt.of(1), oreSettings(false)));
    public static final Supplier<Block> DEEPSLATE_LEAD_ORE = registerBlockAndItem("deepslate_lead_ore",
            () -> new DropExperienceBlock(ConstantInt.of(1), oreSettings(true)));
    public static final Supplier<Block> NICKEL_ORE = registerBlockAndItem("nickel_ore",
            () -> new DropExperienceBlock(ConstantInt.of(1), oreSettings(false)));
    public static final Supplier<Block> DEEPSLATE_NICKEL_ORE = registerBlockAndItem("deepslate_nickel_ore",
            () -> new DropExperienceBlock(ConstantInt.of(1), oreSettings(true)));
    public static final Supplier<Block> TIN_ORE = registerBlockAndItem("tin_ore",
            () -> new DropExperienceBlock(ConstantInt.of(1), oreSettings(false)));
    public static final Supplier<Block> DEEPSLATE_TIN_ORE = registerBlockAndItem("deepslate_tin_ore",
            () -> new DropExperienceBlock(ConstantInt.of(1), oreSettings(true)));
    public static final Supplier<Block> URANIUM_ORE = registerBlockAndItem("uranium_ore",
            () -> new DropExperienceBlock(ConstantInt.of(1), oreSettings(false)));
    public static final Supplier<Block> DEEPSLATE_URANIUM_ORE = registerBlockAndItem("deepslate_uranium_ore",
            () -> new DropExperienceBlock(ConstantInt.of(1), oreSettings(true)));

    // Metal storage blocks
    public static final Supplier<Block> ALUMINUM_BLOCK = registerBlockAndItem("aluminum_block", MetalStorageBlock::new);
    public static final Supplier<Block> RAW_BAUXITE_BLOCK = registerBlockAndItem("raw_bauxite_block", RawOreStorageBlock::new);
    public static final Supplier<Block> CHROMIUM_BLOCK = registerBlockAndItem("chromium_block", MetalStorageBlock::new);
    public static final Supplier<Block> RAW_CHROMIUM_BLOCK = registerBlockAndItem("raw_chromium_block", RawOreStorageBlock::new);
    public static final Supplier<Block> IRIDIUM_BLOCK = registerBlockAndItem("iridium_block", MetalStorageBlock::new);
    public static final Supplier<Block> RAW_IRIDIUM_BLOCK = registerBlockAndItem("raw_iridium_block", RawOreStorageBlock::new);
    public static final Supplier<Block> LEAD_BLOCK = registerBlockAndItem("lead_block", MetalStorageBlock::new);
    public static final Supplier<Block> RAW_LEAD_BLOCK = registerBlockAndItem("raw_lead_block", RawOreStorageBlock::new);
    public static final Supplier<Block> NICKEL_BLOCK = registerBlockAndItem("nickel_block", MetalStorageBlock::new);
    public static final Supplier<Block> RAW_NICKEL_BLOCK = registerBlockAndItem("raw_nickel_block", RawOreStorageBlock::new);
    public static final Supplier<Block> TIN_BLOCK = registerBlockAndItem("tin_block", MetalStorageBlock::new);
    public static final Supplier<Block> RAW_TIN_BLOCK = registerBlockAndItem("raw_tin_block", RawOreStorageBlock::new);
    public static final Supplier<Block> TITANIUM_BLOCK = registerBlockAndItem("titanium_block", MetalStorageBlock::new);
    public static final Supplier<Block> URANIUM_BLOCK = registerBlockAndItem("uranium_block", MetalStorageBlock::new);
    public static final Supplier<Block> RAW_URANIUM_BLOCK = registerBlockAndItem("raw_uranium_block", RawOreStorageBlock::new);

    public static final Supplier<LiquidBlock> SOAP_WATER_BLOCK = BLOCKS.register("soap_water_block",
            () -> new LiquidBlock(IRFluids.SOURCE_SOAP_WATER, BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)));

    /**
     * Registers a new block and item
     *
     * @param name  name of the block
     * @param block the Blocks you want to add and configure using `new {@link net.minecraft.world.item.Item.Properties}()`
     * @return returns the block-registry-object that can then be used to refer to the block
     */
    private static Supplier<Block> registerBlockAndItem(String name, Supplier<Block> block) {
        Supplier<Block> toReturn = BLOCKS.register(name, block);
        registerItemFromBlock(name, toReturn);
        return toReturn;
    }

    private static Supplier<Block> registerBlock(String name, Supplier<Block> block) {
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
