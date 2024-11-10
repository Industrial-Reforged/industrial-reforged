package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.items.container.IEnergyItem;
import com.indref.industrial_reforged.api.items.container.IFluidItem;
import com.indref.industrial_reforged.content.items.tools.RockCutterItem;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public final class IRTabs {
    /**
     * Variable used for registering and storing all item groups under the "indref" mod-id
     */
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, IndustrialReforged.MODID);

    /**
     * Default Item Group for all indref items
     */
    public static final Supplier<CreativeModeTab> ITEMS = CREATIVE_TABS.register("items", () -> CreativeModeTab.builder()
            .title(Component.translatable("creative_tab.indref.items"))
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .icon(() -> new ItemStack(IRItems.HAMMER.get()))
            .displayItems((parameters, output) -> {
                for (DeferredItem<?> item : IRItems.TAB_ITEMS) {
                    if (item.asItem() instanceof RockCutterItem) {
                        addRockCutter(output, parameters, item);
                    } else if (item.asItem() instanceof IEnergyItem) {
                        addPoweredItem(output, item);
                    } else {
                        addItem(output, item);
                    }
                }
            }).build());
    public static final Supplier<CreativeModeTab> BLOCKS = CREATIVE_TABS.register("blocks", () -> CreativeModeTab.builder()
            .title(Component.translatable("creative_tab.indref.blocks"))
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .icon(() -> new ItemStack(IRBlocks.BASIC_MACHINE_FRAME.get()))
            .displayItems((parameters, output) -> {
                for (DeferredItem<?> block : IRBlocks.TAB_BLOCKS) {
                    output.accept(block);
                }
            }).build());

    private static void addItem(CreativeModeTab.Output output, DeferredItem<?> item) {
        output.accept(item.get());
    }

    public static void addPoweredItem(CreativeModeTab.Output output, DeferredItem<?> item) {
        output.accept(item.get().getDefaultInstance());
        ItemStack stack = new ItemStack(item.get());
        IEnergyItem energyItem = (IEnergyItem) stack.getItem();
        energyItem.setEnergyStored(stack, energyItem.getEnergyCapacity(stack));

        output.accept(stack);
    }

    public static void addRockCutter(CreativeModeTab.Output output, CreativeModeTab.ItemDisplayParameters parameters, DeferredItem<?> item) {
        ItemStack stack = new ItemStack(item.get());
        Holder.Reference<Enchantment> enchantment = parameters.holders().asGetterLookup().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH);
        stack.enchant(enchantment, 1);

        output.accept(stack);
        ItemStack energyStack = stack.copy();
        IEnergyItem energyItem = (IEnergyItem) energyStack.getItem();
        energyItem.setEnergyStored(energyStack, energyItem.getEnergyCapacity(energyStack));

        output.accept(energyStack);
    }

    public static void addVariantForAllFluids(CreativeModeTab.Output output, DeferredItem<?> item) {
        // Add base item
        output.accept(item.get().getDefaultInstance());
        Set<Map.Entry<ResourceKey<Fluid>, Fluid>> fluids = BuiltInRegistries.FLUID.entrySet();
        for (Map.Entry<ResourceKey<Fluid>, Fluid> fluid : fluids) {
            ItemStack stack = new ItemStack(item.get());
            if (!fluid.getValue().equals(Fluids.EMPTY) && fluid.getValue().isSource(fluid.getValue().defaultFluidState())) {
                if (item.get() instanceof IFluidItem fluidContainerItem)
                    fluidContainerItem.tryFillFluid(fluid.getValue(), 1000, stack);
                output.accept(stack);
            }
        }
    }

    /**
     * Add a new item to a creative tab
     *
     * @param output Specify the creative tab
     * @param block  Specify the item to add
     */
    private static <T extends Block> void addBlock(CreativeModeTab.Output output, DeferredBlock<T> block) {
        output.accept(block.get());
    }
}
