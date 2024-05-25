package com.indref.industrial_reforged.registries.screen;

import com.indref.industrial_reforged.api.gui.IRAbstractContainerMenu;
import com.indref.industrial_reforged.registries.IRMenuTypes;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.controller.FireboxBlockEntity;
import com.indref.industrial_reforged.util.BlockUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class FireBoxMenu extends IRAbstractContainerMenu<FireboxBlockEntity> {
    private final Level level;
    private final Player player;

    public FireBoxMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, (FireboxBlockEntity) inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(1));
    }

    public FireBoxMenu(int pContainerId, Inventory inv, FireboxBlockEntity entity, ContainerData data) {
        super(IRMenuTypes.FIREBOX_MENU.get(), pContainerId, inv, entity);
        this.level = inv.player.level();
        this.player = inv.player;

        checkContainerSize(inv, 1);

        Optional<IItemHandler> itemHandler = BlockUtils.getBlockEntityCapability(Capabilities.ItemHandler.BLOCK, entity);
        itemHandler.ifPresent(handler -> this.addSlot(new SlotItemHandler(handler, 0, 80, 36)));

        addDataSlots(data);
        addPlayerHotbar(inv);
        addPlayerInventory(inv);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player p_38941_, int p_38942_) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        for (Block block : getBlockEntity().getType().getValidBlocks()) {
            boolean valid = stillValid(ContainerLevelAccess.create(level, getBlockEntity().getBlockPos()), player, block);
            if (valid) {
                return true;
            }
        }
        return false;
    }

    public Player getPlayer() {
        return player;
    }
}
