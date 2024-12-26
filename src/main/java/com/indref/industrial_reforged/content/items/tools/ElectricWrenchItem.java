package com.indref.industrial_reforged.content.items.tools;

import com.indref.industrial_reforged.api.blocks.WrenchableBlock;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.api.items.container.SimpleEnergyItem;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

public class ElectricWrenchItem extends SimpleEnergyItem {
    public ElectricWrenchItem(Item.Properties properties, Holder<EnergyTier> energyTier) {
        super(properties, energyTier);
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {
        Player player = useOnContext.getPlayer();
        Level level = useOnContext.getLevel();
        BlockPos clickPos = useOnContext.getClickedPos();
        BlockState wrenchableBlock = level.getBlockState(clickPos);
        ItemStack itemInHand = useOnContext.getItemInHand();
        IEnergyStorage energyStorage = getEnergyCap(itemInHand);

        // only on the server side
        if (!level.isClientSide) {
            // check if block can be wrenched
            if (wrenchableBlock instanceof WrenchableBlock iWrenchableBlockBlock
                    && iWrenchableBlockBlock.canWrench(level, clickPos, wrenchableBlock)
                    && player.isCrouching()
                    && energyStorage.getEnergyStored() >= 10) {
                // Drop the block itself instead of custom drop
                if (iWrenchableBlockBlock.getDropItem().isEmpty()) {
                    ItemStack dropItem = wrenchableBlock.getBlock().asItem().getDefaultInstance();
                    ItemHandlerHelper.giveItemToPlayer(player, dropItem);
                }
                // Drop the custom drop
                else {
                    ItemStack dropItem = iWrenchableBlockBlock.getDropItem().get().getDefaultInstance();
                    ItemHandlerHelper.giveItemToPlayer(player, dropItem);
                }
                energyStorage.tryDrainEnergy(10, false);
                level.removeBlock(clickPos, false);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    public int getDefaultEnergyCapacity() {
        return 10000;
    }
}

