package com.indref.industrial_reforged.content;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.blocks.transfer.CableBlock;
import com.indref.industrial_reforged.api.blocks.transfer.CableBlockEntity;
import com.indref.industrial_reforged.content.blockentities.EnergyTestBE;
import com.indref.industrial_reforged.content.blockentities.HeatTestBE;
import com.indref.industrial_reforged.content.blockentities.PrimitiveForgeBE;
import com.indref.industrial_reforged.content.blockentities.SimplePressBE;
import com.indref.industrial_reforged.test.energy.TestGeneratorBE;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;

public class IRBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, IndustrialReforged.MODID);

    public static final RegistryObject<BlockEntityType<EnergyTestBE>> ENERGY_TEST =
            BLOCK_ENTITIES.register("energy_test_be", () ->
                    BlockEntityType.Builder.of(EnergyTestBE::new,
                            IRBlocks.TEST_BLOCK_ENERGY.get()).build(null));

    public static final RegistryObject<BlockEntityType<HeatTestBE>> HEAT_TEST =
            BLOCK_ENTITIES.register("heat_test_be", () ->
                    BlockEntityType.Builder.of(HeatTestBE::new,
                            IRBlocks.TEST_BLOCK_HEAT.get()).build(null));

    public static final RegistryObject<BlockEntityType<PrimitiveForgeBE>> PRIMITIVE_FORGE =
            BLOCK_ENTITIES.register("primitive_forge", () ->
                    BlockEntityType.Builder.of(PrimitiveForgeBE::new,
                            IRBlocks.PRIMITIVE_FORGE.get()).build(null));

    public static final RegistryObject<BlockEntityType<SimplePressBE>> SIMPLE_PRESS =
            BLOCK_ENTITIES.register("simple_press", () ->
                    BlockEntityType.Builder.of(SimplePressBE::new,
                            IRBlocks.PRIMITIVE_FORGE.get()).build(null));

    public static final RegistryObject<BlockEntityType<TestGeneratorBE>> TEST_GENERATOR =
            BLOCK_ENTITIES.register("test_generator", () ->
                    BlockEntityType.Builder.of(TestGeneratorBE::new,
                            IRBlocks.TEST_GENERATOR.get()).build(null));
    public static final RegistryObject<BlockEntityType<CableBlockEntity>> CABLE =
            BLOCK_ENTITIES.register("cable", () ->
                    BlockEntityType.Builder.of(CableBlockEntity::new,
                            IRBlocks.TIN_CABLE.get()).build(null));
}
