package com.indref.industrial_reforged.content;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.content.items.armor.HazmatSuiteItem;
import com.indref.industrial_reforged.content.items.food.EnergyDrinkItem;
import com.indref.industrial_reforged.content.items.storage.FluidCellItem;
import com.indref.industrial_reforged.content.items.storage.LunchBagItem;
import com.indref.industrial_reforged.content.items.storage.SeedPouchItem;
import com.indref.industrial_reforged.content.items.storage.ToolboxItem;
import com.indref.industrial_reforged.content.items.tools.*;
import com.indref.industrial_reforged.test.EnergyTestItem;
import com.indref.industrial_reforged.test.HeatTestItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;

import java.util.function.Supplier;

public class IRItems {
    /**
     * Variable used for registering and storing all items under the "indref" mod-id
     */
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, IndustrialReforged.MODID);
    //tools
    public static final RegistryObject<Item> WRENCH = registerItem("wrench",
            () -> new WrenchItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HAMMER = registerItem("hammer",
            () -> new ToolItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> TREE_TAP = registerItem("tree_tap",
            () -> new TreeTapItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SCANNER = registerItem("scanner",
            () -> new ScannerItem(new Item.Properties()));
    public static final RegistryObject<Item> TAPE_MEASURE = registerItem("tape_measure",
            () -> new TapeMeasureItem(new Item.Properties()));

    // Item storages
    public static final RegistryObject<Item> LUNCH_BAG = registerItem("lunch_bag",
            () -> new LunchBagItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> FLUID_CELL = registerItem("fluid_cell",
            () -> new FluidCellItem(new Item.Properties().stacksTo(16), 1000));
    public static final RegistryObject<Item> TOOLBOX = registerItem("toolbox",
            () -> new ToolboxItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SEED_POUCH = registerItem("seed_pouch",
            () -> new SeedPouchItem(new Item.Properties().stacksTo(1)));
    // Canned items
    public static final RegistryObject<Item> EMPTY_CAN = registerItem("empty_can",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> NUKA_COLA = registerItem("nuka_cola",
            () -> new EnergyDrinkItem(new Item.Properties().food(new FoodProperties.Builder().saturationMod(0.1f)
                    .effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 300, 1), 1F)
                    .effect(new MobEffectInstance(MobEffects.LUCK, 300, 1), 1F)
                    .alwaysEat()
                    .fast()
                    .build()
            )));
    public static final RegistryObject<Item> ENERGY_DRINK = registerItem("energy_drink",
            () -> new EnergyDrinkItem(new Item.Properties().food(new FoodProperties.Builder().saturationMod(0.1f)
                    .effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 500, 1), 1F)
                    .effect(new MobEffectInstance(MobEffects.DIG_SPEED, 500, 1), 1F)
                    .alwaysEat()
                    .fast()
                    .build()
            )));
    // armor

    public static final RegistryObject<Item> HAZMAT_BOOTS = registerItem("hazmat_boots",
            () -> new HazmatSuiteItem(ArmorItem.Type.BOOTS, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HAZMAT_LEGGINGS = registerItem("hazmat_leggings",
            () -> new HazmatSuiteItem(ArmorItem.Type.LEGGINGS, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HAZMAT_CHESTPLATE = registerItem("hazmat_chestplate",
            () -> new HazmatSuiteItem(ArmorItem.Type.CHESTPLATE, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> HAZMAT_HELMET = registerItem("hazmat_helmet",
            () -> new HazmatSuiteItem(ArmorItem.Type.HELMET, new Item.Properties().stacksTo(1)));

    //test items
    public static final RegistryObject<Item> ENERGY_TEST_ITEM = registerItem("energy_test",
            () -> new EnergyTestItem(new Item.Properties()));
    public static final RegistryObject<Item> HEAT_TEST_ITEM = registerItem("heat_test",
            () -> new HeatTestItem(new Item.Properties()));

    //misc
    public static final RegistryObject<Item> RUBBER_SHEET = registerItem("rubber_sheet",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> STICKY_RESIN = registerItem("sticky_resin",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CORN_SEEDS = ITEMS.register("corn_seeds",
            () -> new ItemNameBlockItem(IRBlocks.CORN_CROP.get(), new Item.Properties()));


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
