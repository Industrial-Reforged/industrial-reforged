package com.indref.industrial_reforged.compat.jei;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.content.recipes.CentrifugeRecipe;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.util.recipes.RecipeUtils;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class CentrifugeCategory implements IRecipeCategory<CentrifugeRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "centrifuge");
    public static final RecipeType<CentrifugeRecipe> RECIPE_TYPE =
            new RecipeType<>(UID, CentrifugeRecipe.class);
    private final IDrawable icon;
    private final IDrawable background;

    public CentrifugeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(80, 64);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(IRBlocks.CENTRIFUGE.get()));
    }

    @Override
    public RecipeType<CentrifugeRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Centrifuge");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder recipeLayoutBuilder, CentrifugeRecipe centrifugeRecipe, IFocusGroup iFocusGroup) {
        recipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 0, 0)
                .addIngredients(RecipeUtils.iWCToIngredientSaveCount(centrifugeRecipe.ingredient()));

        for (int i = 0; i < centrifugeRecipe.results().size(); i++) {
            recipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, i * 20, 30)
                    .addItemStack(centrifugeRecipe.results().get(i));
        }
    }
}