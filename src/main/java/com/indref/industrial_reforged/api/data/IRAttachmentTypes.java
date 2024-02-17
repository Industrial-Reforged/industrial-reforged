package com.indref.industrial_reforged.api.data;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.data.energy.EnergyStorage;
import com.indref.industrial_reforged.api.data.heat.HeatStorage;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public final class IRAttachmentTypes {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, IndustrialReforged.MODID);

    // Do not try to access these directly! Use capabilities or preferably cast to container block/item/heat

    public static final Supplier<AttachmentType<EnergyStorage>> ENERGY = ATTACHMENT_TYPES.register(
            "energy", () -> AttachmentType.serializable(() -> new EnergyStorage(0)).build());

    public static final Supplier<AttachmentType<HeatStorage>> HEAT = ATTACHMENT_TYPES.register(
            "heat", () -> AttachmentType.serializable(() -> new HeatStorage(0)).build());
}
