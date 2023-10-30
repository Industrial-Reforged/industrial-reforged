package com.indref.industrial_reforged.content.items.tools;

import com.indref.industrial_reforged.content.IRItems;
import com.indref.industrial_reforged.content.items.tools.ToolItem;
import com.indref.industrial_reforged.util.ItemUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

// TODO: 10/22/2023 Implement highlight for the first block pos
public class TapeMeasureItem extends ToolItem {
    public TapeMeasureItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack useItem = ItemUtils.itemStackFromInteractionHand(interactionHand, player);
        CompoundTag tag = useItem.getOrCreateTag();
        useItem.setTag(tag);
        if (player.isShiftKeyDown() && tag.getBoolean("extended")) {
            int[] emptyIntArray = new int[0];
            tag.putBoolean("extended", false);
            tag.putIntArray("firstBlockPos", emptyIntArray);
            return InteractionResultHolder.success(useItem);
        }
        return InteractionResultHolder.fail(useItem);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        ItemStack useItem = useOnContext.getItemInHand();
        CompoundTag tag = useItem.getOrCreateTag();
        BlockPos blockPos = useOnContext.getClickedPos();
        Player player = useOnContext.getPlayer();
        if (!player.isShiftKeyDown() && isExtended(useItem) == 0) {
            int[] blockPosCoords = {blockPos.getX(), blockPos.getY(), blockPos.getZ()};
            tag.putIntArray("firstBlockPos", blockPosCoords);
            tag.putBoolean("extended", true);
            return InteractionResult.SUCCESS;
        } else if (!player.isShiftKeyDown() && isExtended(useItem) == 1) {
            // Player's position
            BlockPos playerPos = new BlockPos(player.getOnPos().getX(), player.getOnPos().getY(), player.getOnPos().getZ());

            // first marked block pos
            int[] firstBlockPos = tag.getIntArray("firstBlockPos");

            // calculate distance between first pos and player pos
            int[] finalPos = {
                    Math.abs(firstBlockPos[0]) - Math.abs(playerPos.getX()),
                    Math.abs(firstBlockPos[1]) - Math.abs(playerPos.getY()),
                    Math.abs(firstBlockPos[2]) - Math.abs(playerPos.getZ())
            };

            player.sendSystemMessage(Component.translatable("tape_measure.block_difference")
                    .withStyle(ChatFormatting.BLUE));

            for (int index = 0; index < finalPos.length; index++) {
                int pos = finalPos[index];
                if (pos != 0) {
                    String prefix = switch (index) {
                        case 0 -> "x: ";
                        case 1 -> "y: ";
                        case 2 -> "z: ";
                        default -> throw new IllegalStateException("Index out of bounds");
                    };
                    player.sendSystemMessage(Component.literal(prefix + Math.abs(pos))
                            .withStyle(ChatFormatting.GRAY));
                }
            }

            tag.putBoolean("extended", true);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide && entity instanceof Player player) {
            if (player.getMainHandItem().is(IRItems.TAPE_MEASURE.get())) {
                ItemStack useItem = player.getMainHandItem();
                int[] blockPos = useItem.getOrCreateTag().getIntArray("firstBlockPos");
                BlockPos playerPos = new BlockPos(player.getOnPos().getX(), player.getOnPos().getY(), player.getOnPos().getZ());
                if (blockPos.length != 0) {
                    player.displayClientMessage(Component.literal(
                                    (Math.abs(blockPos[0]) - Math.abs(playerPos.getX())) + ", "
                                            + (Math.abs(blockPos[1]) - Math.abs(playerPos.getY())) + ", "
                                            + (Math.abs(blockPos[2]) - Math.abs(playerPos.getZ())))
                            , true);
                }
            } else if (player.getOffhandItem().is(IRItems.TAPE_MEASURE.get())) {
            }
        }
    }

    public static float isExtended(ItemStack stack) {
        if (stack.hasTag()) {
            CompoundTag tag = stack.getOrCreateTag();
            return tag.getBoolean("extended") ? 1 : 0;
        } else {
            return 0;
        }
    }
}
