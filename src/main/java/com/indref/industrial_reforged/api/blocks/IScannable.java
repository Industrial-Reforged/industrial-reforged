package com.indref.industrial_reforged.api.blocks;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

/**
 * Implementing this interface for your block
 * to display a custom info message when the
 * player hovers over the block with a scanner
 */
public interface IScannable {
    /**
     * This method will display hover scanner information for the specific block.
     * Note: The lists in the list stand for the lines, so if you want to have
     * a translatable component and a normal on a single line put both of them in one list
     * @param blockState gives you info about the blockstate the scanner is hovering over
     * @return the text that should be displayed
     */
    List<List<Component>> displayText(BlockState blockState);
}
