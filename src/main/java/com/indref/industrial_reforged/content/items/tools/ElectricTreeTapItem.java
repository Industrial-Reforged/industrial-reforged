package com.indref.industrial_reforged.content.items.tools;

import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.api.items.container.SimpleEnergyItem;
import com.indref.industrial_reforged.api.items.tools.electric.ElectricToolItem;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

import static com.indref.industrial_reforged.content.blocks.trees.RubberTreeResinHoleBlock.RESIN;

public class ElectricTreeTapItem extends SimpleEnergyItem implements ElectricToolItem {
    private final IntSupplier energyUsage;
    private final IntSupplier energyCapacity;

    public ElectricTreeTapItem(Properties properties, Supplier<EnergyTier> energyTier, IntSupplier energyUsage, IntSupplier defaultEnergyCapacity) {
        super(properties, energyTier, defaultEnergyCapacity);
        this.energyUsage = energyUsage;
        this.energyCapacity = defaultEnergyCapacity;
    }

    public int getEnergyUsage() {
        return energyUsage.getAsInt();
    }

    @Override
    public int getDefaultEnergyCapacity() {
        return this.energyCapacity.getAsInt();
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        Level level = useOnContext.getLevel();
        BlockPos blockPos = useOnContext.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);
        ItemStack itemInHand = useOnContext.getItemInHand();
        IEnergyStorage energyStorage = getEnergyCap(itemInHand);

        if (blockState.is(IRBlocks.RUBBER_TREE_RESIN_HOLE.get()) && blockState.getValue(RESIN)) {
            Player player = useOnContext.getPlayer();
            if (canWork(itemInHand, player)) {
                level.setBlockAndUpdate(blockPos, blockState.setValue(RESIN, false));
                ItemStack resinDrop = new ItemStack(IRItems.STICKY_RESIN.get());
                RandomSource random = useOnContext.getLevel().random;
                int randomInt = random.nextInt(1, 4);
                resinDrop.setCount(randomInt);
                ItemHandlerHelper.giveItemToPlayer(player, resinDrop);
                consumeEnergy(itemInHand, player);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    public boolean requireEnergyToWork(ItemStack itemStack, Entity player) {
        return true;
    }

    @Override
    public int getEnergyUsage(ItemStack itemStack, @Nullable Entity entity) {
        return this.energyUsage.getAsInt();
    }
}
