package com.indref.industrial_reforged.registries.items.tools;

import com.indref.industrial_reforged.api.items.ElectricDiggerItem;
import com.indref.industrial_reforged.tags.IRTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.neoforged.neoforge.common.ToolAction;
import net.neoforged.neoforge.common.ToolActions;

public class ElectricDrillItem extends ElectricDiggerItem {
    public ElectricDrillItem(float p_204108_, float p_204109_, Tier tier, Properties p_204112_) {
        super(p_204108_, p_204109_, tier, IRTags.Blocks.MINEABLE_WITH_DRILL, p_204112_);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return ToolActions.DEFAULT_PICKAXE_ACTIONS.contains(toolAction) || ToolActions.DEFAULT_SHOVEL_ACTIONS.contains(toolAction);
    }
}
