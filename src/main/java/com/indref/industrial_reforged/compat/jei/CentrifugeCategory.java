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
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

public class CentrifugeCategory implements IRecipeCategory<CentrifugeRecipe> {
    private static final ResourceLocation SLOT_SPRITE = ResourceLocation.withDefaultNamespace("container/slot");
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
        recipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 5, 24)
                .addIngredients(RecipeUtils.iWCToIngredientSaveCount(centrifugeRecipe.ingredient()));

        int x = 60;
        int y = 24;

        int i = 0;
        for (Direction dir : BlockStateProperties.HORIZONTAL_FACING.getPossibleValues()) {
            if (i < centrifugeRecipe.results().size()) {
                ItemStack stack = centrifugeRecipe.results().get(i);
                recipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, x + dir.getNormal().getX() * 20, y + dir.getNormal().getZ() * 20)
                        .addItemStack(stack);
            }
            i++;
        }
    }

    @Override
    public void draw(CentrifugeRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blitSprite(SLOT_SPRITE, 4, 23, 18, 18);

        int x = 60;
        int y = 24;

        for (Direction dir : BlockStateProperties.HORIZONTAL_FACING.getPossibleValues()) {
            guiGraphics.blitSprite(SLOT_SPRITE, x + dir.getNormal().getX() * 20 - 1, y + dir.getNormal().getZ() * 20 - 1, 18, 18);
        }
    }
}