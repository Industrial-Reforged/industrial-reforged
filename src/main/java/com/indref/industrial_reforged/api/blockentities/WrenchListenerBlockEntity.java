package com.indref.industrial_reforged.api.blockentities;

import net.minecraft.world.entity.player.Player;

public interface WrenchListenerBlockEntity {
    default void beforeRemoveByWrench(Player player) {
    }
}
