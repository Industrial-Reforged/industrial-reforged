package com.indref.industrial_reforged.compat.jei;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.client.screen.BasicGeneratorScreen;
import com.indref.industrial_reforged.client.screen.CentrifugeScreen;
import com.indref.industrial_reforged.content.recipes.*;
import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.registries.IRMachines;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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
        registration.addRecipeCategories(new MoldCastingCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new WoodenBasinCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new CentrifugeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new BlastFurnaceCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<CrucibleSmeltingRecipe> crucibleSmeltingRecipes = recipeManager.getAllRecipesFor(CrucibleSmeltingRecipe.TYPE)
                .stream().map(RecipeHolder::value).toList();
        registration.addRecipes(CrucibleSmeltingCategory.RECIPE_TYPE, crucibleSmeltingRecipes);

        Stream<BasinCastingRecipe> castingRecipes = recipeManager.getAllRecipesFor(BasinCastingRecipe.TYPE)
                .stream().map(RecipeHolder::value);
        List<BasinCastingRecipe> castingRecipes1 = new ArrayList<>();
        castingRecipes.forEach(recipe -> {
            if (!(recipe instanceof BasinMoldCastingRecipe)) {
                castingRecipes1.add(recipe);
            }
        });
        registration.addRecipes(CastingCategory.RECIPE_TYPE, castingRecipes1);

        Stream<BasinCastingRecipe> moldCastingRecipes = recipeManager.getAllRecipesFor(BasinCastingRecipe.TYPE)
                .stream().map(RecipeHolder::value);
        List<BasinMoldCastingRecipe> moldCastingRecipes1 = new ArrayList<>();
        moldCastingRecipes.forEach(recipe -> {
            if (recipe instanceof BasinMoldCastingRecipe basinMoldCastingRecipe) {
                moldCastingRecipes1.add(basinMoldCastingRecipe);
            }
        });
        registration.addRecipes(MoldCastingCategory.RECIPE_TYPE, moldCastingRecipes1);

        List<WoodenBasinRecipe> woodenBasinRecipes = recipeManager.getAllRecipesFor(WoodenBasinRecipe.TYPE)
                .stream().map(RecipeHolder::value).toList();
        registration.addRecipes(WoodenBasinCategory.RECIPE_TYPE, woodenBasinRecipes);

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
        registration.addRecipeCatalyst(new ItemStack(IRMachines.BASIC_GENERATOR.getBlockItem()),
                RecipeTypes.FUELING);

        registration.addRecipeCatalyst(new ItemStack(IRBlocks.CERAMIC_CRUCIBLE_CONTROLLER.get()),
                CrucibleSmeltingCategory.RECIPE_TYPE);

        registration.addRecipeCatalyst(new ItemStack(IRBlocks.CERAMIC_CASTING_BASIN.get()),
                CastingCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(IRBlocks.BLAST_FURNACE_CASTING_BASIN.get()),
                CastingCategory.RECIPE_TYPE);

        registration.addRecipeCatalyst(new ItemStack(IRBlocks.CERAMIC_CASTING_BASIN.get()),
                MoldCastingCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(IRBlocks.BLAST_FURNACE_CASTING_BASIN.get()),
                MoldCastingCategory.RECIPE_TYPE);

        registration.addRecipeCatalyst(new ItemStack(IRBlocks.WOODEN_BASIN.get()),
                WoodenBasinCategory.RECIPE_TYPE);

        registration.addRecipeCatalyst(new ItemStack(IRMachines.CENTRIFUGE.getBlock()),
                CentrifugeCategory.RECIPE_TYPE);

        registration.addRecipeCatalyst(new ItemStack(IRBlocks.BLAST_FURNACE_CONTROLLER.get()),
                BlastFurnaceCategory.RECIPE_TYPE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGenericGuiContainerHandler(CentrifugeScreen.class, new WidgetBounds());
        registration.addGenericGuiContainerHandler(BasicGeneratorScreen.class, new WidgetBounds());
    }
}
