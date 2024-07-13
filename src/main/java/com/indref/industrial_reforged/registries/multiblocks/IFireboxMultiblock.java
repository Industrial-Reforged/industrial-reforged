package com.indref.industrial_reforged.registries.multiblocks;

import com.indref.industrial_reforged.api.multiblocks.Multiblock;
import com.indref.industrial_reforged.api.tiers.FireboxTier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.Optional;

public interface IFireboxMultiblock extends Multiblock {
    FireboxTier getTier();

    /**
     * @param multiblockPos the blockpos of a block that is part of the multi
     * @return The controller block pos of the multiblock
     */
    Optional<BlockPos> getControllerPos(BlockPos multiblockPos, Level level);
}
