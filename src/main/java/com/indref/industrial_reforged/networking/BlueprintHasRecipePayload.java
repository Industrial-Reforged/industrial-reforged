package com.indref.industrial_reforged.networking;

import com.indref.industrial_reforged.IndustrialReforged;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

// FIXME: Itemstack might not work
public record BlueprintHasRecipePayload(ItemStack itemStack, boolean hasRecipe) implements CustomPacketPayload {
    public static final Type<BlueprintHasRecipePayload> TYPE = new Type<>(new ResourceLocation(IndustrialReforged.MODID, "item_has_recipe_payload"));
    public static final StreamCodec<RegistryFriendlyByteBuf, BlueprintHasRecipePayload> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC,
            BlueprintHasRecipePayload::itemStack,
            ByteBufCodecs.BOOL,
            BlueprintHasRecipePayload::hasRecipe,
            BlueprintHasRecipePayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
