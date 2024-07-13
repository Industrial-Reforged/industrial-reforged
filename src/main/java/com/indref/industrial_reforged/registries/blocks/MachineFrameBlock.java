package com.indref.industrial_reforged.registries.blocks;

import com.indref.industrial_reforged.api.blocks.WrenchableBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class MachineFrameBlock extends Block implements WrenchableBlock {
    public MachineFrameBlock(Properties properties) {
        super(properties.mapColor(MapColor.METAL)
                .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                .requiresCorrectToolForDrops()
                .strength(4.3F, 6.0F)
                .sound(SoundType.METAL));
    }
}
