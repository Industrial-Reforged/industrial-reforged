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
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public final class IRBlocks {
    public static final BlockSetType RUBBER_SET_TYPE = BlockSetType.register(new BlockSetType(IndustrialReforged.MODID + ":rubber"));
    public static final WoodType RUBBER_WOOD_TYPE = WoodType.register(new WoodType(IndustrialReforged.MODID + ":rubber", RUBBER_SET_TYPE));
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(IndustrialReforged.MODID);
    public static final List<DeferredItem<?>> TAB_BLOCKS = new ArrayList<>();
    public static final List<DeferredBlock<Block>> METAL_STORAGE_BLOCKS = new ArrayList<>();
    public static final List<DeferredBlock<Block>> RAW_STORAGE_BLOCKS = new ArrayList<>();
    public static final List<DeferredBlock<DropExperienceBlock>> ORES = new ArrayList<>();

    public static final DeferredBlock<CableBlock> TIN_CABLE = registerBlockAndItem("tin_cable",
            () -> new CableBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOL).mapColor(MapColor.COLOR_BLACK), 6, EnergyTiers.LOW), true, false);
    public static final DeferredBlock<CableBlock> COPPER_CABLE = registerBlockAndItem("copper_cable",
            () -> new CableBlock(BlockBehaviour.Properties.ofFullCopy(TIN_CABLE.get()), 6, EnergyTiers.MEDIUM), true, false);
    public static final DeferredBlock<CableBlock> GOLD_CABLE = registerBlockAndItem("gold_cable",
            () -> new CableBlock(BlockBehaviour.Properties.ofFullCopy(TIN_CABLE.get()), 6, EnergyTiers.HIGH), true, false);
    public static final DeferredBlock<CableBlock> STEEL_CABLE = registerBlockAndItem("steel_cable",
            () -> new CableBlock(BlockBehaviour.Properties.ofFullCopy(TIN_CABLE.get()), 6, EnergyTiers.EXTREME), true, false);
