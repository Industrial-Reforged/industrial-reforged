package com.indref.industrial_reforged.content.items.tools;

import com.indref.industrial_reforged.api.blockentities.WrenchListenerBlockEntity;
import com.indref.industrial_reforged.api.blocks.CustomWrenchableBlock;
import com.indref.industrial_reforged.tags.IRTags;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

import static com.indref.industrial_reforged.util.BlockUtils.rotateBlock;

public class WrenchItem extends Item {
    public WrenchItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return false;
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
        BlockState wrenchableBlock = level.getBlockState(clickPos);
        ItemStack itemInHand = useOnContext.getItemInHand();

        if (wrenchableBlock.is(IRTags.Blocks.WRENCHABLE) && player.isShiftKeyDown()) {
            ItemStack dropItemStack = new ItemStack(wrenchableBlock.getBlock());

            if (wrenchableBlock.hasBlockEntity()) {
                BlockEntity blockEntity = level.getBlockEntity(clickPos);
                if (blockEntity instanceof WrenchListenerBlockEntity wrenchListenerBlockEntity) {
                    wrenchListenerBlockEntity.beforeRemoveByWrench(player);
                }
                blockEntity.saveToItem(dropItemStack, level.registryAccess());
            }

            if (wrenchableBlock.getBlock() instanceof CustomWrenchableBlock customWrenchableBlock
                    && customWrenchableBlock.canWrench(level, clickPos, wrenchableBlock)) {
                dropItemStack = customWrenchableBlock.getCustomDropItem().copy();
            }

            ItemHandlerHelper.giveItemToPlayer(player, dropItemStack);

            itemInHand.hurtAndBreak(1, player, LivingEntity.getSlotForHand(useOnContext.getHand()));

            level.removeBlock(clickPos, false);

            return InteractionResult.SUCCESS;
        } else if (!player.isShiftKeyDown()) {
            for (Property<?> prop : wrenchableBlock.getProperties()) {
                if (prop instanceof DirectionProperty directionProperty && prop.getName().equals("facing")) {
                    BlockState rotatedState = rotateBlock(wrenchableBlock, directionProperty, wrenchableBlock.getValue(directionProperty));
                    level.setBlock(clickPos, rotatedState, 3);
                    level.playSound(null, clickPos, SoundEvents.ITEM_FRAME_ROTATE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.FAIL;
    }
}
