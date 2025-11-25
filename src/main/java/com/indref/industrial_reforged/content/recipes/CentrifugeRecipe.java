package com.indref.industrial_reforged.content.recipes;

import com.indref.industrial_reforged.util.recipes.SimpleRecipeSerializer;
import com.indref.industrial_reforged.util.recipes.IngredientWithCount;
import com.indref.industrial_reforged.util.recipes.RecipeUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.portingdeadmods.portingdeadlibs.api.recipes.PDLRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record CentrifugeRecipe(IngredientWithCount ingredient, List<ItemStack> results, FluidStack resultFluid,
                               int duration, int energy) implements PDLRecipe<SingleRecipeInput> {
    public static final String NAME = "centrifuge";
    public static final RecipeType<CentrifugeRecipe> TYPE = RecipeUtils.newRecipeType(NAME);
    public static final RecipeSerializer<CentrifugeRecipe> SERIALIZER;

    @Override
    public boolean matches(SingleRecipeInput recipeInput, Level level) {
        return ingredient.test(recipeInput.item());
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, ingredient.ingredient());
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider provider) {
        return results.getFirst();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return TYPE;
    }

    static {
        SERIALIZER = new SimpleRecipeSerializer<>(RecordCodecBuilder.mapCodec(builder -> builder.group(
                IngredientWithCount.CODEC.fieldOf("ingredient").forGetter(CentrifugeRecipe::ingredient),
                ItemStack.CODEC.listOf().fieldOf("results").forGetter(CentrifugeRecipe::results),
                FluidStack.OPTIONAL_CODEC.fieldOf("result_fluid").forGetter(CentrifugeRecipe::resultFluid),
                Codec.INT.fieldOf("duration").forGetter(CentrifugeRecipe::duration),
                Codec.INT.fieldOf("energy").forGetter(CentrifugeRecipe::energy)
        ).apply(builder, CentrifugeRecipe::new)), StreamCodec.composite(
                IngredientWithCount.STREAM_CODEC,
                CentrifugeRecipe::ingredient,
                ItemStack.LIST_STREAM_CODEC,
                CentrifugeRecipe::results,
                FluidStack.OPTIONAL_STREAM_CODEC,
                CentrifugeRecipe::resultFluid,
                ByteBufCodecs.INT,
                CentrifugeRecipe::duration,
                ByteBufCodecs.INT,
                CentrifugeRecipe::energy,
                CentrifugeRecipe::new
        ));
    }
}
