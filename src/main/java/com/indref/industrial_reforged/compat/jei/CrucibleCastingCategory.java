package com.indref.industrial_reforged.compat.jei;

import com.indref.industrial_reforged.content.recipes.CrucibleCastingRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class CrucibleCastingCategory implements IRecipeCategory<CrucibleCastingRecipe> {
    @Override
    public RecipeType<CrucibleCastingRecipe> getRecipeType() {
        return null;
    }

    @Override
    public Component getTitle() {
        return null;
    }

    @Override
    public IDrawable getBackground() {
        return null;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return null;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder iRecipeLayoutBuilder, CrucibleCastingRecipe crucibleCastingRecipe, IFocusGroup iFocusGroup) {

    }
}
