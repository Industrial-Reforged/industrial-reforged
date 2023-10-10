package com.indref.industrial_reforged.event;

import com.indref.industrial_reforged.IndustrialReforged;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = IndustrialReforged.MODID)
public class IREvents {
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        IndustrialReforged.LOGGER.info("tracking on damage");
        if (event.getSource().is(DamageTypes.FALL)) {
            event.getEntity().sendSystemMessage(Component.literal("Falldamage!"));
            event.setCanceled(true);
            bounceUp(event.getEntity());
        }
    }

    private static void bounceUp(Entity entity) {
        Vec3 vec3 = entity.getDeltaMovement();
        if (vec3.y < 0.0D) {
            double d0 = entity instanceof LivingEntity ? 10.0D : 0.8D;
            entity.setDeltaMovement(vec3.x, -vec3.y * d0, vec3.z);
        }

    }
}
