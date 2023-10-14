package com.indref.industrial_reforged.content;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.content.blocks.CornCropBlock;
import com.indref.industrial_reforged.content.blocks.EnergyTestBlock;
import com.indref.industrial_reforged.content.blocks.PrimitiveForgeBlock;
import com.indref.industrial_reforged.content.blocks.SimplePressBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class IRBlocks {
    /**
     * Variable used for registering and storing all blocks under the "indref" mod-id
     */
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, IndustrialReforged.MODID);

    public static final RegistryObject<Block> TEST_BLOCK_ENERGY = registerBlockAndItem("test_block_energy",
            () -> new EnergyTestBlock(BlockBehaviour.Properties.of()));
    public static final RegistryObject<Block> PRIMITIVE_FORGE = registerBlockAndItem("primitive_forge",
            () -> new PrimitiveForgeBlock(BlockBehaviour.Properties.of()));
    public static final RegistryObject<Block> SIMPLE_PRESS = registerBlockAndItem("simple_press",
            () -> new SimplePressBlock(BlockBehaviour.Properties.of()));
    public static final RegistryObject<Block> CORN_CROP = registerBlock("corn_crop",
            () -> new CornCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noOcclusion().noCollission()));


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
