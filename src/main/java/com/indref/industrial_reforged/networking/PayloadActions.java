package com.indref.industrial_reforged.networking;

import com.indref.industrial_reforged.registries.IRDataComponents;
import com.indref.industrial_reforged.client.item_bars.CrucibleProgressRenderer;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.CastingBasinBlockEntity;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.CrucibleWallBlockEntity;
import com.indref.industrial_reforged.registries.blockentities.multiblocks.controller.CrucibleBlockEntity;
import com.indref.industrial_reforged.util.ItemUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class PayloadActions {
    public static void blueprintHasRecipe(BlueprintHasRecipePayload payload, IPayloadContext ctx) {
        payload.itemStack().set(IRDataComponents.HAS_RECIPE, payload.hasRecipe());
    }

    public static void blueprintStoredRecipe(BlueprintStoredRecipePayload payload, IPayloadContext ctx) {
        payload.itemStack().set(IRDataComponents.STORED_RECIPE, payload.itemStacks());
    }

    public static void itemActivitySync(ItemActivityPayload payload, IPayloadContext ctx) {
        payload.itemStack().set(IRDataComponents.ACTIVE, payload.active());
    }

    public static void crucibleControllerSync(CrucibleControllerPayload payload, IPayloadContext ctx) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {
            if (level.getBlockEntity(payload.wallPos()) instanceof CrucibleWallBlockEntity crucibleWallBlockEntity)
                crucibleWallBlockEntity.setControllerPos(payload.controllerPos());
        }
    }

    public static void castingGhostItemSync(CastingGhostItemPayload payload, IPayloadContext ctx) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {
            BlockEntity blockEntity = level.getBlockEntity(payload.blockPos());

            if (blockEntity instanceof CastingBasinBlockEntity castingBasinBlockEntity) {
                castingBasinBlockEntity.resultItem = payload.itemStack();
            }
        }
    }

    public static void castingDurationSync(CastingDurationPayload payload, IPayloadContext ctx) {
        Level level = Minecraft.getInstance().level;
        if (level != null) {
            BlockEntity blockEntity = level.getBlockEntity(payload.blockPos());

            if (blockEntity instanceof CastingBasinBlockEntity castingBasinBlockEntity) {
                castingBasinBlockEntity.duration = payload.duration();
                castingBasinBlockEntity.maxDuration = payload.maxDuration();
            }
        }
    }

    public static void armorActivitySync(ArmorActivityPayload payload, IPayloadContext ctx) {
        Player player = ctx.player();
        player.getItemBySlot(ItemUtils.equipmentSlotFromIndex(payload.slot())).set(IRDataComponents.ACTIVE, payload.active());
    }

    public static void crucibleMeltingProgressSync(CrucibleMeltingProgressPayload payload, IPayloadContext ctx) {
        if (ctx.player() instanceof LocalPlayer localPlayer) {
            Level level = localPlayer.level();
            if (level instanceof ClientLevel clientLevel) {
                BlockEntity blockEntity = clientLevel.getBlockEntity(payload.pos());
                if (blockEntity instanceof CrucibleBlockEntity crucibleBlockEntity) {
                    ItemStackHandler stackHandler = crucibleBlockEntity.getItemHandler();
                    ItemStack stackInSlot = stackHandler.getStackInSlot(payload.slotIndex());
                    CompoundTag tag = ItemUtils.getImmutableTag(stackInSlot).copyTag();
                    tag.putFloat(CrucibleProgressRenderer.BARWIDTH_KEY, payload.progress());
                    stackInSlot.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                }
            }

        }
    }
}
