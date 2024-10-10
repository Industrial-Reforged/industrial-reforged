package com.indref.industrial_reforged.content.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class RawOreStorageBlock extends Block {
    public RawOreStorageBlock() {
        super(Properties.ofFullCopy(Blocks.RAW_IRON_BLOCK));
    }
}
