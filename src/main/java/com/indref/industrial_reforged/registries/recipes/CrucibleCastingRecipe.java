package com.indref.industrial_reforged.registries.recipes;

import com.indref.industrial_reforged.api.recipes.IRRecipe;
import com.indref.industrial_reforged.util.recipes.recipe_inputs.ItemAndFluidRecipeInput;
import com.indref.industrial_reforged.util.recipes.RecipeUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public record CrucibleCastingRecipe(FluidStack fluidStack, ItemStack castItem, ItemStack resultStack, int duration,
                                    boolean consumeCast) implements IRRecipe<ItemAndFluidRecipeInput> {
    public static final String NAME = "crucible_casting";
    public static final RecipeType<CrucibleCastingRecipe> TYPE = RecipeUtils.newRecipeType(NAME);
    public static final RecipeSerializer<CrucibleCastingRecipe> SERIALIZER =
            RecipeUtils.newRecipeSerializer(IRRecipeSerializer.Casting.CODEC, IRRecipeSerializer.Casting.STREAM_CODEC);

    @Override
    public boolean matches(ItemAndFluidRecipeInput recipeInput, Level level) {
        if (level.isClientSide) return false;

        return castItem.is(recipeInput.getItem(0).getItem())
                && recipeInput.fluidStack().is(this.fluidStack.getFluid())
                && recipeInput.fluidStack().getAmount() >= this.fluidStack.getAmount();
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
        return SERIALIZER;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return TYPE;
    }

    public FluidStack fluidStack() {
        return fluidStack.copy();
    }
}
