package com.indref.industrial_reforged.networking;

import com.indref.industrial_reforged.IndustrialReforged;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record BlueprintStoredRecipePayload(ItemStack itemStack, List<ItemStack> itemStacks) implements CustomPacketPayload {
    public static final Type<BlueprintStoredRecipePayload> TYPE = new Type<>(new ResourceLocation(IndustrialReforged.MODID, "item_stored_recipe_payload"));
    public static final StreamCodec<RegistryFriendlyByteBuf, BlueprintStoredRecipePayload> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC,
            BlueprintStoredRecipePayload::itemStack,
            ItemStack.LIST_STREAM_CODEC,
            BlueprintStoredRecipePayload::itemStacks,
            BlueprintStoredRecipePayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
