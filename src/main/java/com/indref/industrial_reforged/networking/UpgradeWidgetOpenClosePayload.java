package com.indref.industrial_reforged.networking;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blockentities.RedstoneBlockEntity;
import com.indref.industrial_reforged.api.gui.MachineContainerMenu;
import com.indref.industrial_reforged.api.gui.slots.UpgradeSlot;
import com.indref.industrial_reforged.client.screen.CentrifugeScreen;
import com.portingdeadmods.portingdeadlibs.utils.codec.CodecUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record UpgradeWidgetOpenClosePayload(boolean open) implements CustomPacketPayload {
    public static final Type<UpgradeWidgetOpenClosePayload> TYPE = new Type<>(IndustrialReforged.rl("upgrade_widget_open_close"));
    public static final StreamCodec<ByteBuf, UpgradeWidgetOpenClosePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            UpgradeWidgetOpenClosePayload::open,
            UpgradeWidgetOpenClosePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().containerMenu instanceof MachineContainerMenu<?> machineMenu) {
                for (UpgradeSlot upgradeSlot : machineMenu.getUpgradeSlots()) {
                    upgradeSlot.setActive(this.open);
                }
            }
        }).exceptionally(err -> {
            IndustrialReforged.LOGGER.error("Failed to handle UpgradeWidgetOpenClose payload", err);
            return null;
        });
    }
}