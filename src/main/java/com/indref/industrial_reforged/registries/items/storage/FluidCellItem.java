package com.indref.industrial_reforged.registries.items.storage;

import com.indref.industrial_reforged.api.items.SimpleFluidItem;
import com.indref.industrial_reforged.api.items.container.IFluidItem;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.util.ItemUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.Nullable;
import org.openjdk.nashorn.internal.runtime.doubleconv.CachedPowers;

import java.util.List;

import static net.minecraft.world.level.block.LiquidBlock.LEVEL;

public class FluidCellItem extends SimpleFluidItem {
    private final int capacity;
    private FluidStack fluid = FluidStack.EMPTY;

    public FluidCellItem(Properties properties, int capacity) {
        super(properties, capacity);
        this.capacity = capacity;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack handItem = ItemUtils.itemStackFromInteractionHand(interactionHand, player);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(
                level, player, this.fluid.isEmpty() ? ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE
        );
        if (blockhitresult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(handItem);
        } else if (blockhitresult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(handItem);
        } else {
            if (handItem.getItem() instanceof IFluidItem fluidItem) {
                BlockPos blockpos = blockhitresult.getBlockPos();
                Direction direction = blockhitresult.getDirection();
                BlockPos blockpos1 = blockpos.relative(direction);
                Fluid fluid1 = IFluidItem.getFluidHandler(handItem).getFluidInTank(0).getFluid();
                if (fluid1 == Fluids.EMPTY) {
                    BlockState blockstate1 = level.getBlockState(blockpos);
                    Block block1 = blockstate1.getBlock();
                    if (block1 instanceof BucketPickup bucketpickup) {
                        ItemStack itemstack2 = getFilledResult(level, blockpos, blockstate1);
                        if (!itemstack2.isEmpty()) {
                            bucketpickup.getPickupSound(blockstate1).ifPresent(p_150709_ -> player.playSound(p_150709_, 1.0F, 1.0F));
                            ItemStack itemstack1 = net.minecraft.world.item.ItemUtils.createFilledResult(handItem, player, itemstack2);

                            return InteractionResultHolder.success(itemstack1);
                        }
                    }
                    return InteractionResultHolder.fail(handItem);
                } else {
                    if (!(level.getBlockState(blockpos).getBlock() instanceof LiquidBlock)
                            && !fluid1.getFluidType().isVaporizedOnPlacement(level, blockpos, IFluidItem.getFluidHandler(handItem).getFluidInTank(0))) {
                        level.setBlock(blockpos1, fluid1.defaultFluidState().createLegacyBlock(), 11);
                        return InteractionResultHolder.success(handItem);
                    }
                }
            }
        }
        return InteractionResultHolder.fail(handItem);
    }

    public ItemStack getFilledResult(LevelAccessor level, BlockPos blockPos, BlockState blockState) {
        if (blockState.getValue(LEVEL) == 0) {
            if (blockState.getBlock() instanceof LiquidBlock liquidBlock) {
                level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 11);
                ItemStack stack = new ItemStack(IRItems.FLUID_CELL.get());
                IFluidItem fluidItem = ItemUtils.getFluidItem(stack);
                fluidItem.tryFillFluid(liquidBlock.getFluid().getSource(), 1000, stack);
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public FluidStack getFluid() {
        return this.fluid;
    }

    @Override
    public int getFluidCapacity() {
        return this.capacity;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
        IFluidHandlerItem item = itemStack.getCapability(Capabilities.FluidHandler.ITEM);
        if (!item.getFluidInTank(0).getFluid().equals(Fluids.EMPTY)) {
            Component descriptionType = Component.translatable("fluid_cell.desc.stored")
                    .append(Component.literal(item.getFluidInTank(0).getDisplayName().getString())
                            .withStyle(ChatFormatting.AQUA));
            Component descriptionAmount = Component.translatable("fluid_cell.desc.amount")
                    .append(Component.literal(String.format("%d/%d",
                            item.getFluidInTank(0).getAmount(),
                            com.indref.industrial_reforged.util.ItemUtils.getFluidItem(itemStack)
                                    .getFluidCapacity())).withStyle(ChatFormatting.AQUA));
            tooltip.add(descriptionType);
            tooltip.add(descriptionAmount);
        }
    }
}
