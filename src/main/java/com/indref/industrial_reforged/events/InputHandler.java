package com.indref.industrial_reforged.events;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;

import java.util.Map;
import java.util.WeakHashMap;

/*
 * Keyboard handling system borrowed from Simply Jetpacks
 * https://github.com/Tomson124/SimplyJetpacks-2/blob/1.12/src/main/java/tonius/simplyjetpacks/handler/SyncHandler.java
 */
public final class InputHandler {
    private static final Map<Player, Boolean> HOLDING_UP = new WeakHashMap<>();
    private static final Map<Player, Boolean> HOLDING_DOWN = new WeakHashMap<>();
    private static final Map<Player, Boolean> HOLDING_FORWARDS = new WeakHashMap<>();
    private static final Map<Player, Boolean> HOLDING_BACKWARDS = new WeakHashMap<>();
    private static final Map<Player, Boolean> HOLDING_LEFT = new WeakHashMap<>();
    private static final Map<Player, Boolean> HOLDING_RIGHT = new WeakHashMap<>();
    private static final Map<Player, Boolean> HOLDING_SPRINT = new WeakHashMap<>();

    public static boolean isHoldingUp(Player player) {
        return HOLDING_UP.containsKey(player) && HOLDING_UP.get(player);
    }

    public static boolean isHoldingDown(Player player) {
        return HOLDING_DOWN.containsKey(player) && HOLDING_DOWN.get(player);
    }

    public static boolean isHoldingForwards(Player player) {
        return HOLDING_FORWARDS.containsKey(player) && HOLDING_FORWARDS.get(player);
    }

    public static boolean isHoldingBackwards(Player player) {
        return HOLDING_BACKWARDS.containsKey(player) && HOLDING_BACKWARDS.get(player);
    }

    public static boolean isHoldingLeft(Player player) {
        return HOLDING_LEFT.containsKey(player) && HOLDING_LEFT.get(player);
    }

    public static boolean isHoldingRight(Player player) {
        return HOLDING_RIGHT.containsKey(player) && HOLDING_RIGHT.get(player);
    }

    public static boolean isHoldingSprint(Player player) {
        return HOLDING_SPRINT.containsKey(player) && HOLDING_SPRINT.get(player);
    }

    public static void update(Player player, boolean up, boolean down, boolean forwards, boolean backwards, boolean left, boolean right, boolean sprint) {
        HOLDING_UP.put(player, up);
        HOLDING_DOWN.put(player, down);
        HOLDING_FORWARDS.put(player, forwards);
        HOLDING_BACKWARDS.put(player, backwards);
        HOLDING_LEFT.put(player, left);
        HOLDING_RIGHT.put(player, right);
        HOLDING_SPRINT.put(player, sprint);
    }

    public static void remove(Player player) {
        HOLDING_UP.remove(player);
        HOLDING_DOWN.remove(player);
        HOLDING_FORWARDS.remove(player);
        HOLDING_BACKWARDS.remove(player);
        HOLDING_LEFT.remove(player);
        HOLDING_RIGHT.remove(player);
        HOLDING_SPRINT.remove(player);
    }

    public static void clear() {
        HOLDING_UP.clear();
        HOLDING_DOWN.clear();
        HOLDING_FORWARDS.clear();
        HOLDING_BACKWARDS.clear();
        HOLDING_LEFT.clear();
        HOLDING_RIGHT.clear();
        HOLDING_SPRINT.clear();
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        remove(event.getEntity());
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        remove(event.getEntity());
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        InputHandler.clear();
    }
}