//    public static final DeferredBlock<MiningPipeBlock> MINING_PIPE = registerBlockAndItem("mining_pipe",
//            () -> new MiningPipeBlock(BlockBehaviour.Properties.of().noOcclusion()), $ -> () -> new MiningPipeBlockItem(new Item.Properties()), true, true);
    public static final DeferredBlock<BlastFurnaceBricksBlock> BLAST_FURNACE_BRICKS = registerBlockAndItem("blast_furnace_bricks",
            () -> new BlastFurnaceBricksBlock(BlockBehaviour.Properties.of().sound(SoundType.STONE)));
    public static final DeferredBlock<BlastFurnaceHatchBlock> BLAST_FURNACE_HATCH = registerBlockAndItem("blast_furnace_hatch",
            () -> new BlastFurnaceHatchBlock(BlockBehaviour.Properties.of().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> BLAST_FURNACE_CONTROLLER = registerBlockAndItem("blast_furnace_controller",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.STONE)), false, false);
    public static final DeferredBlock<BlastFurnacePartBlock> BLAST_FURNACE_PART = registerBlock("blast_furnace_part",
            () -> new BlastFurnacePartBlock(BlockBehaviour.Properties.of().sound(SoundType.STONE)));
    public static final DeferredBlock<SmallFireboxHatchBlock> SMALL_FIREBOX_HATCH = registerBlockAndItem("small_firebox_hatch",
            () -> new SmallFireboxHatchBlock(BlockBehaviour.Properties.of().sound(SoundType.METAL)));
    public static final DeferredBlock<Block> REFRACTORY_STONE = registerBlockAndItem("refractory_stone",
            () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.STONE)));
    public static final DeferredBlock<CoilBlock> COIL = registerBlockAndItem("coil",
            () -> new CoilBlock(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<RefractoryBrickBlock> REFRACTORY_BRICK = registerBlockAndItem("refractory_brick",
            () -> new RefractoryBrickBlock(BlockBehaviour.Properties.of().sound(SoundType.STONE)));
    public static final DeferredBlock<FireboxControllerBlock> FIREBOX_CONTROLLER = registerBlockAndItem("firebox_controller",
            () -> new FireboxControllerBlock(BlockBehaviour.Properties.of(), FireboxTiers.REFRACTORY), false, false);
    public static final DeferredBlock<FireboxPartBlock> FIREBOX_PART = registerBlock("firebox_part",
            () -> new FireboxPartBlock(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<MachineFrameBlock> BASIC_MACHINE_FRAME = registerBlockAndItem("basic_machine_frame",
            () -> new MachineFrameBlock(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<SlabBlock> TERRACOTTA_BRICK_SLAB = registerBlockAndItem("terracotta_brick_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<Block> TERRACOTTA_BRICKS = registerBlockAndItem("terracotta_bricks",
            () -> new Block(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<CrucibleControllerBlock> CERAMIC_CRUCIBLE_CONTROLLER = registerBlockAndItem("ceramic_crucible_controller",
            () -> new CrucibleControllerBlock(BlockBehaviour.Properties.of().noOcclusion(), CrucibleTiers.CERAMIC), false, false);
    public static final DeferredBlock<CruciblePartBlock> CERAMIC_CRUCIBLE_PART = registerBlock("ceramic_crucible_wall",
            () -> new CruciblePartBlock(BlockBehaviour.Properties.of().noOcclusion(), CrucibleTiers.CERAMIC));
    public static final DeferredBlock<FaucetBlock> BLAST_FURNACE_FAUCET = registerBlockAndItem("blast_furnace_faucet",
            () -> new FaucetBlock(BlockBehaviour.Properties.of(), IRBlocks.BLAST_FURNACE_BRICKS.get()));
    public static final DeferredBlock<CastingBasinBlock> CERAMIC_CASTING_BASIN = registerBlockAndItem("ceramic_casting_basin",
            () -> new CastingBasinBlock(BlockBehaviour.Properties.of().noOcclusion(), IRBlocks.TERRACOTTA_BRICKS.get()));
    public static final DeferredBlock<CastingBasinBlock> BLAST_FURNACE_CASTING_BASIN = registerBlockAndItem("blast_furnace_casting_basin",
            () -> new CastingBasinBlock(BlockBehaviour.Properties.of().noOcclusion(), IRBlocks.BLAST_FURNACE_BRICKS.get()));
    public static final DeferredBlock<CentrifugeBlock> CENTRIFUGE = registerBlockAndItem("centrifuge",
            () -> new CentrifugeBlock(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<BasicGeneratorBlock> BASIC_GENERATOR = registerBlockAndItem("basic_generator",
            () -> new BasicGeneratorBlock(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<DrainBlock> DRAIN = registerBlockAndItem("drain",
            () -> new DrainBlock(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<CraftingStationBlock> CRAFTING_STATION = registerBlockAndItem("crafting_station",
            () -> new CraftingStationBlock(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<FenceBlock> IRON_FENCE = registerBlockAndItem("iron_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)), true, false);

    // Rubber
    public static final DeferredBlock<RubberTreeLogBlock> RUBBER_TREE_LOG = registerBlockAndItem("rubber_tree_log", RubberTreeLogBlock::new);
    public static final DeferredBlock<RubberTreeLogBlock> STRIPPED_RUBBER_TREE_LOG = registerBlockAndItem("stripped_rubber_tree_log", RubberTreeLogBlock::new);
    public static final DeferredBlock<RubberTreeLogBlock> RUBBER_TREE_WOOD = registerBlockAndItem("rubber_tree_wood", RubberTreeLogBlock::new);
    public static final DeferredBlock<RubberTreeLogBlock> STRIPPED_RUBBER_TREE_WOOD = registerBlockAndItem("stripped_rubber_tree_wood", RubberTreeLogBlock::new);
    public static final DeferredBlock<RubberTreeLeavesBlock> RUBBER_TREE_LEAVES = registerBlockAndItem("rubber_tree_leaves", RubberTreeLeavesBlock::new);
    public static final DeferredBlock<RubberTreeResinHoleBlock> RUBBER_TREE_RESIN_HOLE = registerBlock("rubber_tree_resin_hole", RubberTreeResinHoleBlock::new);
    public static final DeferredBlock<SaplingBlock> RUBBER_TREE_SAPLING = registerBlockAndItem("rubber_tree_sapling",
            () -> new SaplingBlock(IRTreeGrowers.RUBBER, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)), true, false);
    public static final DeferredBlock<ButtonBlock> RUBBER_TREE_BUTTON = registerBlockAndItem("rubber_tree_button",
            () -> new ButtonBlock(RUBBER_SET_TYPE, 30, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON)), true, false);
    public static final DeferredBlock<Block> RUBBER_TREE_PLANKS = registerBlockAndItem("rubber_tree_planks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final DeferredBlock<StairBlock> RUBBER_TREE_STAIRS = registerBlockAndItem("rubber_tree_stairs",
            () -> new StairBlock(RUBBER_TREE_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS)));
    public static final DeferredBlock<DoorBlock> RUBBER_TREE_DOOR = registerBlockAndItem("rubber_tree_door",
            () -> new DoorBlock(RUBBER_SET_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR)), true, false);
    public static final DeferredBlock<PressurePlateBlock> RUBBER_TREE_PRESSURE_PLATE = registerBlockAndItem("rubber_tree_pressure_plate",
            () -> new PressurePlateBlock(RUBBER_SET_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE)));
    public static final DeferredBlock<FenceBlock> RUBBER_TREE_FENCE = registerBlockAndItem("rubber_tree_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR)), true, false);
    public static final DeferredBlock<TrapDoorBlock> RUBBER_TREE_TRAPDOOR = registerBlockAndItem("rubber_tree_trapdoor",
            () -> new TrapDoorBlock(RUBBER_SET_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR)), true, false);
    public static final DeferredBlock<FenceGateBlock> RUBBER_TREE_FENCE_GATE = registerBlockAndItem("rubber_tree_fence_gate",
            () -> new FenceGateBlock(RUBBER_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE)));
    public static final DeferredBlock<SlabBlock> RUBBER_TREE_SLAB = registerBlockAndItem("rubber_tree_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB)));

    // Ores
    public static final DeferredBlock<DropExperienceBlock> BAUXITE_ORE = oreBlock("bauxite_ore");
    public static final DeferredBlock<DropExperienceBlock> DEEPSLATE_BAUXITE_ORE = oreBlock("deepslate_bauxite_ore");
    public static final DeferredBlock<DropExperienceBlock> CHROMIUM_ORE = oreBlock("chromium_ore");
    public static final DeferredBlock<DropExperienceBlock> DEEPSLATE_CHROMIUM_ORE = oreBlock("deepslate_chromium_ore");
    public static final DeferredBlock<DropExperienceBlock> IRIDIUM_ORE = oreBlock("iridium_ore");
    public static final DeferredBlock<DropExperienceBlock> DEEPSLATE_IRIDIUM_ORE = oreBlock("deepslate_iridium_ore");
    public static final DeferredBlock<DropExperienceBlock> LEAD_ORE = oreBlock("lead_ore");
    public static final DeferredBlock<DropExperienceBlock> DEEPSLATE_LEAD_ORE = oreBlock("deepslate_lead_ore");
    public static final DeferredBlock<DropExperienceBlock> NICKEL_ORE = oreBlock("nickel_ore");
    public static final DeferredBlock<DropExperienceBlock> DEEPSLATE_NICKEL_ORE = oreBlock("deepslate_nickel_ore");
    public static final DeferredBlock<DropExperienceBlock> TIN_ORE = oreBlock("tin_ore");
    public static final DeferredBlock<DropExperienceBlock> DEEPSLATE_TIN_ORE = oreBlock("deepslate_tin_ore");
    public static final DeferredBlock<DropExperienceBlock> URANIUM_ORE = oreBlock("uranium_ore");
    public static final DeferredBlock<DropExperienceBlock> DEEPSLATE_URANIUM_ORE = oreBlock("deepslate_uranium_ore");

    // Metal storage blocks
    public static final DeferredBlock<Block> ALUMINUM_BLOCK = metalStorageBlock("aluminum_block");
    public static final DeferredBlock<Block> RAW_BAUXITE_BLOCK = rawStorageBlock("raw_bauxite_block");
    public static final DeferredBlock<Block> CHROMIUM_BLOCK = metalStorageBlock("chromium_block");
    public static final DeferredBlock<Block> RAW_CHROMIUM_BLOCK = rawStorageBlock("raw_chromium_block");
    public static final DeferredBlock<Block> IRIDIUM_BLOCK = metalStorageBlock("iridium_block");
    public static final DeferredBlock<Block> RAW_IRIDIUM_BLOCK = rawStorageBlock("raw_iridium_block");
    public static final DeferredBlock<Block> LEAD_BLOCK = metalStorageBlock("lead_block");
    public static final DeferredBlock<Block> RAW_LEAD_BLOCK = rawStorageBlock("raw_lead_block");
    public static final DeferredBlock<Block> NICKEL_BLOCK = metalStorageBlock("nickel_block");
    public static final DeferredBlock<Block> RAW_NICKEL_BLOCK = rawStorageBlock("raw_nickel_block");
    public static final DeferredBlock<Block> TIN_BLOCK = metalStorageBlock("tin_block");
    public static final DeferredBlock<Block> RAW_TIN_BLOCK = rawStorageBlock("raw_tin_block");
    public static final DeferredBlock<Block> TITANIUM_BLOCK = metalStorageBlock("titanium_block");
    public static final DeferredBlock<Block> URANIUM_BLOCK = metalStorageBlock("uranium_block");
    public static final DeferredBlock<Block> RAW_URANIUM_BLOCK = rawStorageBlock("raw_uranium_block");
    public static final DeferredBlock<Block> STEEL_BLOCK = metalStorageBlock("steel_block");

    private static <T extends Block> DeferredBlock<T> registerBlockAndItem(String name, Supplier<T> block, boolean addToCreativeTab) {
        return registerBlockAndItem(name, block, addToCreativeTab, true);
    }

    private static <T extends Block> DeferredBlock<T> registerBlockAndItem(String name, Supplier<T> block) {
        return registerBlockAndItem(name, block, true, true);
    }

    private static <T extends Block> DeferredBlock<T> registerBlockAndItem(String name, Supplier<T> block, boolean addToCreative, boolean genModel) {
        return registerBlockAndItem(name, block, deferredBlock -> () -> new BlockItem(deferredBlock.get(), new Item.Properties()), addToCreative, genModel);
    }

    private static <T extends Block> DeferredBlock<T> registerBlockAndItem(String name, Supplier<T> block, Function<DeferredBlock<T>, Supplier<BlockItem>> blockItem, boolean addToCreative, boolean genModel) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        DeferredItem<BlockItem> blockItemDeferredItem = IRItems.ITEMS.register(name, blockItem.apply(toReturn));
        if (genModel) {
            IRItems.BLOCK_ITEMS.add(blockItemDeferredItem);
        }
        if (addToCreative) {
            TAB_BLOCKS.add(blockItemDeferredItem);
        }
        return toReturn;
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    private static DeferredBlock<DropExperienceBlock> oreBlock(String name) {
        DeferredBlock<DropExperienceBlock> blockDeferredBlock = registerBlockAndItem(name, () -> new DropExperienceBlock(ConstantInt.of(1), oreSettings(false)));
        ORES.add(blockDeferredBlock);
        return blockDeferredBlock;
    }

    private static DeferredBlock<Block> rawStorageBlock(String name) {
        DeferredBlock<Block> blockDeferredBlock = registerBlockAndItem(name, () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_IRON_BLOCK)));
        RAW_STORAGE_BLOCKS.add(blockDeferredBlock);
        return blockDeferredBlock;
    }

    private static DeferredBlock<Block> metalStorageBlock(String name) {
        DeferredBlock<Block> blockDeferredBlock = registerBlockAndItem(name, () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
        METAL_STORAGE_BLOCKS.add(blockDeferredBlock);
        return blockDeferredBlock;
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
