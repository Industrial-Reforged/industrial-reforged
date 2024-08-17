package com.indref.industrial_reforged.registries.blockentities;

import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.api.capabilities.IOActions;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.gui.menus.CraftingStationMenu;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CraftingStationBlockEntity extends ContainerBlockEntity implements MenuProvider {
    public CraftingStationBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(IRBlockEntityTypes.CRAFTING_STATION.get(), p_155229_, p_155230_);
        addItemHandler(29);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Test");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
        return new CraftingStationMenu(containerId, inventory, this);
    }

    // TODO: Think about how to implement this
    @Override
    public Map<Direction, Pair<IOActions, int[]>> getItemIO() {
        return Map.of();
    }

    @Override
    public Map<Direction, Pair<IOActions, int[]>> getFluidIO() {
        return Map.of();
    }
}