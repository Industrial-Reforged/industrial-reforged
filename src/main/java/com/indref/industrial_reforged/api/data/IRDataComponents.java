package com.indref.industrial_reforged.api.data;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.data.components.ComponentEnergyStorage;
import com.indref.industrial_reforged.api.data.components.ComponentHeatStorage;
import com.indref.industrial_reforged.api.data.components.ComponentTapeMeasure;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
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
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, IndustrialReforged.MODID);

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

    // Data for capabilities
    public static final Supplier<DataComponentType<ComponentEnergyStorage>> ENERGY = registerDataComponentType("energy",
            () -> builder -> builder.persistent(ComponentEnergyStorage.CODEC).networkSynchronized(ComponentEnergyStorage.STREAM_CODEC));
    public static final Supplier<DataComponentType<ComponentHeatStorage>> HEAT = registerDataComponentType("heat",
            () -> builder -> builder.persistent(ComponentHeatStorage.CODEC).networkSynchronized(ComponentHeatStorage.STREAM_CODEC));
    public static final Supplier<DataComponentType<SimpleFluidContent>> FLUID = registerDataComponentType("fluid",
            () -> builder -> builder.persistent(SimpleFluidContent.CODEC).networkSynchronized(SimpleFluidContent.STREAM_CODEC));
    public static final Supplier<DataComponentType<List<ItemStack>>> ITEM = registerDataComponentType("item",
            () -> builder -> builder.persistent(ItemStack.CODEC.listOf()).networkSynchronized(ItemStack.LIST_STREAM_CODEC));

    public static <T> Supplier<DataComponentType<T>> registerDataComponentType(
            String name, Supplier<UnaryOperator<DataComponentType.Builder<T>>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.get().apply(DataComponentType.builder()).build());
    }
}
