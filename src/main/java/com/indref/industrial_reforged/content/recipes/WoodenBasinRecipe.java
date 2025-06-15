package com.indref.industrial_reforged.content.recipes;

import com.indref.industrial_reforged.api.recipes.SimpleRecipeSerializer;
import com.indref.industrial_reforged.content.recipes.recipeInputs.ItemFluidRecipeInput;
import com.indref.industrial_reforged.util.recipes.FluidIngredientWithAmount;
import com.indref.industrial_reforged.util.recipes.IngredientWithCount;
import com.indref.industrial_reforged.util.recipes.RecipeUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.portingdeadmods.portingdeadlibs.api.recipes.PDLRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
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

public record WoodenBasinRecipe(IngredientWithCount ingredient, FluidIngredientWithAmount fluidIngredient,
                                FluidStack resultFluid,
                                int duration) implements PDLRecipe<ItemFluidRecipeInput> {
    public static final String NAME = "wooden_basin";
    public static final RecipeType<WoodenBasinRecipe> TYPE = RecipeUtils.newRecipeType(NAME);
    public static final RecipeSerializer<WoodenBasinRecipe> SERIALIZER;

    @Override
    public boolean matches(ItemFluidRecipeInput recipeInput, Level level) {
        return ingredient.test(recipeInput.itemStack()) && fluidIngredient.test(recipeInput.fluidStack());
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, ingredient.ingredient());
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
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
                IngredientWithCount.CODEC.fieldOf("ingredient").forGetter(WoodenBasinRecipe::ingredient),
                FluidIngredientWithAmount.CODEC.fieldOf("fluid_ingredient").forGetter(WoodenBasinRecipe::fluidIngredient),
                FluidStack.OPTIONAL_CODEC.fieldOf("result_fluid").forGetter(WoodenBasinRecipe::resultFluid),
                Codec.INT.fieldOf("duration").forGetter(WoodenBasinRecipe::duration)
        ).apply(builder, WoodenBasinRecipe::new)), StreamCodec.composite(
                IngredientWithCount.STREAM_CODEC,
                WoodenBasinRecipe::ingredient,
                FluidIngredientWithAmount.STREAM_CODEC,
                WoodenBasinRecipe::fluidIngredient,
                FluidStack.OPTIONAL_STREAM_CODEC,
                WoodenBasinRecipe::resultFluid,
                ByteBufCodecs.INT,
                WoodenBasinRecipe::duration,
                WoodenBasinRecipe::new
        ));
    }
}
