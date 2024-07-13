package com.indref.industrial_reforged.api.data;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.data.components.ComponentTapeMeasure;
import com.indref.industrial_reforged.api.data.components.EnergyStorage;
import com.indref.industrial_reforged.api.data.components.HeatStorage;
import com.indref.industrial_reforged.api.items.bundles.AdvancedBundleContents;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

// Registry for minecraft's item data component system
// entities and blockentities are handled in IRAttachmentTypes
public final class IRDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENT_TYPES = DeferredRegister.createDataComponents(IndustrialReforged.MODID);

    public static final Supplier<DataComponentType<Boolean>> ACTIVE = registerDataComponentType("active",
            () -> builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));
    public static final Supplier<DataComponentType<Integer>> THERMOMETER_STAGE = registerDataComponentType("thermometer_stage",
            () -> builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
    public static final Supplier<DataComponentType<Integer>> BATTERY_STAGE = registerDataComponentType("battery_stage",
            () -> builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
    public static final Supplier<DataComponentType<Boolean>> HAS_RECIPE = registerDataComponentType("has_recipe",
            () -> builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));
    public static final Supplier<DataComponentType<List<ItemStack>>> STORED_RECIPE = registerDataComponentType("stored_recipe",
            () -> builder -> builder.persistent(ItemStack.CODEC.listOf()).networkSynchronized(ItemStack.LIST_STREAM_CODEC));
    public static final Supplier<DataComponentType<ComponentTapeMeasure>> TAPE_MEASURE_DATA = registerDataComponentType("tape_measure_data",
            () -> builder -> builder.persistent(ComponentTapeMeasure.CODEC).networkSynchronized(ComponentTapeMeasure.STREAM_CODEC));
    public static final Supplier<DataComponentType<AdvancedBundleContents>> ADVANCED_BUNDLE_CONTENTS = registerDataComponentType("advanced_bundle_contents",
            () -> builder -> builder.persistent(AdvancedBundleContents.CODEC).networkSynchronized(AdvancedBundleContents.STREAM_CODEC));

    // Data for capabilities
    public static final Supplier<DataComponentType<EnergyStorage>> ENERGY = registerDataComponentType("energy",
            () -> builder -> builder.persistent(EnergyStorage.CODEC).networkSynchronized(EnergyStorage.STREAM_CODEC));
    public static final Supplier<DataComponentType<HeatStorage>> HEAT = registerDataComponentType("heat",
            () -> builder -> builder.persistent(HeatStorage.CODEC).networkSynchronized(HeatStorage.STREAM_CODEC));
    public static final Supplier<DataComponentType<SimpleFluidContent>> FLUID = registerDataComponentType("fluid",
            () -> builder -> builder.persistent(SimpleFluidContent.CODEC).networkSynchronized(SimpleFluidContent.STREAM_CODEC));
    public static final Supplier<DataComponentType<List<ItemStack>>> ITEM = registerDataComponentType("item",
            () -> builder -> builder.persistent(ItemStack.CODEC.listOf()).networkSynchronized(ItemStack.LIST_STREAM_CODEC));

    public static <T> Supplier<DataComponentType<T>> registerDataComponentType(
            String name, Supplier<UnaryOperator<DataComponentType.Builder<T>>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.get().apply(DataComponentType.builder()).build());
    }
}
