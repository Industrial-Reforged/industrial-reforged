package com.indref.industrial_reforged.registries.blockentities.machines;

import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.recipes.CentrifugeRecipe;
import com.indref.industrial_reforged.registries.gui.menus.CentrifugeMenu;
import com.indref.industrial_reforged.tiers.EnergyTiers;
import com.indref.industrial_reforged.util.recipes.IngredientWithCount;
import com.indref.industrial_reforged.util.recipes.recipe_inputs.ItemRecipeInput;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CentrifugeBlockEntity extends ContainerBlockEntity implements MenuProvider {
    private int duration;

    public CentrifugeBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(IRBlockEntityTypes.CENTRIFUGE.get(), p_155229_, p_155230_);
        addEnergyStorage(getEnergyTier());
        addItemHandler(7, ((slot, itemStack) -> slot != 0));
    }

    @Override
    public @NotNull EnergyTier getEnergyTier() {
        return EnergyTiers.LOW;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Centrifuge");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new CentrifugeMenu(i, inventory, this, new SimpleContainerData(6));
    }

    @Override
    public void commonTick() {
        super.commonTick();
        Optional<CentrifugeRecipe> optionalRecipe = getCurrentRecipe();

        if (optionalRecipe.isPresent()) {
            CentrifugeRecipe recipe = optionalRecipe.get();
            int duration = recipe.duration();
            List<ItemStack> results = recipe.results();
            IngredientWithCount ingredient = recipe.ingredient();
            if (this.duration >= duration) {

            } else {
                this.duration++;
            }
        }
    }

    public Optional<CentrifugeRecipe> getCurrentRecipe() {
        return level.getRecipeManager()
                .getRecipeFor(CentrifugeRecipe.TYPE, new ItemRecipeInput(List.of(getItemHandlerStacks())), level)
                .map(RecipeHolder::value);
    }
}
