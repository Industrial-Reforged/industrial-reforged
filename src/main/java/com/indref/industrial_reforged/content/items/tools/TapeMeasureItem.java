package com.indref.industrial_reforged.content.items.tools;

import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.data.components.ComponentTapeMeasure;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.util.ItemUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

// TODO: 10/22/2023 Implement highlight for the first block pos
public class TapeMeasureItem extends Item {
    public static final String EXTENDED_KEY = "tape_measure_extended";

    public TapeMeasureItem(Properties properties) {
        super(properties.component(IRDataComponents.TAPE_MEASURE, ComponentTapeMeasure.EMPTY));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack useItem = ItemUtils.itemStackFromInteractionHand(interactionHand, player);
        if (player.isShiftKeyDown() && useItem.get(IRDataComponents.TAPE_MEASURE).tapeMeasureExtended()) {
            useItem.set(IRDataComponents.TAPE_MEASURE, new ComponentTapeMeasure(BlockPos.ZERO, false));
            return InteractionResultHolder.success(useItem);
        }
        return InteractionResultHolder.fail(useItem);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        ItemStack useItem = useOnContext.getItemInHand();
        BlockPos blockPos = useOnContext.getClickedPos();
        Player player = useOnContext.getPlayer();
        if (!player.isCrouching() && isExtended(useItem) == 0) {
            useItem.set(IRDataComponents.TAPE_MEASURE, new ComponentTapeMeasure(blockPos, true));
            return InteractionResult.SUCCESS;
        } else if (!player.isCrouching() && isExtended(useItem) == 1) {

            // first marked block pos
            BlockPos firstBlockPos = useItem.getOrDefault(IRDataComponents.TAPE_MEASURE, ComponentTapeMeasure.EMPTY).firstPos();

            if (firstBlockPos != null) {
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
            }

            useItem.set(IRDataComponents.TAPE_MEASURE, new ComponentTapeMeasure(null, false));
            return InteractionResult.SUCCESS;
        } else if (player.isShiftKeyDown() && isExtended(useItem) == 1) {
            useItem.set(IRDataComponents.TAPE_MEASURE, new ComponentTapeMeasure(null, false));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    @Override
    public void inventoryTick(@NotNull ItemStack itemStack, Level level, Entity entity, int slotId, boolean isSelected) {
        RandomSource randomSource = RandomSource.create();
        if (entity instanceof Player player) {
            if (player.getMainHandItem().is(IRItems.TAPE_MEASURE.get())) {
                ItemStack useItem = player.getMainHandItem();
                BlockPos blockPos = useItem.getOrDefault(IRDataComponents.TAPE_MEASURE, ComponentTapeMeasure.EMPTY).firstPos();
                BlockPos playerPos = player.getOnPos();
                if (blockPos != null && !blockPos.equals(new BlockPos(0, 0, 0))) {
                    double d0 = (double) blockPos.getX() + 0.5D + (0.5D - randomSource.nextDouble());
                    double d1 = (double) blockPos.getY() + 1.0D + (0.5D - randomSource.nextDouble());
                    double d2 = (double) blockPos.getZ() + 0.5D + (0.5D - randomSource.nextDouble());
                    level.addParticle(ParticleTypes.HAPPY_VILLAGER, d0, d1, d2, 0.0D, 1.0D, 0.0D);
                }
            } else if (player.getOffhandItem().is(IRItems.TAPE_MEASURE.get())) {
                ItemStack useItem = player.getOffhandItem();
                BlockPos blockPos = useItem.getOrDefault(IRDataComponents.TAPE_MEASURE, ComponentTapeMeasure.EMPTY).firstPos();
                BlockPos playerPos = new BlockPos(player.getOnPos().getX(), player.getOnPos().getY(), player.getOnPos().getZ());
                if (blockPos != null && !blockPos.equals(new BlockPos(0, 0, 0))) {
                    double d3 = (double) randomSource.nextFloat() * 0.4D;
                    level.addParticle(ParticleTypes.HAPPY_VILLAGER, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 0.0D, 1.0D, 0.0D);
                    player.displayClientMessage(Component.literal(
                                    (Math.abs(blockPos.getX()) - Math.abs(playerPos.getX())) + ", "
                                            + (Math.abs(blockPos.getY()) - Math.abs(playerPos.getY())) + ", "
                                            + (Math.abs(blockPos.getZ()) - Math.abs(playerPos.getZ())))
                            , true);
                }
            }
        }
    }

    public static float isExtended(ItemStack stack) {
        return stack.getOrDefault(IRDataComponents.TAPE_MEASURE,
                new ComponentTapeMeasure(BlockPos.ZERO, false)).tapeMeasureExtended() ? 1 : 0;
    }
}