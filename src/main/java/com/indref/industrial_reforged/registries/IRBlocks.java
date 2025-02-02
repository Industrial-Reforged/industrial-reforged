package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.content.blocks.*;
import com.indref.industrial_reforged.content.blocks.generators.BasicGeneratorBlock;
import com.indref.industrial_reforged.content.blocks.machines.CentrifugeBlock;
import com.indref.industrial_reforged.content.blocks.misc.*;
import com.indref.industrial_reforged.content.blocks.multiblocks.controller.BlastFurnaceHatchBlock;
import com.indref.industrial_reforged.content.blocks.multiblocks.controller.CrucibleControllerBlock;
import com.indref.industrial_reforged.content.blocks.multiblocks.controller.FireboxControllerBlock;
import com.indref.industrial_reforged.content.blocks.multiblocks.controller.SmallFireboxHatchBlock;
import com.indref.industrial_reforged.content.blocks.multiblocks.misc.BlastFurnaceBricksBlock;
import com.indref.industrial_reforged.content.blocks.multiblocks.parts.BlastFurnacePartBlock;
import com.indref.industrial_reforged.content.blocks.multiblocks.parts.CruciblePartBlock;
import com.indref.industrial_reforged.content.blocks.multiblocks.parts.FireboxPartBlock;
import com.indref.industrial_reforged.content.blocks.multiblocks.misc.RefractoryBrickBlock;
import com.indref.industrial_reforged.content.blocks.pipes.CableBlock;
import com.indref.industrial_reforged.content.blocks.trees.RubberTreeLeavesBlock;
import com.indref.industrial_reforged.content.blocks.trees.RubberTreeLogBlock;
import com.indref.industrial_reforged.content.blocks.trees.RubberTreeResinHoleBlock;
import com.indref.industrial_reforged.tiers.CrucibleTiers;
import com.indref.industrial_reforged.tiers.EnergyTiers;
import com.indref.industrial_reforged.tiers.FireboxTiers;
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
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public final class IRBlocks {
    public static final BlockSetType RUBBER_SET_TYPE = BlockSetType.register(new BlockSetType(IndustrialReforged.MODID + ":rubber"));
    public static final WoodType RUBBER_WOOD_TYPE = WoodType.register(new WoodType(IndustrialReforged.MODID + ":rubber", RUBBER_SET_TYPE));
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(IndustrialReforged.MODID);
    public static final List<DeferredItem<?>> TAB_BLOCKS = new ArrayList<>();
    public static final List<DeferredBlock<Block>> METAL_STORAGE_BLOCKS = new ArrayList<>();
    public static final List<DeferredBlock<Block>> RAW_STORAGE_BLOCKS = new ArrayList<>();
    public static final Map<DeferredBlock<DropExperienceBlock>, DeferredItem<?>> ORES = new HashMap<>();
    public static final List<DeferredBlock<?>> DROP_SELF_BLOCKS = new ArrayList<>();
    public static final List<DeferredBlock<?>> AXE_MINEABLE = new ArrayList<>();
    public static final List<DeferredBlock<?>> PICKAXE_MINEABLE = new ArrayList<>();

    public static final DeferredBlock<CableBlock> TIN_CABLE = pickaxeMineable(registerBlockAndItem("tin_cable",
            () -> new CableBlock(BlockBehaviour.Properties.of()
                    .sound(SoundType.WOOL)
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(0.8f), 6, EnergyTiers.LOW), true, false));
    public static final DeferredBlock<CableBlock> COPPER_CABLE = pickaxeMineable(registerBlockAndItem("copper_cable",
            () -> new CableBlock(BlockBehaviour.Properties.ofFullCopy(TIN_CABLE.get()), 6, EnergyTiers.MEDIUM), true, false));
    public static final DeferredBlock<CableBlock> GOLD_CABLE = pickaxeMineable(registerBlockAndItem("gold_cable",
            () -> new CableBlock(BlockBehaviour.Properties.ofFullCopy(TIN_CABLE.get()), 6, EnergyTiers.HIGH), true, false));
    public static final DeferredBlock<CableBlock> STEEL_CABLE = pickaxeMineable(registerBlockAndItem("steel_cable",
            () -> new CableBlock(BlockBehaviour.Properties.ofFullCopy(TIN_CABLE.get()), 6, EnergyTiers.EXTREME), true, false));
//    public static final DeferredBlock<MiningPipeBlock> MINING_PIPE = registerBlockAndItem("mining_pipe",
//            () -> new MiningPipeBlock(BlockBehaviour.Properties.of().noOcclusion()), $ -> () -> new MiningPipeBlockItem(new Item.Properties()), true, true);
    public static final DeferredBlock<BlastFurnaceBricksBlock> BLAST_FURNACE_BRICKS = pickaxeMineable(registerBlockAndItem("blast_furnace_bricks",
            () -> new BlastFurnaceBricksBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BRICKS).sound(SoundType.STONE))));
    public static final DeferredBlock<BlastFurnaceHatchBlock> BLAST_FURNACE_HATCH = pickaxeMineable(registerBlockAndItem("blast_furnace_hatch",
            () -> new BlastFurnaceHatchBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BRICKS).sound(SoundType.STONE))));
    public static final DeferredBlock<Block> BLAST_FURNACE_CONTROLLER = pickaxeMineable(multiblockControllerBlock("blast_furnace_controller",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.BRICKS).sound(SoundType.STONE)), false));
    public static final DeferredBlock<BlastFurnacePartBlock> BLAST_FURNACE_PART = pickaxeMineable(registerBlock("blast_furnace_part",
            () -> new BlastFurnacePartBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BRICKS).sound(SoundType.STONE))));
    public static final DeferredBlock<SmallFireboxHatchBlock> SMALL_FIREBOX_HATCH = pickaxeMineable(registerBlockAndItem("small_firebox_hatch",
            () -> new SmallFireboxHatchBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).sound(SoundType.METAL))));
    public static final DeferredBlock<Block> REFRACTORY_STONE = pickaxeMineable(registerBlockAndItem("refractory_stone",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.BRICKS).sound(SoundType.STONE))));
    public static final DeferredBlock<CoilBlock> COIL = pickaxeMineable(registerBlockAndItem("coil",
            () -> new CoilBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK))));
    public static final DeferredBlock<RefractoryBrickBlock> REFRACTORY_BRICK = pickaxeMineable(registerBlockAndItem("refractory_brick",
            () -> new RefractoryBrickBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE).mapColor(MapColor.GOLD))));
    public static final DeferredBlock<FireboxControllerBlock> FIREBOX_CONTROLLER = pickaxeMineable(multiblockControllerBlock("firebox_controller",
            () -> new FireboxControllerBlock(BlockBehaviour.Properties.ofFullCopy(COIL.get()), FireboxTiers.REFRACTORY), false));
    public static final DeferredBlock<FireboxPartBlock> FIREBOX_PART = pickaxeMineable(registerBlock("firebox_part",
            () -> new FireboxPartBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BRICKS))));
    public static final DeferredBlock<MachineFrameBlock> BASIC_MACHINE_FRAME = pickaxeMineable(registerBlockAndItem("basic_machine_frame",
            () -> new MachineFrameBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK))));
    public static final DeferredBlock<SlabBlock> TERRACOTTA_BRICK_SLAB = pickaxeMineable(registerBlockAndItem("terracotta_brick_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA))));
    public static final DeferredBlock<Block> TERRACOTTA_BRICKS = pickaxeMineable(registerBlockAndItem("terracotta_bricks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA))));
    public static final DeferredBlock<CrucibleControllerBlock> CERAMIC_CRUCIBLE_CONTROLLER = pickaxeMineable(multiblockControllerBlock("ceramic_crucible_controller",
            () -> new CrucibleControllerBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA).noOcclusion(), CrucibleTiers.CERAMIC), false));
    public static final DeferredBlock<CruciblePartBlock> CERAMIC_CRUCIBLE_PART = pickaxeMineable(registerBlock("ceramic_crucible_wall",
            () -> new CruciblePartBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.TERRACOTTA).noOcclusion(), CrucibleTiers.CERAMIC)));
    public static final DeferredBlock<FaucetBlock> BLAST_FURNACE_FAUCET = pickaxeMineable(registerBlockAndItem("blast_furnace_faucet",
            () -> new FaucetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE), IRBlocks.BLAST_FURNACE_BRICKS.get())));
    public static final DeferredBlock<CastingBasinBlock> CERAMIC_CASTING_BASIN = pickaxeMineable(registerBlockAndItem("ceramic_casting_basin",
            () -> new CastingBasinBlock(BlockBehaviour.Properties.of().noOcclusion(), IRBlocks.TERRACOTTA_BRICKS.get())));
    public static final DeferredBlock<CastingBasinBlock> BLAST_FURNACE_CASTING_BASIN = pickaxeMineable(registerBlockAndItem("blast_furnace_casting_basin",
            () -> new CastingBasinBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE).noOcclusion(), IRBlocks.BLAST_FURNACE_BRICKS.get())));
    public static final DeferredBlock<CentrifugeBlock> CENTRIFUGE = pickaxeMineable(registerBlockAndItem("centrifuge",
            () -> new CentrifugeBlock(BlockBehaviour.Properties.ofFullCopy(BASIC_MACHINE_FRAME.get()))));
    public static final DeferredBlock<BasicGeneratorBlock> BASIC_GENERATOR = pickaxeMineable(registerBlockAndItem("basic_generator",
            () -> new BasicGeneratorBlock(BlockBehaviour.Properties.ofFullCopy(BASIC_MACHINE_FRAME.get()))));
    public static final DeferredBlock<DrainBlock> DRAIN = pickaxeAxeMineable(registerBlockAndItem("drain",
            () -> new DrainBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRAFTER))));
    public static final DeferredBlock<CraftingStationBlock> CRAFTING_STATION = axeMineable(registerBlockAndItem("crafting_station",
            () -> new CraftingStationBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRAFTING_TABLE))));
    public static final DeferredBlock<FenceBlock> IRON_FENCE = pickaxeMineable(registerBlockAndItem("iron_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)), true, false));

    // Rubber
    public static final DeferredBlock<RubberTreeLogBlock> RUBBER_TREE_LOG = woodBlock("rubber_tree_log", RubberTreeLogBlock::new);
    public static final DeferredBlock<RubberTreeLogBlock> STRIPPED_RUBBER_TREE_LOG = woodBlock("stripped_rubber_tree_log", RubberTreeLogBlock::new);
    public static final DeferredBlock<RubberTreeLogBlock> RUBBER_TREE_WOOD = woodBlock("rubber_tree_wood", RubberTreeLogBlock::new);
    public static final DeferredBlock<RubberTreeLogBlock> STRIPPED_RUBBER_TREE_WOOD = woodBlock("stripped_rubber_tree_wood", RubberTreeLogBlock::new);
    public static final DeferredBlock<RubberTreeLeavesBlock> RUBBER_TREE_LEAVES = woodBlock("rubber_tree_leaves", RubberTreeLeavesBlock::new, true, true, false);
    public static final DeferredBlock<RubberTreeResinHoleBlock> RUBBER_TREE_RESIN_HOLE = woodBlock("rubber_tree_resin_hole", RubberTreeResinHoleBlock::new, false, false, false);
    public static final DeferredBlock<SaplingBlock> RUBBER_TREE_SAPLING = woodBlock("rubber_tree_sapling",
            () -> new SaplingBlock(IRTreeGrowers.RUBBER, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)), true, false);
    public static final DeferredBlock<ButtonBlock> RUBBER_TREE_BUTTON = woodBlock("rubber_tree_button",
            () -> new ButtonBlock(RUBBER_SET_TYPE, 30, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON)), true, false);
    public static final DeferredBlock<Block> RUBBER_TREE_PLANKS = woodBlock("rubber_tree_planks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final DeferredBlock<StairBlock> RUBBER_TREE_STAIRS = woodBlock("rubber_tree_stairs",
            () -> new StairBlock(RUBBER_TREE_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS)));
    public static final DeferredBlock<DoorBlock> RUBBER_TREE_DOOR = woodBlock("rubber_tree_door",
            () -> new DoorBlock(RUBBER_SET_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR)), true, false, false);
    public static final DeferredBlock<PressurePlateBlock> RUBBER_TREE_PRESSURE_PLATE = woodBlock("rubber_tree_pressure_plate",
            () -> new PressurePlateBlock(RUBBER_SET_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE)));
    public static final DeferredBlock<FenceBlock> RUBBER_TREE_FENCE = woodBlock("rubber_tree_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR)), true, false);
    public static final DeferredBlock<TrapDoorBlock> RUBBER_TREE_TRAPDOOR = woodBlock("rubber_tree_trapdoor",
            () -> new TrapDoorBlock(RUBBER_SET_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR)), true, false);
    public static final DeferredBlock<FenceGateBlock> RUBBER_TREE_FENCE_GATE = woodBlock("rubber_tree_fence_gate",
            () -> new FenceGateBlock(RUBBER_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE)));
    public static final DeferredBlock<SlabBlock> RUBBER_TREE_SLAB = woodBlock("rubber_tree_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB)));

    // Ores
    public static final DeferredBlock<DropExperienceBlock> BAUXITE_ORE = oreBlock("bauxite_ore", IRItems.RAW_BAUXITE);
    public static final DeferredBlock<DropExperienceBlock> DEEPSLATE_BAUXITE_ORE = oreBlock("deepslate_bauxite_ore", IRItems.RAW_BAUXITE, true);
    public static final DeferredBlock<DropExperienceBlock> CHROMIUM_ORE = oreBlock("chromium_ore", IRItems.RAW_CHROMIUM);
    public static final DeferredBlock<DropExperienceBlock> DEEPSLATE_CHROMIUM_ORE = oreBlock("deepslate_chromium_ore", IRItems.RAW_CHROMIUM, true);
    public static final DeferredBlock<DropExperienceBlock> IRIDIUM_ORE = oreBlock("iridium_ore", IRItems.RAW_IRIDIUM);
    public static final DeferredBlock<DropExperienceBlock> DEEPSLATE_IRIDIUM_ORE = oreBlock("deepslate_iridium_ore", IRItems.RAW_IRIDIUM, true);
    public static final DeferredBlock<DropExperienceBlock> LEAD_ORE = oreBlock("lead_ore", IRItems.RAW_LEAD);
    public static final DeferredBlock<DropExperienceBlock> DEEPSLATE_LEAD_ORE = oreBlock("deepslate_lead_ore", IRItems.RAW_LEAD, true);
    public static final DeferredBlock<DropExperienceBlock> NICKEL_ORE = oreBlock("nickel_ore", IRItems.RAW_NICKEL);
    public static final DeferredBlock<DropExperienceBlock> DEEPSLATE_NICKEL_ORE = oreBlock("deepslate_nickel_ore", IRItems.RAW_NICKEL, true);
    public static final DeferredBlock<DropExperienceBlock> TIN_ORE = oreBlock("tin_ore", IRItems.RAW_TIN);
    public static final DeferredBlock<DropExperienceBlock> DEEPSLATE_TIN_ORE = oreBlock("deepslate_tin_ore", IRItems.RAW_TIN, true);
    public static final DeferredBlock<DropExperienceBlock> URANIUM_ORE = oreBlock("uranium_ore", IRItems.RAW_URANIUM);
    public static final DeferredBlock<DropExperienceBlock> DEEPSLATE_URANIUM_ORE = oreBlock("deepslate_uranium_ore", IRItems.RAW_URANIUM, true);

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

    private static <T extends Block> DeferredBlock<T> woodBlock(String name, Supplier<T> block, boolean addToCreative, boolean genModel) {
        return woodBlock(name, block, addToCreative, genModel, true);
    }

    private static <T extends Block> DeferredBlock<T> woodBlock(String name, Supplier<T> block, boolean addToCreative, boolean genModel, boolean dropSelf) {
        DeferredBlock<T> toReturn = registerBlockAndItem(name, block, deferredBlock -> () -> new BlockItem(deferredBlock.get(), new Item.Properties()), addToCreative, genModel, dropSelf);
        AXE_MINEABLE.add(toReturn);
        return toReturn;
    }

    private static <T extends Block> DeferredBlock<T> woodBlock(String name, Supplier<T> block) {
        return woodBlock(name, block, true, true, true);
    }

    private static <T extends Block> DeferredBlock<T> registerBlockAndItem(String name, Supplier<T> block, boolean addToCreativeTab) {
        return registerBlockAndItem(name, block, addToCreativeTab, true);
    }

    private static <T extends Block> DeferredBlock<T> registerBlockAndItem(String name, Supplier<T> block) {
        return registerBlockAndItem(name, block, true);
    }

    private static <T extends Block> DeferredBlock<T> registerBlockAndItem(String name, Supplier<T> block, boolean addToCreative, boolean genModel) {
        return registerBlockAndItem(name, block, deferredBlock -> () -> new BlockItem(deferredBlock.get(), new Item.Properties()), addToCreative, genModel, true);
    }

    private static <T extends Block> DeferredBlock<T> multiblockControllerBlock(String name, Supplier<T> block, boolean dropSelf) {
        return registerBlockAndItem(name, block, deferredBlock -> () -> new BlockItem(deferredBlock.get(), new Item.Properties()), false, false, dropSelf);
    }

    private static <T extends Block> DeferredBlock<T> registerBlockAndItem(String name, Supplier<T> block, boolean addToCreative, boolean genModel, boolean dropSelf) {
        return registerBlockAndItem(name, block, deferredBlock -> () -> new BlockItem(deferredBlock.get(), new Item.Properties()), addToCreative, genModel, dropSelf);
    }

    private static <T extends Block> DeferredBlock<T> registerBlockAndItem(String name, Supplier<T> block, Function<DeferredBlock<T>, Supplier<BlockItem>> blockItem, boolean addToCreative, boolean genModel, boolean dropSelf) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        DeferredItem<BlockItem> blockItemDeferredItem = IRItems.ITEMS.register(name, blockItem.apply(toReturn));
        if (genModel) {
            IRItems.BLOCK_ITEMS.add(blockItemDeferredItem);
        }
        if (addToCreative) {
            TAB_BLOCKS.add(blockItemDeferredItem);
        }
        if (dropSelf) {
            DROP_SELF_BLOCKS.add(toReturn);
        }
        return toReturn;
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    private static DeferredBlock<DropExperienceBlock> oreBlock(String name, DeferredItem<?> item) {
        return oreBlock(name, item, false);
    }

    private static DeferredBlock<DropExperienceBlock> oreBlock(String name, DeferredItem<?> item, boolean deepslate) {
        DeferredBlock<DropExperienceBlock> blockDeferredBlock = registerBlockAndItem(name, () -> new DropExperienceBlock(ConstantInt.of(1), oreSettings(deepslate)), true, true, false);
        ORES.put(blockDeferredBlock, item);
        return pickaxeMineable(blockDeferredBlock);
    }

    private static DeferredBlock<Block> rawStorageBlock(String name) {
        DeferredBlock<Block> blockDeferredBlock = registerBlockAndItem(name, () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_IRON_BLOCK)));
        RAW_STORAGE_BLOCKS.add(blockDeferredBlock);
        return pickaxeMineable(blockDeferredBlock);
    }

    private static DeferredBlock<Block> metalStorageBlock(String name, MetalType type) {
        DeferredBlock<Block> blockDeferredBlock = registerBlockAndItem(name, () -> new Block(BlockBehaviour.Properties.ofFullCopy(switch (type) {
            case COPPER -> Blocks.COPPER_BLOCK;
            case IRON -> Blocks.IRON_BLOCK;
            case NETHERITE -> Blocks.NETHERITE_BLOCK;
        })));
        METAL_STORAGE_BLOCKS.add(blockDeferredBlock);
        return pickaxeMineable(blockDeferredBlock);
    }

    private static BlockBehaviour.Properties oreSettings(boolean deepslate) {
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
