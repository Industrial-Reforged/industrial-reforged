package com.indref.industrial_reforged.content.items.tools;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.registries.IRSoundEvents;
import com.portingdeadmods.portingdeadlibs.PDLRegistries;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.Multiblock;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class HammerItem extends Item {
    public HammerItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {
        BlockState controllerState = useOnContext.getLevel().getBlockState(useOnContext.getClickedPos());
        if (!useOnContext.getPlayer().isCrouching()) {
            for (Multiblock multiblock : PDLRegistries.MULTIBLOCK) {
                if (controllerState.is(multiblock.getUnformedController())) {
                    try {
                        boolean formed = multiblock.form(useOnContext.getLevel(), useOnContext.getClickedPos(), useOnContext.getPlayer());
                        if (formed) {
                            useOnContext.getPlayer().playSound(IRSoundEvents.HAMMERING.get(), 1, 0.85f);
                            return InteractionResult.SUCCESS;
                        }
                        return InteractionResult.FAIL;
                    } catch (Exception e) {
                        IndustrialReforged.LOGGER.error("Encountered err", e);
                    }
                }
            }
        }
        return InteractionResult.FAIL;
    }
}
