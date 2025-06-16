package com.indref.industrial_reforged.content.gui.menus;

import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRMenuTypes;
import com.indref.industrial_reforged.content.blockentities.multiblocks.controller.CrucibleBlockEntity;
import com.indref.industrial_reforged.util.capabilities.CapabilityUtils;
import com.portingdeadmods.portingdeadlibs.api.gui.menus.PDLAbstractContainerMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class CrucibleMenu extends PDLAbstractContainerMenu<CrucibleBlockEntity> {
    private final Level level;

    public CrucibleMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, (CrucibleBlockEntity) inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public CrucibleMenu(int pContainerId, Inventory inv, CrucibleBlockEntity entity) {
        super(IRMenuTypes.CRUCIBLE_MENU.get(), pContainerId, inv, entity);
        checkContainerSize(inv, 1);
        this.level = inv.player.level();

        IItemHandler itemHandler = CapabilityUtils.itemHandlerCapability(entity);

        int x = 26;
        int y = 18;
        int index = 0;
        for (int yIndex = 0; yIndex < 3; yIndex++) {
            for (int xIndex = 0; xIndex < 3; xIndex++) {
                this.addSlot(new SlotItemHandler(itemHandler, index, x + xIndex * 18, y + yIndex * 18));
                index++;
            }
        }

        addPlayerHotbar(inv);
        addPlayerInventory(inv);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, getBlockEntity().getBlockPos()),
                player, IRBlocks.CERAMIC_CRUCIBLE_CONTROLLER.get());
    }

    @Override
    protected int getMergeableSlotCount() {
        return 9;
    }
}
