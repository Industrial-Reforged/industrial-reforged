package com.indref.industrial_reforged.events;

import com.indref.industrial_reforged.IRRegistries;
import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.energy.IEnergyStorage;
import com.indref.industrial_reforged.api.transportation.NetworkNode;
import com.indref.industrial_reforged.api.transportation.TransportNetwork;
import com.indref.industrial_reforged.client.renderer.debug.NetworkNodeRenderer;
import com.indref.industrial_reforged.client.sounds.JetpackSound;
import com.indref.industrial_reforged.client.transportation.ClientNodes;
import com.indref.industrial_reforged.content.items.armor.JetpackItem;
import com.indref.industrial_reforged.data.IRDataComponents;
import com.indref.industrial_reforged.data.components.ComponentBlueprint;
import com.indref.industrial_reforged.networking.UpdateInputPayload;
import com.indref.industrial_reforged.util.VecHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.portingdeadmods.portingdeadlibs.api.client.renderers.multiblocks.MultiblockPreviewRenderer;
import com.portingdeadmods.portingdeadlibs.api.multiblocks.Multiblock;
import com.portingdeadmods.portingdeadlibs.api.utils.HorizontalDirection;
import com.portingdeadmods.portingdeadlibs.utils.MultiblockHelper;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Vector3f;
import org.lwjgl.system.linux.liburing.IOURingRecvmsgOut;

import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

