package com.indref.industrial_reforged.screen;

import com.indref.industrial_reforged.api.screen.IRAbstractContainerMenu;
import com.indref.industrial_reforged.content.IRBlocks;
import com.indref.industrial_reforged.content.blockentities.FireboxBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.common.capabilities.Capabilities;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class FireBoxMenu extends IRAbstractContainerMenu {
    public final FireboxBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;
    public FireBoxMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(1));
    }

    public FireBoxMenu(int pContainerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(IRMenuTypes.FIREBOX_MENU.get(), pContainerId, inv);
        checkContainerSize(inv, 1);
        blockEntity = ((FireboxBlockEntity) entity);
        this.level = inv.player.level();
        this.data = data;

        this.blockEntity.getCapability(Capabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            this.addSlot(new SlotItemHandler(iItemHandler, 0, 80, 36));
        });

        addDataSlots(data);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player p_38941_, int p_38942_) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, IRBlocks.COIL.get());
    }
}
