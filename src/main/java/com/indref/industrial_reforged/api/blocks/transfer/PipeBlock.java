package com.indref.industrial_reforged.api.blocks.transfer;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.state.BlockState;

public abstract class PipeBlock extends BaseEntityBlock {
    public PipeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction facing, BlockState facingState, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos facingPos) {
        Player player = Minecraft.getInstance().player;
        player.sendSystemMessage(Component.literal("Facing: "+facing));
        return blockState;
    }
}
