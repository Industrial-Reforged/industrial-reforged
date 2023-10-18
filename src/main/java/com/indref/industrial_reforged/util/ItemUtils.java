package com.indref.industrial_reforged.util;

import com.indref.industrial_reforged.api.items.container.IContainerItem;
import com.indref.industrial_reforged.api.items.container.IEnergyItem;
import com.indref.industrial_reforged.api.items.container.IFluidItem;
import com.indref.industrial_reforged.api.items.container.IHeatItem;
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
    public static final int HEAT_BAR_COLOR = 0xFF8000;

    public enum ChargeType {
        HEAT,
        FLUID,
        ENERGY,
    }

    public static int energyForDurabilityBar(ItemStack itemStack) {
        return Math.round(13.0F - ((1 - getChargeRatio(ChargeType.ENERGY, itemStack)) * 13.0F));
    }

    public static int heatForDurabilityBar(ItemStack itemStack) {
        return Math.round(13.0F - ((1 - getChargeRatio(ChargeType.HEAT, itemStack)) * 13.0F));
    }


    public static int fluidForDurabilityBar(ItemStack itemStack) {
        return Math.round(13.0F - ((1 - getChargeRatio(ChargeType.FLUID, itemStack)) * 13.0F));
    }

    public static IEnergyItem getEnergyItem(ItemStack itemStack) {
        if (itemStack.getItem() instanceof IEnergyItem energyItem) {
            return energyItem;
        }
        return null;
    }

    public static IHeatItem getHeatItem(ItemStack itemStack) {
        if (itemStack.getItem() instanceof IHeatItem heatItem) {
            return heatItem;
        }
        return null;
    }

    public static IFluidItem getFluidItem(ItemStack itemStack) {
        if (itemStack.getItem() instanceof IFluidItem fluidItem) {
            return fluidItem;
        }
        return null;
    }

    public static float getChargeRatio(ChargeType type, ItemStack stack) {
        IContainerItem containerItem = null;
        switch (type) {
            case HEAT -> containerItem = getHeatItem(stack);
            case FLUID -> containerItem = getFluidItem(stack);
            case ENERGY -> containerItem = getEnergyItem(stack);
        }
        assert containerItem != null;
        return (float) containerItem.getStored(stack) / containerItem.getCapacity(stack);
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

}
