package com.indref.industrial_reforged.api.blocks;

import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Optional;

public interface FakeBlockEntity {
    Optional<BlockEntity> getActualBlockEntity();
}
