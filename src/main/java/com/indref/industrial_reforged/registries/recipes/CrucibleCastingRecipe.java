package com.indref.industrial_reforged.registries.recipes;

import com.indref.industrial_reforged.api.recipes.IRRecipe;
import com.indref.industrial_reforged.util.Utils;
import com.indref.industrial_reforged.util.recipes.IngredientUtils;
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

public record CrucibleCastingRecipe(FluidStack fluidStack, ItemStack castItem, ItemStack resultStack, int duration,
                                    boolean consumeCast) implements IRRecipe<SimpleContainer> {
    public static final String NAME = "crucible_casting";
    public static final RecipeType<CrucibleCastingRecipe> TYPE = RecipeUtils.newRecipeType(NAME);

    @Override
    public boolean matches(SimpleContainer simpleContainer, Level level) {
        if (level.isClientSide) return false;

        return castItem.is(simpleContainer.getItem(0).getItem());
    }

    public boolean matchesFluids(FluidStack fluidStack, Level level) {
        if (level.isClientSide) return false;

        return fluidStack.is(this.fluidStack.getFluid()) && fluidStack.getAmount() >= this.fluidStack.getAmount();
    }

    public int getDuration() {
        return duration;
    }

    public boolean shouldConsumeCast() {
        return consumeCast;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider provider) {
        return resultStack.copy();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return TYPE;
    }

    public FluidStack fluidStack() {
        return fluidStack.copy();
    }

    public static class Serializer implements RecipeSerializer<CrucibleCastingRecipe> {
        public static final CrucibleCastingRecipe.Serializer INSTANCE = new CrucibleCastingRecipe.Serializer();
        private static final MapCodec<CrucibleCastingRecipe> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder.group(
                FluidStack.CODEC.fieldOf("fluid").forGetter(CrucibleCastingRecipe::fluidStack),
                ItemStack.CODEC.fieldOf("cast").forGetter(CrucibleCastingRecipe::castItem),
                ItemStack.CODEC.fieldOf("result").forGetter(CrucibleCastingRecipe::resultStack),
                Codec.INT.fieldOf("duration").forGetter(CrucibleCastingRecipe::duration),
                Codec.BOOL.fieldOf("consume_cast").forGetter(CrucibleCastingRecipe::consumeCast)
        ).apply(builder, CrucibleCastingRecipe::new));
        private static final StreamCodec<RegistryFriendlyByteBuf, CrucibleCastingRecipe> STREAM_CODEC = StreamCodec.composite(
                FluidStack.STREAM_CODEC,
                CrucibleCastingRecipe::fluidStack,
                ItemStack.STREAM_CODEC,
                CrucibleCastingRecipe::castItem,
                ItemStack.STREAM_CODEC,
                CrucibleCastingRecipe::resultStack,
                ByteBufCodecs.INT,
                CrucibleCastingRecipe::duration,
                ByteBufCodecs.BOOL,
                CrucibleCastingRecipe::consumeCast,
                CrucibleCastingRecipe::new
        );

        private Serializer() {
        }

        @Override
        public @NotNull MapCodec<CrucibleCastingRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, CrucibleCastingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
