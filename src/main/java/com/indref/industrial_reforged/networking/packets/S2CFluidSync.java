package com.indref.industrial_reforged.networking.packets;

import com.indref.industrial_reforged.api.items.container.IFluidItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.network.NetworkEvent;

// TODO: 10/19/2023 fix this 
public class S2CFluidSync {
    private final int fluidAmount;
    private final Fluid fluid;
    private final ItemStack itemStack;

    public S2CFluidSync(Fluid fluid, int fluidAmount, ItemStack itemStack) {
        this.fluid = fluid;
        this.fluidAmount = fluidAmount;
        this.itemStack = itemStack;
    }

    public S2CFluidSync(FriendlyByteBuf buf) {
        this.fluid = buf.readFluidStack().getFluid();
        this.fluidAmount = buf.readInt();
        this.itemStack = buf.readItem();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(fluidAmount);
        buf.writeItem(itemStack);
    }

    public boolean handle(NetworkEvent.Context supplier) {
        supplier.enqueueWork(() -> {
            if (itemStack.getItem() instanceof IFluidItem fluidItem) {
                fluidItem.tryFillFluid(this.fluid, this.fluidAmount, this.itemStack);
            }
        });
        return true;
    }
}

