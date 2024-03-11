package com.indref.industrial_reforged.networking;

import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.api.blocks.container.IEnergyBlock;
import com.indref.industrial_reforged.networking.data.EnergySyncData;
import com.indref.industrial_reforged.networking.data.FluidSyncData;
import com.indref.industrial_reforged.util.BlockUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class FluidPayload {
    private static final FluidPayload INSTANCE = new FluidPayload();

    public static FluidPayload getInstance() {
        return INSTANCE;
    }

    public void handleData(final FluidSyncData data, final PlayPayloadContext ignored) {
        BlockEntity entity = Minecraft.getInstance().level.getBlockEntity(data.blockPos());
        if (entity instanceof ContainerBlockEntity blockEntity)
            blockEntity.getFluidTank().setFluid(data.fluidStack());
    }
}
