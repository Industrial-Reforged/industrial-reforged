package com.indref.industrial_reforged.api.blocks;

import com.indref.industrial_reforged.api.items.DisplayItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;


/**
 * Implement this interface for your block entity
 * to display a custom info message when the
 * player hovers over the block with a scanner item
 */
public interface DisplayBlock {
    /**
     * This method will display hover scanner information for the specific block.
     * Note: The lists in the list stand for the lines, so if you want to have
     * a translatable component and a normal on a single line put both of them in one list
     * @param displayText the text that should be displayed
     * @param scannedBlock gives you info about the blockstate the scanner is hovering over
     * @param scannedBlockPos gives you info about the blockpos of the block the scanner is hovering over
     * @param level gives you info about the level of the block the scanner is hovering over
     */
    void displayOverlay(List<Component> displayText, BlockState scannedBlock, BlockPos scannedBlockPos, Level level);

    List<DisplayItem> getCompatibleItems();
}
