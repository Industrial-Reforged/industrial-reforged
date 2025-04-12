package com.indref.industrial_reforged.networking.crucible;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.client.renderer.item.bar.CrucibleProgressRenderer;
import com.indref.industrial_reforged.content.blockentities.multiblocks.controller.CrucibleBlockEntity;
import com.indref.industrial_reforged.util.items.ItemUtils;
import com.indref.industrial_reforged.util.capabilities.CapabilityUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record CrucibleMeltingProgressPayload(BlockPos pos, int slotIndex, float progress) implements CustomPacketPayload {
    public static final Type<CrucibleMeltingProgressPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "crucible_melting_progress_payload"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CrucibleMeltingProgressPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            CrucibleMeltingProgressPayload::pos,
            ByteBufCodecs.INT,
            CrucibleMeltingProgressPayload::slotIndex,
            ByteBufCodecs.FLOAT,
            CrucibleMeltingProgressPayload::progress,
            CrucibleMeltingProgressPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext ctx) {
        if (ctx.player() instanceof LocalPlayer localPlayer) {
            Level level = localPlayer.level();
            if (level instanceof ClientLevel clientLevel) {
                BlockEntity blockEntity = clientLevel.getBlockEntity(pos());
                if (blockEntity instanceof CrucibleBlockEntity crucibleBlockEntity) {
                    IItemHandler stackHandler = CapabilityUtils.itemHandlerCapability(crucibleBlockEntity);
                    ItemStack stackInSlot = stackHandler.getStackInSlot(slotIndex());
                    CompoundTag tag = ItemUtils.getImmutableTag(stackInSlot).copyTag();
                    tag.putFloat(CrucibleProgressRenderer.BARWIDTH_KEY, progress());
                    stackInSlot.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                }
            }

        }
    }
}
