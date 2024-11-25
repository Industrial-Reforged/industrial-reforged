package com.indref.industrial_reforged.util;

import com.indref.industrial_reforged.api.fluids.IRFluid;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public final class FluidRegistrationHelper {
    private final List<IRFluid> fluids = new ArrayList<>();

    private final DeferredRegister.Blocks blockRegister;
    private final DeferredRegister.Items itemRegister;
    private final DeferredRegister<FluidType> fluidTypeRegister;
    private final DeferredRegister<Fluid> fluidRegister;

    private boolean createFluidsRegister;

    public FluidRegistrationHelper(DeferredRegister.Blocks blockRegister,
                                   DeferredRegister.Items itemRegister,
                                   DeferredRegister<FluidType> fluidTypeRegister,
                                   DeferredRegister<Fluid> fluidRegister) {
        this.blockRegister = blockRegister;
        this.itemRegister = itemRegister;
        this.fluidTypeRegister = fluidTypeRegister;
        this.fluidRegister = fluidRegister;
    }

    public FluidRegistrationHelper(DeferredRegister.Blocks blockRegister, DeferredRegister.Items itemRegister, String modid) {
        this(blockRegister, itemRegister, DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, modid), DeferredRegister.create(Registries.FLUID, modid));
        this.createFluidsRegister = true;
    }

    public <T extends IRFluid> T registerFluid(T fluid) {

        fluid.stillFluid = getFluidRegister().register(fluid.getName(), fluid.stillFluid);
        fluid.flowingFluid = getFluidRegister().register(flowing(fluid.getName()), fluid.flowingFluid);
        fluid.fluidType = getFluidTypeRegister().register(fluid.getName(), fluid.fluidType);

        fluid.block = () -> new LiquidBlock(fluid.stillFluid.get(), fluid.blockProperties());

        fluid.block = getBlockRegister().register(fluid(fluid.getName()), fluid.block);

        fluid.deferredBucket = getItemRegister().register(bucket(fluid.getName()), () -> new BucketItem(fluid.stillFluid.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

        this.fluids.add(fluid);
        return fluid;
    }

    public DeferredRegister.Blocks getBlockRegister() {
        return blockRegister;
    }

    public DeferredRegister.Items getItemRegister() {
        return itemRegister;
    }

    public DeferredRegister<FluidType> getFluidTypeRegister() {
        return fluidTypeRegister;
    }

    public DeferredRegister<Fluid> getFluidRegister() {
        return fluidRegister;
    }

    public List<IRFluid> getFluids() {
        return fluids;
    }

    public void register(IEventBus modEventBus) {
        if (createFluidsRegister) {
            getFluidRegister().register(modEventBus);
            getFluidTypeRegister().register(modEventBus);
        }
    }

    private static String fluid(String fluid) {
        return fluid + "_fluid";
    }

    private static String flowing(String fluid) {
        return fluid + "_flowing";
    }

    private static String bucket(String fluid) {
        return fluid + "_bucket";
    }
}
