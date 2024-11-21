package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.IndustrialReforged;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class IRFluids {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(Registries.FLUID, IndustrialReforged.MODID);

    public static final Supplier<FlowingFluid> MOLTEN_STEEL_SOURCE = FLUIDS.register("molten_steel",
            () -> new BaseFlowingFluid.Source(IRFluids.MOLTEN_STEEL_PROPERTIES));
    public static final Supplier<FlowingFluid> MOLTEN_STEEL_FLOWING = FLUIDS.register("molten_steel_flowing",
            () -> new BaseFlowingFluid.Flowing(IRFluids.MOLTEN_STEEL_PROPERTIES));

    public static final Supplier<FlowingFluid> MOLTEN_ALUMINUM_SOURCE = FLUIDS.register("molten_aluminum",
            () -> new BaseFlowingFluid.Source(IRFluids.MOLTEN_ALUMINUM_PROPERTIES));
    public static final Supplier<FlowingFluid> MOLTEN_ALUMINUM_FLOWING = FLUIDS.register("molten_aluminum_flowing",
            () -> new BaseFlowingFluid.Flowing(IRFluids.MOLTEN_ALUMINUM_PROPERTIES));

    public static final Supplier<FlowingFluid> OIL_SOURCE = FLUIDS.register("oil",
            () -> new BaseFlowingFluid.Source(IRFluids.OIL_PROPERTIES));
    public static final Supplier<FlowingFluid> OIL_FLOWING = FLUIDS.register("oil_flowing",
            () -> new BaseFlowingFluid.Flowing(IRFluids.OIL_PROPERTIES));

    public static final BaseFlowingFluid.Properties MOLTEN_ALUMINUM_PROPERTIES = new BaseFlowingFluid.Properties(
            IRFluidTypes.MOLTEN_ALUMINUM_FLUID_TYPE, MOLTEN_ALUMINUM_SOURCE, MOLTEN_ALUMINUM_FLOWING)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(IRBlocks.MOLTEN_ALUMINUM_FLUID_BLOCK)
            .bucket(IRItems.MOLTEN_ALUMINUM_BUCKET);

    public static final BaseFlowingFluid.Properties MOLTEN_STEEL_PROPERTIES = new BaseFlowingFluid.Properties(
            IRFluidTypes.MOLTEN_STEEL_FLUID_TYPE, MOLTEN_STEEL_SOURCE, MOLTEN_STEEL_FLOWING)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(IRBlocks.MOLTEN_STEEL_FLUID_BLOCK)
            .bucket(IRItems.MOLTEN_STEEL_BUCKET);

    public static final BaseFlowingFluid.Properties OIL_PROPERTIES = new BaseFlowingFluid.Properties(
            IRFluidTypes.OIL_FLUID_TYPE, OIL_SOURCE, OIL_FLOWING)
            .slopeFindDistance(2).levelDecreasePerBlock(2).block(IRBlocks.OIL_FLUID_BLOCK)
            .bucket(IRItems.OIL_BUCKET);
}
