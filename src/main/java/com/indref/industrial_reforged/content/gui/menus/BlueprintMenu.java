package com.indref.industrial_reforged.content.gui.menus;

import com.indref.industrial_reforged.registries.IRMenuTypes;
import com.indref.industrial_reforged.content.items.misc.BlueprintItem;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BlueprintMenu extends AbstractContainerMenu {
    public BlueprintMenu(Player player, int containerId) {
        super(IRMenuTypes.BLUEPRINT_MENU.get(), containerId);
    }

    public BlueprintMenu(int id, Inventory inventory, RegistryFriendlyByteBuf data) {
        this(inventory.player, id);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.getMainHandItem().getItem() instanceof BlueprintItem || player.getOffhandItem().getItem() instanceof BlueprintItem;
    }
}
