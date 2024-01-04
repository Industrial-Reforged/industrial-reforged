package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.multiblocks.IMultiblock;
import com.indref.industrial_reforged.registries.multiblocks.CrucibleMultiblock;
import com.indref.industrial_reforged.registries.multiblocks.FireBoxMultiblock;
import com.indref.industrial_reforged.tiers.CrucibleTiers;
import com.indref.industrial_reforged.tiers.FireboxTiers;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class IRMultiblocks {
    public static final DeferredRegister<IMultiblock> MULTIBLOCKS = DeferredRegister.create(IRRegistries.MULTIBLOCK, IndustrialReforged.MODID);

    public static final DeferredHolder<IMultiblock, CrucibleMultiblock> CRUCIBLE = MULTIBLOCKS.register("crucible",
            () -> new CrucibleMultiblock(CrucibleTiers.CERAMIC));
    public static final DeferredHolder<IMultiblock, FireBoxMultiblock> FIREBOX = MULTIBLOCKS.register("firebox",
            () -> new FireBoxMultiblock(FireboxTiers.REFRACTORY));
}
