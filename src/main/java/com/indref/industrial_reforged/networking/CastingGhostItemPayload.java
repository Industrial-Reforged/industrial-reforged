package com.indref.industrial_reforged.networking;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.networking.data.CastingDurationSyncData;
import com.indref.industrial_reforged.networking.data.CastingGhostItemSyncData;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.CastingBasinBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public final class CastingGhostItemPayload {
    private static final CastingGhostItemPayload INSTANCE = new CastingGhostItemPayload();

    public static CastingGhostItemPayload getInstance() {
        return INSTANCE;
    }

    public void handleData(final CastingGhostItemSyncData data, final PlayPayloadContext ctx) {
        Level level = Minecraft.getInstance().level;
        if (level != null) {
            BlockEntity blockEntity = level.getBlockEntity(data.blockPos());

            if (blockEntity instanceof CastingBasinBlockEntity castingBasinBlockEntity) {
                castingBasinBlockEntity.resultItem = data.itemStack();
            }
        } else {
            IndustrialReforged.LOGGER.error("Failed to get level for casting duration sync payload");
        }
    }
}
