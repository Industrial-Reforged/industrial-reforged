package com.indref.industrial_reforged.compat.jei;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.content.recipes.CrucibleCastingRecipe;
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
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public class CastingCategory implements IRecipeCategory<CrucibleCastingRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "crucible_casting");
    public static final RecipeType<CrucibleCastingRecipe> RECIPE_TYPE =
            new RecipeType<>(UID, CrucibleCastingRecipe.class);
    private final IDrawable icon;
    private final IDrawable background;

    public CastingCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(80, 64);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(IRBlocks.CERAMIC_CASTING_BASIN.get()));
    }

    @Override
    public RecipeType<CrucibleCastingRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Casting");
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
    public void setRecipe(IRecipeLayoutBuilder recipeLayoutBuilder, CrucibleCastingRecipe castingRecipe, IFocusGroup iFocusGroup) {
        recipeLayoutBuilder.addSlot(RecipeIngredientRole.CATALYST, 0, 0)
                .addIngredients(castingRecipe.castItem());
        FluidStack fluidStack = castingRecipe.fluidStack();
        recipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 0, 20)
                .addFluidStack(fluidStack.getFluid(), fluidStack.getAmount())
                .setFluidRenderer(fluidStack.getAmount(), true, 16, 16);
        recipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, 40, 20)
                .addIngredients(Ingredient.of(castingRecipe.resultStack()));
    }
}
