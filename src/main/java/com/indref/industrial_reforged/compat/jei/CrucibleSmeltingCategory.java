package com.indref.industrial_reforged.compat.jei;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.content.recipes.CrucibleSmeltingRecipe;
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
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public class CrucibleSmeltingCategory implements IRecipeCategory<CrucibleSmeltingRecipe> {
    private static final ResourceLocation SLOT_SPRITE = ResourceLocation.withDefaultNamespace("container/slot");
    private static final ResourceLocation TANK_SPRITE = IndustrialReforged.rl("large_tank");
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "crucible_smelting");
    public static final RecipeType<CrucibleSmeltingRecipe> RECIPE_TYPE =
            new RecipeType<>(UID, CrucibleSmeltingRecipe.class);
    private final IDrawable icon;

    public CrucibleSmeltingCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(IRBlocks.CERAMIC_CRUCIBLE_CONTROLLER.get()));
    }

    @Override
    public RecipeType<CrucibleSmeltingRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Crucible Smelting");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public int getWidth() {
        return 128;
    }

    @Override
    public int getHeight() {
        return 64;
    }

    public int getPadding() {
        return 0;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder iRecipeLayoutBuilder, CrucibleSmeltingRecipe crucibleSmeltingRecipe, IFocusGroup iFocusGroup) {
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 1, 7)
                .addIngredients(RecipeUtils.iWCToIngredientSaveCount(crucibleSmeltingRecipe.ingredient()));
        FluidStack fluidStack = crucibleSmeltingRecipe.resultFluid();
        iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, 73, 27)
                .addFluidStack(fluidStack.getFluid(), fluidStack.getAmount())
                .setFluidRenderer(fluidStack.getAmount(), false, 52, 32);
    }

    @Override
    public void draw(CrucibleSmeltingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        int x = 0;
        int y = 6;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                guiGraphics.blitSprite(SLOT_SPRITE, x + j * 18, y + i * 18, 18, 18);
            }
        }

        guiGraphics.blitSprite(TANK_SPRITE, x + 18 * 4, y, 54, 54);
    }
}
