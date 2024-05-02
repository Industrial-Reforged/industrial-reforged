package com.indref.industrial_reforged.registries.recipes;

import com.indref.industrial_reforged.util.RecipeUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CrucibleSmeltingRecipe implements Recipe<SimpleContainer> {
    public static final String NAME = "crucible_melting";

    private final Ingredient itemStack;
    private final FluidStack fluidStack;
    private final int duration;
    private final int heat;

    public CrucibleSmeltingRecipe(Ingredient input, FluidStack output, int duration, int heat) {
        this.itemStack = input;
        this.fluidStack = output;
        this.duration = duration;
        this.heat = heat;
    }

    public CrucibleSmeltingRecipe(List<Ingredient> input, FluidStack output, int duration, int heat) {
        this(input.get(0), output, duration, heat);
    }

    @Override
    public boolean matches(SimpleContainer simpleContainer, Level level) {
        return itemStack.test(simpleContainer.getItem(0));
    }

    @Override
    public ItemStack assemble(SimpleContainer simpleContainer, HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.withSize(1, itemStack);
    }

    public int getDuration() {
        return duration;
    }

    public int getHeat() {
        return heat;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    public FluidStack getResultFluid() {
        return fluidStack.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Serializer implements RecipeSerializer<CrucibleSmeltingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        private static final MapCodec<CrucibleSmeltingRecipe> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder.group(
                Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter((recipe) -> recipe.getIngredients().stream().toList()),
                FluidStack.CODEC.fieldOf("result").forGetter(CrucibleSmeltingRecipe::getResultFluid),
                Codec.INT.fieldOf("duration").forGetter(recipe -> recipe.duration),
                Codec.INT.fieldOf("heat").forGetter(recipe -> recipe.heat)
        ).apply(builder, CrucibleSmeltingRecipe::new));
        private static final StreamCodec<RegistryFriendlyByteBuf, CrucibleSmeltingRecipe> STREAM_CODEC = StreamCodec.composite(
                RecipeUtils.INGREDIENT_STREAM_LIST_CODEC,
                recipe -> recipe.getIngredients().stream().toList(),
                FluidStack.STREAM_CODEC,
                CrucibleSmeltingRecipe::getResultFluid,
                ByteBufCodecs.INT,
                CrucibleSmeltingRecipe::getDuration,
                ByteBufCodecs.INT,
                CrucibleSmeltingRecipe::getHeat,
                CrucibleSmeltingRecipe::new
        );

        private Serializer() {
        }

        @Override
        public MapCodec<CrucibleSmeltingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CrucibleSmeltingRecipe> streamCodec() {
            return null;
        }
    }

    public static class Type implements RecipeType<CrucibleSmeltingRecipe> {
        public static final Type INSTANCE = new Type();

        private Type() {
        }

        @Override
        public String toString() {
            return NAME;
        }
    }
}
