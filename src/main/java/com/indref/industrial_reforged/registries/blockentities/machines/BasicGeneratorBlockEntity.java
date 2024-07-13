package com.indref.industrial_reforged.registries.blockentities.machines;

import com.indref.industrial_reforged.api.blocks.generator.GeneratorBlockEntity;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.gui.menus.BasicGeneratorMenu;
import com.indref.industrial_reforged.tiers.EnergyTiers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.CommonHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class BasicGeneratorBlockEntity extends GeneratorBlockEntity implements MenuProvider {
    public BasicGeneratorBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(IRBlockEntityTypes.BASIC_GENERATOR.get(), p_155229_, p_155230_);
        addItemHandler(2, (slot, item) -> {
            boolean canInsertFuel = slot == 0 && item.getBurnTime(RecipeType.SMELTING) > 0;
            boolean canInsertBattery = slot  == 1 && item.getCapability(IRCapabilities.EnergyStorage.ITEM) != null;
            return canInsertFuel || canInsertBattery;
        });
    }

    @Override
    public @NotNull EnergyTier getEnergyTier() {
        return EnergyTiers.LOW;
    }

    @Override
    public int getGenerationAmount() {
        return 10;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("title.indref.basic_generator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new BasicGeneratorMenu(i, inventory, this);
    }
}
