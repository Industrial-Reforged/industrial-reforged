package com.indref.industrial_reforged.content.items.storage;

import com.indref.industrial_reforged.api.items.SimpleFluidItem;
import com.indref.industrial_reforged.api.items.container.IFluidItem;
import com.indref.industrial_reforged.content.IRItems;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.SoundAction;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FluidCellItem extends SimpleFluidItem {
    private int capacity;
    private Fluid fluid = Fluids.EMPTY;

    public FluidCellItem(Properties properties, int capacity) {
        super(properties, capacity);
        this.capacity = capacity;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        InteractionResultHolder<ItemStack> ret = ForgeEventFactory.onBucketUse(player, level, stack, hit);

        if (ret != null) {
            return ret;
        } else if (hit.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(stack);
        } else if (hit.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(stack);
        } else if (getFluid() != Fluids.EMPTY) {
            return InteractionResultHolder.fail(stack);
        }

        BlockPos pos = hit.getBlockPos();
        Direction direction = hit.getDirection();
        BlockPos pos1 = pos.relative(direction);

        if (level.mayInteract(player, pos) && player.mayUseItemAt(pos1, direction, stack)) {
            BlockState state = level.getBlockState(pos);
            IFluidHandlerItem cap = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElseThrow(NullPointerException::new);
            if (cap.getFluidInTank(0).isEmpty()) {
                if (state.getBlock() instanceof LiquidBlock liquidBlock) {
                    if (state.getValue(BlockStateProperties.LEVEL) == 0) {
                        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
                    }

                    Fluid fluid = liquidBlock.getFluid();
                    if (fluid != Fluids.EMPTY) {
                        player.awardStat(Stats.ITEM_USED.get(this));
                        SoundEvent soundevent = fluid.getFluidType().getSound(SoundAction.get("fill"));

                        if (soundevent == null) {
                            soundevent = fluid.isSame(Fluids.LAVA) ? SoundEvents.BUCKET_FILL_LAVA : SoundEvents.BUCKET_FILL;
                        }

                        player.playSound(soundevent, 1F, 1F);

                        ItemStack newStack = new ItemStack(this);
                        IFluidHandlerItem newCap = newStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElseThrow(NullPointerException::new);
                        newCap.fill(new FluidStack(fluid, 1000), IFluidHandler.FluidAction.EXECUTE);
                        this.fluid = fluid;

                        if (!level.isClientSide()) {
                            CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, new ItemStack(fluid.getBucket()));
                        }

                        return InteractionResultHolder.sidedSuccess(ItemUtils.createFilledResult(stack, player, newStack), level.isClientSide());
                    }
                }

            } else {
                if (!(state.getBlock() instanceof LiquidBlock) && !cap.getFluidInTank(0).getFluid().getFluidType().isVaporizedOnPlacement(level, pos, cap.getFluidInTank(0))) {
                    level.setBlock(pos1, cap.getFluidInTank(0).getFluid().defaultFluidState().createLegacyBlock(), 11);
                    if (!player.isCreative()) {
                        this.fluid = Fluids.EMPTY;
                        stack.shrink(1);
                        ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(IRItems.FLUID_CELL.get()));
                    }
                    return InteractionResultHolder.success(stack);
                }
            }
        }


        return InteractionResultHolder.fail(stack);
    }

    @Override
    public Fluid getFluid() {
        return this.fluid;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
        itemStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(
                (fluidHandlerItem) -> {
                    if (!fluidHandlerItem.getFluidInTank(0).getFluid().equals(Fluids.EMPTY)) {
                        Component descriptionType = MutableComponent.create(ComponentContents.EMPTY)
                                .append(Component.translatable("item.indref.fluid_cell.desc.stored")
                                        .append(Component.literal(fluidHandlerItem.getFluidInTank(0).getDisplayName().getString())
                                                .withStyle(ChatFormatting.AQUA)));
                        Component descriptionAmount = MutableComponent.create(ComponentContents.EMPTY)
                                .append(Component.translatable("item.indref.fluid_cell.desc.amount"))
                                .append(Component.literal(String.format("%d/%d",
                                        fluidHandlerItem.getFluidInTank(0).getAmount(),
                                        com.indref.industrial_reforged.util.ItemUtils.getFluidItem(itemStack)
                                .getCapacity(itemStack))).withStyle(ChatFormatting.AQUA));
                        tooltip.add(descriptionType);
                        tooltip.add(descriptionAmount);
                    }
                });
    }
}
