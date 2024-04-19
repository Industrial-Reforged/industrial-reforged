package com.indref.industrial_reforged.registries.screen;

import com.indref.industrial_reforged.api.gui.IRAbstractContainerMenu;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRMenuTypes;
import com.indref.industrial_reforged.registries.blockentities.machines.CentrifugeBlockEntity;
import com.indref.industrial_reforged.registries.blockentities.machines.CraftingStationBlockEntity;
import com.indref.industrial_reforged.util.BlockUtils;
import com.indref.industrial_reforged.util.recipes.ItemhandlerCraftingContainer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CentrifugeMenu extends IRAbstractContainerMenu {

    public final CentrifugeBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;
    private final ContainerLevelAccess access;
    private final Player player;


    public CentrifugeMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(1));
    }

    public CentrifugeMenu(int containerId, Inventory inv, BlockEntity blockEntity, ContainerData data) {
        super(IRMenuTypes.CENTRIFUGE_MENU.get(), containerId, inv);
        this.level = inv.player.level();
        this.data = data;
        this.player = inv.player;
        this.access = ContainerLevelAccess.create(level, blockEntity.getBlockPos());
        this.blockEntity = (CentrifugeBlockEntity) blockEntity;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, IRBlocks.CENTRIFUGE.get());
    }
}
