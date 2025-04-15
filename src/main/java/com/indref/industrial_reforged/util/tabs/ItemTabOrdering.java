package com.indref.industrial_reforged.util.tabs;

import com.indref.industrial_reforged.IRConfig;
import com.indref.industrial_reforged.content.items.storage.ToolboxItem;
import com.indref.industrial_reforged.data.IRDataComponents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import org.apache.commons.lang3.function.TriConsumer;

import java.util.function.BiConsumer;

public enum ItemTabOrdering implements TabOrdering {
    PRIMITIVE_TOOLS,
    REGULAR_ARMOR,
    PRIMITIVE_COMPONENTS,
    MISC_ITEMS,
    CASTING_MOLDS,
    BASIC_ELECTRIC_TOOLS,
    ELECTRIC_COMPONENTS,
    CIRCUITS,
    BATTERIES,
    RAW_ORES,
    INGOTS,
    DUSTS,
    PLATES,
    WIRES,
    RODS,
    FLUID_CELLS(ItemTabOrdering::addFluidCells),
    TOOL_BOX(ItemTabOrdering::addToolBoxes),
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
        return null;
    }

    public static TabPosition noPosition() {
        return NONE.withPosition(-1);
    }

    private static void addToolBoxes(CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output, ItemLike item) {
        for (DyeColor color : DyeColor.values()) {
            ItemStack stack = item.asItem().getDefaultInstance();
            stack.set(DataComponents.DYED_COLOR, new DyedItemColor(color.getTextColor(), true));
            output.accept(stack);
        }
    }

    private static void addFluidCells(CreativeModeTab.ItemDisplayParameters params, CreativeModeTab.Output output, ItemLike item) {
        output.accept(item);
        for (Fluid fluid : BuiltInRegistries.FLUID) {
            ItemStack stack = item.asItem().getDefaultInstance();
            stack.set(IRDataComponents.FLUID, SimpleFluidContent.copyOf(new FluidStack(fluid, IRConfig.fluidCellCapacity)));
            output.accept(stack);
        }
    }

}
