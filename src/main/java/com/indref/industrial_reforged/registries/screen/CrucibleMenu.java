package com.indref.industrial_reforged.registries.screen;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.gui.IRAbstractContainerMenu;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRMenuTypes;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.controller.CrucibleBlockEntity;
import com.indref.industrial_reforged.util.BlockUtils;
import com.indref.industrial_reforged.util.Utils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class CrucibleMenu extends IRAbstractContainerMenu {
    public final CrucibleBlockEntity blockEntity;
    private final Level level;

    public CrucibleMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public CrucibleMenu(int pContainerId, Inventory inv, BlockEntity entity) {
        super(IRMenuTypes.CRUCIBLE_MENU.get(), pContainerId, inv);
        checkContainerSize(inv, 1);
        blockEntity = ((CrucibleBlockEntity) entity);
        this.level = inv.player.level();

        Optional<ItemStackHandler> itemHandler = blockEntity.getItemHandler();

        itemHandler.ifPresent(handler -> {
            int x = 26;
            int y = 18;
            int index = 0;
            for (int yIndex = 0; yIndex < 3; yIndex++) {
                for (int xIndex = 0; xIndex < 3; xIndex++) {
                    this.addSlot(new SlotItemHandler(handler, index, x + xIndex * 18, y + yIndex * 18));
                    index++;
                }
            }

            addPlayerHotbar(inv);
            addPlayerInventory(inv);
        });
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int slotId) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, IRBlocks.CERAMIC_CRUCIBLE_CONTROLLER.get());
    }

    public Optional<FluidStack> getFluidStack() {
         if (this.blockEntity.getFluidTank().isPresent()) {
             return Optional.of(this.blockEntity.getFluidTank().get().getFluid());
         }
         return Optional.empty();
    }
}
