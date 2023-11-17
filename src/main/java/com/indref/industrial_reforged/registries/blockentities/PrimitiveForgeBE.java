package com.indref.industrial_reforged.registries.blockentities;

import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.capabilities.Capabilities;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.util.LazyOptional;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PrimitiveForgeBE extends BlockEntity {
    private final ItemStackHandler itemHandler = new ItemStackHandler(2);

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;

    public int progress = 0;
    public int maxProgress = 0;

    public final String NBT_KEY_PROGRESS = "progress";
    public final String NBT_KEY_INVENTORY = "inventory";

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;

    public PrimitiveForgeBE(BlockPos blockPos, BlockState blockState) {
        super(IRBlockEntityTypes.PRIMITIVE_FORGE.get(), blockPos, blockState);
        this.data = new ContainerData() {
            @Override
            public int get(int p_39284_) {
                return 0;
            }

            @Override
            public void set(int p_39285_, int p_39286_) {
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if(hasRecipe() && progress == getMaxProgress()) {
            setChanged(pLevel, pPos, pState);
            craftItem();
        }
    }

    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == Capabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put(NBT_KEY_INVENTORY, itemHandler.serializeNBT());
        pTag.putInt(NBT_KEY_PROGRESS, progress);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound(NBT_KEY_INVENTORY));
        progress = pTag.getInt(NBT_KEY_PROGRESS);
    }

    public ItemStack getItemBySlot(int slotIndex) {
        return this.itemHandler.getStackInSlot(slotIndex);
    }

    public void increaseProgress() {
        if (progress >= getMaxProgress()) {
            this.progress = 0;
        }
        this.progress++;
    }

    public int getMaxProgress() {
        this.maxProgress = 3;
        return this.maxProgress;
    }

    public boolean hasRecipe() {
        boolean hasCraftingItem = this.itemHandler.getStackInSlot(INPUT_SLOT).getItem() == Items.IRON_INGOT;

        return hasCraftingItem && canInsertAmountIntoOutputSlot(1) && canInsertItemIntoOutputSlot(Items.HEAVY_WEIGHTED_PRESSURE_PLATE);
    }

    private boolean canInsertItemIntoOutputSlot(Item item) {
        return getItemBySlot(OUTPUT_SLOT).isEmpty() || getItemBySlot(OUTPUT_SLOT).is(item);
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + count <= this.itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
    }

    public void craftItem() {
        getItemBySlot(INPUT_SLOT).shrink(1);
        if (getItemBySlot(OUTPUT_SLOT).equals(ItemStack.EMPTY)) {
            this.itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(Items.HEAVY_WEIGHTED_PRESSURE_PLATE));
        } else {
            getItemBySlot(OUTPUT_SLOT).grow(1);
        }
    }

}
