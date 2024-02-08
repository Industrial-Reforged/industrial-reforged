package com.indref.industrial_reforged.registries.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
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
    public static final String NAME = "crucible_smelting";

    private final Ingredient itemStack;
    private final FluidStack fluidStack;
    private final int duration;

    public CrucibleSmeltingRecipe(Ingredient input, FluidStack output, Integer duration) {
        this.itemStack = input;
        this.fluidStack = output;
        this.duration = duration;
    }

    public CrucibleSmeltingRecipe(List<Ingredient> input, FluidStack output, Integer duration) {
        this.itemStack = input.get(0);
        this.fluidStack = output;
        this.duration = duration;
    }

    @Override
    public boolean matches(SimpleContainer simpleContainer, Level level) {
        return itemStack.test(simpleContainer.getItem(0));
    }

    @Override
    public @NotNull ItemStack assemble(SimpleContainer simpleContainer, RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.withSize(1, itemStack);
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(RegistryAccess registryAccess) {
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
        private static final Codec<CrucibleSmeltingRecipe> CODEC = RecordCodecBuilder.create((builder) -> builder.group(
                Ingredient.CODEC.listOf().fieldOf("ingredient").forGetter((recipe) -> recipe.getIngredients().stream().toList()),
                FluidStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.getResultFluid()),
                Codec.INT.fieldOf("duration").forGetter(recipe -> recipe.duration)
        ).apply(builder, CrucibleSmeltingRecipe::new));

        private Serializer() {
        }

        @Override
        public Codec<CrucibleSmeltingRecipe> codec() {
            return CODEC;
        }

        @Override
        public CrucibleSmeltingRecipe fromNetwork(FriendlyByteBuf buf) {
            return buf.readWithCodecTrusted(NbtOps.INSTANCE, CODEC);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, CrucibleSmeltingRecipe recipe) {
            buf.writeWithCodec(NbtOps.INSTANCE, CODEC, recipe);
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
