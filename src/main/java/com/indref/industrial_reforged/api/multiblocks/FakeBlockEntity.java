package com.indref.industrial_reforged.api.multiblocks;

import net.minecraft.core.BlockPos;

import java.util.Optional;

public interface FakeBlockEntity {
    Optional<BlockPos> getActualBlockEntityPos();
}
