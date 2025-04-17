package com.indref.industrial_reforged.compat.jei;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.content.recipes.BlastFurnaceRecipe;
import com.indref.industrial_reforged.content.recipes.CentrifugeRecipe;
import com.indref.industrial_reforged.content.recipes.CrucibleCastingRecipe;
import com.indref.industrial_reforged.content.recipes.CrucibleSmeltingRecipe;
import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.data.IRDataMaps;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.BlastFurnaceBlock;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JeiPlugin
public class IRJeiPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "jei_plugin");
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        for (Item item : BuiltInRegistries.ITEM) {
            IEnergyStorage energyStorage = item.getDefaultInstance().getCapability(IRCapabilities.EnergyStorage.ITEM);
            if (energyStorage != null) {
                registration.registerSubtypeInterpreter(item, (IRSubTypeInterpreter<ItemStack>) (ingredient, context) -> context == UidContext.Ingredient ? ingredient.get(IRDataComponents.ENERGY) : null);
            }
            IFluidHandler fluidHandler = item.getDefaultInstance().getCapability(Capabilities.FluidHandler.ITEM);
            if (fluidHandler != null) {
                registration.registerSubtypeInterpreter(item, (IRSubTypeInterpreter<ItemStack>) (ingredient, context) -> context == UidContext.Ingredient ? ingredient.get(IRDataComponents.FLUID) : null);
            }
        }

        registration.registerSubtypeInterpreter(IRItems.TOOLBOX.get(), (IRSubTypeInterpreter<ItemStack>) (ingredient, context) -> context == UidContext.Ingredient ? ingredient.get(DataComponents.DYED_COLOR) : null);

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
