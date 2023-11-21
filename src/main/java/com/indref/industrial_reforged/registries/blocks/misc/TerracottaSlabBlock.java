package com.indref.industrial_reforged.registries.blocks.misc;

import com.indref.industrial_reforged.api.multiblocks.IMultiBlockController;
import com.indref.industrial_reforged.api.multiblocks.IMultiblock;
import com.indref.industrial_reforged.registries.multiblocks.CrucibleMultiblock;
import net.minecraft.world.level.block.SlabBlock;

public class TerracottaSlabBlock extends SlabBlock implements IMultiBlockController {
    public TerracottaSlabBlock(Properties p_56359_) {
        super(p_56359_);
    }

    @Override
    public IMultiblock getMultiblock() {
        return CrucibleMultiblock.CERAMIC;
    }
}
