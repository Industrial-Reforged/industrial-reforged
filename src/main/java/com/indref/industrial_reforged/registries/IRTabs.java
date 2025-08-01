package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.translations.IRTranslations;
import com.indref.industrial_reforged.util.tabs.TabOrdering;
import com.portingdeadmods.portingdeadlibs.api.fluids.PDLFluid;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class IRTabs {
    /**
     * Variable used for registering and storing all item groups under the "indref" mod-id
     */
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, IndustrialReforged.MODID);

    /**
     * Default Item Group for all indref items
     */
    public static final Supplier<CreativeModeTab> ITEMS = CREATIVE_TABS.register("items", () -> CreativeModeTab.builder()
            .title(IRTranslations.Tabs.ITEMS.component())
            .icon(() -> new ItemStack(IRItems.WRENCH.get()))
            .displayItems((parameters, output) -> {
                Map<TabOrdering, Map<Integer, DeferredItem<?>>> sortedItems = IRItems.TAB_ITEMS.entrySet()
                        .stream()
                        .filter(e -> !e.getKey().isNone())
                        .sorted(Comparator.comparingInt(entry -> entry.getKey().getPriority()))
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> e.getValue().entrySet()
                                        .stream()
                                        .sorted(Map.Entry.comparingByKey())
                                        .collect(Collectors.toMap(
                                                Map.Entry::getKey,
                                                Map.Entry::getValue,
                                                (a, b) -> a,
                                                LinkedHashMap::new
                                        )),
                                (a, b) -> a,
                                LinkedHashMap::new
                        ));
                for (Map.Entry<TabOrdering, Map<Integer, DeferredItem<?>>> entry : sortedItems.entrySet()) {
                    TabOrdering ordering = entry.getKey();
                    for (DeferredItem<?> item : entry.getValue().values()) {
                        ordering.tabAppendFunction().accept(parameters, output, item);
                    }
                }

                for (PDLFluid fluid : IRFluids.HELPER.getFluids()) {
                    DeferredItem<BucketItem> deferredBucket = fluid.getDeferredBucket();
                    IndustrialReforged.LOGGER.debug("Bucket: {}", deferredBucket);
                    output.accept(deferredBucket);
                }
            }).build());

    public static final Supplier<CreativeModeTab> BLOCKS = CREATIVE_TABS.register("blocks", () -> CreativeModeTab.builder()
            .title(IRTranslations.Tabs.BLOCKS.component())
            .icon(() -> new ItemStack(IRBlocks.BASIC_MACHINE_FRAME.get()))
            .displayItems((parameters, output) -> {
                Map<TabOrdering, Map<Integer, Supplier<? extends Block>>> sortedItems = IRBlocks.TAB_BLOCKS.entrySet()
                        .stream()
                        .filter(e -> !e.getKey().isNone())
                        .sorted(Comparator.comparingInt(entry -> entry.getKey().getPriority()))
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> e.getValue().entrySet()
                                        .stream()
                                        .sorted(Map.Entry.comparingByKey())
                                        .collect(Collectors.toMap(
                                                Map.Entry::getKey,
                                                Map.Entry::getValue,
                                                (a, b) -> a,
                                                LinkedHashMap::new
                                        )),
                                (a, b) -> a,
                                LinkedHashMap::new
                        ));
                for (Map.Entry<TabOrdering, Map<Integer, Supplier<? extends Block>>> entry : sortedItems.entrySet()) {
                    TabOrdering ordering = entry.getKey();
                    for (Supplier<? extends Block> block : entry.getValue().values()) {
                        if (!ordering.isNone()) {
                            ordering.tabAppendFunction().accept(parameters, output, block.get().asItem());
                        }
                    }
                }
            }).build());
}
