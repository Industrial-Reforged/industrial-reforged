package com.indref.industrial_reforged.registries.blockentities.machines;

import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.api.blocks.container.IEnergyBlock;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.screen.CentrifugeMenu;
import com.indref.industrial_reforged.tiers.EnergyTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CentrifugeBlockEntity extends ContainerBlockEntity implements MenuProvider {
    public CentrifugeBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(IRBlockEntityTypes.CENTRIFUGE.get(), p_155229_, p_155230_);
        addEnergyStorage(getEnergyTier().get());
        addItemHandler(7);
    }

    @Override
    public Optional<EnergyTier> getEnergyTier() {
        return Optional.of(EnergyTiers.LOW);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Centrifuge");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new CentrifugeMenu(i, inventory, this, new SimpleContainerData(6));
    }
}
