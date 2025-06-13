package com.indref.industrial_reforged.content.recipes;

import com.indref.industrial_reforged.util.recipes.FluidIngredientWithAmount;
import com.indref.industrial_reforged.util.recipes.IngredientWithCount;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.portingdeadmods.portingdeadlibs.utils.codec.CodecUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

public final class IRRecipeSerializer {
    protected static final class Casting {
        public static final Codec<Item> ITEM_CODEC = CodecUtils.registryCodec(BuiltInRegistries.ITEM);
        public static final StreamCodec<ByteBuf, Item> ITEM_STREAM_CODEC = CodecUtils.registryStreamCodec(BuiltInRegistries.ITEM);

        static final MapCodec<BasinCastingRecipe> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder.group(
                Ingredient.CODEC.fieldOf("mold_item").forGetter(BasinCastingRecipe::ingredient),
                FluidIngredientWithAmount.CODEC.fieldOf("fluid").forGetter(BasinCastingRecipe::fluidIngredient),
                ItemStack.CODEC.fieldOf("result").forGetter(BasinCastingRecipe::resultStack),
                Codec.INT.fieldOf("duration").forGetter(BasinCastingRecipe::duration)
        ).apply(builder, BasinCastingRecipe::new));

        static final StreamCodec<RegistryFriendlyByteBuf, BasinCastingRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC,
                BasinCastingRecipe::ingredient,
                FluidIngredientWithAmount.STREAM_CODEC,
                BasinCastingRecipe::fluidIngredient,
                ItemStack.STREAM_CODEC,
                BasinCastingRecipe::resultStack,
                ByteBufCodecs.INT,
                BasinCastingRecipe::duration,
                BasinCastingRecipe::new
        );
    }

    protected static final class MoldCasting {
        static final MapCodec<BasinMoldCastingRecipe> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder.group(
                Ingredient.CODEC.fieldOf("mold_ingredient").forGetter(BasinMoldCastingRecipe::ingredient),
                FluidIngredientWithAmount.CODEC.fieldOf("fluid").forGetter(BasinMoldCastingRecipe::fluidIngredient),
                ItemStack.CODEC.fieldOf("result").forGetter(BasinMoldCastingRecipe::resultStack),
                Codec.INT.fieldOf("duration").forGetter(BasinMoldCastingRecipe::duration)
        ).apply(builder, BasinMoldCastingRecipe::new));

        static final StreamCodec<RegistryFriendlyByteBuf, BasinMoldCastingRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC,
                BasinMoldCastingRecipe::ingredient,
                FluidIngredientWithAmount.STREAM_CODEC,
                BasinMoldCastingRecipe::fluidIngredient,
                ItemStack.STREAM_CODEC,
                BasinMoldCastingRecipe::resultStack,
                ByteBufCodecs.INT,
                BasinMoldCastingRecipe::duration,
                BasinMoldCastingRecipe::new
        );
    }

    protected static final class CrucibleMelting {
        static final MapCodec<CrucibleSmeltingRecipe> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder.group(
                IngredientWithCount.CODEC.fieldOf("fluidIngredient").forGetter(CrucibleSmeltingRecipe::ingredient),
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
                IngredientWithCount.CODEC.fieldOf("fluidIngredient").forGetter(CentrifugeRecipe::ingredient),
                ItemStack.CODEC.listOf().fieldOf("results").forGetter(CentrifugeRecipe::results),
                FluidStack.OPTIONAL_CODEC.fieldOf("result_fluid").forGetter(CentrifugeRecipe::resultFluid),
                Codec.INT.fieldOf("duration").forGetter(CentrifugeRecipe::duration),
                Codec.INT.fieldOf("energy").forGetter(CentrifugeRecipe::energy)
        ).apply(builder, CentrifugeRecipe::new));
        static final StreamCodec<RegistryFriendlyByteBuf, CentrifugeRecipe> STREAM_CODEC = StreamCodec.composite(
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
        );
    }

    protected static final class BlastFurnace {
        static final MapCodec<BlastFurnaceRecipe> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
                IngredientWithCount.CODEC.listOf().fieldOf("ingredients").forGetter(BlastFurnaceRecipe::ingredients),
                FluidStack.CODEC.fieldOf("result").forGetter(BlastFurnaceRecipe::resultFluid),
                Codec.INT.fieldOf("duration").forGetter(BlastFurnaceRecipe::duration),
                Codec.INT.fieldOf("heat").forGetter(BlastFurnaceRecipe::heat)
        ).apply(builder, BlastFurnaceRecipe::new));
        static final StreamCodec<RegistryFriendlyByteBuf, BlastFurnaceRecipe> STREAM_CODEC = StreamCodec.composite(
                IngredientWithCount.STREAM_LIST_CODEC,
                BlastFurnaceRecipe::ingredients,
                FluidStack.STREAM_CODEC,
                BlastFurnaceRecipe::resultFluid,
                ByteBufCodecs.INT,
                BlastFurnaceRecipe::duration,
                ByteBufCodecs.INT,
                BlastFurnaceRecipe::heat,
                BlastFurnaceRecipe::new
        );
    }
}
