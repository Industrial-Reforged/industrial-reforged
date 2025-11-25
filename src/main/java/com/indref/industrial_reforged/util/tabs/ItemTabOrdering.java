package com.indref.industrial_reforged.util.tabs;

import com.indref.industrial_reforged.IRConfig;
import com.indref.industrial_reforged.capabilites.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.EnergyHandler;
import com.indref.industrial_reforged.content.items.tools.RockCutterItem;
import com.indref.industrial_reforged.data.IRDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import org.apache.commons.lang3.function.TriConsumer;

public enum ItemTabOrdering implements TabOrdering {
    PRIMITIVE_TOOLS,
    REGULAR_ARMOR,
    BASIC_ELECTRIC_TOOLS(ItemTabOrdering::addEnergyToolItem),
    BASIC_ELECTRIC_ARMOR(ItemTabOrdering::addEnergyItem),
    PRIMITIVE_COMPONENTS,
    MISC_ITEMS,
    ELECTRIC_COMPONENTS,
    CIRCUITS,
    BATTERIES(ItemTabOrdering::addEnergyItem),
    UPGRADES,
    CASTING_MOLDS,
    RAW_ORES,
    INGOTS,
    DUSTS,
    PLATES,
    WIRES,
    RODS,
    FLUID_CELLS(ItemTabOrdering::addFluidCellItems),
    TOOL_BOX(ItemTabOrdering::addToolBoxItems),
    NONE(-1);

    private final int priority;
    private final TriConsumer<CreativeModeTab.ItemDisplayParameters, CreativeModeTab.Output, ItemLike> tabAppendFunction;

    ItemTabOrdering(int priority, TriConsumer<CreativeModeTab.ItemDisplayParameters, CreativeModeTab.Output, ItemLike> tabAppendFunction) {
        this.priority = priority;
        this.tabAppendFunction = tabAppendFunction;
    }

    ItemTabOrdering(int priority) {
        this.priority = priority;
        this.tabAppendFunction = (params, output, item) -> output.accept(item);
    }

    ItemTabOrdering(TriConsumer<CreativeModeTab.ItemDisplayParameters, CreativeModeTab.Output, ItemLike> tabAppendFunction) {
        this.priority = ordinal();
        this.tabAppendFunction = tabAppendFunction;
    }

    ItemTabOrdering() {
        this.priority = ordinal();
        this.tabAppendFunction = (params, output, item) -> output.accept(item);
    }

    @Override
    public boolean isNone() {
        return this == NONE;
    }

    @Override
    public int getPriority() {
        return this.priority;
    }

    @Override
    public TriConsumer<CreativeModeTab.ItemDisplayParameters, CreativeModeTab.Output, ItemLike> tabAppendFunction() {
        return this.tabAppendFunction;
    }

    public static TabPosition noPosition() {
        return NONE.withPosition(-1);
    }

    private static void addEnergyToolItem(CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output, ItemLike item) {
        if (item.asItem() instanceof RockCutterItem) {
            addRockCutterItem(params, output, item);
        } else {
            addEnergyItem(params, output, item);
        }
    }

    private static void addRockCutterItem(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output, ItemLike item) {
        ItemStack stack = new ItemStack(item);
        Holder<Enchantment> enchantment = parameters.holders().holderOrThrow(Enchantments.SILK_TOUCH);
        stack.enchant(enchantment, 1);

        output.accept(stack);
        ItemStack energyStack = stack.copy();
        EnergyHandler energyStorage = energyStack.getCapability(IRCapabilities.ENERGY_ITEM);
        energyStorage.setEnergyStored(energyStorage.getEnergyCapacity());

        output.accept(energyStack);
    }

    private static void addEnergyItem(CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output, ItemLike item) {
        ItemStack stack = new ItemStack(item.asItem());
        output.accept(stack.copy());

        EnergyHandler energyStorage = stack.getCapability(IRCapabilities.ENERGY_ITEM);
        if (energyStorage != null) {
            energyStorage.setEnergyStored(energyStorage.getEnergyCapacity());
            output.accept(stack);
        }

    }

    private static void addFluidCellItems(CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output, ItemLike item) {
        output.accept(item);
        for (Fluid fluid : BuiltInRegistries.FLUID) {
            if (!fluid.equals(Fluids.EMPTY) && fluid.defaultFluidState().isSource()) {
                ItemStack stack = item.asItem().getDefaultInstance();
                stack.set(IRDataComponents.FLUID, SimpleFluidContent.copyOf(new FluidStack(fluid, IRConfig.fluidCellCapacity)));
                output.accept(stack);
            }
        }
    }

    private static void addToolBoxItems(CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output, ItemLike item) {
        for (DyeColor color : DyeColor.values()) {
            ItemStack itemStack = item.asItem().getDefaultInstance();
            int textureDiffuseColor = color.getTextureDiffuseColor();
            Vec3i rgb = new Vec3i((int) Math.min(FastColor.ARGB32.red(textureDiffuseColor) * 1.4, 255),
                    (int) Math.min(FastColor.ARGB32.blue(textureDiffuseColor) * 1.4, 255),
                    (int) Math.min(FastColor.ARGB32.green(textureDiffuseColor) * 1.4, 255));
            itemStack.set(DataComponents.DYED_COLOR, new DyedItemColor(FastColor.ARGB32.color(rgb.getX(), rgb.getY(), rgb.getZ()), true));
            output.accept(itemStack);
        }
    }

}
