package com.indref.industrial_reforged.content.items.misc;

import com.indref.industrial_reforged.content.gui.menus.BlueprintMenu;
import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.data.components.ComponentBlueprint;
import com.indref.industrial_reforged.registries.IRMultiblocks;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.Multiblock;
import com.portingdeadmods.portingdeadlibs.api.utils.HorizontalDirection;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlueprintItem extends Item implements MenuProvider {
    public static final String HAS_RECIPE_KEY = "blueprint_has_recipe";

    public BlueprintItem(Properties properties) {
        super(properties.component(IRDataComponents.BLUEPRINT, ComponentBlueprint.EMPTY));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        player.openMenu(this);
        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        BlockPos relative = context.getClickedPos().relative(context.getClickedFace());
        Multiblock multiblock = IRMultiblocks.BLAST_FURNACE.get();
        ComponentBlueprint blueprint = new ComponentBlueprint(relative,
                multiblock.getFixedDirection() != null ? multiblock.getFixedDirection() : HorizontalDirection.fromRegularDirection(context.getHorizontalDirection().getClockWise()),
                multiblock);
        context.getItemInHand().set(IRDataComponents.BLUEPRINT, blueprint);
        return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("indref.container.blueprint");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new BlueprintMenu(player, containerId);
    }
}
