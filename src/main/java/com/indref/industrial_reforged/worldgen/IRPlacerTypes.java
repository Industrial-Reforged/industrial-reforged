package com.indref.industrial_reforged.worldgen;

import com.indref.industrial_reforged.IndustrialReforged;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class IRPlacerTypes {
    public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACERS = DeferredRegister.create(BuiltInRegistries.FOLIAGE_PLACER_TYPE, IndustrialReforged.MODID);
    public static final DeferredRegister<TrunkPlacerType<?>> TRUNK_PLACERS = DeferredRegister.create(Registries.TRUNK_PLACER_TYPE, IndustrialReforged.MODID);

    public static final Supplier<FoliagePlacerType<RubberTreeFoliagePlacer>> RUBBER_TREE_FOLIAGE_PLACER =
        FOLIAGE_PLACERS.register("rubber_tree_foliage_placer", () -> new FoliagePlacerType<>(RubberTreeFoliagePlacer.CODEC));
    public static final Supplier<TrunkPlacerType<RubberTreeTrunkPlacer>> RUBBER_TREE_TRUNK_PLACER =
        TRUNK_PLACERS.register("rubber_tree_trunk_placer", () -> new TrunkPlacerType<>(RubberTreeTrunkPlacer.CODEC));
}