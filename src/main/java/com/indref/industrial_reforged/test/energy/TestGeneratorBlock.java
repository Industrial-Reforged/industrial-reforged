package com.indref.industrial_reforged.test.energy;

import com.indref.industrial_reforged.api.blocks.generator.GeneratorBlock;
import com.indref.industrial_reforged.api.blocks.generator.GeneratorBlockEntity;
import com.indref.industrial_reforged.content.IRBlockEntityTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TestGeneratorBlock extends GeneratorBlock {

    public TestGeneratorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        player.sendSystemMessage(Component.literal("Production: "+((TestGeneratorBE) blockEntity).getGenerationAmount()+"EU/t"));
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TestGeneratorBE(blockPos, blockState);
    }

    @Override
    public BlockEntityType<? extends GeneratorBlockEntity> getBlockEntity() {
        return IRBlockEntityTypes.TEST_GENERATOR.get();
    }
}
