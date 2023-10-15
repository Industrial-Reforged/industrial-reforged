package com.indref.industrial_reforged.util;

import com.indref.industrial_reforged.api.items.container.IEnergyContainerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nullable;
import java.util.Optional;

public class ItemUtils {
    // The corresponding rgb value: rgb(77,166,255)
    public static final int ENERGY_BAR_COLOR = 0x4DA6FF;
    public static int energyForDurabilityBar(ItemStack itemStack) {
        return Math.round(13.0F - ((1 - getChargeRatio(itemStack)) * 13.0F));
    }

    public static IEnergyContainerItem getEnergyItem(ItemStack itemStack) {
        if (itemStack.getItem() instanceof IEnergyContainerItem energyItem) {
            return energyItem;
        }
        return null;
    }

    public static float getChargeRatio(ItemStack stack) {
        IEnergyContainerItem energyItem = getEnergyItem(stack);
        return (float) energyItem.getStored(stack) / energyItem.getCapacity(stack);
    }

    public static ItemStack itemStackFromInteractionHand(InteractionHand interactionHand, Player player) {
        switch (interactionHand) {
            case MAIN_HAND -> {
                return player.getMainHandItem();
            }
            case OFF_HAND -> {
                return player.getOffhandItem();
            }
        }
        return ItemStack.EMPTY;
    }

    public static boolean emptyContents(Fluid fluid, @Nullable Player player, Level level, BlockPos blockPos, @Nullable BlockHitResult blockHitResult, @Nullable ItemStack container) {
        if (!(fluid instanceof FlowingFluid)) {
            return false;
        } else {
            BlockState blockstate = level.getBlockState(blockPos);
            Block block = blockstate.getBlock();
            boolean flag = blockstate.canBeReplaced(fluid);
            boolean flag1 = blockstate.isAir() || flag || block instanceof LiquidBlockContainer && ((LiquidBlockContainer)block).canPlaceLiquid(level, blockPos, blockstate, fluid);
            Optional<FluidStack> containedFluidStack = Optional.ofNullable(container).flatMap(FluidUtil::getFluidContained);
            if (!flag1) {
                return blockHitResult != null && emptyContents(fluid, player, level, blockHitResult.getBlockPos().relative(blockHitResult.getDirection()), (BlockHitResult)null, container);
            } else if (containedFluidStack.isPresent() && fluid.getFluidType().isVaporizedOnPlacement(level, blockPos, containedFluidStack.get())) {
                fluid.getFluidType().onVaporize(player, level, blockPos, containedFluidStack.get());
                return true;
            } else if (level.dimensionType().ultraWarm() && fluid.is(FluidTags.WATER)) {
                int i = blockPos.getX();
                int j = blockPos.getY();
                int k = blockPos.getZ();
                level.playSound(player, blockPos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (level.random.nextFloat() - level.random.nextFloat()) * 0.8F);

                for(int l = 0; l < 8; ++l) {
                    level.addParticle(ParticleTypes.LARGE_SMOKE, (double)i + Math.random(), (double)j + Math.random(), (double)k + Math.random(), 0.0D, 0.0D, 0.0D);
                }

                return true;
            } else if (block instanceof LiquidBlockContainer && ((LiquidBlockContainer)block).canPlaceLiquid(level,blockPos,blockstate,fluid)) {
                ((LiquidBlockContainer)block).placeLiquid(level, blockPos, blockstate, ((FlowingFluid)fluid).getSource(false));
                playEmptySound(fluid, player, level, blockPos);
                return true;
            } else {
                if (!level.isClientSide && flag && !blockstate.liquid()) {
                    level.destroyBlock(blockPos, true);
                }

                if (!level.setBlock(blockPos, fluid.defaultFluidState().createLegacyBlock(), 11) && !blockstate.getFluidState().isSource()) {
                    return false;
                } else {
                    playEmptySound(fluid, player, level, blockPos);
                    return true;
                }
            }
        }
    }

    private static void playEmptySound(Fluid fluid, @Nullable Player p_40696_, LevelAccessor p_40697_, BlockPos p_40698_) {
        SoundEvent soundevent = fluid.getFluidType().getSound(p_40696_, p_40697_, p_40698_, net.minecraftforge.common.SoundActions.BUCKET_EMPTY);
        if(soundevent == null) soundevent = fluid.is(FluidTags.LAVA) ? SoundEvents.BUCKET_EMPTY_LAVA : SoundEvents.BUCKET_EMPTY;
        p_40697_.playSound(p_40696_, p_40698_, soundevent, SoundSource.BLOCKS, 1.0F, 1.0F);
        p_40697_.gameEvent(p_40696_, GameEvent.FLUID_PLACE, p_40698_);
    }

}
