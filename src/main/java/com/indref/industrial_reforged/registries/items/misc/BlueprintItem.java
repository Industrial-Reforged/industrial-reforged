package com.indref.industrial_reforged.registries.items.misc;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.networking.data.ItemActivitySyncData;
import com.indref.industrial_reforged.networking.data.ItemNbtSyncData;
import com.indref.industrial_reforged.registries.screen.CraftingStationMenu;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

public class BlueprintItem extends Item {
    public static final String HAS_RECIPE_KEY = "blueprint_has_recipe";

    public BlueprintItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        CompoundTag tag = itemStack.getOrCreateTag();
        if (tag.getBoolean(HAS_RECIPE_KEY) && player.isShiftKeyDown()) {
            resetRecipe(itemStack);
            return InteractionResultHolder.success(itemStack);
        }
        return InteractionResultHolder.pass(itemStack);
    }

    public static void setRecipe(int blueprintSlot, CraftingStationMenu menu) {
        CompoundTag items = new CompoundTag();
        for (int i = 0; i < 9; i++) {
            ItemStack item = menu.getItems().get(i);
            CompoundTag itemTag = new CompoundTag();
            item.save(itemTag);
            items.put(String.valueOf(i), itemTag);
            IndustrialReforged.LOGGER.debug("Item: {}, slot: {}", item, i);
        }
        menu.getSlot(blueprintSlot).getItem().getOrCreateTag().putBoolean(HAS_RECIPE_KEY, true);
        PacketDistributor.SERVER.noArg().send(new ItemNbtSyncData(blueprintSlot, "storedRecipe", items));
        PacketDistributor.SERVER.noArg().send(new ItemActivitySyncData(blueprintSlot, HAS_RECIPE_KEY, true));
    }

    public static float hasRecipe(ItemStack itemStack) {
        if (itemStack.hasTag()) {
            CompoundTag tag = itemStack.getOrCreateTag();
            return tag.getBoolean(HAS_RECIPE_KEY) ? 1 : 0;
        }
        return 0;
    }

    public static void resetRecipe(ItemStack itemStack) {
        itemStack.getOrCreateTag().putBoolean(HAS_RECIPE_KEY, false);
        itemStack.removeTagKey("storedRecipe");
    }
}
