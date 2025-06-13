package com.indref.industrial_reforged.content.recipes;

import com.indref.industrial_reforged.content.recipes.recipeInputs.BasinCastingRecipeInput;
import com.indref.industrial_reforged.data.IRDataMaps;
import com.indref.industrial_reforged.util.recipes.FluidIngredientWithAmount;
import com.indref.industrial_reforged.util.recipes.RecipeUtils;
import com.portingdeadmods.portingdeadlibs.utils.RegistryUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

// Takes in an fluidIngredient and a molten metal and outputs an item (the mold)
public class BasinMoldCastingRecipe extends BasinCastingRecipe {
    public static final RecipeSerializer<BasinMoldCastingRecipe> SERIALIZER =
            RecipeUtils.newRecipeSerializer(IRRecipeSerializer.MoldCasting.CODEC, IRRecipeSerializer.MoldCasting.STREAM_CODEC);
    public static final String NAME = "basin_mold_casting";

    public BasinMoldCastingRecipe(Ingredient ingredient, FluidIngredientWithAmount fluid, ItemStack moldResultItem, int duration) {
        super(ingredient, fluid, moldResultItem, duration);
    }

    @Override
    public boolean matches(BasinCastingRecipeInput recipeInput, Level level) {
        if (!ingredient().test(recipeInput.catalystItem())) return false;
        TagKey<Item> data = RegistryUtils.holder(BuiltInRegistries.ITEM, resultStack().getItem()).getData(IRDataMaps.MOLD_INGREDIENTS);
        if (data != null) {
            return recipeInput.catalystItem().is(data)
                    && this.fluidIngredient().test(recipeInput.fluidStack());
        }
        return false;
    }

    @Override
    public @NotNull RecipeSerializer<BasinMoldCastingRecipe> getSerializer() {
        return SERIALIZER;
    }
}
