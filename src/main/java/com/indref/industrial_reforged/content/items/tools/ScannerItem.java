package com.indref.industrial_reforged.content.items.tools;

import com.indref.industrial_reforged.api.blocks.container.IEnergyBlock;
import com.indref.industrial_reforged.api.items.IToolItem;
import com.indref.industrial_reforged.api.items.SimpleElectricItem;
import com.indref.industrial_reforged.networking.IRPackets;
import com.indref.industrial_reforged.networking.packets.S2CEnergySync;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class ScannerItem extends SimpleElectricItem implements IToolItem {
    public ScannerItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int slotId, boolean isSelected) {
        HitResult hitResult = Minecraft.getInstance().hitResult;
        if (hitResult != null) if (hitResult.getType().equals(HitResult.Type.BLOCK) && isSelected) {
            BlockHitResult blockHitResult = (BlockHitResult) hitResult;
            BlockPos blockPos = blockHitResult.getBlockPos();
            BlockState blockState = level.getBlockState(blockPos);
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof IEnergyBlock energyBlock) {
                IRPackets.sendToServer(new S2CEnergySync(energyBlock.getStored(blockEntity), blockPos));
            }

        }
    }

    @Override
    public int getCapacity() {
        return 10000;
    }
}
