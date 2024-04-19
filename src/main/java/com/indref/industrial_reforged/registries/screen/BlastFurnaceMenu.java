package com.indref.industrial_reforged.registries.screen;

import com.indref.industrial_reforged.api.gui.IRAbstractContainerMenu;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRMenuTypes;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.controller.BlastFurnaceBlockEntity;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.controller.CrucibleBlockEntity;
import com.indref.industrial_reforged.util.BlockUtils;
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

public class BlastFurnaceMenu extends IRAbstractContainerMenu {
    public final BlastFurnaceBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    public BlastFurnaceMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(1));
    }

    public BlastFurnaceMenu(int pContainerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(IRMenuTypes.BLAST_FURNACE_MENU.get(), pContainerId, inv);
        checkContainerSize(inv, 1);
        blockEntity = (BlastFurnaceBlockEntity) entity;
        this.level = inv.player.level();
        this.data = data;

        Optional<ItemStackHandler> itemHandler = blockEntity.getItemHandler();

        itemHandler.ifPresent(handler -> {
            this.addSlot(new SlotItemHandler(handler, 0, 44, 37));
            this.addSlot(new SlotItemHandler(handler, 1, 18, 37));

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
                player, IRBlocks.BLAST_FURNACE_HATCH.get());
    }

    public Optional<FluidStack> getFluidStack() {
         if (this.blockEntity.getFluidTank().isPresent())
             return Optional.of(this.blockEntity.getFluidTank().get().getFluid());
         return Optional.empty();
    }
}
