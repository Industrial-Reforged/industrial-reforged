package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.IRRegistries;
import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.multiblocks.Multiblock;
import com.indref.industrial_reforged.content.multiblocks.BlastFurnaceMultiblock;
import com.indref.industrial_reforged.content.multiblocks.CrucibleMultiblock;
import com.indref.industrial_reforged.content.multiblocks.FireboxMultiblock;
import com.indref.industrial_reforged.content.multiblocks.SmallFireboxMultiblock;
import com.indref.industrial_reforged.tiers.CrucibleTiers;
import com.indref.industrial_reforged.tiers.FireboxTiers;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class IRMultiblocks {
    public static final DeferredRegister<Multiblock> MULTIBLOCKS = DeferredRegister.create(IRRegistries.MULTIBLOCK, IndustrialReforged.MODID);

    public static final DeferredHolder<Multiblock, CrucibleMultiblock> CRUCIBLE_CERAMIC = MULTIBLOCKS.register("crucible_ceramic",
            () -> new CrucibleMultiblock(CrucibleTiers.CERAMIC));
    public static final DeferredHolder<Multiblock, FireboxMultiblock> REFRACTORY_FIREBOX = MULTIBLOCKS.register("refractory_firebox",
            () -> new FireboxMultiblock(FireboxTiers.REFRACTORY));
    public static final DeferredHolder<Multiblock, BlastFurnaceMultiblock> BLAST_FURNACE = MULTIBLOCKS.register("blast_furnace",
            BlastFurnaceMultiblock::new);
    public static final DeferredHolder<Multiblock, SmallFireboxMultiblock> SMALL_FIREBOX = MULTIBLOCKS.register("small_firebox",
            () -> new SmallFireboxMultiblock(FireboxTiers.SMALL));
}
