package com.indref.industrial_reforged.registries.items.tools;

import com.indref.industrial_reforged.api.multiblocks.IMultiBlockController;
import com.indref.industrial_reforged.api.multiblocks.IMultiblock;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.multiblocks.CrucibleMultiblock;
import com.indref.industrial_reforged.util.MultiblockHelper;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class HammerItem extends ToolItem {
    public HammerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        BlockState controllerState = useOnContext.getLevel().getBlockState(useOnContext.getClickedPos());
        if (!useOnContext.getLevel().isClientSide() && !useOnContext.getPlayer().isCrouching()){
            if (controllerState.getBlock() instanceof IMultiBlockController controller) {
                MultiblockHelper.form(controller.getMultiblock(), useOnContext.getClickedPos(), useOnContext.getLevel(), useOnContext.getPlayer());
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }
}
