package com.indref.industrial_reforged.util;

import com.indref.industrial_reforged.events.ClientEvents;
import net.neoforged.neoforge.fluids.FluidStack;

public final class IRClientUtils {
    public static void setPlayerInCrucible(FluidStack crucibleFluid) {
        ClientEvents.playerInCrucibleFluid = crucibleFluid;
    }

}
