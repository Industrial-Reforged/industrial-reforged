package com.indref.industrial_reforged.content.blockentities;

import com.indref.industrial_reforged.IRConfig;
import com.indref.industrial_reforged.api.blockentities.MachineBlockEntity;
import com.indref.industrial_reforged.capabilites.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.EnergyHandler;
import com.indref.industrial_reforged.impl.energy.EnergyHandlerImpl;
import com.indref.industrial_reforged.impl.energy.EnergyHandlerWrapper;
import com.indref.industrial_reforged.content.blocks.pipes.CableBlock;
import com.indref.industrial_reforged.content.menus.BatteryBoxMenu;
import com.indref.industrial_reforged.registries.IREnergyTiers;
import com.indref.industrial_reforged.registries.IRMachines;
import com.indref.industrial_reforged.registries.IRNetworks;
import com.indref.industrial_reforged.translations.IRTranslations;
import com.indref.industrial_reforged.util.BlockUtils;
import com.portingdeadmods.portingdeadlibs.utils.capabilities.HandlerUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BatteryBoxBlockEntity extends MachineBlockEntity implements MenuProvider {
    private final EnergyHandlerWrapper.NoFill outputEnergyHandler;

    public BatteryBoxBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRMachines.BATTERY_BOX.getBlockEntityType(), blockPos, blockState);
        addEuStorage(EnergyHandlerImpl.NoDrain::new, IREnergyTiers.LOW, IRConfig.batteryBoxEnergyCapacity);
        addItemHandler(HandlerUtils::newItemStackHandler, builder -> builder
                .slots(2)
                .validator((slot, item) -> item.getCapability(IRCapabilities.ENERGY_ITEM) != null)
                .onChange(this::onItemsChanged));

        this.outputEnergyHandler = new EnergyHandlerWrapper.NoFill(this.getEuStorage());
    }

    private void onItemsChanged(int slot) {
        this.updateData();
    }

    @Override
    public boolean supportsUpgrades() {
        return false;
    }

    @Override
    public int emitRedstoneLevel() {
        return BlockUtils.calcRedstoneFromEnergy(this.getEuStorage());
    }

    @Override
    public void tick() {
        super.tick();

        if (!level.isClientSide()) {
            if (this.getRedstoneSignalType().isActive(this.getRedstoneSignalStrength())) {
                EnergyHandler thisEnergyStorage = this.getEuStorage();
                if (level instanceof ServerLevel serverLevel) {
                    int min = Math.min(thisEnergyStorage.getEnergyTier().maxOutput(), thisEnergyStorage.getEnergyStored());
                    Direction outputDirection = getOutputDirection();
                    if (level.getBlockState(worldPosition.relative(outputDirection)).getBlock() instanceof CableBlock) {
                        int remainder = IRNetworks.ENERGY_NETWORK.get().transport(serverLevel, this.worldPosition, min, outputDirection);
                        thisEnergyStorage.forceDrainEnergy(min - remainder, false);
                    }
                }
            }
        }
    }

    private @NotNull Direction getOutputDirection() {
        return this.getBlockState().getValue(BlockStateProperties.FACING);
    }

    @Override
    public EnergyHandler getEuHandlerOnSide(Direction direction) {
        Direction facing = getOutputDirection();
        if (facing.getOpposite() == direction) {
            return this.outputEnergyHandler;
        }
        return super.getEuHandlerOnSide(direction);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return IRTranslations.Menus.BATTERY_BOX.component();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new BatteryBoxMenu(i, inventory, this);
    }
}
