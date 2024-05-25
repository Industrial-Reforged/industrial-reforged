package com.indref.industrial_reforged.registries.recipes;

import com.indref.industrial_reforged.api.recipes.IRRecipe;
import com.indref.industrial_reforged.util.recipes.RecipeUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record CrucibleSmeltingRecipe(Ingredient ingredient, FluidStack resultFluid, int duration,
                                     int heat) implements IRRecipe<SimpleContainer> {
    public static final String NAME = "crucible_melting";
    public static final RecipeType<CrucibleSmeltingRecipe> TYPE = RecipeUtils.newRecipeType(NAME);

    public CrucibleSmeltingRecipe(List<Ingredient> input, FluidStack output, int duration, int heat) {
        this(input.get(0), output, duration, heat);
    }

    @Override
    public boolean matches(SimpleContainer simpleContainer, Level level) {
        return ingredient.test(simpleContainer.getItem(0));
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull SimpleContainer simpleContainer, HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return NonNullList.withSize(1, ingredient);
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    public FluidStack resultFluid() {
        return resultFluid.copy();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return TYPE;
    }

    public static class Serializer implements RecipeSerializer<CrucibleSmeltingRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        private static final MapCodec<CrucibleSmeltingRecipe> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder.group(
                Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter((recipe) -> recipe.getIngredients().stream().toList()),
                FluidStack.CODEC.fieldOf("result").forGetter(CrucibleSmeltingRecipe::resultFluid),
                Codec.INT.fieldOf("duration").forGetter(recipe -> recipe.duration),
                Codec.INT.fieldOf("heat").forGetter(recipe -> recipe.heat)
        ).apply(builder, CrucibleSmeltingRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, CrucibleSmeltingRecipe> STREAM_CODEC = StreamCodec.composite(
                RecipeUtils.INGREDIENT_STREAM_LIST_CODEC,
                recipe -> recipe.getIngredients().stream().toList(),
                FluidStack.STREAM_CODEC,
                CrucibleSmeltingRecipe::resultFluid,
                ByteBufCodecs.INT,
                CrucibleSmeltingRecipe::duration,
                ByteBufCodecs.INT,
                CrucibleSmeltingRecipe::heat,
                CrucibleSmeltingRecipe::new
        );

        private Serializer() {
        }

        @Override
        public @NotNull MapCodec<CrucibleSmeltingRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, CrucibleSmeltingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
