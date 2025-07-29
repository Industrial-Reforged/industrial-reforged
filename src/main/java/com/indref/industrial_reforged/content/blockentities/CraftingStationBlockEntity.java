package com.indref.industrial_reforged.content.blockentities;

import com.google.common.collect.ImmutableMap;
import com.indref.industrial_reforged.api.blockentities.IRContainerBlockEntity;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.content.menus.CraftingStationMenu;
import com.indref.industrial_reforged.translations.IRTranslations;
import com.portingdeadmods.portingdeadlibs.api.utils.IOAction;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CraftingStationBlockEntity extends IRContainerBlockEntity implements MenuProvider {
    public CraftingStationBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(IRBlockEntityTypes.CRAFTING_STATION.get(), p_155229_, p_155230_);
        addItemHandler(29);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return IRTranslations.Menus.CRAFTING_STATION.component();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
        return new CraftingStationMenu(containerId, inventory, this);
    }

    @Override
    public <T> ImmutableMap<Direction, Pair<IOAction, int[]>> getSidedInteractions(BlockCapability<T, @Nullable Direction> capability) {
        return ImmutableMap.of();
    }
}