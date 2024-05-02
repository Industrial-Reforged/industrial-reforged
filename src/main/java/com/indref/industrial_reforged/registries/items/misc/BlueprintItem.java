package com.indref.industrial_reforged.registries.items.misc;

import com.indref.industrial_reforged.api.data.IRDataComponents;
import com.indref.industrial_reforged.networking.BlueprintHasRecipePayload;
import com.indref.industrial_reforged.networking.BlueprintStoredRecipePayload;
import com.indref.industrial_reforged.networking.ItemActivityPayload;
import com.indref.industrial_reforged.registries.screen.CraftingStationMenu;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class BlueprintItem extends Item {
    public static final String HAS_RECIPE_KEY = "blueprint_has_recipe";

    public BlueprintItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.getOrDefault(IRDataComponents.HAS_RECIPE, false) && player.isShiftKeyDown()) {
            resetRecipe(itemStack);
            return InteractionResultHolder.success(itemStack);
        }
        return InteractionResultHolder.pass(itemStack);
    }

    /**
     * Note: This should be called client-side (for example a screen like the crafting station)
     */
    public static void setRecipe(int blueprintSlot, CraftingStationMenu menu) {
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            ItemStack item = menu.getItems().get(i);
            items.add(item);
        }
        ItemStack item = menu.getSlot(blueprintSlot).getItem();
        item.getOrDefault(IRDataComponents.HAS_RECIPE, false);
        PacketDistributor.sendToServer(new BlueprintStoredRecipePayload(item, items));
        PacketDistributor.sendToServer(new BlueprintHasRecipePayload(item, true));
    }

    public static float hasRecipe(ItemStack itemStack) {
        return itemStack.getOrDefault(IRDataComponents.HAS_RECIPE, false) ? 1 : 0;
    }

    public static void resetRecipe(ItemStack itemStack) {
        itemStack.set(IRDataComponents.HAS_RECIPE, false);
        itemStack.remove(IRDataComponents.STORED_RECIPE);
    }
}
