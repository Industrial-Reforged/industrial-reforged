package com.indref.industrial_reforged.registries.items.misc;

import com.indref.industrial_reforged.data.IRDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;

public class BlueprintItem extends Item {
    public static final String HAS_RECIPE_KEY = "blueprint_has_recipe";

    public BlueprintItem(Properties properties) {
        super(properties.component(IRDataComponents.BLUEPRINT_POS, BlockPos.ZERO));
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        context.getItemInHand().set(IRDataComponents.BLUEPRINT_POS, context.getClickedPos());
        return InteractionResult.SUCCESS;
    }
}
