package com.indref.industrial_reforged.registries.items.tools;

import com.indref.industrial_reforged.api.blocks.Wrenchable;
import com.indref.industrial_reforged.util.BlockUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.indref.industrial_reforged.util.BlockUtils.rotateBlock;


// TODO: 10/8/2023 add sounds for pickup make picked up blocks keep their nbt

public class WrenchItem extends ToolItem {
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
        assert player != null;

        if (!level.isClientSide) {
            if (wrenchableBlock instanceof Wrenchable iWrenchableBlock && player.isShiftKeyDown()) {
                if (iWrenchableBlock.getDropItem().isEmpty()) {
                    ItemStack dropItem = wrenchableBlock.getBlock().asItem().getDefaultInstance();
                    ItemHandlerHelper.giveItemToPlayer(player, dropItem);
                } else {
                    ItemStack dropItem = iWrenchableBlock.getDropItem().get().getDefaultInstance();
                    ItemHandlerHelper.giveItemToPlayer(player, dropItem);
                }
                if (isDamageable(itemInHand)) {
                    itemInHand.hurtAndBreak(1, Objects.requireNonNull(useOnContext.getPlayer()), (player1) -> {
                        player1.broadcastBreakEvent(useOnContext.getHand());
                    });
                }
                level.removeBlock(clickPos, false);
            } else {
                for (Property<?> prop : wrenchableBlock.getProperties()) {
                    if (prop instanceof DirectionProperty && prop.getName().equals("facing")) {
                        BlockState rotatedState = rotateBlock(wrenchableBlock, (DirectionProperty) prop, wrenchableBlock.getValue(prop));
                        level.setBlock(clickPos, rotatedState, 3);
                        level.playSound(null, clickPos, SoundEvents.ITEM_FRAME_ROTATE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }
}
