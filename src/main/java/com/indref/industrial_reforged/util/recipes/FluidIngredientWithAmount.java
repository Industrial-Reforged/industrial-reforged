package com.indref.industrial_reforged.util.recipes;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

public record FluidIngredientWithAmount(FluidIngredient fluidIngredient, int amount) {
    public static final FluidIngredientWithAmount EMPTY = new FluidIngredientWithAmount(FluidIngredient.empty(), -1);
    // Note: for implementation reasons amount has to be above fluidIngredient, otherwise we will get a JSON Null issue thingy
    private static final Codec<Pair<Integer, FluidIngredient>> PAIR_CODEC = Codec.pair(
            Codec.INT.optionalFieldOf("amount", 1).codec(),
            FluidIngredient.CODEC
    );

    public boolean test(FluidStack fluidStack) {
        return fluidIngredient.test(fluidStack) && fluidStack.getAmount() >= amount;
    }

    public static final Codec<FluidIngredientWithAmount> CODEC = PAIR_CODEC.xmap(pair -> new FluidIngredientWithAmount(pair.getSecond(), pair.getFirst()),
            iwc -> new Pair<>(iwc.amount(), iwc.fluidIngredient()));
    public static final StreamCodec<RegistryFriendlyByteBuf, FluidIngredientWithAmount> STREAM_CODEC = StreamCodec.composite(
            FluidIngredient.STREAM_CODEC,
            FluidIngredientWithAmount::fluidIngredient,
            ByteBufCodecs.INT,
            FluidIngredientWithAmount::amount,
            FluidIngredientWithAmount::new
    );

    public static FluidIngredientWithAmount of(FluidStack fluidStack) {
        return new FluidIngredientWithAmount(FluidIngredient.of(fluidStack), fluidStack.getAmount());
    }

    public static FluidIngredientWithAmount of(TagKey<Fluid> fluidTagKey) {
        return new FluidIngredientWithAmount(FluidIngredient.tag(fluidTagKey), 1);
    }

    public static FluidIngredientWithAmount of(TagKey<Fluid> fluidTagKey, int amount) {
        return new FluidIngredientWithAmount(FluidIngredient.tag(fluidTagKey), amount);
    }

    public static FluidIngredientWithAmount of(Fluid fluid) {
        return new FluidIngredientWithAmount(FluidIngredient.of(fluid), 1);
    }

    public static FluidIngredientWithAmount of(Fluid fluid, int amount) {
        return new FluidIngredientWithAmount(FluidIngredient.of(fluid), amount);
    }
}
