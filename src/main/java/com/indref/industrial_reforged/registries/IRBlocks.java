package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.content.blocks.*;
import com.indref.industrial_reforged.content.blocks.machines.primitive.*;
import com.indref.industrial_reforged.content.blocks.multiblocks.controller.BlastFurnaceHatchBlock;
import com.indref.industrial_reforged.content.blocks.multiblocks.controller.CrucibleControllerBlock;
import com.indref.industrial_reforged.content.blocks.multiblocks.controller.FireboxControllerBlock;
import com.indref.industrial_reforged.content.blocks.multiblocks.controller.SmallFireboxHatchBlock;
import com.indref.industrial_reforged.content.blocks.multiblocks.parts.BlastFurnacePartBlock;
import com.indref.industrial_reforged.content.blocks.multiblocks.parts.CruciblePartBlock;
import com.indref.industrial_reforged.content.blocks.multiblocks.parts.FireboxPartBlock;
import com.indref.industrial_reforged.content.blocks.pipes.CableBlock;
import com.indref.industrial_reforged.content.blocks.trees.RubberTreeLeavesBlock;
import com.indref.industrial_reforged.content.blocks.trees.RubberTreeLogBlock;
import com.indref.industrial_reforged.content.blocks.trees.RubberTreeResinHoleBlock;
import com.indref.industrial_reforged.content.multiblocks.tiers.CrucibleTiers;
import com.indref.industrial_reforged.content.multiblocks.tiers.FireboxTiers;
import com.indref.industrial_reforged.util.tabs.BlockTabOrdering;
import com.indref.industrial_reforged.util.tabs.ItemTabOrdering;
import com.indref.industrial_reforged.util.tabs.TabOrdering;
import com.indref.industrial_reforged.util.tabs.TabPosition;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public final class IRBlocks {
    public static final BlockSetType RUBBER_SET_TYPE = BlockSetType.register(new BlockSetType(IndustrialReforged.MODID + ":rubber"));
    public static final WoodType RUBBER_WOOD_TYPE = WoodType.register(new WoodType(IndustrialReforged.MODID + ":rubber", RUBBER_SET_TYPE));
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(IndustrialReforged.MODID);
    public static final Map<TabOrdering, Map<Integer, Supplier<? extends Block>>> TAB_BLOCKS = new HashMap<>();
    public static final List<DeferredBlock<Block>> METAL_STORAGE_BLOCKS = new ArrayList<>();
    public static final List<DeferredBlock<Block>> RAW_STORAGE_BLOCKS = new ArrayList<>();
    public static final Map<DeferredBlock<Block>, DeferredItem<?>> ORES = new HashMap<>();
    public static final List<DeferredBlock<?>> DROP_SELF_BLOCKS = new ArrayList<>();
    public static final List<DeferredBlock<?>> AXE_MINEABLE = new ArrayList<>();
    public static final List<DeferredBlock<?>> PICKAXE_MINEABLE = new ArrayList<>();

    // EARLY GAME BLOCKS
    public static final DeferredBlock<DrainBlock> DRAIN = pickaxeAxeMineable(registerBlockAndItem("drain",
            () -> new DrainBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRAFTER)), BlockTabOrdering.PRIMITIVE_MACHINES));
    public static final DeferredBlock<CraftingStationBlock> CRAFTING_STATION = axeMineable(registerBlockAndItem("crafting_station",
            () -> new CraftingStationBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRAFTING_TABLE)), BlockTabOrdering.PRIMITIVE_MACHINES));
    // BLAST FURNACE
    public static final DeferredBlock<Block> BLAST_FURNACE_BRICKS = pickaxeMineable(registerBlockAndItem("blast_furnace_bricks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.BRICKS).sound(SoundType.STONE)), BlockTabOrdering.BRICKS));
    public static final DeferredBlock<BlastFurnaceHatchBlock> BLAST_FURNACE_HATCH = pickaxeMineable(registerBlockAndItem("blast_furnace_hatch",
            () -> new BlastFurnaceHatchBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BRICKS).sound(SoundType.STONE)), BlockTabOrdering.HATCHES_COILS));
    public static final DeferredBlock<BlastFurnaceMultiblockBlock> BLAST_FURNACE_CONTROLLER = pickaxeMineable(multiblockControllerBlock("blast_furnace_controller",
            () -> new BlastFurnaceMultiblockBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BRICKS).sound(SoundType.STONE)), false));
    public static final DeferredBlock<BlastFurnacePartBlock> BLAST_FURNACE_PART = pickaxeMineable(registerBlock("blast_furnace_part",
            () -> new BlastFurnacePartBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BRICKS).sound(SoundType.STONE))));

    public static final DeferredBlock<SmallFireboxHatchBlock> SMALL_FIREBOX_HATCH = pickaxeMineable(registerBlockAndItem("small_firebox_hatch",
            () -> new SmallFireboxHatchBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.METAL)), BlockTabOrdering.HATCHES_COILS));
    public static final DeferredBlock<CoilBlock> COIL = pickaxeMineable(registerBlockAndItem("coil",
            () -> new CoilBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK)), BlockTabOrdering.HATCHES_COILS));
    public static final DeferredBlock<Block> REFRACTORY_BRICK = pickaxeMineable(registerBlockAndItem("refractory_brick",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE).mapColor(MapColor.GOLD)), BlockTabOrdering.HATCHES_COILS));
    public static final DeferredBlock<FireboxControllerBlock> FIREBOX_CONTROLLER = pickaxeMineable(multiblockControllerBlock("firebox_controller",
            () -> new FireboxControllerBlock(BlockBehaviour.Properties.ofFullCopy(COIL.get()), FireboxTiers.REFRACTORY), false));
    public static final DeferredBlock<FireboxPartBlock> FIREBOX_PART = pickaxeMineable(registerBlock("firebox_part",
            () -> new FireboxPartBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BRICKS))));
    public static final DeferredBlock<SlabBlock> TERRACOTTA_BRICK_SLAB = pickaxeMineable(registerBlockAndItem("terracotta_brick_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA)), BlockTabOrdering.BRICKS));
    public static final DeferredBlock<TerracottaBricks> TERRACOTTA_BRICKS = pickaxeMineable(registerBlockAndItem("terracotta_bricks",
            () -> new TerracottaBricks(BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA)), BlockTabOrdering.BRICKS));
    public static final DeferredBlock<CrucibleControllerBlock> CERAMIC_CRUCIBLE_CONTROLLER = pickaxeMineable(multiblockControllerBlock("ceramic_crucible_controller",
            () -> new CrucibleControllerBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA).noOcclusion(), CrucibleTiers.CERAMIC), false));
    public static final DeferredBlock<CruciblePartBlock> CERAMIC_CRUCIBLE_PART = pickaxeMineable(registerBlock("ceramic_crucible_wall",
            () -> new CruciblePartBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA).noOcclusion(), CrucibleTiers.CERAMIC)));
    public static final DeferredBlock<FaucetBlock> BLAST_FURNACE_FAUCET = pickaxeMineable(registerBlockAndItem("blast_furnace_faucet",
            () -> new FaucetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE), IRBlocks.BLAST_FURNACE_BRICKS.get()), BlockTabOrdering.FAUCET));
    public static final DeferredBlock<CastingBasinBlock> CERAMIC_CASTING_BASIN = pickaxeMineable(registerBlockAndItem("ceramic_casting_basin",
            () -> new CastingBasinBlock(BlockBehaviour.Properties.ofFullCopy(TERRACOTTA_BRICKS.get()), IRBlocks.TERRACOTTA_BRICKS.get()), BlockTabOrdering.CASTING_BASIN));
    public static final DeferredBlock<CastingBasinBlock> BLAST_FURNACE_CASTING_BASIN = pickaxeMineable(registerBlockAndItem("blast_furnace_casting_basin",
            () -> new CastingBasinBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE), IRBlocks.BLAST_FURNACE_BRICKS.get()), BlockTabOrdering.CASTING_BASIN));
    public static final DeferredBlock<WoodenBasinBlock> WOODEN_BASIN = axeMineable(registerBlockAndItem("wooden_basin",
            () -> new WoodenBasinBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL)), BlockTabOrdering.PRIMITIVE_MACHINES));
    // MACHINES
    public static final DeferredBlock<Block> BASIC_MACHINE_FRAME = pickaxeMineable(registerBlockAndItem("basic_machine_frame",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)), BlockTabOrdering.BASIC_MACHINES));
    public static final DeferredBlock<BatteryBoxBlock> BATTERY_BOX = pickaxeMineable(registerBlockAndItem("battery_box",
            () -> new BatteryBoxBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)), BlockTabOrdering.BASIC_MACHINES, false));
    public static final BlockBehaviour.Properties DEFAULT_BLOCK_PROPERTIES = BlockBehaviour.Properties.of()
            .sound(SoundType.WOOL)
            .mapColor(MapColor.COLOR_BLACK)
            .strength(0.8f);
    // CABLES
    public static final DeferredBlock<CableBlock> TIN_CABLE = pickaxeMineable(registerBlockAndItem("tin_cable",
            () -> new CableBlock(DEFAULT_BLOCK_PROPERTIES, 6, IREnergyTiers.LOW), BlockTabOrdering.CABLES, false));
    public static final DeferredBlock<CableBlock> COPPER_CABLE = pickaxeMineable(registerBlockAndItem("copper_cable",
            () -> new CableBlock(DEFAULT_BLOCK_PROPERTIES, 6, IREnergyTiers.MEDIUM), BlockTabOrdering.CABLES, false));
    public static final DeferredBlock<CableBlock> GOLD_CABLE = pickaxeMineable(registerBlockAndItem("gold_cable",
            () -> new CableBlock(DEFAULT_BLOCK_PROPERTIES, 6, IREnergyTiers.HIGH), BlockTabOrdering.CABLES, false));
    public static final DeferredBlock<CableBlock> STEEL_CABLE = pickaxeMineable(registerBlockAndItem("steel_cable",
            () -> new CableBlock(DEFAULT_BLOCK_PROPERTIES, 6, IREnergyTiers.EXTREME), BlockTabOrdering.CABLES, false));
    public static final DeferredBlock<FenceBlock> IRON_FENCE = pickaxeMineable(registerBlockAndItem("iron_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)), BlockTabOrdering.MISC_BLOCKS, false));
    public static final DeferredBlock<WoodenScaffoldingBlock> WOODEN_SCAFFOLDING = axeMineable(registerBlockAndItem("wooden_scaffolding",
            () -> new WoodenScaffoldingBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).noOcclusion()), BlockTabOrdering.MISC_BLOCKS));

    // Rubber
    public static final DeferredBlock<RubberTreeLogBlock> RUBBER_TREE_LOG = woodBlock("rubber_tree_log", RubberTreeLogBlock::new);
    public static final DeferredBlock<RubberTreeLogBlock> STRIPPED_RUBBER_TREE_LOG = woodBlock("stripped_rubber_tree_log", RubberTreeLogBlock::new);
    public static final DeferredBlock<RubberTreeLogBlock> RUBBER_TREE_WOOD = woodBlock("rubber_tree_wood", RubberTreeLogBlock::new);
    public static final DeferredBlock<RubberTreeLogBlock> STRIPPED_RUBBER_TREE_WOOD = woodBlock("stripped_rubber_tree_wood", RubberTreeLogBlock::new);
    public static final DeferredBlock<RubberTreeLeavesBlock> RUBBER_TREE_LEAVES = woodBlock("rubber_tree_leaves", RubberTreeLeavesBlock::new, true, false);
    public static final DeferredBlock<RubberTreeResinHoleBlock> RUBBER_TREE_RESIN_HOLE = woodBlock("rubber_tree_resin_hole", RubberTreeResinHoleBlock::new, BlockTabOrdering.NONE, false, false);
    public static final DeferredBlock<SaplingBlock> RUBBER_TREE_SAPLING = woodBlock("rubber_tree_sapling",
            () -> new SaplingBlock(IRTreeGrowers.RUBBER, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)), false, true);
    public static final DeferredBlock<ButtonBlock> RUBBER_TREE_BUTTON = woodBlock("rubber_tree_button",
            () -> new ButtonBlock(RUBBER_SET_TYPE, 30, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON)), false, true);
    public static final DeferredBlock<Block> RUBBER_TREE_PLANKS = woodBlock("rubber_tree_planks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final DeferredBlock<StairBlock> RUBBER_TREE_STAIRS = woodBlock("rubber_tree_stairs",
            () -> new StairBlock(RUBBER_TREE_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS)));
    public static final DeferredBlock<DoorBlock> RUBBER_TREE_DOOR = woodBlock("rubber_tree_door",
            () -> new DoorBlock(RUBBER_SET_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR)), false, false);
    public static final DeferredBlock<PressurePlateBlock> RUBBER_TREE_PRESSURE_PLATE = woodBlock("rubber_tree_pressure_plate",
            () -> new PressurePlateBlock(RUBBER_SET_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE)));
    public static final DeferredBlock<FenceBlock> RUBBER_TREE_FENCE = woodBlock("rubber_tree_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR)), false, true);
    public static final DeferredBlock<TrapDoorBlock> RUBBER_TREE_TRAPDOOR = woodBlock("rubber_tree_trapdoor",
            () -> new TrapDoorBlock(RUBBER_SET_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR)), false, true);
    public static final DeferredBlock<FenceGateBlock> RUBBER_TREE_FENCE_GATE = woodBlock("rubber_tree_fence_gate",
            () -> new FenceGateBlock(RUBBER_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE)));
    public static final DeferredBlock<SlabBlock> RUBBER_TREE_SLAB = woodBlock("rubber_tree_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB)));

    // Ores
    public static final DeferredBlock<Block> BAUXITE_ORE = oreBlock("bauxite_ore", IRItems.RAW_BAUXITE);
    public static final DeferredBlock<Block> DEEPSLATE_BAUXITE_ORE = oreBlock("deepslate_bauxite_ore", IRItems.RAW_BAUXITE, true);
    public static final DeferredBlock<Block> CHROMIUM_ORE = oreBlock("chromium_ore", IRItems.RAW_CHROMIUM);
    public static final DeferredBlock<Block> DEEPSLATE_CHROMIUM_ORE = oreBlock("deepslate_chromium_ore", IRItems.RAW_CHROMIUM, true);
    public static final DeferredBlock<Block> IRIDIUM_ORE = oreBlock("iridium_ore", IRItems.RAW_IRIDIUM);
    public static final DeferredBlock<Block> DEEPSLATE_IRIDIUM_ORE = oreBlock("deepslate_iridium_ore", IRItems.RAW_IRIDIUM, true);
    public static final DeferredBlock<Block> LEAD_ORE = oreBlock("lead_ore", IRItems.RAW_LEAD);
    public static final DeferredBlock<Block> DEEPSLATE_LEAD_ORE = oreBlock("deepslate_lead_ore", IRItems.RAW_LEAD, true);
    public static final DeferredBlock<Block> NICKEL_ORE = oreBlock("nickel_ore", IRItems.RAW_NICKEL);
    public static final DeferredBlock<Block> DEEPSLATE_NICKEL_ORE = oreBlock("deepslate_nickel_ore", IRItems.RAW_NICKEL, true);
    public static final DeferredBlock<Block> TIN_ORE = oreBlock("tin_ore", IRItems.RAW_TIN);
    public static final DeferredBlock<Block> DEEPSLATE_TIN_ORE = oreBlock("deepslate_tin_ore", IRItems.RAW_TIN, true);
    public static final DeferredBlock<Block> URANIUM_ORE = oreBlock("uranium_ore", IRItems.RAW_URANIUM);
    public static final DeferredBlock<Block> DEEPSLATE_URANIUM_ORE = oreBlock("deepslate_uranium_ore", IRItems.RAW_URANIUM, true);

    // Metal storage blocks
    public static final DeferredBlock<Block> ALUMINUM_BLOCK = metalStorageBlock("aluminum_block", MetalType.IRON);
    public static final DeferredBlock<Block> RAW_BAUXITE_BLOCK = rawStorageBlock("raw_bauxite_block");
    public static final DeferredBlock<Block> CHROMIUM_BLOCK = metalStorageBlock("chromium_block", MetalType.IRON);
    public static final DeferredBlock<Block> RAW_CHROMIUM_BLOCK = rawStorageBlock("raw_chromium_block");
    public static final DeferredBlock<Block> IRIDIUM_BLOCK = metalStorageBlock("iridium_block", MetalType.NETHERITE);
    public static final DeferredBlock<Block> RAW_IRIDIUM_BLOCK = rawStorageBlock("raw_iridium_block");
    public static final DeferredBlock<Block> LEAD_BLOCK = metalStorageBlock("lead_block", MetalType.IRON);
    public static final DeferredBlock<Block> RAW_LEAD_BLOCK = rawStorageBlock("raw_lead_block");
    public static final DeferredBlock<Block> NICKEL_BLOCK = metalStorageBlock("nickel_block", MetalType.IRON);
    public static final DeferredBlock<Block> RAW_NICKEL_BLOCK = rawStorageBlock("raw_nickel_block");
    public static final DeferredBlock<Block> TIN_BLOCK = metalStorageBlock("tin_block", MetalType.COPPER);
    public static final DeferredBlock<Block> RAW_TIN_BLOCK = rawStorageBlock("raw_tin_block");
    public static final DeferredBlock<Block> TITANIUM_BLOCK = metalStorageBlock("titanium_block", MetalType.NETHERITE);
    public static final DeferredBlock<Block> URANIUM_BLOCK = metalStorageBlock("uranium_block", MetalType.COPPER);
    public static final DeferredBlock<Block> RAW_URANIUM_BLOCK = rawStorageBlock("raw_uranium_block");
    public static final DeferredBlock<Block> STEEL_BLOCK = metalStorageBlock("steel_block", MetalType.NETHERITE);

    private static <T extends Block> DeferredBlock<T> axeMineable(DeferredBlock<T> block) {
        AXE_MINEABLE.add(block);
        return block;
    }

    private static <T extends Block> DeferredBlock<T> pickaxeAxeMineable(DeferredBlock<T> block) {
        PICKAXE_MINEABLE.add(block);
        AXE_MINEABLE.add(block);
        return block;
    }

    private static <T extends Block> DeferredBlock<T> pickaxeMineable(DeferredBlock<T> block) {
        PICKAXE_MINEABLE.add(block);
        return block;
    }

    private static <T extends Block> DeferredBlock<T> woodBlock(String name, Supplier<T> block, boolean genModel) {
        return woodBlock(name, block, genModel, true);
    }

    private static <T extends Block> DeferredBlock<T> woodBlock(String name, Supplier<T> block, boolean genModel, boolean dropSelf) {
        return woodBlock(name, block, BlockTabOrdering.RUBBER_TREES, genModel, dropSelf);
    }

    private static <T extends Block> DeferredBlock<T> woodBlock(String name, Supplier<T> block, BlockTabOrdering ordering, boolean genModel, boolean dropSelf) {
        DeferredBlock<T> toReturn = registerBlockAndItem(name, block, deferredBlock -> () -> new BlockItem(deferredBlock.get(), new Item.Properties()), ordering, genModel, dropSelf);
        AXE_MINEABLE.add(toReturn);
        return toReturn;
    }

    private static <T extends Block> DeferredBlock<T> woodBlock(String name, Supplier<T> block) {
        return woodBlock(name, block, true, true);
    }

    private static <T extends Block> DeferredBlock<T> registerBlockAndItem(String name, Supplier<T> block, BlockTabOrdering ordering) {
        return registerBlockAndItem(name, block, ordering, true);
    }

    private static <T extends Block> DeferredBlock<T> registerBlockAndItem(String name, Supplier<T> block, BlockTabOrdering ordering, boolean genModel) {
        return registerBlockAndItem(name, block, deferredBlock -> () -> new BlockItem(deferredBlock.get(), new Item.Properties()), ordering, genModel, true);
    }

    private static <T extends Block> DeferredBlock<T> multiblockControllerBlock(String name, Supplier<T> block, boolean dropSelf) {
        return registerBlockAndItem(name, block, deferredBlock -> () -> new BlockItem(deferredBlock.get(), new Item.Properties()), BlockTabOrdering.NONE, false, dropSelf);
    }

    private static <T extends Block> DeferredBlock<T> registerBlockAndItem(String name, Supplier<T> block, BlockTabOrdering ordering, boolean genModel, boolean dropSelf) {
        return registerBlockAndItem(name, block, deferredBlock -> () -> new BlockItem(deferredBlock.get(), new Item.Properties()), ordering, genModel, dropSelf);
    }

    private static <T extends Block> DeferredBlock<T> registerBlockAndItem(String name, Supplier<T> block, Function<DeferredBlock<T>, Supplier<BlockItem>> blockItem, BlockTabOrdering ordering, boolean genModel, boolean dropSelf) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        DeferredItem<BlockItem> blockItemDeferredItem = IRItems.ITEMS.register(name, blockItem.apply(toReturn));
        if (genModel) {
            IRItems.BLOCK_ITEMS.add(blockItemDeferredItem);
        }
        if (!ordering.isNone()) {
            putTabBlock(ordering, toReturn);
        }
        if (dropSelf) {
            DROP_SELF_BLOCKS.add(toReturn);
        }
        return toReturn;
    }

    public static void putTabBlock(BlockTabOrdering tabOrdering, Supplier<? extends Block> block) {
        putTabBlock(calculateTabPosition(tabOrdering), block);
    }

    private static void putTabBlock(TabPosition position, Supplier<? extends Block> block) {
        TAB_BLOCKS.computeIfAbsent(position.orderingCategory(), k -> new Int2ObjectOpenHashMap<>())
                .put(position.categoryPosition(), block);
    }

    private static TabPosition calculateTabPosition(BlockTabOrdering tabOrdering) {
        return tabOrdering.withPosition(TAB_BLOCKS.getOrDefault(tabOrdering, Collections.emptyMap()).size());
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    private static DeferredBlock<Block> oreBlock(String name, DeferredItem<?> item) {
        return oreBlock(name, item, false);
    }

    private static DeferredBlock<Block> oreBlock(String name, DeferredItem<?> item, boolean deepslate) {
        DeferredBlock<Block> blockDeferredBlock = registerBlockAndItem(name, () -> new Block(oreProperties(deepslate)), BlockTabOrdering.ORES, true, false);
        ORES.put(blockDeferredBlock, item);
        return pickaxeMineable(blockDeferredBlock);
    }

    private static DeferredBlock<Block> rawStorageBlock(String name) {
        DeferredBlock<Block> blockDeferredBlock = registerBlockAndItem(name, () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_IRON_BLOCK)), BlockTabOrdering.ORES);
        RAW_STORAGE_BLOCKS.add(blockDeferredBlock);
        return pickaxeMineable(blockDeferredBlock);
    }

    private static DeferredBlock<Block> metalStorageBlock(String name, MetalType type) {
        DeferredBlock<Block> blockDeferredBlock = registerBlockAndItem(name, () -> new Block(BlockBehaviour.Properties.ofFullCopy(switch (type) {
            case COPPER -> Blocks.COPPER_BLOCK;
            case IRON -> Blocks.IRON_BLOCK;
            case NETHERITE -> Blocks.NETHERITE_BLOCK;
        })), BlockTabOrdering.METALS);
        METAL_STORAGE_BLOCKS.add(blockDeferredBlock);
        return pickaxeMineable(blockDeferredBlock);
    }

    private static BlockBehaviour.Properties oreProperties(boolean deepslate) {
        if (deepslate) {
            return BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).strength(4.5F, 3.0F)
                    .requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM);
        }
        return BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(3.0F, 3.0F)
                .requiresCorrectToolForDrops().instrument(NoteBlockInstrument.BASEDRUM);
    }

    public enum MetalType {
        COPPER,
        IRON,
        NETHERITE,
    }
}
