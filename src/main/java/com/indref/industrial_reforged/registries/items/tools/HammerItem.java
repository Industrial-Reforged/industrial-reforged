package com.indref.industrial_reforged.registries.items.tools;

import com.indref.industrial_reforged.api.multiblocks.IMultiBlockController;
import com.indref.industrial_reforged.util.MultiblockHelper;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.attachment.AttachmentHolder;
import net.neoforged.neoforge.attachment.AttachmentInternals;
import net.neoforged.neoforge.attachment.AttachmentType;

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
