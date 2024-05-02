package com.indref.industrial_reforged.registries.recipes;

import com.indref.industrial_reforged.util.IngredientUtils;
import com.indref.industrial_reforged.util.IngredientWithCount;
import com.indref.industrial_reforged.util.RecipeUtils;
import com.indref.industrial_reforged.util.Utils;
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
import java.util.Map;

@SuppressWarnings("deprecation")
public class BlastFurnaceRecipe implements Recipe<SimpleContainer> {
    public static final String NAME = "blast_furnace";

    private final NonNullList<IngredientWithCount> ingredients;
    private final FluidStack resultFluid;
    private final int duration;

    public BlastFurnaceRecipe(List<IngredientWithCount> ingredients, FluidStack resultFluid, int duration) {
        this.ingredients = NonNullList.create();
        this.ingredients.addAll(ingredients);
        this.resultFluid = resultFluid;
        this.duration = duration;
    }

    public BlastFurnaceRecipe(NonNullList<IngredientWithCount> ingredients, FluidStack resultFluid, int duration) {
        this.ingredients = ingredients;
        this.resultFluid = resultFluid;
        this.duration = duration;
    }

    @Override
    public boolean matches(SimpleContainer simpleContainer, Level level) {
        if (level.isClientSide) return false;

        return RecipeUtils.compareItems(simpleContainer.getItems(), getIngredientsWithCount());
    }

    @Override
    public @NotNull ItemStack assemble(SimpleContainer simpleContainer, HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return Utils.listToNonNullList(IngredientUtils.iWCToIngredients(ingredients));
    }

    public @NotNull NonNullList<IngredientWithCount> getIngredientsWithCount() {
        return ingredients;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
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
        return Type.INSTANCE;
    }

    public FluidStack getResultFluid() {
        return resultFluid.copy();
    }

    public static class Serializer implements RecipeSerializer<BlastFurnaceRecipe> {
        public static final BlastFurnaceRecipe.Serializer INSTANCE = new BlastFurnaceRecipe.Serializer();
        private static final MapCodec<BlastFurnaceRecipe> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
                IngredientWithCount.CODEC.listOf().fieldOf("ingredients").forGetter(BlastFurnaceRecipe::getIngredientsWithCount),
                FluidStack.CODEC.fieldOf("result").forGetter(BlastFurnaceRecipe::getResultFluid),
                Codec.INT.fieldOf("duration").forGetter(BlastFurnaceRecipe::getDuration)
        ).apply(builder, BlastFurnaceRecipe::new));
        private static final StreamCodec<RegistryFriendlyByteBuf, BlastFurnaceRecipe> STREAM_CODEC = StreamCodec.composite(
                IngredientWithCount.STREAM_LIST_CODEC,
                BlastFurnaceRecipe::getIngredientsWithCount,
                FluidStack.STREAM_CODEC,
                BlastFurnaceRecipe::getResultFluid,
                ByteBufCodecs.INT,
                BlastFurnaceRecipe::getDuration,
                BlastFurnaceRecipe::new
        );

        private Serializer() {
        }

        @Override
        public @NotNull MapCodec<BlastFurnaceRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, BlastFurnaceRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

    public static class Type implements RecipeType<BlastFurnaceRecipe> {
        public static final BlastFurnaceRecipe.Type INSTANCE = new BlastFurnaceRecipe.Type();

        private Type() {
        }

        @Override
        public String toString() {
            return NAME;
        }
    }
}
