package com.indref.industrial_reforged.util;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.fluids.FluidStack;

public record SingleFluidStack(FluidStack fluidStack) {
    public static final SingleFluidStack EMPTY = new SingleFluidStack(FluidStack.EMPTY);
    public static final Codec<SingleFluidStack> CODEC = FluidStack.CODEC.xmap(SingleFluidStack::new, SingleFluidStack::fluidStack);
    public static final StreamCodec<RegistryFriendlyByteBuf, SingleFluidStack> STREAM_CODEC = FluidStack.STREAM_CODEC.map(SingleFluidStack::new, SingleFluidStack::fluidStack);

    public SingleFluidStack copy() {
        return new SingleFluidStack(fluidStack.copy());
    }
}