@EventBusSubscriber(modid = IndustrialReforged.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public final class ClientEvents {
    public static FluidStack playerInCrucibleFluid = null;
    private static boolean up = false;
    private static boolean down = false;
    private static boolean forwards = false;
    private static boolean backwards = false;
    private static boolean left = false;
    private static boolean right = false;
    private static boolean sprint = false;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Pre event) {
        var mc = Minecraft.getInstance();
        var settings = mc.options;

        if (mc.getConnection() == null)
            return;

        boolean upNow = settings.keyJump.isDown();
        boolean downNow = settings.keyShift.isDown();
        boolean forwardsNow = settings.keyUp.isDown();
        boolean backwardsNow = settings.keyDown.isDown();
        boolean leftNow = settings.keyLeft.isDown();
        boolean rightNow = settings.keyRight.isDown();
        boolean sprintNow = settings.keySprint.isDown();

        if (upNow != up || downNow != down || forwardsNow != forwards || backwardsNow != backwards || leftNow != left || rightNow != right || sprintNow != sprint) {
            up = upNow;
            down = downNow;
            forwards = forwardsNow;
            backwards = backwardsNow;
            left = leftNow;
            right = rightNow;
            sprint = sprintNow;

            update(up, down, forwards, backwards, left, right, sprint);
        }

        renderParticles();
    }

    private static void update(boolean up, boolean down, boolean forwards, boolean backwards, boolean left, boolean right, boolean sprint) {
        LocalPlayer player = Minecraft.getInstance().player;

        PacketDistributor.sendToServer(new UpdateInputPayload(up, down, forwards, backwards, left, right, sprint));
        InputHandler.update(player, up, down, forwards, backwards, left, right, sprint);
    }

    private static void renderParticles() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && mc.level != null && !mc.isPaused()) {
            ItemStack chest = mc.player.getItemBySlot(EquipmentSlot.CHEST);
            Item item = chest.getItem();

            if (!chest.isEmpty() && item instanceof JetpackItem && isFlying(mc.player)) {
                if (mc.options.particles().get() != ParticleStatus.MINIMAL) {
                    var playerPos = mc.player.position().add(0, 1.5, 0);

                    float random = (ThreadLocalRandom.current().nextFloat() - 0.5F) * 0.1F;
                    double[] sneakBonus = mc.player.isCrouching() ? new double[] { -0.30, -0.10 } : new double[] { 0, 0 };

                    var vLeft = VecHelper.rotate(new Vec3(-0.18, -0.90 + sneakBonus[1], -0.30 + sneakBonus[0]), mc.player.yBodyRot, 0, 0);
                    var vRight = VecHelper.rotate(new Vec3(0.18, -0.90 + sneakBonus[1], -0.30 + sneakBonus[0]), mc.player.yBodyRot, 0, 0);

                    double speedSide = 0.14D;
                    var v = playerPos.add(vLeft).add(mc.player.getDeltaMovement().scale(speedSide));
                    mc.particleEngine.createParticle(ParticleTypes.FLAME, v.x, v.y, v.z, random, -0.2D, random);
                    mc.particleEngine.createParticle(ParticleTypes.SMOKE, v.x, v.y, v.z, random, -0.2D, random);

                    v = playerPos.add(vRight).add(mc.player.getDeltaMovement().scale(speedSide));
                    mc.particleEngine.createParticle(ParticleTypes.FLAME, v.x, v.y, v.z, random, -0.2D, random);
                    mc.particleEngine.createParticle(ParticleTypes.SMOKE, v.x, v.y, v.z, random, -0.2D, random);
                }

                if (!JetpackSound.playing(mc.player.getId())) {
                    mc.getSoundManager().play(new JetpackSound(mc.player));
                }
            }
        }
    }

    public static boolean isFlying(Player player) {
        if (player.isSpectator())
            return false;

        ItemStack stack = player.getItemBySlot(EquipmentSlot.CHEST);

        if (!stack.isEmpty()/* && isEngineOn(stack)*/) {
            IEnergyStorage energy = stack.getCapability(IRCapabilities.EnergyStorage.ITEM);

            if (energy.getEnergyStored() > 0 || player.isCreative()) {
                if (false /*isHovering(stack)*/) {
                    return !player.onGround();
                }

                return InputHandler.isHoldingUp(player);
            }
        }

        return false;
    }

    @SubscribeEvent
    public static void renderLevel(RenderLevelStageEvent event) {
        //renderMultiblockPreview(event);
        //renderNetworkNodes(event);
    }

    @SubscribeEvent
    public static void renderFog(ViewportEvent.RenderFog event) {
        Camera camera = event.getCamera();
        if (playerInCrucibleFluid != null)
            IClientFluidTypeExtensions.of(playerInCrucibleFluid.getFluid().defaultFluidState()).modifyFogRender(camera, event.getMode(), event.getRenderer().getRenderDistance(), (float) event.getPartialTick(), event.getNearPlaneDistance(), event.getFarPlaneDistance(), event.getFogShape());
    }

    @SubscribeEvent
    public static void renderFog(ViewportEvent.ComputeFogColor event) {
        Camera camera = event.getCamera();
        if (playerInCrucibleFluid != null) {
            Vector3f vector3f = IClientFluidTypeExtensions.of(playerInCrucibleFluid.getFluid().defaultFluidState()).modifyFogColor(camera, (float) event.getPartialTick(), Minecraft.getInstance().level, (int) event.getRenderer().getRenderDistance(), (float) event.getRenderer().getDarkenWorldAmount((float) event.getPartialTick()), new Vector3f(event.getRed(), event.getGreen(), event.getBlue()));
            event.setRed(vector3f.x);
            event.setGreen(vector3f.y);
            event.setBlue(vector3f.z);
        }
    }

    public static void renderMultiblockPreview(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            Vec3 cameraPos = event.getCamera().getPosition();

            Minecraft mc = Minecraft.getInstance();
            PoseStack poseStack = event.getPoseStack();
            MultiBufferSource.BufferSource bufferSource = event.getLevelRenderer().renderBuffers.bufferSource();

            ItemStack itemStack = mc.player.getMainHandItem();

            if (!itemStack.has(IRDataComponents.BLUEPRINT)) {
                itemStack = mc.player.getOffhandItem();
            }

            if (itemStack.has(IRDataComponents.BLUEPRINT)) {
                ComponentBlueprint blueprint = itemStack.getOrDefault(IRDataComponents.BLUEPRINT, ComponentBlueprint.EMPTY);
                if (blueprint.controllerPos() == null || blueprint.direction() == null || blueprint.multiblock() == null)
                    return;

                Multiblock multiblock = blueprint.multiblock();
                Vec3i relativeControllerPos = MultiblockHelper.getRelativeControllerPos(multiblock);
                BlockPos firstPos = MultiblockHelper.getFirstBlockPos(blueprint.direction(), blueprint.controllerPos(), relativeControllerPos);

                MultiblockPreviewRenderer.renderPreview(multiblock, firstPos, mc.level, HorizontalDirection.NORTH, poseStack, bufferSource, cameraPos);
            }
        }
    }

    private static void renderNetworkNodes(RenderLevelStageEvent event) {
        Vec3 cameraPos = event.getCamera().getPosition();
        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource bufferSource = event.getLevelRenderer().renderBuffers.bufferSource();

        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            for (TransportNetwork<?> network : IRRegistries.NETWORK) {
                for (NetworkNode<?> node : ClientNodes.NODES.getOrDefault(network, Collections.emptyMap()).values()) {
                    NetworkNodeRenderer.render(node, poseStack, bufferSource, cameraPos);
                }

                for (BlockPos interactor : ClientNodes.INTERACTORS.getOrDefault(network, Collections.emptySet())) {
                    NetworkNodeRenderer.renderInteractor(interactor, poseStack, bufferSource, cameraPos);
                }
            }

            if (NetworkNodeRenderer.selectedNode != null) {
                NetworkNodeRenderer.render(NetworkNodeRenderer.selectedNode, poseStack, bufferSource, cameraPos);
            }
        }
    }
}
