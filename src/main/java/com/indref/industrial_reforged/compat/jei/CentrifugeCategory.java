package com.indref.industrial_reforged.compat.jei;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.content.recipes.CentrifugeRecipe;
import com.indref.industrial_reforged.registries.IRMachines;
import com.indref.industrial_reforged.translations.IRTranslations;
import com.indref.industrial_reforged.util.recipes.RecipeUtils;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

public class CentrifugeCategory implements IRecipeCategory<CentrifugeRecipe> {
    private static final ResourceLocation ENERGY_BAR =
            ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "energy_bar_border");
    private static final ResourceLocation ENERGY_BAR_EMPTY =
            ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "energy_bar_empty_border");
    private static final ResourceLocation SLOT_SPRITE = ResourceLocation.withDefaultNamespace("container/slot");
    private static final ResourceLocation PROGRESS_ARROWS_SPRITE = IndustrialReforged.rl("container/centrifuge/progress_arrows");
    private static final ResourceLocation TANK_SPRITE = IndustrialReforged.rl("small_tank");
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "centrifuge");
    public static final RecipeType<CentrifugeRecipe> RECIPE_TYPE =
            new RecipeType<>(UID, CentrifugeRecipe.class);
    private final IDrawable icon;

    public CentrifugeCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(IRMachines.CENTRIFUGE.getBlock()));
    }

    @Override
    public RecipeType<CentrifugeRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return IRTranslations.Jei.CENTRIFUGE.component();
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public int getWidth() {
        return 148;
    }

    @Override
    public int getHeight() {
        return 96;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder recipeLayoutBuilder, CentrifugeRecipe centrifugeRecipe, IFocusGroup iFocusGroup) {
        int x = getWidth() / 2 - 9 + 1;
        int y = getHeight() / 2 - 9;

        recipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, x, y)
                .addIngredients(RecipeUtils.iWCToIngredientSaveCount(centrifugeRecipe.ingredient()));

        int i = 0;
        for (Direction dir : BlockStateProperties.HORIZONTAL_FACING.getPossibleValues()) {
            if (i < centrifugeRecipe.results().size()) {
                ItemStack stack = centrifugeRecipe.results().get(i);
                recipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, x + dir.getNormal().getX() * 36, y + dir.getNormal().getZ() * 36)
                        .addItemStack(stack);
            }
            i++;
        }

        recipeLayoutBuilder.addOutputSlot(126, 24)
                .addFluidStack(centrifugeRecipe.resultFluid().getFluid(), centrifugeRecipe.resultFluid().getAmount())
                .setFluidRenderer(8000, true, 16, 52);
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, CentrifugeRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        IRecipeCategory.super.getTooltip(tooltip, recipe, recipeSlotsView, mouseX, mouseY);
        int energyBarX = 10;
        int energyBarY = 20;

        boolean shouldRenderTooltip = shouldRenderTooltip(energyBarX, energyBarY, (int) mouseX, (int) mouseY);
        if (shouldRenderTooltip) {
            tooltip.add(IRTranslations.General.ENERGY_NAME.component());
            tooltip.add(IRTranslations.Jei.ENERGY_USAGE.component(IRTranslations.General.ENERGY_UNIT.component().getString(), recipe.energy()));
        }
    }

    @Override
    public void draw(CentrifugeRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        int x = getWidth() / 2 - 9;
        int y = getHeight() / 2 - 9;

        int energyBarX = 10;
        int energyBarY = 20;
        guiGraphics.blitSprite(recipe.energy() > 0 ? ENERGY_BAR : ENERGY_BAR_EMPTY, energyBarX, energyBarY, 12 * 6/5, 48 * 6/5);

        guiGraphics.blitSprite(PROGRESS_ARROWS_SPRITE, x - 15, y - 17, 48, 48);
        guiGraphics.blitSprite(SLOT_SPRITE, x, y - 1, 18, 18);

        for (Direction dir : BlockStateProperties.HORIZONTAL_FACING.getPossibleValues()) {
            guiGraphics.blitSprite(SLOT_SPRITE, x + dir.getNormal().getX() * 36, y + dir.getNormal().getZ() * 36 - 1, 18, 18);
        }

        guiGraphics.blitSprite(TANK_SPRITE, x + 60, y - 16, 18, 54);
        guiGraphics.drawString(Minecraft.getInstance().font, String.format("%d sec", recipe.duration() / 20), 4, 8, ChatFormatting.DARK_GRAY.getColor(), false);
    }

    private boolean shouldRenderTooltip(int x, int y, int mouseX, int mouseY) {
        int width = 12 * 6/5;
        int height = 48 * 6/5;
        boolean matchesOnX = mouseX > x && mouseX < x + width;
        boolean matchesOnY = mouseY > y && mouseY < y + height;
        return matchesOnX && matchesOnY;
    }
    
}