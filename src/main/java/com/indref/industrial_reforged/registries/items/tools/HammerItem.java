package com.indref.industrial_reforged.registries.items.tools;

import com.indref.industrial_reforged.api.multiblocks.IMultiBlockController;
import com.indref.industrial_reforged.util.MultiblockHelper;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;

public class HammerItem extends ToolItem {
    public HammerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        if (!useOnContext.getLevel().isClientSide() && !useOnContext.getPlayer().isCrouching()){
            if (useOnContext.getLevel().getBlockState(useOnContext.getClickedPos()).getBlock() instanceof IMultiBlockController controller) {
                MultiblockHelper.form(controller.getMultiblock(), useOnContext.getClickedPos(), useOnContext.getLevel(), useOnContext.getPlayer());
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }
}
