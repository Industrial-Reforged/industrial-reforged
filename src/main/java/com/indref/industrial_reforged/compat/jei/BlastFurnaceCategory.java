package com.indref.industrial_reforged.compat.jei;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.content.recipes.BlastFurnaceRecipe;
import com.indref.industrial_reforged.registries.IRBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public class BlastFurnaceCategory implements IRecipeCategory<BlastFurnaceRecipe> {
    private static final ResourceLocation PROGRESS_SPRITE = ResourceLocation.withDefaultNamespace("container/furnace/burn_progress");
    private static final ResourceLocation SLOT_SPRITE = ResourceLocation.withDefaultNamespace("container/slot");
    private static final ResourceLocation TANK_SPRITE = IndustrialReforged.rl("small_tank");

    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "blast_furnace");
    public static final RecipeType<BlastFurnaceRecipe> RECIPE_TYPE = new RecipeType<>(UID, BlastFurnaceRecipe.class);
    private final IDrawable icon;

    public BlastFurnaceCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(IRBlocks.BLAST_FURNACE_CONTROLLER.get()));
    }

    @Override
    public RecipeType<BlastFurnaceRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Blast Furnace");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public int getWidth() {
        return 96;
    }

    @Override
    public int getHeight() {
        return 64;
    }

    public int getPadding() {
        return 0;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BlastFurnaceRecipe recipe, IFocusGroup focuses) {
        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            builder.addInputSlot(getPadding() + i * 28, getHeight() / 2 - 8).addIngredients(recipe.getIngredients().get(i));
        }

        FluidStack fluidStack = recipe.resultFluid();
        builder.addOutputSlot(getPadding() + 76, getHeight() / 2 - 26)
                .addFluidStack(fluidStack.getFluid(), fluidStack.getAmount())
                .setFluidRenderer(fluidStack.getAmount(), false, 16, 52);
    }

    @Override
    public void draw(BlastFurnaceRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blitSprite(PROGRESS_SPRITE, getPadding() + 48, getHeight() / 2 - 8, 24, 16);
        guiGraphics.blitSprite(TANK_SPRITE, getPadding() + 75, getHeight() / 2 - 27, 18, 54);
        guiGraphics.blitSprite(SLOT_SPRITE, getPadding() - 1, getHeight() / 2 - 9, 18, 18);
        guiGraphics.blitSprite(SLOT_SPRITE, getPadding() + 27, getHeight() / 2 - 9, 18, 18);
    }
}
