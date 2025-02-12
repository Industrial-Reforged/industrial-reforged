package com.indref.industrial_reforged.content.fluids;

import com.portingdeadmods.portingdeadlibs.api.fluids.PDLFluid;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import org.joml.Vector4i;

public class BioMassFluid extends PDLFluid {
    public BioMassFluid(String name) {
        super(name);
        this.fluidType = registerFluidType(FluidType.Properties.create(), new Vector4i(88, 214, 113, 255), FluidTemplates.WATER);
    }

    @Override
    public BaseFlowingFluid.Properties fluidProperties() {
        return super.fluidProperties().block(this.block).bucket(this.deferredBucket);
    }

    @Override
    public BlockBehaviour.Properties blockProperties() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.WATER);
    }
}
