package com.indref.industrial_reforged.api.multiblocks;

import org.apache.commons.lang3.IntegerRange;

public record MultiblockLayer(boolean dynamic, IntegerRange range, int[] layer) {
}
