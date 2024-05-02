package com.indref.industrial_reforged.registries.blockentities.multiblocks;

import com.indref.industrial_reforged.api.multiblocks.SavesControllerPos;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

// TODO: manually determine controllerpos based on blockstate
@Deprecated
public class CrucibleWallBlockEntity extends BlockEntity implements SavesControllerPos {
    private Optional<BlockPos> controllerPos = Optional.empty();

    public CrucibleWallBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRBlockEntityTypes.CRUCIBLE_WALL.get(), blockPos, blockState);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        controllerPos.ifPresent(pos -> {
            tag.putLong("controllerPos", pos.asLong());
        });
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        long rawPos = tag.getLong("controllerPos");
        if (rawPos != 0) {
            this.controllerPos = Optional.of(BlockPos.of(rawPos));
        } else {
            this.controllerPos = Optional.empty();
        }
    }

    @Override
    public void setControllerPos(BlockPos blockPos) {
        this.controllerPos = Optional.ofNullable(blockPos);
        // NetworkingHelper.sendToClient(new CrucibleControllerSyncData(this.worldPosition, blockPos));
    }

    public Optional<BlockPos> getControllerPos() {
        return controllerPos;
    }
}
