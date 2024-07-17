package com.indref.industrial_reforged.registries.items.tools;

import com.indref.industrial_reforged.api.multiblocks.Multiblock;
import com.indref.industrial_reforged.registries.IRRegistries;
import com.indref.industrial_reforged.util.MultiblockHelper;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class HammerItem extends ToolItem {
    public HammerItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {
        BlockState controllerState = useOnContext.getLevel().getBlockState(useOnContext.getClickedPos());
        if (!useOnContext.getPlayer().isCrouching()){
            for (Multiblock multiblock : IRRegistries.MULTIBLOCK) {
                if (controllerState.is(multiblock.getController())) {
                    MultiblockHelper.form(multiblock, useOnContext.getClickedPos(), useOnContext.getLevel(), useOnContext.getPlayer());
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.FAIL;
    }
}
