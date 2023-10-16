package com.indref.industrial_reforged.content.blocks;

import com.indref.industrial_reforged.worldgen.RubberTreeGrower;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class RubberTreeSaplingBlock extends SaplingBlock {

    public RubberTreeSaplingBlock() {
        super(new RubberTreeGrower(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING));
    }

}
