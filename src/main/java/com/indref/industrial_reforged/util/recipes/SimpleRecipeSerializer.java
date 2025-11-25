package com.indref.industrial_reforged.util.recipes;

import com.mojang.serialization.MapCodec;
import com.portingdeadmods.portingdeadlibs.api.recipes.PDLRecipe;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;

public record SimpleRecipeSerializer<T extends PDLRecipe<?>>(MapCodec<T> codec,
                                                             StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) implements RecipeSerializer<T> {
}
