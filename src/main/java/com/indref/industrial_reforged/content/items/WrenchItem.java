package com.indref.industrial_reforged.content.items;

import com.indref.industrial_reforged.api.blocks.IWrenchable;
import com.indref.industrial_reforged.api.items.ToolItem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static net.minecraft.world.level.block.Block.popResource;

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
        // only on the server side
        if (!level.isClientSide) {
            // check if block can be wrenched
            if (wrenchableBlock instanceof IWrenchable iWrenchableBlock) {
                // Drop the block itself instead of custom drop
                if (iWrenchableBlock.getDropItem() == null) {
                    // drop item (the wrenchable block)
                    ItemStack dropItem = wrenchableBlock.asItem().getDefaultInstance();
                    // Calculate free slot or slot that already has the drop item
                    Pair<Integer, Boolean> freeSlot = isFreeOrSpecific(player, dropItem);
                    // check if inventory doesn't have an empty slot
                    if (freeSlot.getFirst() != -1) {
                        // slot that already has drop item
                        if (freeSlot.getSecond()) {
                            int matchSlotItemCount = player.getInventory().items.get(freeSlot.getFirst()).getCount();
                            player.getInventory().items.get(freeSlot.getFirst()).setCount(matchSlotItemCount + 1);
                        } else {
                            player.getInventory().items.set(freeSlot.getFirst(), dropItem);
                        }
                    } else {
                        popResource(level, clickPos, dropItem);
                    }
                }
                // Drop a custom drop
                else {
                    // drop item (the wrenchable block)
                    ItemStack dropItem = iWrenchableBlock.getDropItem().getDefaultInstance();
                    // Calculate free slot or slot that already has the drop item
                    Pair<Integer, Boolean> freeSlot = isFreeOrSpecific(player, dropItem);
                    player.sendSystemMessage(Component.literal("Matching slot: " + freeSlot));
                    // check if inventory doesn't have an empty slot
                    if (freeSlot.getFirst() != -1) {
                        // slot that already has drop item
                        if (freeSlot.getSecond()) {
                            int matchSlotItemCount = player.getInventory().items.get(freeSlot.getFirst()).getCount();
                            player.getInventory().items.get(freeSlot.getFirst()).setCount(matchSlotItemCount + 1);
                        } else {
                            player.getInventory().items.set(freeSlot.getFirst(), dropItem);
                        }
                    } else {
                        popResource(level, clickPos, dropItem);
                    }
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

    /**
     * Checks for empty slot or slot with specific item in inventory
     *
     * @param player   reference to the player and his inventory
     * @param specific specify the item you want to match
     * @return Pair(the integer returns the index of the matching slot. - 1 means that no slot is free, the boolean returns true if a matching item was found)
     */
    private static Pair<Integer, Boolean> isFreeOrSpecific(Player player, ItemStack specific) {
        for (int i = 0; i < player.getInventory().items.size(); ++i) {
            if (player.getInventory().items.get(i).is(specific.getItem())) {
                return Pair.of(i, true);
            }
        }
        for (int i = 0; i < player.getInventory().items.size(); ++i) {
            if (player.getInventory().items.get(i).isEmpty()) {
                return Pair.of(i, false);
            }
        }
        return Pair.of(-1, false);
    }
}
