package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.content.fluids.*;
import com.portingdeadmods.portingdeadlibs.utils.FluidRegistrationHelper;
import net.minecraft.core.Vec3i;

public final class IRFluids {
    public static final FluidRegistrationHelper HELPER = new FluidRegistrationHelper(IRBlocks.BLOCKS, IRItems.ITEMS, IndustrialReforged.MODID);

    public static final OilFluid OIL = HELPER.registerFluid(new OilFluid("oil"));
    public static final BioMassFluid BIO_MASS = HELPER.registerFluid(new BioMassFluid("bio_mass"));
    public static final MethaneFluid METHANE = HELPER.registerFluid(new MethaneFluid("methane"));
    public static final StickyResinFluid STICKY_RESIN = HELPER.registerFluid(new StickyResinFluid("sticky_resin"));
    public static final EpoxyResinFluid EPOXY_RESIN = HELPER.registerFluid(new EpoxyResinFluid("epoxy_resin"));
    public static final MoltenMetalFluid MOLTEN_ALUMINUM = HELPER.registerFluid(new MoltenMetalFluid("molten_aluminum",
            new Vec3i(226, 232, 242)));
    public static final MoltenMetalFluid MOLTEN_STEEL = HELPER.registerFluid(new MoltenMetalFluid("molten_steel",
            new Vec3i(78, 86, 92)));
    public static final MoltenMetalFluid MOLTEN_COPPER = HELPER.registerFluid(new MoltenMetalFluid("molten_copper",
            new Vec3i(231, 124, 86)));
    public static final MoltenMetalFluid MOLTEN_IRON = HELPER.registerFluid(new MoltenMetalFluid("molten_iron",
            new Vec3i(216, 175, 147)));
    public static final MoltenMetalFluid MOLTEN_TIN = HELPER.registerFluid(new MoltenMetalFluid("molten_tin",
            new Vec3i(168, 181, 168)));
    public static final MoltenMetalFluid MOLTEN_NICKEL = HELPER.registerFluid(new MoltenMetalFluid("molten_nickel",
            new Vec3i(229, 233, 210)));
    public static final MoltenMetalFluid MOLTEN_GOLD = HELPER.registerFluid(new MoltenMetalFluid("molten_gold",
            new Vec3i(255, 225, 109), FluidTemplates.MOLTEN_GOLD));
    public static final MoltenMetalFluid MOLTEN_LEAD = HELPER.registerFluid(new MoltenMetalFluid("molten_lead",
            new Vec3i(71, 72, 100), FluidTemplates.MOLTEN_LEAD));
}
