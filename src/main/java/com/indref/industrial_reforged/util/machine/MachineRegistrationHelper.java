package com.indref.industrial_reforged.util.machine;

import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.util.tabs.BlockTabOrdering;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

public final class MachineRegistrationHelper {
    private final List<IRMachine> machines;
    private final DeferredRegister<Block> blockRegister;
    private final DeferredRegister<Item> itemRegister;
    private final DeferredRegister<BlockEntityType<?>> blockEntityRegister;
    private final DeferredRegister<MenuType<?>> menuTypeRegister;

    public MachineRegistrationHelper(DeferredRegister<Block> blockRegister, DeferredRegister<Item> itemRegister, DeferredRegister<BlockEntityType<?>> blockEntityRegister, DeferredRegister<MenuType<?>> menuTypeRegister) {
        this.machines = new ArrayList<>();
        this.blockRegister = blockRegister;
        this.itemRegister = itemRegister;
        this.blockEntityRegister = blockEntityRegister;
        this.menuTypeRegister = menuTypeRegister;
    }

    public <T extends IRMachine> T registerMachine(T machine) {
        this.machines.add(machine);
        IRBlocks.putTabBlock(BlockTabOrdering.BASIC_MACHINES, machine.blockSupplier());
        IRItems.BLOCK_ITEMS.add(machine.blockItemSupplier());
        return machine;
    }

    public IRMachine registerMachine(String name, IRMachine.Builder builder) {
        return registerMachine(builder.build(name, this));
    }

    public void register(IEventBus modEventBus) {

    }

    public DeferredRegister<Block> getBlockRegister() {
        return blockRegister;
    }

    public DeferredRegister<Item> getItemRegister() {
        return itemRegister;
    }

    public DeferredRegister<BlockEntityType<?>> getBlockEntityRegister() {
        return blockEntityRegister;
    }

    public DeferredRegister<MenuType<?>> getMenuTypeRegister() {
        return menuTypeRegister;
    }

    public List<IRMachine> getMachines() {
        return machines;
    }
}
