package com.indref.industrial_reforged.content.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class MetalStorageBlock extends Block {
    public MetalStorageBlock() {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK));
    }
}
