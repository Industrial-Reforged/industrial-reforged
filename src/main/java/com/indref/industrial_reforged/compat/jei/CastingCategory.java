package com.indref.industrial_reforged.compat.jei;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.content.recipes.BasinCastingRecipe;
import com.indref.industrial_reforged.data.IRDataMaps;
import com.indref.industrial_reforged.data.maps.CastingMoldValue;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.translations.IRTranslations;
import com.indref.industrial_reforged.util.recipes.FluidIngredientWithAmount;
import com.portingdeadmods.portingdeadlibs.utils.RegistryUtils;
import com.portingdeadmods.portingdeadlibs.utils.renderers.GuiUtils;
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CastingCategory implements IRecipeCategory<BasinCastingRecipe> {
    private static final ResourceLocation PROGRESS_SPRITE = ResourceLocation.withDefaultNamespace("container/furnace/burn_progress");
    private static final ResourceLocation SLOT_SPRITE = ResourceLocation.fromNamespaceAndPath("jei", "textures/jei/atlas/gui/output_slot.png");
    private static final ResourceLocation CASTING_BASIN_SPRITE = ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "casting_basin");

    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "crucible_casting");
    public static final RecipeType<BasinCastingRecipe> RECIPE_TYPE =
            new RecipeType<>(UID, BasinCastingRecipe.class);

    private final IDrawable icon;

    public CastingCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(IRBlocks.CERAMIC_CASTING_BASIN.get()));
    }

    @Override
    public RecipeType<BasinCastingRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return IRTranslations.Jei.CASTING.component();
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
        return 40;
    }

    public int getPadding() {
        return 6;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder recipeLayoutBuilder, BasinCastingRecipe castingRecipe, IFocusGroup iFocusGroup) {
        int y = 0;

        FluidIngredientWithAmount fluidIngredient = castingRecipe.fluidIngredient();
        IRecipeSlotBuilder builder = recipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, getPadding() + 2, y + 18);
        for (FluidStack fluidStack : fluidIngredient.fluidIngredient().getStacks()) {
            builder.addFluidStack(fluidStack.getFluid(), fluidIngredient.amount());
        }
        builder.setFluidRenderer(fluidIngredient.amount(), false, 16, 16);

        IRecipeSlotBuilder slotBuilder = recipeLayoutBuilder.addSlot(RecipeIngredientRole.CATALYST, getPadding() + 32, y)
                .addIngredients(castingRecipe.ingredient());

        slotBuilder.addRichTooltipCallback((recipeSlotView, tooltip) -> {
            Optional<ItemStack> displayedItemStack = recipeSlotView.getDisplayedItemStack();
            if (displayedItemStack.isPresent()) {
                CastingMoldValue moldValue = RegistryUtils.holder(BuiltInRegistries.ITEM, displayedItemStack.get().getItem()).getData(IRDataMaps.CASTING_MOLDS);
                if (moldValue != null && moldValue.consumeCast()) {
                    tooltip.add(IRTranslations.Jei.ITEM_CONSUMED.component().withStyle(ChatFormatting.DARK_GRAY));
                }
            }
        });

        recipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, getPadding() + 64, y + 19)
                .addIngredients(Ingredient.of(castingRecipe.resultStack()));
    }

    @Override
    public void draw(BasinCastingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        int y = 0;

        guiGraphics.blitSprite(PROGRESS_SPRITE, getPadding() + 28, y + 20, 24, 16);
        guiGraphics.blitSprite(CASTING_BASIN_SPRITE, getPadding(), y + 17, 20, 20);
        GuiUtils.drawImg(guiGraphics, SLOT_SPRITE, getPadding() + 59, y + 14, 26, 26);

        Font font = Minecraft.getInstance().font;

        String durationText = "%d sec".formatted(recipe.duration() / 20);

        guiGraphics.drawString(font, durationText, 0, 0, ChatFormatting.DARK_GRAY.getColor(), false);
    }
}
