package com.indref.industrial_reforged.util.recipes;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class RecipeUtils {
    public static final StreamCodec<RegistryFriendlyByteBuf, List<Ingredient>> INGREDIENT_STREAM_LIST_CODEC = Ingredient.CONTENTS_STREAM_CODEC.apply(
            ByteBufCodecs.collection(NonNullList::createWithCapacity)
    );

    // I HAVE WRITTEN THIS DAMN ALGORITHM SO MANY
    // TIMES ACROSS TWO DIFFERENT MODS AND HAVE
    // NEVER GOTTEN IT TO WORK PROPERLY. I SWEAR
    // I WILL GET IT TO WORK THIS TIME

    // Stolen from minecraft's RecipeMatcher utility class :>
    // Changed a bit of stuff like predicates -> IngredientWithCount

    /**
     * This compares two lists of items/ingredients.
     * It does not care about the order of these.
     */
    public static boolean compareItems(List<ItemStack> inputs, List<IngredientWithCount> ingredients) {
        int elements = inputs.size();
        if (elements != ingredients.size()) {
            return false;
        } else {
            int[] ret = new int[elements];

            Arrays.fill(ret, -1);

            BitSet data = new BitSet((elements + 2) * elements);

            for (int x = 0; x < elements; ++x) {
                int matched = 0;
                int offset = (x + 2) * elements;
                IngredientWithCount test = ingredients.get(x);

                for (int y = 0; y < elements; ++y) {
                    if (!data.get(y) && test.test(inputs.get(y))) {
                        data.set(offset + y);
                        ++matched;
                    }
                }

                if (matched == 0) {
                    return false;
                }

                if (matched == 1 && !claim(ret, data, x, elements)) {
                    return false;
                }
            }

            if (data.nextClearBit(0) >= elements) {
                return true;
            } else return backtrack(data, ret, 0, elements);
        }
    }

    private static boolean claim(int[] ret, BitSet data, int claimed, int elements) {
        Queue<Integer> pending = new LinkedList<>();
        pending.add(claimed);

        while (pending.peek() != null) {
            int test = (Integer) pending.poll();
            int offset = (test + 2) * elements;
            int used = data.nextSetBit(offset) - offset;
            if (used >= elements || used < 0) {
                throw new IllegalStateException("What? We matched something, but it wasn't set in the range of this test! Test: " + test + " Used: " + used);
            }

            data.set(used);
            data.set(elements + test);
            ret[used] = test;

            for (int x = 0; x < elements; ++x) {
                offset = (x + 2) * elements;
                if (data.get(offset + used) && !data.get(elements + x)) {
                    data.clear(offset + used);
                    int count = 0;

                    for (int y = offset; y < offset + elements; ++y) {
                        if (data.get(y)) {
                            ++count;
                        }
                    }

                    if (count == 0) {
                        return false;
                    }

                    if (count == 1) {
                        pending.add(x);
                    }
                }
            }
        }

        return true;
    }

    private static boolean backtrack(BitSet data, int[] ret, int start, int elements) {
        int test = data.nextClearBit(elements + start) - elements;
        if (test >= elements) {
            return true;
        } else if (test < 0) {
            throw new IllegalStateException("This should never happen, negative test in backtrack!");
        } else {
            int offset = (test + 2) * elements;

            for (int x = 0; x < elements; ++x) {
                if (data.get(offset + x) && !data.get(x)) {
                    data.set(x);
                    if (backtrack(data, ret, test + 1, elements)) {
                        ret[x] = test;
                        return true;
                    }

                    data.clear(x);
                }
            }

            return false;
        }
    }

    public static <T extends Recipe<?>> RecipeType<T> newRecipeType(String name) {
        return new RecipeType<>() {
            @Override
            public String toString() {
                return name;
            }
        };
    }

    public static<T extends Recipe<?>>RecipeSerializer<T> newRecipeSerializer(MapCodec<T> mapCodec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
        return new RecipeSerializer<T>() {
            @Override
            public @NotNull MapCodec<T> codec() {
                return mapCodec;
            }

            @Override
            public @NotNull StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
                return streamCodec;
            }
        };
    }

    public static List<IngredientWithCount> ingredientsToIWC(List<Ingredient> ingredients) {
        return ingredients.stream().map(ingredient -> new IngredientWithCount(ingredient, 1)).toList();
    }

    public static List<Ingredient> iWCToIngredients(List<IngredientWithCount> ingredientsWithCount) {
        return ingredientsWithCount.stream().map(IngredientWithCount::ingredient).toList();
    }

    public static List<Ingredient> iWCToIngredientsSaveCount(List<IngredientWithCount> ingredientsWithCount) {
        return ingredientsWithCount.stream().map(RecipeUtils::iWCToIngredientSaveCount).toList();
    }

    public static @NotNull Ingredient iWCToIngredientSaveCount(IngredientWithCount ingredientWithCount) {
        Ingredient ingredient = ingredientWithCount.ingredient();
        for (ItemStack itemStack : ingredient.getItems()) {
            itemStack.setCount(ingredientWithCount.count());
        }
        return ingredient;
    }
}
