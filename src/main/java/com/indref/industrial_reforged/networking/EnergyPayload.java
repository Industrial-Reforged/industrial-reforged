package com.indref.industrial_reforged.networking;

import com.indref.industrial_reforged.api.blocks.container.IEnergyBlock;
import com.indref.industrial_reforged.networking.data.EnergySyncData;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class EnergyPayload {
    private static final EnergyPayload INSTANCE = new EnergyPayload();

    public static EnergyPayload getInstance() {
        return INSTANCE;
    }

    public void handleData(final EnergySyncData data, final PlayPayloadContext ignored) {
        BlockEntity entity = Minecraft.getInstance().level.getBlockEntity(data.blockPos());
        if (entity instanceof IEnergyBlock blockEntity) {
            blockEntity.setEnergyStored(entity, data.energy());
        }
    }
}
