package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.IRRegistries;
import com.indref.industrial_reforged.IndustrialReforged;
import com.portingdeadmods.portingdeadlibs.PDLRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class IRSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, IndustrialReforged.MODID);

    public static final Supplier<SoundEvent> HAMMERING = registerSoundEvent("hammering");
    public static final Supplier<SoundEvent> JETPACK = registerSoundEvent("jetpack");

    private static Supplier<SoundEvent> registerSoundEvent(String name){
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, name)));
    }
}
