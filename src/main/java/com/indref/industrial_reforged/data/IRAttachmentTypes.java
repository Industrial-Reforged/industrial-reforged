package com.indref.industrial_reforged.data;

import com.indref.industrial_reforged.IndustrialReforged;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public final class IRAttachmentTypes {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, IndustrialReforged.MODID);

    public static final Supplier<AttachmentType<Boolean>> IN_CRUCIBLE = ATTACHMENTS.register("in_crucible",
            () -> AttachmentType.builder(() -> false).build());
}
