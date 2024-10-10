package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.content.recipes.BlastFurnaceRecipe;
import com.indref.industrial_reforged.content.recipes.CentrifugeRecipe;
import com.indref.industrial_reforged.content.recipes.CrucibleCastingRecipe;
import com.indref.industrial_reforged.content.recipes.CrucibleSmeltingRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class IRRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, IndustrialReforged.MODID);

    static {
        SERIALIZERS.register(CrucibleSmeltingRecipe.NAME, () -> CrucibleSmeltingRecipe.SERIALIZER);
        SERIALIZERS.register(CrucibleCastingRecipe.NAME, () -> CrucibleCastingRecipe.SERIALIZER);
        SERIALIZERS.register(BlastFurnaceRecipe.NAME, () -> BlastFurnaceRecipe.SERIALIZER);
        SERIALIZERS.register(CentrifugeRecipe.NAME, () -> CentrifugeRecipe.SERIALIZER);
    }
}
