package com.indref.industrial_reforged.datagen.data;

import com.indref.industrial_reforged.content.multiblocks.CrucibleMultiblock;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRItems;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class IRBlockLootTableProvider extends BlockLootSubProvider {
    private final Set<Block> knownBlocks = new ReferenceOpenHashSet<>();

    public IRBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Collections.emptySet(), FeatureFlags.VANILLA_SET, registries);
    }

    @Override
    protected void generate() {
        for (DeferredBlock<?> block : IRBlocks.DROP_SELF_BLOCKS) {
            dropSelf(block.get());
        }

        for (Map.Entry<DeferredBlock<Block>, DeferredItem<?>> entry : IRBlocks.ORES.entrySet()) {
            add(entry.getKey().get(), block -> createOreDrop(block, entry.getValue().asItem()));
        }

        add(IRBlocks.RUBBER_TREE_LEAVES.get(), block -> createLeavesDrops(block, IRBlocks.RUBBER_TREE_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
        add(IRBlocks.RUBBER_TREE_DOOR.get(), this::createDoorTable);

        dropOther(IRBlocks.CERAMIC_CRUCIBLE_CONTROLLER.get(), IRBlocks.TERRACOTTA_BRICK_SLAB);
        add(IRBlocks.CERAMIC_CRUCIBLE_PART.get(), block -> createStateDrop(block, CrucibleMultiblock.CRUCIBLE_WALL, Map.of(
                CrucibleMultiblock.WallStates.FENCE, IRBlocks.IRON_FENCE.get(),
                CrucibleMultiblock.WallStates.EDGE_BOTTOM, IRBlocks.TERRACOTTA_BRICKS.get(),
                CrucibleMultiblock.WallStates.EDGE_TOP, IRBlocks.TERRACOTTA_BRICKS.get(),
                CrucibleMultiblock.WallStates.WALL_BOTTOM, IRBlocks.TERRACOTTA_BRICKS.get(),
                CrucibleMultiblock.WallStates.WALL_TOP, IRBlocks.TERRACOTTA_BRICKS.get()
        )));

        dropOther(IRBlocks.BLAST_FURNACE_PART.get(), IRBlocks.BLAST_FURNACE_BRICKS);

        dropOther(IRBlocks.FIREBOX_CONTROLLER.get(), IRBlocks.COIL);
        dropOther(IRBlocks.FIREBOX_PART.get(), IRBlocks.REFRACTORY_BRICK);

        add(IRBlocks.RUBBER_TREE_RESIN_HOLE.get(), createResinLogTable(IRBlocks.RUBBER_TREE_LOG.get(), IRItems.STICKY_RESIN.get()));
    }

    private LootTable.Builder createResinLogTable(ItemLike logBlock, ItemLike resinItem) {
        return LootTable.lootTable()
                .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1F))
                                .add(LootItem.lootTableItem(logBlock))
                ).withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1F))
                                .add(LootItem.lootTableItem(resinItem))
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0F, 1F)))
                );
    }

    protected <T extends Comparable<T> & StringRepresentable> LootTable.Builder createStateDrop(
            Block block, Property<T> property, Map<T, Block> drops
    ) {
        LootPool.Builder builder = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0F));

        for (Map.Entry<T, Block> entry : drops.entrySet()) {
            builder.add(LootItem.lootTableItem(entry.getValue())
                    .when(
                            LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                    .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(property, entry.getKey()))
                    ));
        }

        return LootTable.lootTable()
                .withPool(
                        this.applyExplosionCondition(
                                block,
                                builder
                        )
                );
    }

    @Override
    public @NotNull Set<Block> getKnownBlocks() {
        return knownBlocks;
    }

    @Override
    protected void add(@NotNull Block block, @NotNull LootTable.Builder table) {
        super.add(block, table);
        knownBlocks.add(block);
    }
}