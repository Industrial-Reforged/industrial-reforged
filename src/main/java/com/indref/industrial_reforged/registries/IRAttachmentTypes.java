package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.capabilities.energy.EnergyStorage;
import com.indref.industrial_reforged.api.capabilities.heat.HeatStorage;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

// Registry for neoforge's entity and block entity data attachment system
// items are handled in IRDataComponents
public final class IRAttachmentTypes {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, IndustrialReforged.MODID);

    // Do not try to access these directly! Use capabilities or preferably cast to container block/item/heat

    public static final Supplier<AttachmentType<EnergyStorage>> ENERGY = ATTACHMENT_TYPES.register(
            "energy", () -> AttachmentType.serializable(() -> EnergyStorage.EMPTY).build());

    public static final Supplier<AttachmentType<HeatStorage>> HEAT = ATTACHMENT_TYPES.register(
            "heat", () -> AttachmentType.serializable(() -> HeatStorage.EMPTY).build());
}
