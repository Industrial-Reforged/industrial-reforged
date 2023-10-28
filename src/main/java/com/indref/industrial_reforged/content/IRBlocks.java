package com.indref.industrial_reforged.content;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.content.blocks.*;
import com.indref.industrial_reforged.test.TestMultiblockController;
import com.indref.industrial_reforged.test.TestMultiblockPart;
import com.indref.industrial_reforged.worldgen.RubberTreeGrower;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class IRBlocks {
    /**
     * Variable used for registering and storing all blocks under the "indref" mod-id
     */
	public static final BlockSetType RUBBER_SET_TYPE = BlockSetType.register(new BlockSetType(IndustrialReforged.MODID + ":rubber"));
	public static final WoodType RUBBER_WOOD_TYPE = WoodType.register(new WoodType(IndustrialReforged.MODID + ":rubber", RUBBER_SET_TYPE));
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, IndustrialReforged.MODID);

    public static final RegistryObject<Block> TEST_BLOCK_ENERGY = registerBlockAndItem("test_block_energy",
            () -> new EnergyTestBlock(BlockBehaviour.Properties.of()));
    public static final RegistryObject<Block> REFACTORY_BRICK = registerBlockAndItem("refactory_brick",
            () -> new RefactoryBrickBlock(BlockBehaviour.Properties.of()));
    public static final RegistryObject<Block> TEST_BLOCK_HEAT = registerBlockAndItem("test_block_heat",
            () -> new HeatTestBlock(BlockBehaviour.Properties.of()));
    public static final RegistryObject<Block> BASIC_MACHINE_FRAME = registerBlockAndItem("basic_machine_frame",
            () -> new MachineFrameBlock(BlockBehaviour.Properties.of()));
    public static final RegistryObject<Block> PRIMITIVE_FORGE = registerBlockAndItem("primitive_forge",
            () -> new PrimitiveForgeBlock(BlockBehaviour.Properties.of()));
    public static final RegistryObject<Block> SIMPLE_PRESS = registerBlockAndItem("simple_press",
            () -> new SimplePressBlock(BlockBehaviour.Properties.of()));
    public static final RegistryObject<Block> CORN_CROP = registerBlock("corn_crop",
            () -> new CornCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noOcclusion().noCollission()));
    public static final RegistryObject<Block> TEST_CONTROLLER = registerBlockAndItem("test_controller",
            () -> new TestMultiblockController(BlockBehaviour.Properties.of()));
    public static final RegistryObject<Block> TEST_PART = registerBlockAndItem("test_part",
            () -> new TestMultiblockPart(BlockBehaviour.Properties.of()));

    // Rubber
    public static final RegistryObject<Block> RUBBER_TREE_LOG = registerBlockAndItem("rubber_tree_log",
            () -> new RubberTreeLogBlock());
    public static final RegistryObject<Block> STRIPPED_RUBBER_TREE_LOG = registerBlockAndItem("stripped_rubber_tree_log",
    		() -> new RubberTreeLogBlock());
    public static final RegistryObject<Block> RUBBER_TREE_WOOD = registerBlockAndItem("rubber_tree_wood",
    		() -> new RubberTreeLogBlock());
    public static final RegistryObject<Block> STRIPPED_RUBBER_TREE_WOOD = registerBlockAndItem("stripped_rubber_tree_wood",
    		() -> new RubberTreeLogBlock());
    public static final RegistryObject<Block> RUBBER_TREE_LEAVES = registerBlockAndItem("rubber_tree_leaves",
            () -> new RubberTreeLeavesBlock());
    public static final RegistryObject<Block> RUBBER_TREE_SAPLING = registerBlockAndItem("rubber_tree_sapling",
            () -> new SaplingBlock(new RubberTreeGrower(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> RUBBER_TREE_RESIN_HOLE = registerBlockAndItem("rubber_tree_resin_hole",
            () -> new RubberTreeResinHoleBlock());
    public static final RegistryObject<Block> RUBBER_TREE_PLANKS = registerBlockAndItem("rubber_tree_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    @SuppressWarnings("deprecation")
	public static final RegistryObject<Block> RUBBER_TREE_STAIRS = registerBlockAndItem("rubber_tree_stairs",
    		() -> new StairBlock(RUBBER_TREE_PLANKS.get().defaultBlockState(),BlockBehaviour.Properties.copy(Blocks.OAK_STAIRS)));
    public static final RegistryObject<Block> RUBBER_TREE_DOOR = registerBlockAndItem("rubber_tree_door",
    		() -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR), RUBBER_SET_TYPE));
    public static final RegistryObject<Block> RUBBER_TREE_PRESSURE_PLATE = registerBlockAndItem("rubber_tree_pressure_plate",
    		() -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(Blocks.OAK_PRESSURE_PLATE), RUBBER_SET_TYPE));
    public static final RegistryObject<Block> RUBBER_TREE_FENCE = registerBlockAndItem("rubber_tree_fence",
    		() -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR)));
    public static final RegistryObject<Block> RUBBER_TREE_TRAPDOOR = registerBlockAndItem("rubber_tree_trapdoor",
    		() -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_TRAPDOOR), RUBBER_SET_TYPE));
    public static final RegistryObject<Block> RUBBER_TREE_FENCE_GATE = registerBlockAndItem("rubber_tree_fence_gate",
    		() -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE), RUBBER_WOOD_TYPE));
    public static final RegistryObject<Block> RUBBER_TREE_BUTTON = registerBlockAndItem("rubber_tree_button",
    		() -> new ButtonBlock(BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON), RUBBER_SET_TYPE, 30, true));
    public static final RegistryObject<Block> RUBBER_TREE_SLAB = registerBlockAndItem("rubber_tree_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB)));

    /**
     * Registers a new block and item
     *
     * @param name  name of the block
     * @param block the Blocks you want to add and configure using `new {@link net.minecraft.world.item.Item.Properties}()`
     * @return returns the block-registry-object that can then be used to refer to the block
     */
    private static RegistryObject<Block> registerBlockAndItem(String name, Supplier<Block> block) {
        RegistryObject<Block> toReturn = BLOCKS.register(name, block);
        registerItemFromBlock(name, toReturn);
        return toReturn;
    }

    private static RegistryObject<Block> registerBlock(String name, Supplier<Block> block) {
        return BLOCKS.register(name, block);
    }

    private static <T extends Block> void registerItemFromBlock(String name, RegistryObject<T> block) {
        IRItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}
