package com.indref.industrial_reforged.registries.blockentities;

import com.indref.industrial_reforged.networking.data.ItemSyncData;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.util.BlockUtils;
import net.minecraft.BlockUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.PacketDistributor;

public class CastingTableBlockEntity extends BlockEntity {
    private final ItemStackHandler itemHandler = new ItemStackHandler(29) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
                PacketDistributor.ALL.noArg().send(new ItemSyncData(worldPosition, 1, new ItemStack[]{itemHandler.getStackInSlot(0)}));
            }
        }
    };

    public CastingTableBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(IRBlockEntityTypes.CASTING_TABLE.get(), p_155229_, p_155230_);
    }

    public ItemStack[] getRenderStacks() {
        return new ItemStack[]{itemHandler.getStackInSlot(0)};
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }


    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!level.isClientSide()) {
            PacketDistributor.ALL.noArg().send(new ItemSyncData(worldPosition, 1, new ItemStack[]{itemHandler.getStackInSlot(0)}));
        }
    }

    public void tick(BlockPos blockPos, BlockState blockState) {
        try {
            IItemHandler clientItemHandler = BlockUtils.getBlockEntityCapability(Capabilities.ItemHandler.BLOCK, Minecraft.getInstance().level.getBlockEntity(blockPos));
            if (clientItemHandler.getStackInSlot(0).isEmpty() && !level.isClientSide()) {
                PacketDistributor.ALL.noArg().send(new ItemSyncData(worldPosition, 1, new ItemStack[]{itemHandler.getStackInSlot(0)}));
            }
        } catch (Exception e) {

        }
    }
}
