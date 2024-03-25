package com.indref.industrial_reforged.networking;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.networking.data.CastingDurationSyncData;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.CastingBasinBlockEntity;
import com.indref.industrial_reforged.registries.blocks.CastingBasinBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import java.util.Optional;

public final class CastingDurationPayload {
    private static final CastingDurationPayload INSTANCE = new CastingDurationPayload();

    public static CastingDurationPayload getInstance() {
        return INSTANCE;
    }

    public void handleData(final CastingDurationSyncData data, final PlayPayloadContext ctx) {
        Level level = Minecraft.getInstance().level;
        if (level != null) {
            BlockEntity blockEntity = level.getBlockEntity(data.blockPos());

            if (blockEntity instanceof CastingBasinBlockEntity castingBasinBlockEntity) {
                castingBasinBlockEntity.duration = data.duration();
                castingBasinBlockEntity.maxDuration = data.maxDuration();
            }
        } else {
            IndustrialReforged.LOGGER.error("Failed to get level for casting duration sync payload");
        }
    }
}
