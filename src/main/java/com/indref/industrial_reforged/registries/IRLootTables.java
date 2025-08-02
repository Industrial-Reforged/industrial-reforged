package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.data.IRDataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.SetComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class IRLootTables {
    public static final Map<ResourceKey<LootTable>, LootTable.Builder> LOOT_TABLES = new HashMap<>();

    public static final ResourceKey<LootTable> DUNGEON = register("chests/dungeon", LootTable.lootTable()
            .withPool(LootPool.lootPool()
                    .add(LootItem.lootTableItem(IRItems.GUIDE)
                            .when(chance(0.35)))
            )
            .withPool(LootPool.lootPool()
                    .setRolls(UniformGenerator.between(2, 4))
                    .setBonusRolls(UniformGenerator.between(1, 3))
                    .add(LootItem.lootTableItem(IRItems.STEEL_INGOT)
                            .apply(countBetween(0, 2)))
                    .add(LootItem.lootTableItem(IRItems.TIN_INGOT)
                            .apply(countBetween(0, 4)))
                    .add(LootItem.lootTableItem(IRItems.ALUMINUM_PLATE)
                            .apply(countBetween(0, 1)))
                    .add(LootItem.lootTableItem(IRItems.CIRCUIT_BOARD))
                    .add(LootItem.lootTableItem(IRItems.COPPER_WIRE)
                            .apply(countBetween(0, 2)))
                    .add(LootItem.lootTableItem(IRItems.FLUID_CELL)
                            .apply(fluid(Fluids.WATER, 2000))
                            .when(chance(0.35)))
                    .add(LootItem.lootTableItem(IRItems.FLUID_CELL)
                            .apply(fluid(IRFluids.MOLTEN_ALUMINUM.getStillFluid(), 500))
                            .when(chance(0.55)))
                    .add(LootItem.lootTableItem(IRItems.FLUID_CELL)
                            .apply(fluid(IRFluids.MOLTEN_TIN.getStillFluid(), 800))
                            .when(chance(0.45)))
            ));

    private static LootItemConditionalFunction.@NotNull Builder<?> fluid(FlowingFluid fluid, int amount) {
        return SetComponentsFunction.setComponent(IRDataComponents.FLUID.get(), SimpleFluidContent.copyOf(new FluidStack(fluid, amount)));
    }

    private static LootItemCondition.@NotNull Builder chance(double chance) {
        return LootItemRandomChanceCondition.randomChance((float) chance);
    }

    private static LootItemConditionalFunction.@NotNull Builder<?> countBetween(int min, int max) {
        return SetItemCountFunction.setCount(UniformGenerator.between(min, max));
    }

    private static ResourceKey<LootTable> register(String name, LootTable.Builder table) {
        return register(ResourceKey.create(Registries.LOOT_TABLE, IndustrialReforged.rl(name)), table);
    }

    private static ResourceKey<LootTable> register(ResourceKey<LootTable> name, LootTable.Builder table) {
        LOOT_TABLES.put(name, table);
        return name;
    }
}
