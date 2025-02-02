package com.indref.industrial_reforged.networking;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.data.IRDataComponents;
import com.portingdeadmods.portingdeadlibs.utils.codec.CodecUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;


// Use conversion methods in ItemUtils class for the slot
public record ArmorActivityPayload(EquipmentSlot slot, boolean active) implements CustomPacketPayload {
    public static final Type<ArmorActivityPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "armor_activity_payload"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ArmorActivityPayload> STREAM_CODEC = StreamCodec.composite(
            CodecUtils.enumStreamCodec(EquipmentSlot.class),
            ArmorActivityPayload::slot,
            ByteBufCodecs.BOOL,
            ArmorActivityPayload::active,
            ArmorActivityPayload::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext ctx) {
        Player player = ctx.player();
        player.getItemBySlot(slot()).set(IRDataComponents.ACTIVE, active());
    }
}
