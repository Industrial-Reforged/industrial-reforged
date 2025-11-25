package com.indref.industrial_reforged.api.blocks;

import com.indref.industrial_reforged.IRRegistries;
import com.indref.industrial_reforged.api.blockentities.MachineBlockEntity;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.impl.tiers.EnergyTierImpl;
import com.indref.industrial_reforged.util.BlockUtils;
import com.indref.industrial_reforged.util.items.TooltipUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.portingdeadmods.portingdeadlibs.api.blocks.ContainerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public abstract class MachineBlock extends ContainerBlock implements EnergyTierBlock {
    protected final Supplier<? extends EnergyTier> energyTier;

    public MachineBlock(Properties properties, Supplier<? extends EnergyTier> energyTier) {
        super(properties);
        this.energyTier = energyTier;
    }

    public EnergyTier getEnergyTier() {
        return this.energyTier.get();
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);

        MachineBlockEntity be = BlockUtils.getBE(level, pos, MachineBlockEntity.class);
        be.initCapCache();
        be.setRedstoneSignalStrength(level.getBestNeighborSignal(pos));
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return BlockUtils.getBE(level, pos, MachineBlockEntity.class).emitRedstoneLevel();
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        TooltipUtils.addEnergyTierTooltip(tooltipComponents, this.getEnergyTier());

    }

    protected <T extends MachineBlock> MapCodec<T> machineBlockCodec(BiFunction<BlockBehaviour.Properties, Supplier<? extends EnergyTier>, T> factory) {
        return RecordCodecBuilder.mapCodec(inst -> inst.group(
                propertiesCodec(),
                IRRegistries.ENERGY_TIER.byNameCodec().fieldOf("energy_tier").forGetter(m -> m.energyTier.get())
        ).apply(inst, (p, e) -> factory.apply(p, () -> e)));
    }

}
