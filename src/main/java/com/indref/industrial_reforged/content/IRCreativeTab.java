package com.indref.industrial_reforged.content;

import com.indref.industrial_reforged.IndustrialReforged;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

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
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> new ItemStack(IRItems.WRENCH.get()))
            .displayItems((parameters, output) -> {
                // Tools
                addItem(output, IRItems.WRENCH);
                addItem(output, IRItems.HAMMER);
                addItem(output, IRItems.SCANNER);

                // Storage items
                addItem(output, IRItems.TOOLBOX);
                addItem(output, IRItems.SEED_POUCH);

                // armor
                addItem(output, IRItems.HAZMAT_BOOTS);

                // misc and crafting items
                addItem(output, IRItems.RUBBER_SHEET);
                addItem(output, IRItems.CORN_SEEDS);

                // test objects
                addItem(output, IRItems.ENERGY_TEST_ITEM);
                addBlock(output, IRBlocks.TEST_BLOCK_ENERGY);
            }).build());

    /**
     * Add a new item to a creative tab
     * @param output Specify the creative tab
     * @param item Specify the item to add
     */
    private static void addItem(CreativeModeTab.Output output, RegistryObject<Item> item) {
        output.accept(item.get());
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
