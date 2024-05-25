package com.indref.industrial_reforged.util.recipes;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public record IngredientWithCount(Ingredient ingredient, int count) {
    public static final IngredientWithCount EMPTY = new IngredientWithCount(Ingredient.EMPTY, -1);
    // Note: for implementation reasons count has to be above ingredient, otherwise we will get a JSON Null issue thingy
    private static final Codec<Pair<Integer, Ingredient>> PAIR_CODEC = Codec.pair(
            Codec.INT.optionalFieldOf("count", 1).codec(),
            Ingredient.CODEC
    );

    public boolean test(ItemStack itemStack) {
        return ingredient.test(itemStack) && itemStack.getCount() >= count;
    }

    public static final Codec<IngredientWithCount> CODEC = PAIR_CODEC.xmap(pair -> new IngredientWithCount(pair.getSecond(), pair.getFirst()),
            iwc -> new Pair<>(iwc.count, iwc.ingredient));
    public static final StreamCodec<RegistryFriendlyByteBuf, IngredientWithCount> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            IngredientWithCount::ingredient,
            ByteBufCodecs.INT,
            IngredientWithCount::count,
            IngredientWithCount::new
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, List<IngredientWithCount>> STREAM_LIST_CODEC = STREAM_CODEC.apply(
            ByteBufCodecs.collection(NonNullList::createWithCapacity)
    );
}
