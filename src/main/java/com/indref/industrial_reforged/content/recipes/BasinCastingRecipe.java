package com.indref.industrial_reforged.content.recipes;

import com.indref.industrial_reforged.data.IRDataMaps;
import com.indref.industrial_reforged.content.recipes.recipeInputs.ItemFluidRecipeInput;
import com.indref.industrial_reforged.util.recipes.FluidIngredientWithAmount;
import com.indref.industrial_reforged.util.recipes.RecipeUtils;
import com.portingdeadmods.portingdeadlibs.api.recipes.PDLRecipe;
import com.portingdeadmods.portingdeadlibs.utils.RegistryUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BasinCastingRecipe implements PDLRecipe<ItemFluidRecipeInput> {
    public static final String NAME = "basin_casting";
    public static final RecipeType<BasinCastingRecipe> TYPE = RecipeUtils.newRecipeType(NAME);
    public static final RecipeSerializer<BasinCastingRecipe> SERIALIZER =
            RecipeUtils.newRecipeSerializer(IRRecipeSerializer.Casting.CODEC, IRRecipeSerializer.Casting.STREAM_CODEC);
    private final Ingredient ingredient;
    private final FluidIngredientWithAmount fluidIngredient;
    private final ItemStack resultStack;
    private final int duration;

    public BasinCastingRecipe(Ingredient moldItem, FluidIngredientWithAmount fluidIngredient, ItemStack resultStack,
                              int duration) {
        this.ingredient = moldItem;
        this.fluidIngredient = fluidIngredient;
        this.resultStack = resultStack;
        this.duration = duration;
    }

    @Override
    public boolean matches(ItemFluidRecipeInput recipeInput, Level level) {
        return ingredient.test(recipeInput.itemStack())
                && RegistryUtils.holder(BuiltInRegistries.ITEM, recipeInput.itemStack().getItem()).getData(IRDataMaps.CASTING_MOLDS) != null
                && this.fluidIngredient().test(recipeInput.fluidStack());
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider provider) {
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

    public Ingredient ingredient() {
        return ingredient;
    }

    public FluidIngredientWithAmount fluidIngredient() {
        return fluidIngredient;
    }

    public ItemStack resultStack() {
        return resultStack;
    }

    public int duration() {
        return duration;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (BasinCastingRecipe) obj;
        return Objects.equals(this.ingredient, that.ingredient) &&
                Objects.equals(this.fluidIngredient, that.fluidIngredient) &&
                Objects.equals(this.resultStack, that.resultStack) &&
                this.duration == that.duration;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredient, fluidIngredient, resultStack, duration);
    }

    @Override
    public String toString() {
        return "BasinCastingRecipe[" +
                "moldItem=" + ingredient + ", " +
                "fluidIngredient=" + fluidIngredient + ", " +
                "resultStack=" + resultStack + ", " +
                "duration=" + duration + ']';
    }

}
