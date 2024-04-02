package com.indref.industrial_reforged.registries.blockentities.multiblocks.controller;

import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.screen.BlastFurnaceMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This is the blockentity for the blast furnace.
 * It is attached to the blast furnace hatch.
 * <p>
 * Since the blast furnace has 4 hatches, only one
 * is the actual blockentity that handles the
 * logic and the others just point to that block.
 */
public class BlastFurnaceBlockEntity extends ContainerBlockEntity implements MenuProvider {
    private boolean mainController;
    private BlockPos mainControllerPos = null;

    public BlastFurnaceBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(IRBlockEntityTypes.BLAST_FURNACE.get(), p_155229_, p_155230_);
        addItemHandler(2);
        addFluidTank(9000);
        getFluidTank().setFluid(new FluidStack(Fluids.WATER, 3000));
    }

    public void setMainController(boolean mainController) {
        this.mainController = mainController;
    }

    public boolean isMainController() {
        return mainController;
    }

    public void setMainControllerPos(BlockPos mainControllerPos) {
        this.mainControllerPos = mainControllerPos;
    }

    public BlockPos getMainControllerPos() {
        return mainControllerPos;
    }

    public BlastFurnaceBlockEntity getActualBlockEntity() {
        return (BlastFurnaceBlockEntity) level.getBlockEntity(mainControllerPos);
    }

    @Override
    protected void saveOther(CompoundTag tag) {
        tag.putBoolean("isController", isMainController());
        if (mainControllerPos != null) {
            tag.putLong("mainControllerPos", mainControllerPos.asLong());
        }
    }

    @Override
    protected void loadOther(CompoundTag tag) {
        this.mainController = tag.getBoolean("isController");
        this.mainControllerPos = BlockPos.of(tag.getLong("mainControllerPos"));
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.empty();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new BlastFurnaceMenu(containerId, inventory, this, null);
    }
}
