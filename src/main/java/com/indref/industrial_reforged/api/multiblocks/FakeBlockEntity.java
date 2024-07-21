package com.indref.industrial_reforged.api.multiblocks;

import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.core.BlockPos;

import java.util.List;
import java.util.Optional;

public interface FakeBlockEntity {
    Optional<BlockPos> getActualBlockEntityPos();

    ObjectSet<BlockPos> getFakePositions();
}
