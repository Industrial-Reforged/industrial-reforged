package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.registries.gui.menus.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class IRMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, IndustrialReforged.MODID);
    public static final Supplier<MenuType<FireBoxMenu>> FIREBOX_MENU =
            registerMenuType("firebox_menu", FireBoxMenu::new);
    public static final Supplier<MenuType<CrucibleMenu>> CRUCIBLE_MENU =
            registerMenuType("crucible_menu", CrucibleMenu::new);
    public static final Supplier<MenuType<BlastFurnaceMenu>> BLAST_FURNACE_MENU =
            registerMenuType("blast_furnace_menu", BlastFurnaceMenu::new);
    public static final Supplier<MenuType<CraftingStationMenu>> CRAFTING_STATION_MENU =
            registerMenuType("crafting_station_menu", CraftingStationMenu::new);
    public static final Supplier<MenuType<CentrifugeMenu>> CENTRIFUGE_MENU =
            registerMenuType("centrifuge_menu", CentrifugeMenu::new);
    public static final Supplier<MenuType<BasicGeneratorMenu>> BASIC_GENERATOR_MENU =
            registerMenuType("basic_generator_menu", BasicGeneratorMenu::new);

    private static <T extends AbstractContainerMenu>Supplier<MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }
}
