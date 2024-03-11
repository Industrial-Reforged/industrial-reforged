package com.indref.industrial_reforged.registries.blockentities.multiblocks;

import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.networking.data.ItemSyncData;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.util.BlockUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.PacketDistributor;

public class CastingTableBlockEntity extends ContainerBlockEntity {

    public CastingTableBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(IRBlockEntityTypes.CASTING_TABLE.get(), p_155229_, p_155230_);
        addItemHandler(2);
        addFluidTank(1000);
    }

    public ItemStack[] getRenderStacks() {
        return new ItemStack[]{
                getItemHandler().getStackInSlot(0),
                getItemHandler().getStackInSlot(1)
        };
    }

    public void tick(BlockPos blockPos, BlockState blockState) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity != null) {
                IItemHandler clientItemHandler = BlockUtils.getBlockEntityCapability(Capabilities.ItemHandler.BLOCK, blockEntity);
                if (!clientItemHandler.equals(getItemHandler()) && !this.level.isClientSide()) {
                    PacketDistributor.ALL.noArg().send(new ItemSyncData(worldPosition, 2, new ItemStack[]{
                            getItemHandler().getStackInSlot(0),
                            getItemHandler().getStackInSlot(1)
                    }));
                }
            }
        }
    }
}
