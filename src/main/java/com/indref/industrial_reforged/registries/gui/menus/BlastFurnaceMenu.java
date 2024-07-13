package com.indref.industrial_reforged.registries.gui.menus;

import com.indref.industrial_reforged.api.gui.IRAbstractContainerMenu;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRMenuTypes;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.controller.BlastFurnaceBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class BlastFurnaceMenu extends IRAbstractContainerMenu<BlastFurnaceBlockEntity> {
    private final ContainerData data;

    public BlastFurnaceMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, (BlastFurnaceBlockEntity) inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(1));
    }

    public BlastFurnaceMenu(int pContainerId, Inventory inv, BlastFurnaceBlockEntity entity, ContainerData data) {
        super(IRMenuTypes.BLAST_FURNACE_MENU.get(), pContainerId, inv, entity);
        checkContainerSize(inv, 2);
        this.data = data;

        ItemStackHandler itemHandler = blockEntity.getItemHandler();

        this.addSlot(new SlotItemHandler(itemHandler, 0, 44, 37));
        this.addSlot(new SlotItemHandler(itemHandler, 1, 18, 37));

        addPlayerHotbar(inv);
        addPlayerInventory(inv);
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()),
                player, IRBlocks.BLAST_FURNACE_HATCH.get());
    }

    public Optional<FluidStack> getFluidStack() {
        return Optional.of(this.blockEntity.getFluidTank().getFluid());
    }
}
