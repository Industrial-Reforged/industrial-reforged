package com.indref.industrial_reforged.content.recipes;

import com.indref.industrial_reforged.data.IRDataMaps;
import com.indref.industrial_reforged.data.maps.CastingMoldValue;
import com.indref.industrial_reforged.util.RegistryUtils;
import com.indref.industrial_reforged.util.recipes.IngredientWithCount;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public final class IRRecipeSerializer {
    protected static final class Casting {
        public static final Codec<Item> ITEM_CODEC = RegistryUtils.registryCodec(BuiltInRegistries.ITEM);
        public static final StreamCodec<ByteBuf, Item> ITEM_STREAM_CODEC = RegistryUtils.registryStreamCodec(BuiltInRegistries.ITEM);

        static final MapCodec<CrucibleCastingRecipe> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder.group(
                FluidStack.OPTIONAL_CODEC.fieldOf("fluid").forGetter(CrucibleCastingRecipe::fluidStack),
                ITEM_CODEC.fieldOf("mold_item").forGetter(CrucibleCastingRecipe::moldItem),
                ItemStack.CODEC.fieldOf("result").forGetter(CrucibleCastingRecipe::resultStack),
                Codec.INT.fieldOf("duration").forGetter(CrucibleCastingRecipe::duration)
        ).apply(builder, CrucibleCastingRecipe::new));

        static final StreamCodec<RegistryFriendlyByteBuf, CrucibleCastingRecipe> STREAM_CODEC = StreamCodec.composite(
                FluidStack.STREAM_CODEC,
                CrucibleCastingRecipe::fluidStack,
                ITEM_STREAM_CODEC,
                CrucibleCastingRecipe::moldItem,
                ItemStack.STREAM_CODEC,
                CrucibleCastingRecipe::resultStack,
                ByteBufCodecs.INT,
                CrucibleCastingRecipe::duration,
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
