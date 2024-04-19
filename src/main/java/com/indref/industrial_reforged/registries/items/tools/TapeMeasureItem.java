package com.indref.industrial_reforged.registries.items.tools;

import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.util.ItemUtils;
import com.indref.industrial_reforged.util.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
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
    public static final String EXTENDED_KEY = "tape_measure_extended";

    public TapeMeasureItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack useItem = ItemUtils.itemStackFromInteractionHand(interactionHand, player);
        CompoundTag tag = useItem.getOrCreateTag();
        useItem.setTag(tag);
        if (player.isShiftKeyDown() && tag.getBoolean(EXTENDED_KEY)) {
            tag.putBoolean(EXTENDED_KEY, false);
            tag.putLong("firstBlockPos", 0);
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
        if (!player.isCrouching() && isExtended(useItem) == 0) {
            tag.putLong("firstBlockPos", blockPos.asLong());
            tag.putBoolean(EXTENDED_KEY, true);
            return InteractionResult.SUCCESS;
        } else if (!player.isCrouching() && isExtended(useItem) == 1) {

            // first marked block pos
            BlockPos firstBlockPos = BlockPos.of(tag.getLong("firstBlockPos"));

            // calculate distance between first pos and player pos
            int[] finalPos = {
                    Math.abs(firstBlockPos.getX()) - Math.abs(blockPos.getX()),
                    Math.abs(firstBlockPos.getY()) - Math.abs(blockPos.getY()),
                    Math.abs(firstBlockPos.getZ()) - Math.abs(blockPos.getZ())
            };

            if (useOnContext.getLevel().isClientSide) {
                player.sendSystemMessage(Component.translatable("tape_measure.block_difference")
                        .withStyle(ChatFormatting.BLUE));
            }

            for (int index = 0; index < finalPos.length; index++) {
                int pos = finalPos[index];
                if (pos != 0) {
                    String prefix = switch (index) {
                        case 0 -> "x: ";
                        case 1 -> "y: ";
                        case 2 -> "z: ";
                        default -> throw new IllegalStateException("Index out of bounds");
                    };
                    if (useOnContext.getLevel().isClientSide) {
                        player.sendSystemMessage(Component.literal(prefix + Math.abs(pos))
                                .withStyle(ChatFormatting.GRAY));
                    }
                }
            }

            tag.putBoolean(EXTENDED_KEY, false);
            tag.putIntArray("firstBlockPos", new int[0]);
            return InteractionResult.SUCCESS;
        } else if (player.isShiftKeyDown() && isExtended(useItem) == 1) {
            tag.putBoolean(EXTENDED_KEY, false);
            tag.putIntArray("firstBlockPos", new int[0]);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int slotId, boolean isSelected) {
        RandomSource randomSource = RandomSource.create();
        if (entity instanceof Player player) {
            if (player.getMainHandItem().is(IRItems.TAPE_MEASURE.get())) {
                ItemStack useItem = player.getMainHandItem();
                int[] blockPos = useItem.getOrCreateTag().getIntArray("firstBlockPos");
                BlockPos playerPos = new BlockPos(player.getOnPos().getX(), player.getOnPos().getY(), player.getOnPos().getZ());
                if (blockPos.length != 0) {
                    double d0 = (double) blockPos[0] + 0.5D + (0.5D - randomSource.nextDouble());
                    double d1 = (double) blockPos[1] + 1.0D + (0.5D - randomSource.nextDouble());
                    double d2 = (double) blockPos[2] + 0.5D + (0.5D - randomSource.nextDouble());
                    double d3 = (double) randomSource.nextFloat() * 0.4D;
                    level.addParticle(ParticleTypes.HAPPY_VILLAGER, d0, d1, d2, 0.0D, 1.0D, 0.0D);
                    player.displayClientMessage(Component.literal(
                                    (Math.abs(blockPos[0]) - Math.abs(playerPos.getX())) + ", "
                                            + -(Math.abs(blockPos[1]) - Math.abs(playerPos.getY())) + ", "
                                            + (Math.abs(blockPos[2]) - Math.abs(playerPos.getZ())))
                            , true);
                }
            } else if (player.getOffhandItem().is(IRItems.TAPE_MEASURE.get())) {
                ItemStack useItem = player.getOffhandItem();
                int[] blockPos = useItem.getOrCreateTag().getIntArray("firstBlockPos");
                BlockPos playerPos = new BlockPos(player.getOnPos().getX(), player.getOnPos().getY(), player.getOnPos().getZ());
                if (blockPos.length != 0) {
                    double d3 = (double) randomSource.nextFloat() * 0.4D;
                    level.addParticle(ParticleTypes.HAPPY_VILLAGER, blockPos[0], blockPos[1], blockPos[2], 0.0D, 1.0D, 0.0D);
                    player.displayClientMessage(Component.literal(
                                    (Math.abs(blockPos[0]) - Math.abs(playerPos.getX())) + ", "
                                            + (Math.abs(blockPos[1]) - Math.abs(playerPos.getY())) + ", "
                                            + (Math.abs(blockPos[2]) - Math.abs(playerPos.getZ())))
                            , true);
                }
            }
        }
    }

    public static float isExtended(ItemStack stack) {
        if (stack.hasTag()) {
            CompoundTag tag = stack.getOrCreateTag();
            return tag.getBoolean(EXTENDED_KEY) ? 1 : 0;
        } else {
            return 0;
        }
    }
}