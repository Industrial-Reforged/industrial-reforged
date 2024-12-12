package com.indref.industrial_reforged.compat.jei;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.content.recipes.CentrifugeRecipe;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.util.recipes.RecipeUtils;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class CentrifugeCategory implements IRecipeCategory<CentrifugeRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "centrifuge");
    public static final RecipeType<CentrifugeRecipe> RECIPE_TYPE =
            new RecipeType<>(UID, CentrifugeRecipe.class);
    private final IDrawable icon;

    public CentrifugeCategory(IGuiHelper guiHelper) {
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
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public int getWidth() {
        return 96;
    }

    @Override
    public int getHeight() {
        return 64;
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

    @Override
    public void draw(CentrifugeRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
    }
}