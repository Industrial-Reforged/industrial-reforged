package com.indref.industrial_reforged.compat.jei;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.content.recipes.BlastFurnaceRecipe;
import com.indref.industrial_reforged.content.recipes.CentrifugeRecipe;
import com.indref.industrial_reforged.content.recipes.CrucibleCastingRecipe;
import com.indref.industrial_reforged.content.recipes.CrucibleSmeltingRecipe;
import com.indref.industrial_reforged.registries.IRBlocks;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.BlastFurnaceBlock;

import java.util.List;

@JeiPlugin
public class IRJeiPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new CrucibleSmeltingCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new CastingCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new CentrifugeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new BlastFurnaceCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<CrucibleSmeltingRecipe> crucibleSmeltingRecipes = recipeManager.getAllRecipesFor(CrucibleSmeltingRecipe.TYPE)
                .stream().map(RecipeHolder::value).toList();
        registration.addRecipes(CrucibleSmeltingCategory.RECIPE_TYPE, crucibleSmeltingRecipes);

        List<CrucibleCastingRecipe> castingRecipes = recipeManager.getAllRecipesFor(CrucibleCastingRecipe.TYPE)
                .stream().map(RecipeHolder::value).toList();
        registration.addRecipes(CastingCategory.RECIPE_TYPE, castingRecipes);

        List<CentrifugeRecipe> centrifugingRecipes = recipeManager.getAllRecipesFor(CentrifugeRecipe.TYPE)
                .stream().map(RecipeHolder::value).toList();
        registration.addRecipes(CentrifugeCategory.RECIPE_TYPE, centrifugingRecipes);

        List<BlastFurnaceRecipe> blastFurnaceRecipes = recipeManager.getAllRecipesFor(BlastFurnaceRecipe.TYPE)
                .stream().map(RecipeHolder::value).toList();
        registration.addRecipes(BlastFurnaceCategory.RECIPE_TYPE, blastFurnaceRecipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(IRBlocks.FIREBOX_CONTROLLER.get()),
                RecipeTypes.FUELING);
        registration.addRecipeCatalyst(new ItemStack(IRBlocks.BASIC_GENERATOR.get()),
                RecipeTypes.FUELING);

        registration.addRecipeCatalyst(new ItemStack(IRBlocks.CERAMIC_CRUCIBLE_CONTROLLER.get()),
                CrucibleSmeltingCategory.RECIPE_TYPE);

        registration.addRecipeCatalyst(new ItemStack(IRBlocks.CERAMIC_CASTING_BASIN.get()),
                CastingCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(IRBlocks.BLAST_FURNACE_CASTING_BASIN.get()),
                CastingCategory.RECIPE_TYPE);

        registration.addRecipeCatalyst(new ItemStack(IRBlocks.CENTRIFUGE.get()),
                CentrifugeCategory.RECIPE_TYPE);

        registration.addRecipeCatalyst(new ItemStack(IRBlocks.BLAST_FURNACE_CONTROLLER.get()),
                BlastFurnaceCategory.RECIPE_TYPE);
    }
}
