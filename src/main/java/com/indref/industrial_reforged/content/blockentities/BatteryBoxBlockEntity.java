package com.indref.industrial_reforged.content.blockentities;

import com.indref.industrial_reforged.IRConfig;
import com.indref.industrial_reforged.api.blockentities.MachineBlockEntity;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.content.blocks.BatteryBoxBlock;
import com.indref.industrial_reforged.content.menus.BatteryBoxMenu;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.IREnergyTiers;
import com.indref.industrial_reforged.registries.IRNetworks;
import com.indref.industrial_reforged.translations.IRTranslations;
import com.indref.industrial_reforged.util.BlockUtils;
import com.indref.industrial_reforged.util.capabilities.CapabilityUtils;
import com.portingdeadmods.portingdeadlibs.api.utils.IOAction;
import com.portingdeadmods.portingdeadlibs.utils.capabilities.SidedCapUtils;
import it.unimi.dsi.fastutil.Pair;
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
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BatteryBoxBlockEntity extends MachineBlockEntity implements MenuProvider {
    private final Map<Direction, Pair<IOAction, int[]>> sidedInteractions;

    public BatteryBoxBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRBlockEntityTypes.BATTERY_BOX.get(), blockPos, blockState);
        addEuStorage(IREnergyTiers.LOW, IRConfig.batteryBoxEnergyCapacity);
        addItemHandler(2, (slot, item) -> item.getCapability(IRCapabilities.EnergyStorage.ITEM) != null);

        this.sidedInteractions = new HashMap<>();
        onBlockUpdated();
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
    public void commonTick() {
        super.commonTick();

//        if (!level.isClientSide()) {
//            IEnergyStorage thisEnergyStorage = this.getEuStorage();
//            if (level instanceof ServerLevel serverLevel) {
//                int min = Math.min(thisEnergyStorage.getEnergyTier().get().maxOutput(), thisEnergyStorage.getEnergyStored());
//                int remainder = IRNetworks.ENERGY_NETWORK.get().transport(serverLevel, this.worldPosition, min, this.getBlockState().getValue(BlockStateProperties.FACING));
//                thisEnergyStorage.tryDrainEnergy(min - remainder, false);
//            }
//        }
    }

    public void onBlockUpdated() {
        for (Direction direction : Direction.values()) {
            this.sidedInteractions.put(direction, Pair.of(IOAction.INSERT, new int[]{0}));
        }
        this.sidedInteractions.put(getBlockState().getValue(BatteryBoxBlock.FACING).getOpposite(), Pair.of(IOAction.EXTRACT, new int[]{0}));
        if (this.level != null) {
            this.invalidateCapabilities();
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();

        onBlockUpdated();
    }

    @Override
    public <T> Map<Direction, Pair<IOAction, int[]>> getSidedInteractions(BlockCapability<T, @Nullable Direction> blockCapability) {
        return this.sidedInteractions;
    }

    @Override
    public IEnergyStorage getEuHandlerOnSide(Direction direction) {
        IEnergyStorage euHandlerOnSide = super.getEuHandlerOnSide(direction);
        return euHandlerOnSide;
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
