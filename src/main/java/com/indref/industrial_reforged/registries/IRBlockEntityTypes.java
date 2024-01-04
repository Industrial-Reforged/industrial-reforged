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
                    BlockEntityType.Builder.of(CrucibleBlockEntity::new, IRBlocks.CERAMIC_CRUCIBLE_CONTROLLER.get()).build(null));
    public static final Supplier<BlockEntityType<TestGeneratorBE>> TEST_GEN =
            BLOCK_ENTITIES.register("test_gen", () ->
                    BlockEntityType.Builder.of(TestGeneratorBE::new, IRBlocks.TEST_GENERATOR.get()).build(null));
    public static final Supplier<BlockEntityType<TestBlockEntity>> TEST_BLOCK =
            BLOCK_ENTITIES.register("test_block", () ->
                    BlockEntityType.Builder.of(TestBlockEntity::new, IRBlocks.BASIC_MACHINE_FRAME.get()).build(null));
    public static final Supplier<BlockEntityType<CentrifugeBlockEntity>> CENTRIFUGE =
            BLOCK_ENTITIES.register("centrifuge", () ->
                    BlockEntityType.Builder.of(CentrifugeBlockEntity::new, IRBlocks.CENTRIFUGE.get()).build(null));
    public static final Supplier<BlockEntityType<DrainBlockEntity>> DRAIN =
            BLOCK_ENTITIES.register("drain", () ->
                    BlockEntityType.Builder.of(DrainBlockEntity::new, IRBlocks.DRAIN.get()).build(null));
}
