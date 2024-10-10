package com.indref.industrial_reforged.registries.recipes;

import com.indref.industrial_reforged.util.recipes.IngredientWithCount;
import com.indref.industrial_reforged.util.recipes.RecipeUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.FluidStack;

public final class IRRecipeSerializer {
    protected static final class Casting {
        static final MapCodec<CrucibleCastingRecipe> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder.group(
                FluidStack.CODEC.fieldOf("fluid").forGetter(CrucibleCastingRecipe::fluidStack),
                Ingredient.CODEC.fieldOf("cast").forGetter(CrucibleCastingRecipe::castItem),
                ItemStack.CODEC.fieldOf("result").forGetter(CrucibleCastingRecipe::resultStack),
                Codec.INT.fieldOf("duration").forGetter(CrucibleCastingRecipe::duration),
                Codec.BOOL.fieldOf("consume_cast").forGetter(CrucibleCastingRecipe::consumeCast)
        ).apply(builder, CrucibleCastingRecipe::new));
        static final StreamCodec<RegistryFriendlyByteBuf, CrucibleCastingRecipe> STREAM_CODEC = StreamCodec.composite(
                FluidStack.STREAM_CODEC,
                CrucibleCastingRecipe::fluidStack,
                Ingredient.CONTENTS_STREAM_CODEC,
                CrucibleCastingRecipe::castItem,
                ItemStack.STREAM_CODEC,
                CrucibleCastingRecipe::resultStack,
                ByteBufCodecs.INT,
                CrucibleCastingRecipe::duration,
                ByteBufCodecs.BOOL,
                CrucibleCastingRecipe::consumeCast,
                CrucibleCastingRecipe::new
        );
    }

    protected static final class CrucibleMelting {
        static final MapCodec<CrucibleSmeltingRecipe> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder.group(
                IngredientWithCount.CODEC.fieldOf("ingredient").forGetter(CrucibleSmeltingRecipe::ingredient),
                FluidStack.CODEC.fieldOf("result").forGetter(CrucibleSmeltingRecipe::resultFluid),
                Codec.INT.fieldOf("duration").forGetter(CrucibleSmeltingRecipe::duration),
                Codec.INT.fieldOf("heat").forGetter(CrucibleSmeltingRecipe::heat)
        ).apply(builder, CrucibleSmeltingRecipe::new));

        static final StreamCodec<RegistryFriendlyByteBuf, CrucibleSmeltingRecipe> STREAM_CODEC = StreamCodec.composite(
                IngredientWithCount.STREAM_CODEC,
                CrucibleSmeltingRecipe::ingredient,
                FluidStack.STREAM_CODEC,
                CrucibleSmeltingRecipe::resultFluid,
                ByteBufCodecs.INT,
                CrucibleSmeltingRecipe::duration,
                ByteBufCodecs.INT,
                CrucibleSmeltingRecipe::heat,
                CrucibleSmeltingRecipe::new
        );
    }

    protected static final class Centrifuge {
        static final MapCodec<CentrifugeRecipe> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder.group(
                IngredientWithCount.CODEC.fieldOf("ingredient").forGetter(CentrifugeRecipe::ingredient),
                ItemStack.CODEC.listOf().fieldOf("results").forGetter(CentrifugeRecipe::results),
                Codec.INT.fieldOf("duration").forGetter(CentrifugeRecipe::duration),
                Codec.INT.fieldOf("energy").forGetter(CentrifugeRecipe::energy)
        ).apply(builder, CentrifugeRecipe::new));
        static final StreamCodec<RegistryFriendlyByteBuf, CentrifugeRecipe> STREAM_CODEC = StreamCodec.composite(
                IngredientWithCount.STREAM_CODEC,
                CentrifugeRecipe::ingredient,
                ItemStack.LIST_STREAM_CODEC,
                CentrifugeRecipe::results,
                ByteBufCodecs.INT,
                CentrifugeRecipe::duration,
                ByteBufCodecs.INT,
                CentrifugeRecipe::energy,
                CentrifugeRecipe::new
        );
    }

    protected static final class BlastFurnace {
        static final MapCodec<BlastFurnaceRecipe> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
                IngredientWithCount.CODEC.listOf().fieldOf("ingredients").forGetter(BlastFurnaceRecipe::ingredients),
                FluidStack.CODEC.fieldOf("result").forGetter(BlastFurnaceRecipe::resultFluid),
                Codec.INT.fieldOf("duration").forGetter(BlastFurnaceRecipe::duration)
        ).apply(builder, BlastFurnaceRecipe::new));
        static final StreamCodec<RegistryFriendlyByteBuf, BlastFurnaceRecipe> STREAM_CODEC = StreamCodec.composite(
                IngredientWithCount.STREAM_LIST_CODEC,
                BlastFurnaceRecipe::ingredients,
                FluidStack.STREAM_CODEC,
                BlastFurnaceRecipe::resultFluid,
                ByteBufCodecs.INT,
                BlastFurnaceRecipe::duration,
                BlastFurnaceRecipe::new
        );
    }
}
