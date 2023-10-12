package com.indref.industrial_reforged.content.blockentities;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.content.IRBlocks;
import com.indref.industrial_reforged.content.blockentities.impl.EnergyTestBE;
import com.indref.industrial_reforged.content.blockentities.impl.PrimitiveForgeBE;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class IRBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, IndustrialReforged.MODID);

    public static final RegistryObject<BlockEntityType<EnergyTestBE>> ENERGY_TEST =
            BLOCK_ENTITIES.register("energy_test_be", () ->
                    BlockEntityType.Builder.of(EnergyTestBE::new,
                            IRBlocks.TEST_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<PrimitiveForgeBE>> PRIMITIVE_FORGE =
            BLOCK_ENTITIES.register("primitive_forge", () ->
                    BlockEntityType.Builder.of(PrimitiveForgeBE::new,
                            IRBlocks.PRIMITIVE_FORGE.get()).build(null));

}
