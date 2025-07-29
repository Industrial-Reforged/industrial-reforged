package com.indref.industrial_reforged.content.menus;

import com.indref.industrial_reforged.registries.IRMenuTypes;
import com.indref.industrial_reforged.content.blockentities.multiblocks.controller.FireboxBlockEntity;
import com.indref.industrial_reforged.util.capabilities.CapabilityUtils;
import com.portingdeadmods.portingdeadlibs.api.gui.menus.PDLAbstractContainerMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class FireBoxMenu extends PDLAbstractContainerMenu<FireboxBlockEntity> {
    private final Level level;
    private final Player player;

    public FireBoxMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, (FireboxBlockEntity) inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public FireBoxMenu(int pContainerId, Inventory inv, FireboxBlockEntity entity) {
        super(IRMenuTypes.FIREBOX_MENU.get(), pContainerId, inv, entity);
        this.level = inv.player.level();
        this.player = inv.player;

        checkContainerSize(inv, 1);

        IItemHandler itemHandler = CapabilityUtils.itemHandlerCapability(entity);
        this.addSlot(new SlotItemHandler(itemHandler, 0, 80, 48));

        this.addPlayerHotbar(inv);
        this.addPlayerInventory(inv);
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

    @Override
    protected int getMergeableSlotCount() {
        return 1;
    }

    public Player getPlayer() {
        return player;
    }
}
