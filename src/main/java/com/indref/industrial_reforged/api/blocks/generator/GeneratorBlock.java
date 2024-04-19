package com.indref.industrial_reforged.api.blocks.generator;

import com.indref.industrial_reforged.api.blocks.RotatableEntityBlock;
import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.util.EnergyNetUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class GeneratorBlock extends RotatableEntityBlock {

    public GeneratorBlock(Properties properties) {
        super(properties);
    }

    public abstract Optional<BlockEntityType<? extends ContainerBlockEntity>> getBlockEntity();

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide()) return null;

        if (getBlockEntity().isEmpty())
            return null;

        return createTickerHelper(blockEntityType, getBlockEntity().get(),
                (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick());
    }

    public static boolean isPartOfEnet(ServerLevel level, BlockPos blockPos) {
        return EnergyNetUtils.getEnergyNets(level).getEnets().getNetworkRaw(blockPos).isPresent();
    }
}
