package com.indref.industrial_reforged.registries.blocks.multiblocks.misc;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CrucibleLegsBlock extends Block {
    public CrucibleLegsBlock(Properties properties) {
        super(properties);
    }

    public static final class VoxelShapes {
        public static final VoxelShape NORTH_BOTTOM = Block.box(10, 0, 0, 14, 16, 6);
    }
}
