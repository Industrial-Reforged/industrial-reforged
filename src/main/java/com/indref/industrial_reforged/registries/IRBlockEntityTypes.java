package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.registries.blockentities.generators.BasicGeneratorBlockEntity;
import com.indref.industrial_reforged.registries.blockentities.machines.CentrifugeBlockEntity;
import com.indref.industrial_reforged.registries.blockentities.CraftingStationBlockEntity;
import com.indref.industrial_reforged.registries.blockentities.DrainBlockEntity;
import com.indref.industrial_reforged.registries.blockentities.CastingBasinBlockEntity;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.misc.CrucibleWallBlockEntity;
import com.indref.industrial_reforged.registries.blockentities.misc.FaucetBlockEntity;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.controller.BlastFurnaceBlockEntity;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.controller.CrucibleBlockEntity;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.controller.FireboxBlockEntity;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.controller.SmallFireboxBlockEntity;
import com.indref.industrial_reforged.registries.blocks.multiblocks.FaucetBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class IRBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, IndustrialReforged.MODID);
    public static final Supplier<BlockEntityType<FireboxBlockEntity>> FIREBOX =
            BLOCK_ENTITIES.register("firebox", () ->
                    BlockEntityType.Builder.of(FireboxBlockEntity::new,
                            IRBlocks.COIL.get()).build(null));
    public static final Supplier<BlockEntityType<SmallFireboxBlockEntity>> SMALL_FIREBOX =
            BLOCK_ENTITIES.register("small_firebox", () ->
                    BlockEntityType.Builder.of(SmallFireboxBlockEntity::new,
                            IRBlocks.SMALL_FIREBOX_HATCH.get()).build(null));
    public static final Supplier<BlockEntityType<CrucibleBlockEntity>> CRUCIBLE =
            BLOCK_ENTITIES.register("crucible", () ->
                    BlockEntityType.Builder.of(CrucibleBlockEntity::new,
                            IRBlocks.CERAMIC_CRUCIBLE_CONTROLLER.get()).build(null));
    public static final Supplier<BlockEntityType<CrucibleWallBlockEntity>> CRUCIBLE_WALL =
            BLOCK_ENTITIES.register("crucible_wall", () ->
                    BlockEntityType.Builder.of(CrucibleWallBlockEntity::new,
                            IRBlocks.CERAMIC_CRUCIBLE_WALL.get()).build(null));
    public static final Supplier<BlockEntityType<FaucetBlockEntity>> FAUCET =
            BLOCK_ENTITIES.register("faucet", () ->
                    BlockEntityType.Builder.of(FaucetBlockEntity::new, getFaucetBlocks()).build(null));
    public static final Supplier<BlockEntityType<CentrifugeBlockEntity>> CENTRIFUGE =
            BLOCK_ENTITIES.register("centrifuge", () ->
                    BlockEntityType.Builder.of(CentrifugeBlockEntity::new,
                            IRBlocks.CENTRIFUGE.get()).build(null));
    public static final Supplier<BlockEntityType<DrainBlockEntity>> DRAIN =
            BLOCK_ENTITIES.register("drain", () ->
                    BlockEntityType.Builder.of(DrainBlockEntity::new,
                            IRBlocks.DRAIN.get()).build(null));
    public static final Supplier<BlockEntityType<BasicGeneratorBlockEntity>> BASIC_GENERATOR =
            BLOCK_ENTITIES.register("basic_generator", () ->
                    BlockEntityType.Builder.of(BasicGeneratorBlockEntity::new,
                            IRBlocks.BASIC_GENERATOR.get()).build(null));
    public static final Supplier<BlockEntityType<CraftingStationBlockEntity>> CRAFTING_STATION =
            BLOCK_ENTITIES.register("crafting_station", () ->
                    BlockEntityType.Builder.of(CraftingStationBlockEntity::new,
                            IRBlocks.CRAFTING_STATION.get()).build(null));
    public static final Supplier<BlockEntityType<CastingBasinBlockEntity>> CASTING_BASIN =
            BLOCK_ENTITIES.register("casting_basin", () ->
                    BlockEntityType.Builder.of(CastingBasinBlockEntity::new,
                            IRBlocks.CERAMIC_CASTING_BASIN.get()).build(null));
    public static final Supplier<BlockEntityType<BlastFurnaceBlockEntity>> BLAST_FURNACE =
            BLOCK_ENTITIES.register("blast_furnace", () ->
                    BlockEntityType.Builder.of(BlastFurnaceBlockEntity::new,
                            IRBlocks.BLAST_FURNACE_HATCH.get()).build(null));

    private static Block[] getFaucetBlocks() {
        List<Block> blocks = new ArrayList<>();
        for (Block block : BuiltInRegistries.BLOCK) {
            if (block instanceof FaucetBlock) {
                blocks.add(block);
            }
        }
        Block[] blocks1 = new Block[blocks.size()];
        blocks.toArray(blocks1);
        return blocks1;
    }
}
