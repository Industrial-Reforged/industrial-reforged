package com.indref.industrial_reforged.content.fluids;

import com.indref.industrial_reforged.api.fluids.IRFluid;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;

public class MoltenMetalFluid extends IRFluid {
    public MoltenMetalFluid(String name, Vec3i color) {
        super(name);
        this.fluidType = registerFluidType(FluidType.Properties.create(), color, FluidTemplates.MOLTEN_METAL);
    }

    @Override
    public BaseFlowingFluid.Properties fluidProperties() {
        return super.fluidProperties().block(this.block).bucket(this.deferredBucket);
    }

    @Override
    public BlockBehaviour.Properties blockProperties() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.LAVA);
    }
}
