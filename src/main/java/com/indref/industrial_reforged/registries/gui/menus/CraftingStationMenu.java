package com.indref.industrial_reforged.registries.gui.menus;

import com.indref.industrial_reforged.api.gui.IRAbstractContainerMenu;
import com.indref.industrial_reforged.registries.IRBlocks;
import com.indref.industrial_reforged.registries.IRMenuTypes;
import com.indref.industrial_reforged.registries.blockentities.CraftingStationBlockEntity;
import com.indref.industrial_reforged.util.CapabilityUtils;
import com.indref.industrial_reforged.util.recipes.craftingStation.ItemhandlerCraftingContainer;
import com.indref.industrial_reforged.util.recipes.craftingStation.SmartItemHandlerSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class CraftingStationMenu extends IRAbstractContainerMenu<CraftingStationBlockEntity> {
    public static final int BLUEPRINT_SLOT = 28;

    private final ItemhandlerCraftingContainer craftSlots;
    private final ResultContainer resultSlots = new ResultContainer();
    private final ContainerLevelAccess access;
    private final Player player;

    public CraftingStationMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, (CraftingStationBlockEntity) inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public CraftingStationMenu(int pContainerId, Inventory inv, CraftingStationBlockEntity entity) {
        super(IRMenuTypes.CRAFTING_STATION_MENU.get(), pContainerId, inv, entity);
        checkContainerSize(inv, 1);
        this.player = inv.player;
        this.access = ContainerLevelAccess.create(entity.getLevel(), entity.getBlockPos());
        IItemHandler itemHandler = CapabilityUtils.itemHandlerCapability(entity);
        this.craftSlots = new ItemhandlerCraftingContainer(itemHandler, this, 3, 3);

        addCraftingSlots();
        addStorageSlots(itemHandler);
        // Output slot
        this.addSlot(new ResultSlot(inv.player, craftSlots, resultSlots, 27, 131, 29));
        // Blueprint
        this.addSlot(new SlotItemHandler(itemHandler, BLUEPRINT_SLOT, 154, 29));
        addPlayerHotbar(inv, 185);
        addPlayerInventory(inv, 127);

        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = itemHandler.getStackInSlot(i + 18);
            this.craftSlots.setItem(i, itemStack);
        }
    }

    @Override
    public void slotsChanged(Container inventory) {
        this.access.execute(
                (level, blockPos) -> slotChangedCraftingGrid(this, level, this.player, this.craftSlots, this.resultSlots)
        );
    }

    protected static void slotChangedCraftingGrid(
            AbstractContainerMenu menu, Level level, Player player, CraftingContainer container, ResultContainer result
    ) {
        if (!level.isClientSide) {
            CraftingInput craftinginput = container.asCraftInput();
            ServerPlayer serverplayer = (ServerPlayer) player;
            ItemStack itemstack = ItemStack.EMPTY;
            Optional<RecipeHolder<CraftingRecipe>> optional = level.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, craftinginput, level);
            if (optional.isPresent()) {
                RecipeHolder<CraftingRecipe> recipeholder = optional.get();
                CraftingRecipe craftingrecipe = recipeholder.value();
                if (result.setRecipeUsed(level, serverplayer, recipeholder)) {
                    ItemStack itemStack1 = craftingrecipe.assemble(craftinginput, level.registryAccess());
                    if (itemStack1.isItemEnabled(level.enabledFeatures())) {
                        itemstack = itemStack1;
                    }
                }
            }

            result.setItem(0, itemstack);
            menu.setRemoteSlot(0, itemstack);
        }
    }


    private void addCraftingSlots() {
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
