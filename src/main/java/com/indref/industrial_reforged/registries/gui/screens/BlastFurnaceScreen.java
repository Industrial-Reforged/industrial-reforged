package com.indref.industrial_reforged.registries.gui.screens;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.api.gui.IRAbstractContainerScreen;
import com.indref.industrial_reforged.registries.gui.components.FluidTankGuiComponent;
import com.indref.industrial_reforged.registries.gui.components.HeatDisplayGuiComponent;
import com.indref.industrial_reforged.registries.gui.menus.BlastFurnaceMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public class BlastFurnaceScreen extends IRAbstractContainerScreen<BlastFurnaceMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(IndustrialReforged.MODID, "textures/gui/blast_furnace.png");
    private static final ResourceLocation PROGRESS_SPRITE = ResourceLocation.withDefaultNamespace("container/furnace/burn_progress");

    public BlastFurnaceScreen(BlastFurnaceMenu p_97741_, Inventory p_97742_, Component p_97743_) {
        super(p_97741_, p_97742_, p_97743_);
    }

    @Override
    protected void init() {
        super.init();
        initComponents(
                new FluidTankGuiComponent(new Vector2i(this.leftPos + 97, this.topPos + 17), FluidTankGuiComponent.TankVariants.LARGE),
                new HeatDisplayGuiComponent(new Vector2i(this.leftPos, this.topPos), false)
        );
    }

    @Override
    public @NotNull ResourceLocation getBackgroundTexture() {
        return TEXTURE;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float delta, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, delta, mouseX, mouseY);
        float progress = (float) this.menu.blockEntity.getProgress() / this.menu.blockEntity.getMaxProgress();
        int scaledProgress = Mth.ceil(progress * 24.0F);
        guiGraphics.blitSprite(PROGRESS_SPRITE, 24, 16, 0, 0, this.leftPos + 67, this.topPos + 38, scaledProgress, 16);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
