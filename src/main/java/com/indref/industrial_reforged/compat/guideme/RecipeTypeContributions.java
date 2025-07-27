package com.indref.industrial_reforged.compat.guideme;

import com.indref.industrial_reforged.content.recipes.CentrifugeRecipe;
import com.indref.industrial_reforged.registries.IRMachines;
import com.indref.industrial_reforged.util.recipes.RecipeUtils;
import guideme.compiler.tags.RecipeTypeMappingSupplier;
import guideme.document.block.LytBlock;
import guideme.document.block.LytSlotGrid;
import guideme.document.block.recipes.LytStandardRecipeBox;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;

public class RecipeTypeContributions implements RecipeTypeMappingSupplier {
    @Override
    public void collect(RecipeTypeMappings mappings) {
        mappings.add(CentrifugeRecipe.TYPE, RecipeTypeContributions::centrifuge);
    }

    private static LytStandardRecipeBox<CentrifugeRecipe> centrifuge(RecipeHolder<CentrifugeRecipe> holder) {
        return LytStandardRecipeBox.builder()
                .icon(IRMachines.CENTRIFUGE.getBlock())
                .title(IRMachines.CENTRIFUGE.getBlockItem().getDescription().getString())
                .input(RecipeUtils.iWCToIngredientSaveCount(holder.value().ingredient()))
                .output(LytSlotGrid.row(holder.value().results().stream().map(Ingredient::of).toList(), false))
                .build(holder);
    }
}
