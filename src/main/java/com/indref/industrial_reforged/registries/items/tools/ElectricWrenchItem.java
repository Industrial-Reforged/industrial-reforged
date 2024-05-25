package com.indref.industrial_reforged.registries.items.tools;

import com.indref.industrial_reforged.api.blocks.Wrenchable;
import com.indref.industrial_reforged.api.items.ToolItem;
import com.indref.industrial_reforged.api.items.container.SimpleElectricItem;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

public class ElectricWrenchItem extends SimpleElectricItem implements ToolItem {
    public ElectricWrenchItem(Item.Properties properties, EnergyTier energyTier) {
        super(properties, energyTier);
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
            if (wrenchableBlock instanceof Wrenchable iWrenchableBlock && player.isCrouching() && getEnergyStored(itemInHand) >= 10) {
                // Drop the block itself instead of custom drop
                if (iWrenchableBlock.getDropItem().isEmpty()) {
                    ItemStack dropItem = wrenchableBlock.asItem().getDefaultInstance();
                    ItemHandlerHelper.giveItemToPlayer(player, dropItem);
                }
                // Drop the custom drop
                else {
                    ItemStack dropItem = iWrenchableBlock.getDropItem().get().getDefaultInstance();
                    ItemHandlerHelper.giveItemToPlayer(player, dropItem);
                }
                setEnergyStored(itemInHand, getEnergyStored(itemInHand)-10);
                level.removeBlock(clickPos, false);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    public int getEnergyCapacity(ItemStack itemStack) {
        return 10000;
    }
}

