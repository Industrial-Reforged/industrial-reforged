package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.fluids.BaseFluidType;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FastColor;
import net.neoforged.neoforge.common.SoundAction;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.joml.Vector3f;

import java.util.function.Supplier;

public final class IRFluidTypes {
    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, IndustrialReforged.MODID);

    public static final Supplier<FluidType> SOAP_WATER_FLUID_TYPE = register("soap_water",
            FluidType.Properties.create().lightLevel(2).density(15).viscosity(5).sound(SoundAction.get("drink"),
                    SoundEvents.HONEY_DRINK), new Vec3i(224, 56, 208), FluidTemplate.WATER);
    public static final Supplier<FluidType> OIL_FLUID_TYPE = register("oil",
            FluidType.Properties.create().lightLevel(2).density(15).viscosity(5).sound(SoundAction.get("drink"),
                    SoundEvents.HONEY_DRINK), new Vec3i(255, 255, 255), FluidTemplate.OIL);
    public static final Supplier<FluidType> MOLTEN_STEEL_FLUID_TYPE = register("molten_steel",
            FluidType.Properties.create().lightLevel(2).density(15).viscosity(5).sound(SoundAction.get("drink"),
                    SoundEvents.HONEY_DRINK), new Vec3i(78, 86, 92), FluidTemplate.MOLTEN_METAL);

    private static Supplier<FluidType> register(String name, FluidType.Properties properties, Vec3i color, FluidTemplate template) {
        return FLUID_TYPES.register(name, () -> new BaseFluidType(template.still, template.flowing, template.overlay, color, properties));
    }

    public enum FluidTemplate {
        MOLTEN_METAL(ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "fluid/molten_fluid_still"),
                ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "fluid/molten_fluid_flow"),
                ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "fluid/molten_fluid_overlay")),
        OIL(ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "fluid/oil_fluid_still"),
                ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "fluid/oil_fluid_flow"),
                ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "fluid/oil_overlay")),
        WATER(ResourceLocation.parse("block/water_still"),
                ResourceLocation.parse("block/water_flow"),
                ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "misc/in_soap_water"));

        private final ResourceLocation still;
        private final ResourceLocation flowing;
        private final ResourceLocation overlay;

        FluidTemplate(ResourceLocation still, ResourceLocation flowing, ResourceLocation overlay) {
            this.still = still;
            this.flowing = flowing;
            this.overlay = overlay;
        }
    }
}
