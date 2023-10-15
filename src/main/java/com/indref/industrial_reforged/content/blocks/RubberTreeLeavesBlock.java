package com.indref.industrial_reforged.content.blocks;

import java.util.ArrayList;
import java.util.List;

import com.indref.industrial_reforged.content.IRBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams.Builder;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class RubberTreeLeavesBlock extends LeavesBlock {

	public RubberTreeLeavesBlock() {
		super(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES));
	}

    @Override
    public boolean isFlammable(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return true;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return 30;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return 60;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, Builder builder) {
        if (getLootTable() != BuiltInLootTables.EMPTY)
          return super.getDrops(state, builder); 
        List<ItemStack> drops = new ArrayList<>();
        ItemStack stack = builder.getOptionalParameter(LootContextParams.TOOL);
        if (stack.getItem() == Items.SHEARS || EnchantmentHelper.getTagEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0)
          drops.add(new ItemStack(this)); 
        if ((builder.getLevel()).random.nextInt(35) == 0)
          drops.add(new ItemStack(IRBlocks.RUBBER_TREE_SAPLING.get())); 
        return drops;
      }
}
