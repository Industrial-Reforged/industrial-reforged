package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.multiblocks.Multiblock;
import com.indref.industrial_reforged.registries.multiblocks.BlastFurnaceMultiblock;
import com.indref.industrial_reforged.registries.multiblocks.CrucibleMultiblock;
import com.indref.industrial_reforged.registries.multiblocks.FireBoxMultiblock;
import com.indref.industrial_reforged.tiers.CrucibleTiers;
import com.indref.industrial_reforged.tiers.FireboxTiers;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class IRMultiblocks {
    public static final DeferredRegister<Multiblock> MULTIBLOCKS = DeferredRegister.create(IRRegistries.MULTIBLOCK, IndustrialReforged.MODID);

    public static final DeferredHolder<Multiblock, CrucibleMultiblock> CRUCIBLE_CERAMIC = MULTIBLOCKS.register("crucible_ceramic",
            () -> new CrucibleMultiblock(CrucibleTiers.CERAMIC));
    public static final DeferredHolder<Multiblock, FireBoxMultiblock> REFRACTORY_FIREBOX = MULTIBLOCKS.register("refractory_firebox",
            () -> new FireBoxMultiblock(FireboxTiers.REFRACTORY));
    public static final DeferredHolder<Multiblock, BlastFurnaceMultiblock> BLAST_FURNACE = MULTIBLOCKS.register("blast_furnace",
            BlastFurnaceMultiblock::new);
}
