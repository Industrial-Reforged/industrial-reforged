package com.indref.industrial_reforged.networking.data;

import com.indref.industrial_reforged.IndustrialReforged;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public record FluidSyncData(FluidStack fluidStack, BlockPos blockPos) implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(IndustrialReforged.MODID, "fluid_sync_data");
    public FluidSyncData(final FriendlyByteBuf buffer) {
        this(buffer.readFluidStack(), buffer.readBlockPos());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeFluidStack(fluidStack());
        buffer.writeBlockPos(blockPos());
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}
