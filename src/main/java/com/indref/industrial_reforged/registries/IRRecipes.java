package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.registries.recipes.CrucibleSmeltingRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class IRRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, IndustrialReforged.MODID);

    public static final Supplier<RecipeSerializer<CrucibleSmeltingRecipe>> ANVIL_SMASHING_SERIALIZER =
            SERIALIZERS.register(CrucibleSmeltingRecipe.NAME, () -> CrucibleSmeltingRecipe.Serializer.INSTANCE);
}
