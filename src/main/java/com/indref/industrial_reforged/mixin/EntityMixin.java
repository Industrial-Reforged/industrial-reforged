package com.indref.industrial_reforged.mixin;

import com.indref.industrial_reforged.util.VerticalCollider;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public class EntityMixin implements VerticalCollider {
    @Override
    public boolean verticalCollisionAbove() {
        return false;
    }
}
