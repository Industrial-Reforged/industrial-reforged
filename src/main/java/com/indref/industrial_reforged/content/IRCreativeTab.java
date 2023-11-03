package com.indref.industrial_reforged.content;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.items.container.IEnergyItem;
import com.indref.industrial_reforged.api.items.container.IFluidItem;
import com.indref.industrial_reforged.api.items.container.IHeatItem;
import com.indref.industrial_reforged.networking.IRPackets;
import com.indref.industrial_reforged.networking.packets.S2CFluidSync;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;

import java.util.Map;
import java.util.Set;

public class IRCreativeTab {
    /**
     * Variable used for registering and storing all item groups under the "indref" mod-id
     */
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, IndustrialReforged.MODID);

    /**
     * Default Item Group for all indref items
     */
    public static final RegistryObject<CreativeModeTab> MAIN = CREATIVE_TABS.register("main", () -> CreativeModeTab.builder()
            .title(Component.translatable("creative_tab.indref"))
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .icon(() -> new ItemStack(IRBlocks.BASIC_MACHINE_FRAME.get()))
            .displayItems((parameters, output) -> {
                // Tools
                addItem(output, IRItems.WRENCH);
                addItem(output, IRItems.HAMMER);
                addItem(output, IRItems.TREE_TAP);
                addPoweredItem(output, IRItems.SCANNER);
                addItem(output, IRItems.TAPE_MEASURE);

                // Storage items
                addItem(output, IRItems.TOOLBOX);
                addItem(output, IRItems.SEED_POUCH);
                addItem(output, IRItems.LUNCH_BAG);
                addVariantForAllFluids(output, IRItems.FLUID_CELL);

                // armor
                addItem(output, IRItems.HAZMAT_BOOTS);
                addItem(output, IRItems.HAZMAT_LEGGINGS);
                addItem(output, IRItems.HAZMAT_CHESTPLATE);
                addItem(output, IRItems.HAZMAT_HELMET);

                //food
                addItem(output, IRItems.EMPTY_CAN);
                addItem(output, IRItems.NUKA_COLA);
                addItem(output, IRItems.ENERGY_DRINK);

                // misc and crafting items
                addItem(output, IRItems.RUBBER_SHEET);
                addItem(output, IRItems.STICKY_RESIN);
                addItem(output, IRItems.CORN_SEEDS);

                // misc blocks
                addBlock(output, IRBlocks.BASIC_MACHINE_FRAME);
                addBlock(output, IRBlocks.REFRACTORY_BRICK);
                addBlock(output, IRBlocks.COIL);

                // Machines
                addBlock(output, IRBlocks.SIMPLE_PRESS);

                // test objects
                addPoweredItem(output, IRItems.ENERGY_TEST_ITEM);
                addPoweredItem(output, IRItems.HEAT_TEST_ITEM);
                addBlock(output, IRBlocks.TEST_BLOCK_ENERGY);
                addBlock(output, IRBlocks.TEST_CONTROLLER);
                addBlock(output, IRBlocks.TEST_PART);
                
                // Rubber Tree
                addBlock(output, IRBlocks.RUBBER_TREE_BUTTON);
                addBlock(output, IRBlocks.RUBBER_TREE_DOOR);
                addBlock(output, IRBlocks.RUBBER_TREE_FENCE);
                addBlock(output, IRBlocks.RUBBER_TREE_FENCE_GATE);
                addBlock(output, IRBlocks.RUBBER_TREE_LEAVES);
                addBlock(output, IRBlocks.RUBBER_TREE_LOG);
                addBlock(output, IRBlocks.RUBBER_TREE_PLANKS);
                addBlock(output, IRBlocks.RUBBER_TREE_PRESSURE_PLATE);
                addBlock(output, IRBlocks.RUBBER_TREE_SAPLING);
                addBlock(output, IRBlocks.RUBBER_TREE_SLAB);
                addBlock(output, IRBlocks.RUBBER_TREE_STAIRS);
                addBlock(output, IRBlocks.RUBBER_TREE_TRAPDOOR);
                addBlock(output, IRBlocks.RUBBER_TREE_WOOD);
                addBlock(output, IRBlocks.STRIPPED_RUBBER_TREE_LOG);
                addBlock(output, IRBlocks.STRIPPED_RUBBER_TREE_WOOD);
            }).build());

    /**
     * Add a new item to a creative tab
     * @param output Specify the creative tab
     * @param item Specify the item to add
     */
    private static void addItem(CreativeModeTab.Output output, RegistryObject<Item> item) {
        output.accept(item.get());
    }

    public static void addPoweredItem(CreativeModeTab.Output output, RegistryObject<Item> item) {
        // Add base item
        output.accept(item.get());
        ItemStack stack = new ItemStack(item.get());
        if (item.get() instanceof IEnergyItem energyItem)
            energyItem.setStored(stack, energyItem.getCapacity());

        if (item.get() instanceof IHeatItem heatItem)
            heatItem.setStored(stack, heatItem.getCapacity());

        output.accept(stack);
    }

    public static void addVariantForAllFluids(CreativeModeTab.Output output, RegistryObject<Item> item) {
        // Add base item
        output.accept(item.get());
        Set<Map.Entry<ResourceKey<Fluid>, Fluid>> fluids = ForgeRegistries.FLUIDS.getEntries();
        IndustrialReforged.LOGGER.info(fluids.toString());
        for (Map.Entry<ResourceKey<Fluid>, Fluid> fluid : fluids) {
            ItemStack stack = new ItemStack(item.get());
            if (!fluid.getValue().equals(Fluids.EMPTY) && fluid.getValue().isSource(fluid.getValue().defaultFluidState())) {
                if (item.get() instanceof IFluidItem fluidContainerItem)
                    fluidContainerItem.tryFill(fluid.getValue(), 1000, stack);
                IRPackets.sendToClients(new S2CFluidSync(fluid.getValue(), 1000, stack));
                IndustrialReforged.LOGGER.info("Registering fluid cell: "+fluid.getValue());
                output.accept(stack);
            }
        }
    }

    /**
     * Add a new item to a creative tab
     * @param output Specify the creative tab
     * @param block Specify the item to add
     */
    private static void addBlock(CreativeModeTab.Output output, RegistryObject<Block> block) {
        output.accept(block.get());
    }
}
