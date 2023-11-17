package com.indref.industrial_reforged.registries.items.tools;

import com.indref.industrial_reforged.api.blocks.IWrenchable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


// TODO: 10/8/2023 add sounds for pickup make picked up blocks keep their nbt

public class WrenchItem extends ToolItem {
    public WrenchItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return 100;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return true;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {
        Player player = useOnContext.getPlayer();
        Level level = useOnContext.getLevel();
        BlockPos clickPos = useOnContext.getClickedPos();
        Block wrenchableBlock = level.getBlockState(clickPos).getBlock();
        ItemStack itemInHand = useOnContext.getItemInHand();
        assert player != null;

        // only on the server side
        if (!level.isClientSide) {
            // check if block can be wrenched
            if (wrenchableBlock instanceof IWrenchable iWrenchableBlock && player.isCrouching()) {
                // Drop the block itself instead of custom drop
                if (iWrenchableBlock.getDropItem() == null) {
                    ItemStack dropItem = wrenchableBlock.asItem().getDefaultInstance();
                    ItemHandlerHelper.giveItemToPlayer(player, dropItem);
                }
                // Drop the custom drop
                else {
                    ItemStack dropItem = iWrenchableBlock.getDropItem().getDefaultInstance();
                    ItemHandlerHelper.giveItemToPlayer(player, dropItem);
                }
                if (isDamageable(itemInHand)) {
                    itemInHand.hurtAndBreak(1, Objects.requireNonNull(useOnContext.getPlayer()), (player1) -> {
                        player1.broadcastBreakEvent(useOnContext.getHand());
                    });
                }
                level.removeBlock(clickPos, false);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }
}
