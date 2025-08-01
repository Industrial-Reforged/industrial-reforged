package com.indref.industrial_reforged.content.items.armor;

import com.indref.industrial_reforged.IRConfig;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyHandler;
import com.indref.industrial_reforged.api.items.container.IEnergyItem;
import com.indref.industrial_reforged.api.tiers.EnergyTier;
import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.data.components.ComponentEuStorage;
import com.indref.industrial_reforged.events.InputHandler;
import com.indref.industrial_reforged.util.items.TooltipUtils;
import com.indref.industrial_reforged.util.items.ItemBarUtils;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Supplier;

public class JetpackItem extends ArmorItem implements IEnergyItem {
    private static final int STAGES = 6;
    private final Supplier<EnergyTier> energyTierSupplier;

    public JetpackItem(Properties properties, Holder<ArmorMaterial> material, Supplier<EnergyTier> energyTierSupplier) {
        super(material, Type.CHESTPLATE, properties.stacksTo(1).durability(0).component(IRDataComponents.ENERGY, ComponentEuStorage.EMPTY));
        this.energyTierSupplier = energyTierSupplier;
    }

    public float getJetpackStage(ItemStack stack) {
        IEnergyHandler energyStorage = getEnergyCap(stack);
        return ((float) energyStorage.getEnergyStored() / energyStorage.getEnergyCapacity()) * (STAGES - 1);
    }

    public int getEnergyUsage(ItemStack itemStack) {
        return IRConfig.jetpackEnergyUsage;
    }

    @Override
    public int getDefaultEnergyCapacity() {
        return IRConfig.jetpackCapacity;
    }

    @Override
    public EnergyTier getEnergyTier() {
        return energyTierSupplier.get();
    }

    /*
     * Jetpack logic is very much like Simply Jetpacks/Iron Jetpacks, since I used it to learn how to make this work
     * Credit to Tonius & Tomson124
     * https://github.com/Tomson124/SimplyJetpacks-2/blob/1.12/src/main/java/tonius/simplyjetpacks/item/rewrite/Itemjava
     */
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean isSelected) {
        if (entity instanceof Player player) {
            var chest = player.getItemBySlot(EquipmentSlot.CHEST);
            if (chest.isEmpty() || chest != stack)
                return;

            var item = chest.getItem();
            if (item instanceof JetpackItem/* && JetpackUtils.isEngineOn(chest)*/) {
                var hover = false;//JetpackUtils.isHovering(chest);

                if (InputHandler.isHoldingUp(player) || hover && !player.onGround()) {
                    double motionY = player.getDeltaMovement().y();
                    double speedHoverDescend = 0.25D;
                    double speedHoverSlow = 0.075D;
                    double hoverSpeed = InputHandler.isHoldingDown(player) ? speedHoverDescend : speedHoverSlow;
                    double accelVert = 0.12D;
                    double currentAccel = accelVert * (motionY < 0.3D ? 2.5D : 1.0D);
                    double speedVert = 0.41D;
                    double currentSpeedVertical = speedVert * (player.isInWater() ? 0.4D : 1.0D);

                    double sprintFuel = 2.1D;
                    double usage = player.isSprinting() || InputHandler.isHoldingSprint(player) ? getEnergyUsage(stack) * sprintFuel : getEnergyUsage(stack);

                    var energy = getEnergyCap(stack);

                    if (!player.isCreative()) {
                        energy.drainEnergy((int) usage, false);
                    }

                    if (hover && player.isFallFlying()) {
                        player.stopFallFlying();
                    }

                    if (energy.getEnergyStored() > 0 || player.isCreative()) {
                        double throttle = 1D;//JetpackUtils.getThrottle(stack);
                        double sprintSpeedVert = 1.05D;
                        double verticalSprintMulti = motionY >= 0 && InputHandler.isHoldingSprint(player) ? sprintSpeedVert : 1.0D;

                        if (InputHandler.isHoldingUp(player)) {
                            if (!hover) {
                                fly(player, Math.min(motionY + currentAccel, currentSpeedVertical) * throttle * verticalSprintMulti);
                            } else {
                                if (InputHandler.isHoldingDown(player)) {
                                    fly(player, Math.min(motionY + currentAccel, -speedHoverSlow));
                                } else {
                                    double speedHoverAscend = 0.27D;
                                    fly(player, Math.min(motionY + currentAccel, speedHoverAscend) * throttle * verticalSprintMulti);
                                }
                            }
                        } else {
                            fly(player, Math.min(motionY + currentAccel, -hoverSpeed));
                        }

                        double speedSide = 0.14D;
                        double speedSideways = (player.isCrouching() ? speedSide * 0.5F : speedSide) * throttle;
                        double sprintSpeed = 1.1D;
                        double speedForward = (player.isSprinting() ? speedSideways * sprintSpeed : speedSideways) * throttle;

                        if (!player.isFallFlying()) {
                            if (InputHandler.isHoldingForwards(player)) {
                                player.moveRelative(1, new Vec3(0, 0, speedForward));
                            }

                            if (InputHandler.isHoldingBackwards(player)) {
                                player.moveRelative(1, new Vec3(0, 0, -speedSideways * 0.8F));
                            }

                            if (InputHandler.isHoldingLeft(player)) {
                                player.moveRelative(1, new Vec3(speedSideways, 0, 0));
                            }

                            if (InputHandler.isHoldingRight(player)) {
                                player.moveRelative(1, new Vec3(-speedSideways, 0, 0));
                            }
                        }

                        if (!level.isClientSide()) {
                            player.fallDistance = 0.0F;

                            if (player instanceof ServerPlayer serverPlayer) {
                                serverPlayer.connection.aboveGroundTickCount = 0;
                            }
                        }
                    }
                }
            }
        }
    }

    private static void fly(Player player, double y) {
        var motion = player.getDeltaMovement();
        player.setDeltaMovement(motion.x(), y, motion.z());
    }

    @Override
    public int getBarWidth(ItemStack itemStack) {
        return ItemBarUtils.energyBarWidth(itemStack);
    }

    @Override
    public int getBarColor(ItemStack itemStack) {
        return ItemBarUtils.energyBarColor(itemStack);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, ctx, tooltip, flag);
        TooltipUtils.addEnergyTooltip(tooltip, stack);
    }

}
