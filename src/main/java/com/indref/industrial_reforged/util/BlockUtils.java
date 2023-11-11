package com.indref.industrial_reforged.util;

import net.minecraft.core.BlockPos;

public final class BlockUtils {
    public static BlockPos[] getBlocksAroundSelf(BlockPos selfPos) {
        return new BlockPos[] {
                selfPos.offset(1, 0, 0),
                selfPos.offset(0, 1, 0),
                selfPos.offset(0, 0, 1),
                selfPos.offset(-1, 0, 0),
                selfPos.offset(0, -1, 0),
                selfPos.offset(0, 0, -1)
        };
    }

    public static BlockPos[] getBlocksAroundSelf3x3(BlockPos selfPos) {
        return new BlockPos[] {
                selfPos.offset(1, 0, 0),
                selfPos.offset(0, 0, 1),
                selfPos.offset(-1, 0, 0),
                selfPos.offset(0, 0, -1),
                selfPos.offset(1, 0, -1),
                selfPos.offset(-1, 0, 1),
                selfPos.offset(1, 0, 1),
                selfPos.offset(-1, 0, -1),
        };
    }
}
