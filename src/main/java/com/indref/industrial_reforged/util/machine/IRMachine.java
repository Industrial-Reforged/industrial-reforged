package com.indref.industrial_reforged.util.machine;

import com.indref.industrial_reforged.api.blockentities.MachineBlockEntity;
import com.indref.industrial_reforged.api.blocks.MachineBlock;
import com.indref.industrial_reforged.api.gui.MachineContainerMenu;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public record IRMachine(String name, Supplier<EnergyTier> energyTierSupplier,
                        Supplier<? extends MachineBlock> blockSupplier, Supplier<BlockItem> blockItemSupplier,
                        Supplier<BlockEntityType<? extends MachineBlockEntity>> blockEntityTypeSupplier,
                        @Nullable Supplier<MenuType<? extends MachineContainerMenu<?>>> menuTypeSupplier) {
    public static final BlockBehaviour.Properties DEFAULT_BLOCK_PROPERTIES = BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK);

    public EnergyTier getEnergyTier() {
        return energyTierSupplier.get();
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

    public @Nullable <T extends MachineContainerMenu<?>> MenuType<T> getMenuType() {
        return menuTypeSupplier != null ? ((MenuType<T>) menuTypeSupplier.get()) : null;
    }

    public static Builder builder(Supplier<EnergyTier> energyTierSupplier) {
        return new Builder(energyTierSupplier);
    }

    public static class Builder {
        private BiFunction<BlockBehaviour.Properties, Supplier<EnergyTier>, ? extends MachineBlock> blockFactory;
        private BlockBehaviour.Properties blockProperties;
        private Supplier<? extends BlockItem> blockItemSupplier;
        private IContainerFactory<? extends MachineContainerMenu<?>> menuSupplier;
        private AltContainerFactory<?, ?> secondMenuSupplier;
        private BlockEntityType.BlockEntitySupplier<? extends MachineBlockEntity> blockEntitySupplier;
        private final Supplier<EnergyTier> energyTierSupplier;

        private Builder(Supplier<EnergyTier> energyTierSupplier) {
            this.energyTierSupplier = energyTierSupplier;
        }

        public Builder blockEntity(BlockEntityType.BlockEntitySupplier<? extends MachineBlockEntity> blockEntitySupplier) {
            this.blockEntitySupplier = blockEntitySupplier;
            return this;
        }

        public Builder block(BiFunction<BlockBehaviour.Properties, Supplier<EnergyTier>, ? extends MachineBlock> blockFactory, BlockBehaviour.Properties properties) {
            this.blockFactory = blockFactory;
            this.blockProperties = properties;
            return this;
        }

        public Builder block(BiFunction<BlockBehaviour.Properties, Supplier<EnergyTier>, ? extends MachineBlock> blockFactory) {
            return this.block(blockFactory, DEFAULT_BLOCK_PROPERTIES);
        }

        public <T extends MachineBlockEntity> Builder menu(IContainerFactory<? extends MachineContainerMenu<T>> menuSupplier) {
            this.menuSupplier = menuSupplier;
            return this;
        }

        public Builder blockItem(Function<Item.Properties, ? extends BlockItem> blockItemFunction, Item.Properties properties) {
            this.blockItemSupplier = () -> blockItemFunction.apply(properties);
            return this;
        }

        public IRMachine build(String name, MachineRegistrationHelper registrationHelper) {
            Objects.requireNonNull(this.blockFactory, "%s machine's block was not initialized".formatted(name));
            Objects.requireNonNull(this.blockEntitySupplier, "%s machine's block entity was not initialized".formatted(name));

            Supplier<? extends MachineBlock> blockSupplier = () -> this.blockFactory.apply(this.blockProperties, this.energyTierSupplier);
            DeferredHolder<Block, ? extends MachineBlock> registeredBlock = registrationHelper.getBlockRegister().register(name, blockSupplier);
            if (this.blockItemSupplier == null) {
                this.blockItemSupplier = () -> new BlockItem(registeredBlock.get(), new Item.Properties());
            }

            return new IRMachine(
                    name,
                    this.energyTierSupplier,
                    registeredBlock,
                    registrationHelper.getItemRegister().register(name, this.blockItemSupplier),
                    registrationHelper.getBlockEntityRegister().register(name, () -> BlockEntityType.Builder.of(this.blockEntitySupplier, registeredBlock.get()).build(null)),
                    this.menuSupplier != null ? registrationHelper.getMenuTypeRegister().register(name, () -> IMenuTypeExtension.create(this.menuSupplier)) : null
            );
        }
    }
}
