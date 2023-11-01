package com.indref.industrial_reforged.content.items.tools;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

// TODO: 10/22/2023 Implement highlight for the first block pos
public class TapeMeasureItem extends ToolItem {
    public TapeMeasureItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext useOnContext) {
        Player player = useOnContext.getPlayer();
        player.sendSystemMessage(Component.literal("Used"));
        return InteractionResult.SUCCESS;
    }

    public static float isExtended(@NotNull ItemStack stack) {
        return 1f;
    }

    @Override
    public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity entity, int p_41407_, boolean p_41408_) {
        BlockHitResult blockHitResult = (BlockHitResult) Minecraft.getInstance().hitResult;
        entity.sendSystemMessage(Component.literal(String.valueOf(blockHitResult.getBlockPos())));
    }
}
