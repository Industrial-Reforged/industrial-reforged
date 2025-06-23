package com.indref.industrial_reforged.util.machine;

import com.indref.industrial_reforged.api.blockentities.MachineBlockEntity;
import com.indref.industrial_reforged.api.blocks.MachineBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class IRMachine {
    public static final BlockBehaviour.Properties DEFAULT_BLOCK_PROPERTIES = BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK);

    protected final String name;
    public final Supplier<? extends MachineBlock> blockSupplier;
    public final Supplier<BlockItem> blockItemSupplier;
    public final Supplier<BlockEntityType<? extends MachineBlockEntity>> blockEntityTypeSupplier;

    public IRMachine(String name, Supplier<? extends MachineBlock> blockSupplier, Supplier<BlockItem> blockItemSupplier, Supplier<BlockEntityType<? extends MachineBlockEntity>> blockEntityTypeSupplier) {
        this.name = name;
        this.blockSupplier = blockSupplier;
        this.blockItemSupplier = blockItemSupplier;
        this.blockEntityTypeSupplier = blockEntityTypeSupplier;
    }

    public String getName() {
        return name;
    }

    public MachineBlock getBlock() {
        return blockSupplier.get();
    }

    public BlockItem getBlockItem() {
        return blockItemSupplier.get();
    }

    public BlockEntityType<? extends MachineBlockEntity> getBlockEntityType() {
        return blockEntityTypeSupplier.get();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Supplier<? extends MachineBlock> blockSupplier;
        private Supplier<? extends BlockItem> blockItemSupplier;
        private BlockEntityType.BlockEntitySupplier<? extends MachineBlockEntity> blockEntitySupplier;

        private Builder() {
        }

        public Builder blockEntity(BlockEntityType.BlockEntitySupplier<? extends MachineBlockEntity> blockEntitySupplier) {
            this.blockEntitySupplier = blockEntitySupplier;
            return this;
        }

        public Builder block(Function<BlockBehaviour.Properties, ? extends MachineBlock> blockFunction, BlockBehaviour.Properties properties) {
            this.blockSupplier = () -> blockFunction.apply(properties);
            return this;
        }

        public Builder block(Function<BlockBehaviour.Properties, ? extends MachineBlock> blockFunction) {
            return this.block(blockFunction, DEFAULT_BLOCK_PROPERTIES);
        }

        public Builder blockItem(Function<Item.Properties, ? extends BlockItem> blockItemFunction, Item.Properties properties) {
            this.blockItemSupplier = () -> blockItemFunction.apply(properties);
            return this;
        }

        public IRMachine build(String name, MachineRegistrationHelper registrationHelper) {
            Objects.requireNonNull(this.blockSupplier, "%s machine's block was not initialized".formatted(name));
            Objects.requireNonNull(this.blockEntitySupplier, "%s machine's block was not initialized".formatted(name));
            DeferredHolder<Block, ? extends MachineBlock> registeredBlock = registrationHelper.getBlockRegister().register(name, this.blockSupplier);
            if (this.blockItemSupplier == null) {
                this.blockItemSupplier = () -> new BlockItem(registeredBlock.get(), new Item.Properties());
            }
            return new IRMachine(
                    name,
                    registeredBlock,
                    registrationHelper.getItemRegister().register(name, this.blockItemSupplier),
                    registrationHelper.getBlockEntityRegister().register(name, () -> BlockEntityType.Builder.of(this.blockEntitySupplier, registeredBlock.get()).build(null))
            );
        }
    }
}
