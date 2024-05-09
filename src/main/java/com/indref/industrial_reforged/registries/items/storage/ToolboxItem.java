package com.indref.industrial_reforged.registries.items.storage;

import com.indref.industrial_reforged.api.items.ToolItem;
import com.indref.industrial_reforged.api.items.bundles.AdvancedBundleItem;
import com.indref.industrial_reforged.registries.IRItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

//TODO: Refactor this into api and use caps
public class ToolboxItem extends AdvancedBundleItem {
    public ToolboxItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public int getSlots(ItemStack itemStack) {
        return 1;
    }
}
