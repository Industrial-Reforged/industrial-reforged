package com.indref.industrial_reforged.content.fluids;

import com.portingdeadmods.portingdeadlibs.api.fluids.PDLFluid;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import org.joml.Vector4i;

public class MoltenMetalFluid extends PDLFluid {
    public MoltenMetalFluid(String name, Vec3i color) {
        super(name);
        this.fluidType = registerFluidType(FluidType.Properties.create(), new Vector4i(color.getX(), color.getY(), color.getZ(), 255), FluidTemplates.MOLTEN_METAL);
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
