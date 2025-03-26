package com.indref.industrial_reforged.content.blocks;

import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.registries.IRMultiblocks;
import com.indref.industrial_reforged.registries.IRSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class TerracottaBricks extends Block {
    public TerracottaBricks(Properties properties) {
        super(properties);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.is(IRItems.HAMMER.get())) {
            BlockPos controllerPos = pos.relative(hitResult.getDirection().getOpposite()).below();
            if (level.getBlockState(controllerPos).is(IRBlocks.TERRACOTTA_BRICK_SLAB)) {
                boolean formed = IRMultiblocks.CRUCIBLE_CERAMIC.get().form(level, pos, player);
                if (formed) {
                    player.playSound(IRSoundEvents.HAMMERING.get(), 1, 0.85f);
                    return ItemInteractionResult.SUCCESS;
                }
                return ItemInteractionResult.FAIL;
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
}
