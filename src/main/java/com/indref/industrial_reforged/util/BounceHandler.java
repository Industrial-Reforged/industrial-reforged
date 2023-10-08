/*
 * This class is a modified version of the useful slime
 * mod's BounceHandler Class licensed under the MIT License
 */

package com.indref.industrial_reforged.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.IdentityHashMap;

public class BounceHandler {

    public static final IdentityHashMap<Entity, BounceHandler> BOUNCING_ENTITIES = new IdentityHashMap<>();
    public final LivingEntity entity;
    private int timer;
    private boolean wasInAir;
    private double bounce;
    private int bounceTick;
    private double lastMoveX;
    private double lastMoveZ;

    public BounceHandler(final LivingEntity entity, final double bounce) {
        this.entity = entity;
        this.timer = 0;
        this.wasInAir = false;
        this.bounce = bounce;

        if (bounce != 0) {
            bounceTick = entity.tickCount;
        } else {
            bounceTick = 0;
        }

        BounceHandler.BOUNCING_ENTITIES.put(entity, this);
    }

    public void onPlayerTick(Player player) {
        if (player == entity && !player.isFallFlying()) {
            if (player.tickCount == bounceTick) {
                final Vec3 vec3 = player.getDeltaMovement();
                player.setDeltaMovement(vec3.x, bounce, vec3.z);
                bounceTick = 0;
            }
            if (!entity.onGround() && entity.tickCount != bounceTick && (lastMoveX != entity.getDeltaMovement().x || lastMoveZ != entity.getDeltaMovement().z)) {
                final double d = 0.935D;
                final Vec3 vec32 = entity.getDeltaMovement();
                player.setDeltaMovement(vec32.x / d, vec32.y, vec32.z / d);
                entity.hasImpulse = true;
                lastMoveX = entity.getDeltaMovement().x;
                lastMoveZ = entity.getDeltaMovement().z;
            }
            if (wasInAir && entity.onGround()) {
                if (timer == 0) {
                    timer = entity.tickCount;
                } else if (entity.tickCount - timer > 5) {
                    BounceHandler.BOUNCING_ENTITIES.remove(entity);
                }
            } else {
                timer = 0;
                wasInAir = true;
            }
        }
    }

    public static void addBounceHandler(final LivingEntity entity) {
        addBounceHandler(entity, 0);
    }

    public static void addBounceHandler(final LivingEntity entity, final double bounce) {
        if (!(entity instanceof Player)) {
            return;
        }

        final BounceHandler handler = BounceHandler.BOUNCING_ENTITIES.get(entity);

        if (handler == null) {
            new BounceHandler(entity, bounce);
        } else if (bounce != 0) {
            handler.bounce = bounce;
            handler.bounceTick = entity.tickCount;
        }
    }
}
