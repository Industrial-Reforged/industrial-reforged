package com.indref.industrial_reforged.registries.recipes;

import com.indref.industrial_reforged.api.recipes.IRRecipe;
import com.indref.industrial_reforged.util.recipes.IngredientWithCount;
import com.indref.industrial_reforged.util.recipes.RecipeUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record CentrifugeRecipe(IngredientWithCount ingredient, List<ItemStack> results,
                               int duration) implements IRRecipe<SimpleContainer> {
    public static final String NAME = "centrifuge";
    public static final RecipeType<CentrifugeRecipe> TYPE = RecipeUtils.newRecipeType(NAME);

    @Override
    public boolean matches(SimpleContainer simpleContainer, Level level) {
        if (level.isClientSide()) return false;

        return ingredient.test(simpleContainer.getItem(0));
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return TYPE;
    }

    public static class Serializer implements RecipeSerializer<CentrifugeRecipe> {
        public static final CentrifugeRecipe.Serializer INSTANCE = new CentrifugeRecipe.Serializer();
        private static final MapCodec<CentrifugeRecipe> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder.group(
                IngredientWithCount.CODEC.fieldOf("ingredient").forGetter(CentrifugeRecipe::ingredient),
                ItemStack.CODEC.listOf().fieldOf("results").forGetter(CentrifugeRecipe::results),
                Codec.INT.fieldOf("duration").forGetter(CentrifugeRecipe::duration)
        ).apply(builder, CentrifugeRecipe::new));
        private static final StreamCodec<RegistryFriendlyByteBuf, CentrifugeRecipe> STREAM_CODEC = StreamCodec.composite(
                IngredientWithCount.STREAM_CODEC,
                CentrifugeRecipe::ingredient,
                ItemStack.LIST_STREAM_CODEC,
                CentrifugeRecipe::results,
                ByteBufCodecs.INT,
                CentrifugeRecipe::duration,
                CentrifugeRecipe::new
        );

        private Serializer() {
        }

        @Override
        public @NotNull MapCodec<CentrifugeRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, CentrifugeRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
