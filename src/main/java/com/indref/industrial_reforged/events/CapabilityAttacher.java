package com.indref.industrial_reforged.events;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.items.container.IEnergyItem;
import com.indref.industrial_reforged.api.items.container.IFluidItem;
import com.indref.industrial_reforged.api.items.container.IHeatItem;
import com.indref.industrial_reforged.capabilities.EnergyWrapper;
import com.indref.industrial_reforged.capabilities.HeatWrapper;
import com.indref.industrial_reforged.capabilities.IRCapabilities;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack;

@Mod.EventBusSubscriber(modid = IndustrialReforged.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class CapabilityAttacher {

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof IEnergyItem)
                event.registerItem(IRCapabilities.EnergyStorage.ITEM, (stack, ctx) -> new EnergyWrapper.Item(stack), item);

            if (item instanceof IHeatItem) {
                event.registerItem(IRCapabilities.HeatStorage.ITEM, (stack, ctx) -> new HeatWrapper.Item(stack), item);
            }

            if (item instanceof IFluidItem fluidItem) {
                event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ctx) -> new FluidHandlerItemStack(stack, fluidItem.getFluidCapacity()), item);
            }
        }
    }
}