package com.indref.industrial_reforged.registries.blockentities;

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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FireboxBlockEntity extends BlockEntity implements MenuProvider/*, IHeatBlock */ {
    private int burnTime;
    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        public int getSlots() {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            burnTime = CommonHooks.getBurnTime(itemHandler.getStackInSlot(slot), RecipeType.SMELTING);
            if (!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return CommonHooks.getBurnTime(stack, RecipeType.SMELTING) > 0;
        }
    };
    private static final int INPUT_SLOT = 0;
    protected final ContainerData data;

    public FireboxBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(IRBlockEntityTypes.FIREBOX.get(), blockPos, blockState);
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
    public Component getDisplayName() {
        return Component.literal("Firebox");
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        ItemStack itemStack = itemHandler.getStackInSlot(INPUT_SLOT);
        BlockEntity self = level.getBlockEntity(blockPos);
        // IHeatBlock heatBlock = (IHeatBlock) self;
        // Check if item is a fuel item
        if (CommonHooks.getBurnTime(itemStack, RecipeType.SMELTING) > 0) {
            if (burnTime <= 0) {
                burnTime = CommonHooks.getBurnTime(itemStack, RecipeType.SMELTING);
                itemStack.shrink(1);
            }
            burnTime--;
            //heatBlock.tryFillHeat(self, 1);
        }
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
        return new FireBoxMenu(containerId, inventory, this, this.data);
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("burnTime", burnTime);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        burnTime = pTag.getInt("burnTime");
    }

    // Heat capacity
    public int getHeatCapacity() {
        return 4000;
    }
}
