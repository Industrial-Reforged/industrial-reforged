package com.indref.industrial_reforged.api.blocks;

import net.minecraft.core.BlockPos;

import java.util.Optional;

public interface FakeBlockEntity {
    Optional<BlockPos> getActualBlockEntityPos();
}
