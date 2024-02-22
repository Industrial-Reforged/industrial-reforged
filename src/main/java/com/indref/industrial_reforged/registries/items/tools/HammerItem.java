package com.indref.industrial_reforged.registries.items.tools;

import com.indref.industrial_reforged.api.multiblocks.MultiBlockController;
import com.indref.industrial_reforged.util.MultiblockUtils;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;

public class HammerItem extends ToolItem {
    public HammerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        BlockState controllerState = useOnContext.getLevel().getBlockState(useOnContext.getClickedPos());
        if (!useOnContext.getLevel().isClientSide() && !useOnContext.getPlayer().isCrouching()){
            if (controllerState.getBlock() instanceof MultiBlockController controller) {
                MultiblockUtils.form(controller.getMultiblock(), useOnContext.getClickedPos(), useOnContext.getLevel(), useOnContext.getPlayer());
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }
}
