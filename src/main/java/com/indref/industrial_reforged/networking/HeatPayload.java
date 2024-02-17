package com.indref.industrial_reforged.networking;

import com.indref.industrial_reforged.api.blocks.container.IHeatBlock;
import com.indref.industrial_reforged.networking.data.HeatSyncData;
import com.indref.industrial_reforged.util.BlockUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class HeatPayload {
    private static final HeatPayload INSTANCE = new HeatPayload();

    public static HeatPayload getInstance() {
        return INSTANCE;
    }

    public void handleData(final HeatSyncData data, final PlayPayloadContext ignored) {
        BlockEntity entity = Minecraft.getInstance().level.getBlockEntity(data.blockPos());
        if (BlockUtils.isHeatBlock(entity)) {
            ((IHeatBlock) entity).setHeatStored(entity, data.heat());
        }
    }
}
