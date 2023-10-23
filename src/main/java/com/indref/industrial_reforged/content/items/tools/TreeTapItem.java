package com.indref.industrial_reforged.content.items.tools;

import com.indref.industrial_reforged.content.IRBlocks;
import com.indref.industrial_reforged.content.IRItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Random;

import static com.indref.industrial_reforged.content.blocks.RubberTreeResinHoleBlock.RESIN;

public class TreeTapItem extends ToolItem {
    public TreeTapItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        Level level = useOnContext.getLevel();
        BlockPos blockPos = useOnContext.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);
        if (blockState.is(IRBlocks.RUBBER_TREE_RESIN_HOLE.get()) &&
                blockState.getValue(RESIN)) {
            level.setBlockAndUpdate(blockPos, blockState.setValue(RESIN, false));
            ItemStack resinDrop = new ItemStack(IRItems.RUBBER_SHEET.get());
            Random random = new Random();
            int randomInt = random.nextInt(1, 4);
            resinDrop.setCount(randomInt);
            ItemHandlerHelper.giveItemToPlayer(useOnContext.getPlayer(), resinDrop);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }
}
