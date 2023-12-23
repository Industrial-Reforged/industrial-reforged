package com.indref.industrial_reforged.registries.blockentities;

import com.indref.industrial_reforged.api.tiers.templates.CrucibleTier;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.blocks.multiblocks.CrucibleControllerBlock;
import com.indref.industrial_reforged.registries.screen.CrucibleMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CrucibleBlockEntity extends BlockEntity implements MenuProvider {
    private final CrucibleTier tier;
    protected final ContainerData data;

    private final ItemStackHandler itemHandler = new ItemStackHandler(9) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private final FluidTank fluidTank = new FluidTank(8000) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if (!level.isClientSide()) {
                // IRPackets.sendToClients(new FluidSyncS2CPacket(this.fluid, worldPosition));
            }
        }
    };

    public CrucibleBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRBlockEntityTypes.CRUCIBLE.get(), blockPos, blockState);
        this.tier = ((CrucibleControllerBlock) blockState.getBlock()).getTier();
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return 0;
            }

            @Override
            public void set(int pIndex, int pValue) {
            }

            @Override
            public int getCount() {
                return 1;
            }
        };
    }

    public int getHeatCapacity() {
        return tier.heatCapacity();
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("Crucible");
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag = fluidTank.writeToNBT(pTag);
        super.saveAdditional(pTag);
    }

    public FluidStack getFluid() {
        return fluidTank.getFluid();
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        fluidTank.readFromNBT(pTag);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
        return new CrucibleMenu(containerId, inventory, this, this.data);
    }
}
