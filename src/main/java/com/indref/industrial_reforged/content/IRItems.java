package com.indref.industrial_reforged.content;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.items.EnergyTestItem;
import com.indref.industrial_reforged.api.items.ToolItem;
import com.indref.industrial_reforged.content.items.ScannerItem;
import com.indref.industrial_reforged.content.items.ToolboxItem;
import com.indref.industrial_reforged.content.items.WrenchItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class IRItems {
    /**
     * Variable used for registering and storing all items under the "indref" mod-id
     */
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, IndustrialReforged.MODID);

    public static final RegistryObject<Item> WRENCH = registerItem("wrench", () -> new WrenchItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HAMMER = registerItem("hammer", () -> new ToolItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> TOOLBOX = registerItem("toolbox", () -> new ToolboxItem(new Item.Properties()));
    public static final RegistryObject<Item> SCANNER = registerItem("scanner", () -> new ScannerItem(new Item.Properties()));
    public static final RegistryObject<Item> ENERGY_TEST_ITEM = registerItem("energy_test", () -> new EnergyTestItem(new Item.Properties()));
    /**
     * Registers a new item
     *
     * @param name the item name referred to as id in-game
     * @param item the item you want to add and configure using `new {@link net.minecraft.world.item.Item.Properties}()`
     * @return returns the item-registry-object built from the parameters
     */
    private static RegistryObject<Item> registerItem(String name, Supplier<Item> item) {
        return ITEMS.register(name, item);
    }

}
