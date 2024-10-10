package com.indref.industrial_reforged.registries.items.tools;

import com.indref.industrial_reforged.api.items.container.SimpleElectricItem;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import java.util.Random;

import static com.indref.industrial_reforged.registries.blocks.trees.RubberTreeResinHoleBlock.RESIN;

public class ElectricTreeTapItem extends SimpleElectricItem {
    public ElectricTreeTapItem(Properties properties, EnergyTier energyTier) {
        super(properties, energyTier);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        Level level = useOnContext.getLevel();
        BlockPos blockPos = useOnContext.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);
        if (blockState.is(IRBlocks.RUBBER_TREE_RESIN_HOLE.get()) && blockState.getValue(RESIN)) {
            if (getEnergyStored(useOnContext.getItemInHand()) >= 10) {
                level.setBlockAndUpdate(blockPos, blockState.setValue(RESIN, false));
                ItemStack resinDrop = new ItemStack(IRItems.STICKY_RESIN.get());
                Random random = new Random();
                int randomInt = random.nextInt(1, 4);
                resinDrop.setCount(randomInt);
                ItemHandlerHelper.giveItemToPlayer(useOnContext.getPlayer(), resinDrop);
                setEnergyStored(useOnContext.getItemInHand(), getEnergyStored(useOnContext.getItemInHand()) - 10);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }
}
