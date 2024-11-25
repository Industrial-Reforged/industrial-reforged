package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.fluids.IRFluid;
import com.indref.industrial_reforged.api.items.container.IEnergyItem;
import com.indref.industrial_reforged.api.items.container.IFluidItem;
import com.indref.industrial_reforged.content.items.storage.FluidCellItem;
import com.indref.industrial_reforged.content.items.storage.ToolboxItem;
import com.indref.industrial_reforged.content.items.tools.RockCutterItem;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.Vec3i;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.DyedItemColor;
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
                    } else if (item.asItem() instanceof FluidCellItem) {
                        addVariantForAllFluids(output, item);
                    } else if (item.asItem() instanceof ToolboxItem) {
                        addVariantForAllColors(output, item);
                    }else {
                        addItem(output, item);
                    }
                }

                for (IRFluid fluid : IRFluids.HELPER.getFluids()) {
                    DeferredItem<BucketItem> deferredBucket = fluid.getDeferredBucket();
                    IndustrialReforged.LOGGER.debug("Bucket: {}", deferredBucket);
                    output.accept(deferredBucket);
                }
            }).build());

    private static void addVariantForAllColors(CreativeModeTab.Output output, DeferredItem<?> item) {
        for (DyeColor color : DyeColor.values()) {
            ItemStack itemStack = item.toStack();
            int textureDiffuseColor = color.getTextureDiffuseColor();
            Vec3i rgb = new Vec3i((int) Math.min(FastColor.ARGB32.red(textureDiffuseColor) * 1.4, 255),
                    (int) Math.min(FastColor.ARGB32.blue(textureDiffuseColor) * 1.4, 255),
                    (int) Math.min(FastColor.ARGB32.green(textureDiffuseColor) * 1.4, 255));
            itemStack.set(DataComponents.DYED_COLOR, new DyedItemColor(FastColor.ARGB32.color(rgb.getX(), rgb.getY(), rgb.getZ()), false));
            output.accept(itemStack);
        }
    }

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
