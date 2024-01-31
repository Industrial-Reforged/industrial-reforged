package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.registries.blockentities.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class IRBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, IndustrialReforged.MODID);
    public static final Supplier<BlockEntityType<FireboxBlockEntity>> FIREBOX =
            BLOCK_ENTITIES.register("firebox", () ->
                    BlockEntityType.Builder.of(FireboxBlockEntity::new,
                            IRBlocks.COIL.get()).build(null));
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
                    BlockEntityType.Builder.of(FaucetBlockEntity::new,
                            IRBlocks.CERAMIC_FAUCET.get()).build(null));
    public static final Supplier<BlockEntityType<TestGeneratorBE>> TEST_GEN =
            BLOCK_ENTITIES.register("test_gen", () ->
                    BlockEntityType.Builder.of(TestGeneratorBE::new,
                            IRBlocks.TEST_GENERATOR.get()).build(null));
    public static final Supplier<BlockEntityType<TestBlockEntity>> TEST_BLOCK =
            BLOCK_ENTITIES.register("test_block", () ->
                    BlockEntityType.Builder.of(TestBlockEntity::new,
                            IRBlocks.BASIC_MACHINE_FRAME.get()).build(null));
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
    public static final Supplier<BlockEntityType<CastingTableBlockEntity>> CASTING_TABLE =
            BLOCK_ENTITIES.register("casting_table", () ->
                    BlockEntityType.Builder.of(CastingTableBlockEntity::new,
                            IRBlocks.CERAMIC_CASTING_TABLE.get()).build(null));
}
