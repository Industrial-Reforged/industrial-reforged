package com.indref.industrial_reforged.registries.blockentities.multiblocks.misc;

import com.indref.industrial_reforged.api.multiblocks.util.SavesControllerPosBlockEntity;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.util.CapabilityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CrucibleWallBlockEntity extends BlockEntity implements SavesControllerPosBlockEntity {
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
    }

    public Optional<BlockPos> getControllerPos() {
        return controllerPos;
    }

    public IFluidHandler getFluidHandler() {
        Optional<BlockPos> controllerPos = getControllerPos();
        if (controllerPos.isPresent()) {
            BlockEntity blockEntity = level.getBlockEntity(controllerPos.get());
            if (blockEntity != null) {
                return CapabilityUtils.fluidHandlerCapability(blockEntity);
            }
        }
        return null;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return saveWithoutMetadata(provider);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        super.onDataPacket(net, pkt, lookupProvider);
    }
}
