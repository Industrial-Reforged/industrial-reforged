package com.indref.industrial_reforged.registries.items.misc;

import com.indref.industrial_reforged.capabilities.energy.network.IEnergyNets;
import com.indref.industrial_reforged.registries.blockentities.CableBlockEntity;
import com.indref.industrial_reforged.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class Debugger extends Item {
    public Debugger(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        player.sendSystemMessage(Component.literal("BlockPos: "+ ((BlockHitResult) Minecraft.getInstance().hitResult).getBlockPos()));
        return InteractionResultHolder.success(player.getItemInHand(interactionHand));
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        Player player = useOnContext.getPlayer();
        BlockState blockState = useOnContext.getLevel().getBlockState(useOnContext.getClickedPos());
        player.sendSystemMessage(Component.literal("Blockstate: "+blockState.getProperties()));
        player.sendSystemMessage(Component.literal("BlockPos: "+useOnContext.getClickedPos()));
        if (useOnContext.getLevel().getBlockEntity(useOnContext.getClickedPos()) instanceof CableBlockEntity cableBlock) {
            IEnergyNets enets = Util.getEnergyNets(useOnContext.getLevel());
            player.sendSystemMessage(Component.literal("Network: "+enets.getNetwork(useOnContext.getClickedPos())));
        }
        return InteractionResult.SUCCESS;
    }
}
