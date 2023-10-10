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
    public static Item icon = IRItems.WRENCH.get();

    /**
     * Variable used for registering and storing all item groups under the "indref" mod-id
     */
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, IndustrialReforged.MODID);

    /**
     * Default Item Group for all indref items
     */
    public static final RegistryObject<CreativeModeTab> MAIN = CREATIVE_TABS.register("main", () -> CreativeModeTab.builder()
            .title(Component.literal("Industrial Reforged"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> new ItemStack(IRItems.WRENCH.get()))
            .displayItems((parameters, output) -> {
                addItem(output, IRItems.WRENCH);
                addBlock(output, IRBlocks.TEST_BLOCK);
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
