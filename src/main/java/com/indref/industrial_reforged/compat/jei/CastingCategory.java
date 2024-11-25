package com.indref.industrial_reforged.compat.jei;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.content.recipes.CrucibleCastingRecipe;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.util.recipes.RecipeUtils;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public class CastingCategory implements IRecipeCategory<CrucibleCastingRecipe> {
    private static final ResourceLocation PROGRESS_SPRITE = ResourceLocation.withDefaultNamespace("container/furnace/burn_progress");

    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "crucible_casting");
    public static final RecipeType<CrucibleCastingRecipe> RECIPE_TYPE =
            new RecipeType<>(UID, CrucibleCastingRecipe.class);
    private final IDrawable icon;

    public CastingCategory(IGuiHelper guiHelper) {
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
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public int getWidth() {
        return 80;
    }

    @Override
    public int getHeight() {
        return 64;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder recipeLayoutBuilder, CrucibleCastingRecipe castingRecipe, IFocusGroup iFocusGroup) {
        FluidStack fluidStack = castingRecipe.fluidStack();
        recipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 0, 25)
                .addFluidStack(fluidStack.getFluid(), fluidStack.getAmount())
                .setFluidRenderer(fluidStack.getAmount(), false, 16, 16);

        IRecipeSlotBuilder slotBuilder = recipeLayoutBuilder.addSlot(RecipeIngredientRole.CATALYST, 32, 5)
                .addIngredients(castingRecipe.castItem());

        if (castingRecipe.consumeCast()) {
            slotBuilder.addRichTooltipCallback((recipeSlotView, tooltip) -> tooltip.add(Component.literal("Item is consumed").withStyle(ChatFormatting.DARK_GRAY)));
        }
        
        recipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, 60, 25)
                .addIngredients(Ingredient.of(castingRecipe.resultStack()));
    }

    @Override
    public void draw(CrucibleCastingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blitSprite(PROGRESS_SPRITE, 28, 25, 24, 16);
    }
}
