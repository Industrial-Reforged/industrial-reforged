package com.indref.industrial_reforged.networking;

import com.indref.industrial_reforged.api.blocks.container.IEnergyBlock;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.networking.data.EnergySyncData;
import com.indref.industrial_reforged.networking.data.ItemNbtSyncData;
import com.indref.industrial_reforged.networking.data.ItemSyncData;
import com.indref.industrial_reforged.util.BlockUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class ItemPayload {
    private static final ItemPayload INSTANCE = new ItemPayload();

    public static ItemPayload getInstance() {
        return INSTANCE;
    }

    public void handleData(final ItemSyncData data, final PlayPayloadContext ignored) {
        BlockEntity entity = Minecraft.getInstance().level.getBlockEntity(data.getBlockPos());
        IItemHandler itemHandler = BlockUtils.getBlockEntityCapability(Capabilities.ItemHandler.BLOCK, entity);
        if (itemHandler != null) {
            for (int i = 0; i < data.getItemCount(); i++) {
                itemHandler.insertItem(i, data.getItemStacks()[i], false);
            }
        }
    }
}
