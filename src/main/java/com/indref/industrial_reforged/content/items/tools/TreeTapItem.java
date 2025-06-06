package com.indref.industrial_reforged.content.items.tools;

import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRItems;
import com.portingdeadmods.portingdeadlibs.utils.ItemUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import java.util.Random;

import static com.indref.industrial_reforged.content.blocks.trees.RubberTreeResinHoleBlock.RESIN;

public class TreeTapItem extends Item {
    public TreeTapItem(Properties properties) {
        super(properties.durability(80));
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        Level level = useOnContext.getLevel();
        BlockPos blockPos = useOnContext.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);
        if (blockState.is(IRBlocks.RUBBER_TREE_RESIN_HOLE.get()) && blockState.getValue(RESIN)) {
            level.setBlockAndUpdate(blockPos, blockState.setValue(RESIN, false));
            ItemStack resinDrop = new ItemStack(IRItems.STICKY_RESIN.get());
            Random random = new Random();
            int randomInt = random.nextInt(1, 4);
            resinDrop.setCount(randomInt);
            ItemUtils.giveItemToPlayerNoSound(useOnContext.getPlayer(), resinDrop);
            level.playSound(null, blockPos, SoundEvents.HONEY_BLOCK_PLACE, SoundSource.PLAYERS);
            useOnContext.getItemInHand().hurtAndBreak(1, useOnContext.getPlayer(), LivingEntity.getSlotForHand(useOnContext.getHand()));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }
}
