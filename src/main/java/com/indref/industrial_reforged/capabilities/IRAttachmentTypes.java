package com.indref.industrial_reforged.capabilities;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.capabilities.energy.EnergyStorage;
import com.indref.industrial_reforged.capabilities.heat.HeatStorage;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class IRAttachmentTypes {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, IndustrialReforged.MODID);

    // Do not try to access these directly! Use capabilities or preferably cast to container block/item/energy

    protected static final Supplier<AttachmentType<EnergyStorage>> ENERGY = ATTACHMENT_TYPES.register(
            "energy", () -> AttachmentType.serializable(() -> new EnergyStorage(0)).build());

    protected static final Supplier<AttachmentType<HeatStorage>> HEAT = ATTACHMENT_TYPES.register(
            "heat", () -> AttachmentType.serializable(() -> new HeatStorage(0)).build());
}
