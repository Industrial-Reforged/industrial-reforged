package com.indref.industrial_reforged.util;

import com.indref.industrial_reforged.events.CommonEvents;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.fluids.FluidStack;

public final class IRClientUtils {
    public static void setPlayerInCrucible(FluidStack crucibleFluid) {
        CommonEvents.Client.playerInCrucibleFluid = crucibleFluid;
    }
}
