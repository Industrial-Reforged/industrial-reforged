package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.items.armor.HazmatSuiteItem;
import com.indref.industrial_reforged.registries.items.food.EnergyDrinkItem;
import com.indref.industrial_reforged.registries.items.misc.Debugger;
import com.indref.industrial_reforged.registries.items.misc.FertilizerItem;
import com.indref.industrial_reforged.registries.items.misc.MiningPipeBlockItem;
import com.indref.industrial_reforged.registries.items.storage.FluidCellItem;
import com.indref.industrial_reforged.registries.items.storage.LunchBagItem;
import com.indref.industrial_reforged.registries.items.storage.ToolboxItem;
import com.indref.industrial_reforged.registries.items.tools.*;
import com.indref.industrial_reforged.test.EnergyTestItem;
import com.indref.industrial_reforged.test.HeatTestItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;

import java.util.Map;
import java.util.function.Supplier;

public class IRItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, IndustrialReforged.MODID);
    //tools
    public static final RegistryObject<Item> WRENCH = registerItem("wrench",
            () -> new WrenchItem(new Item.Properties()));
    public static final RegistryObject<Item> HAMMER = registerItem("hammer",
            () -> new HammerItem(new Item.Properties()));
    public static final RegistryObject<Item> TREE_TAP = registerItem("tree_tap",
            () -> new TreeTapItem(new Item.Properties()));
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
    public static final RegistryObject<Item> DEBUGGER = registerItem("debugger",
            () -> new Debugger(new Item.Properties()));


    //misc
    public static final RegistryObject<Item> RUBBER_SHEET = registerItem("rubber_sheet",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> STICKY_RESIN = registerItem("sticky_resin",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FERTILIZER = registerItem("fertilizer",
            () -> new FertilizerItem(new Item.Properties()));

    public static final RegistryObject<Item> CORN_SEEDS = ITEMS.register("corn_seeds",
            () -> new ItemNameBlockItem(IRBlocks.CORN_CROP.get(), new Item.Properties()));

    // Needs to be registered manual for custom placement
    public static final RegistryObject<Item> MINING_PIPE = registerItem("mining_pipe",
            () -> new MiningPipeBlockItem(new Item.Properties()));

    //ores
    public static final RegistryObject<Item> RAW_BAUXITE = registerItem("raw_bauxite",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_CHROMIUM = registerItem("raw_chromium",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_IRIDIUM = registerItem("raw_iridium",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_LEAD = registerItem("raw_lead",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_NICKEL = registerItem("raw_nickel",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_TIN = registerItem("raw_tin",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_URANIUM = registerItem("raw_uranium",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SOAP_WATER_BUCKET = registerItem("soap_water_bucket",
            () -> new BucketItem(IRFluids.SOURCE_SOAP_WATER,
                    new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

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
