package com.indref.industrial_reforged.registries.screen;

import com.indref.industrial_reforged.api.gui.IRAbstractContainerMenu;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRMenuTypes;
import com.indref.industrial_reforged.registries.blockentities.CraftingStationBlockEntity;
import com.indref.industrial_reforged.util.BlockUtils;
import com.indref.industrial_reforged.util.recipes.ItemhandlerCraftingContainer;
import com.indref.industrial_reforged.util.recipes.SmartItemHandlerSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class CraftingStationMenu extends IRAbstractContainerMenu {
    public final CraftingStationBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;
    private ItemhandlerCraftingContainer craftSlots;
    private final ResultContainer resultSlots = new ResultContainer();
    private final ContainerLevelAccess access;
    private final Player player;
    private final IItemHandler itemHandler;

    public CraftingStationMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(1));
    }

    public CraftingStationMenu(int pContainerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(IRMenuTypes.CRAFTING_STATION_MENU.get(), pContainerId, inv);
        checkContainerSize(inv, 1);
        blockEntity = ((CraftingStationBlockEntity) entity);
        this.level = inv.player.level();
        this.data = data;
        this.player = inv.player;
        this.access = ContainerLevelAccess.create(level, blockEntity.getBlockPos());
        this.itemHandler = BlockUtils.getBlockEntityCapability(Capabilities.ItemHandler.BLOCK, blockEntity);
        this.craftSlots = new ItemhandlerCraftingContainer(itemHandler, this, 3, 3);

        addCraftingSlots(itemHandler);
        addStorageSlots(itemHandler);
        // Output slot
        this.addSlot(new ResultSlot(inv.player, craftSlots, resultSlots, 27, 131, 29));
        this.addSlot(new SlotItemHandler(itemHandler, 27, 154, 29));
        addDataSlots(data);
        addPlayerHotbar(inv, 185);
        addPlayerInventory(inv, 127);
    }

    @Override
    public void slotsChanged(Container p_39366_) {
        this.access.execute((p_39386_, p_39387_) -> slotChangedCraftingGrid(this, p_39386_, this.player, this.craftSlots, this.resultSlots));
    }

    protected static void slotChangedCraftingGrid(
            AbstractContainerMenu p_150547_, Level p_150548_, Player p_150549_, CraftingContainer p_150550_, ResultContainer p_150551_
    ) {
        if (!p_150548_.isClientSide) {
            ServerPlayer serverplayer = (ServerPlayer)p_150549_;
            ItemStack itemstack = ItemStack.EMPTY;
            Optional<RecipeHolder<CraftingRecipe>> optional = p_150548_.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, p_150550_, p_150548_);
            if (optional.isPresent()) {
                RecipeHolder<CraftingRecipe> recipeholder = optional.get();
                CraftingRecipe craftingrecipe = recipeholder.value();
                if (p_150551_.setRecipeUsed(p_150548_, serverplayer, recipeholder)) {
                    ItemStack itemstack1 = craftingrecipe.assemble(p_150550_, p_150548_.registryAccess());
                    if (itemstack1.isItemEnabled(p_150548_.enabledFeatures())) {
                        itemstack = itemstack1;
                    }
                }
            }

            p_150551_.setItem(0, itemstack);
            p_150547_.setRemoteSlot(0, itemstack);
        }
    }

    private void addCraftingSlots(IItemHandler itemHandler) {
        int x = 37;
        int y = 10;
        int index = 0;
        for (int yIndex = 0; yIndex < 3; yIndex++) {
            for (int xIndex = 0; xIndex < 3; xIndex++) {
                this.addSlot(new SmartItemHandlerSlot(craftSlots, index, index + 18, x + xIndex * 18, y + yIndex * 18));
                index++;
            }
        }
    }

    private void addStorageSlots(IItemHandler itemHandler) {
        int x = 8;
        int y = 82;
        int index = 0;
        for (int yIndex = 0; yIndex < 2; yIndex++) {
            for (int xIndex = 0; xIndex < 9; xIndex++) {
                this.addSlot(new SlotItemHandler(itemHandler, index, x + xIndex * 18, y + yIndex * 18));
                index++;
            }
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int slotId) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(access, player, IRBlocks.CRAFTING_STATION.get());
    }
}
