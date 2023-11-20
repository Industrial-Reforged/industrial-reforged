package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.IndustrialReforged;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class IRFluids {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(Registries.FLUID, IndustrialReforged.MODID);

    public static final Supplier<FlowingFluid> SOURCE_SOAP_WATER = FLUIDS.register("soap_water_fluid",
            () -> new BaseFlowingFluid.Source(IRFluids.SOAP_WATER_FLUID_PROPERTIES));
    public static final Supplier<FlowingFluid> FLOWING_SOAP_WATER = FLUIDS.register("flowing_soap_water",
            () -> new BaseFlowingFluid.Flowing(IRFluids.SOAP_WATER_FLUID_PROPERTIES));


    public static final BaseFlowingFluid.Properties SOAP_WATER_FLUID_PROPERTIES = new BaseFlowingFluid.Properties(
            IRFluidTypes.SOAP_WATER_FLUID_TYPE, SOURCE_SOAP_WATER, FLOWING_SOAP_WATER)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(IRBlocks.SOAP_WATER_BLOCK)
            .bucket(IRItems.SOAP_WATER_BUCKET);
}
