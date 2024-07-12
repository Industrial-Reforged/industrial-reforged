package com.indref.industrial_reforged.api.data;

import com.indref.industrial_reforged.IndustrialReforged;
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
            "energy", () -> AttachmentType.builder(() -> EnergyStorage.EMPTY).serialize(EnergyStorage.CODEC).build());

    public static final Supplier<AttachmentType<HeatStorage>> HEAT = ATTACHMENT_TYPES.register(
            "heat", () -> AttachmentType.builder(() -> HeatStorage.EMPTY).serialize(HeatStorage.CODEC).build());
}
