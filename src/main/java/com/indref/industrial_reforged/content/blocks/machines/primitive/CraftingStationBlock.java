package com.indref.industrial_reforged.content.blocks.machines.primitive;

import com.indref.industrial_reforged.api.blockentities.IRContainerBlockEntity;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.mojang.serialization.MapCodec;
import com.portingdeadmods.portingdeadlibs.api.blocks.RotatableContainerBlock;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;

public class CraftingStationBlock extends RotatableContainerBlock {
    public CraftingStationBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean tickingEnabled() {
        return false;
    }

    @Override
    public BlockEntityType<? extends IRContainerBlockEntity> getBlockEntityType() {
        return IRBlockEntityTypes.CRAFTING_STATION.get();
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(CraftingStationBlock::new);
    }
}
