package com.indref.industrial_reforged.registries.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public class CrucibleCastingRecipe implements Recipe<SimpleContainer> {
    public static final String NAME = "crucible_casting";

    private final FluidStack fluidStack;
    private final Item castItem;
    private final ItemStack resultStack;
    private final int duration;
    private final boolean consumeCast;

    public CrucibleCastingRecipe(FluidStack fluidStack, ItemStack castItem, ItemStack resultStack, int duration, boolean consumeCast) {
        this.fluidStack = fluidStack;
        this.castItem = castItem.getItem();
        this.resultStack = resultStack;
        this.duration = duration;
        this.consumeCast = consumeCast;
    }

    @Override
    public boolean matches(SimpleContainer simpleContainer, Level level) {
        if (level.isClientSide) return false;

        return castItem.equals(simpleContainer.getItem(0).getItem());
    }

    @Override
    public ItemStack assemble(SimpleContainer simpleContainer, HolderLookup.Provider provider) {
        return resultStack.copy();
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
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider provider) {
        return resultStack.copy();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public FluidStack getFluid() {
        return fluidStack.copy();
    }

    public static class Serializer implements RecipeSerializer<CrucibleCastingRecipe> {
        public static final CrucibleCastingRecipe.Serializer INSTANCE = new CrucibleCastingRecipe.Serializer();
        private static final MapCodec<CrucibleCastingRecipe> CODEC = RecordCodecBuilder.mapCodec((builder) -> builder.group(
                FluidStack.CODEC.fieldOf("fluid").forGetter(CrucibleCastingRecipe::getFluid),
                ItemStack.CODEC.fieldOf("cast").forGetter(recipe -> recipe.castItem.getDefaultInstance()),
                ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.resultStack),
                Codec.INT.fieldOf("duration").forGetter(recipe -> recipe.duration),
                Codec.BOOL.fieldOf("consume_cast").forGetter(recipe -> recipe.consumeCast)
        ).apply(builder, CrucibleCastingRecipe::new));
        private static final StreamCodec<RegistryFriendlyByteBuf, CrucibleCastingRecipe> STREAM_CODEC = StreamCodec.composite(
                FluidStack.STREAM_CODEC,
                CrucibleCastingRecipe::getFluid,
                ItemStack.STREAM_CODEC,
                recipe -> recipe.castItem.getDefaultInstance(),
                ItemStack.STREAM_CODEC,
                recipe -> recipe.resultStack,
                ByteBufCodecs.INT,
                recipe -> recipe.duration,
                ByteBufCodecs.BOOL,
                recipe -> recipe.consumeCast,
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

    public static class Type implements RecipeType<CrucibleCastingRecipe> {
        public static final CrucibleCastingRecipe.Type INSTANCE = new CrucibleCastingRecipe.Type();

        private Type() {
        }

        @Override
        public String toString() {
            return NAME;
        }
    }
}
