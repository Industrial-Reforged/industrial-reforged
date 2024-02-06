package com.indref.industrial_reforged.networking.data;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.util.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class ItemSyncData implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(IndustrialReforged.MODID, "item_sync_data");
    private int itemCount;
    private BlockPos blockPos;
    private ItemStack[] itemStacks;

    public ItemSyncData(BlockPos blockPos, int itemCount, ItemStack[] itemStacks) {
        this.itemCount = itemCount;
        this.blockPos = blockPos;
        this.itemStacks = itemStacks;
    }

    public ItemSyncData(final FriendlyByteBuf buffer) {
        BlockPos blockPos1 = buffer.readBlockPos();
        int itemCount = buffer.readInt();
        ItemStack[] itemStacks = new ItemStack[itemCount];
        for (int i = 0; i < itemCount; i++) {
            itemStacks[i] = buffer.readItem();

            this.itemCount = itemCount;
            this.blockPos = blockPos1;
            this.itemStacks = itemStacks;
        }
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(blockPos);
        buffer.writeInt(itemCount);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public int getItemCount() {
        return itemCount;
    }

    public ItemStack[] getItemStacks() {
        return itemStacks;
    }
}
