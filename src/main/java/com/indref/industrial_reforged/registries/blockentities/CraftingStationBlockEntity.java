package com.indref.industrial_reforged.registries.blockentities;

import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.registries.screen.CraftingStationMenu;
import com.indref.industrial_reforged.registries.screen.FireBoxMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CraftingStationBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(28) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private final ContainerData data;

    public CraftingStationBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(IRBlockEntityTypes.CRAFTING_STATION.get(), p_155229_, p_155230_);
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

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Test");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory inventory, @NotNull Player player) {
        return new CraftingStationMenu(containerId, inventory, this, this.data);
    }
}