package com.indref.industrial_reforged.registries.blockentities.multiblocks.controller;

import com.indref.industrial_reforged.api.blocks.container.ContainerBlockEntity;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.screen.FireBoxMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FireboxBlockEntity extends ContainerBlockEntity implements MenuProvider {
    private static final int INPUT_SLOT = 0;

    private int burnTime;
    private final ContainerData data;

    public FireboxBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRBlockEntityTypes.FIREBOX.get(), blockPos, blockState);
        addItemHandler(1, (slot, itemStack) -> CommonHooks.getBurnTime(itemStack, RecipeType.SMELTING) > 0);
        addHeatStorage(4000);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return 0;
            }

            @Override
            public void set(int pIndex, int pValue) {
            }

            @Override
            public int getCount() {
                return 1;
            }
        };
    }

    @Override
    public void onItemsChanged(int slot) {
        this.burnTime = CommonHooks.getBurnTime(getItemHandler().getStackInSlot(slot), RecipeType.SMELTING);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Firebox");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(getItemHandler().getSlots());
        for (int i = 0; i < getItemHandler().getSlots(); i++) {
            inventory.setItem(i, getItemHandler().getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        ItemStack itemStack = getItemHandler().getStackInSlot(INPUT_SLOT);
        if (CommonHooks.getBurnTime(itemStack, RecipeType.SMELTING) > 0) {
            if (burnTime <= 0) {
                burnTime = CommonHooks.getBurnTime(itemStack, RecipeType.SMELTING);
                itemStack.shrink(1);
            }
            burnTime--;
            this.tryFillHeat(this, 1);
        }
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
        return new FireBoxMenu(containerId, inventory, this, this.data);
    }

    @Override
    protected void saveOther(CompoundTag pTag) {
        pTag.putInt("burnTime", burnTime);
    }

    @Override
    protected void loadOther(CompoundTag pTag) {
        burnTime = pTag.getInt("burnTime");
    }
}
