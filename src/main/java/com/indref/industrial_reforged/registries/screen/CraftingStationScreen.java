package com.indref.industrial_reforged.registries.screen;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.networking.data.ItemNbtSyncData;
import com.indref.industrial_reforged.registries.items.misc.BlueprintItem;
import com.indref.industrial_reforged.util.SimpleFunction;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class CraftingStationScreen extends AbstractContainerScreen<CraftingStationMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(IndustrialReforged.MODID, "textures/gui/crafting_station.png");

    public CraftingStationScreen(CraftingStationMenu p_97741_, Inventory p_97742_, Component p_97743_) {
        super(p_97741_, p_97742_, p_97743_);
    }

    @Override
    protected void init() {
        this.imageHeight = 209;
        super.init();
        this.inventoryLabelY = 1000;
        this.titleLabelY = 1000;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
        ItemStack itemStack = getMenu().getSlot(CraftingStationMenu.BLUEPRINT_SLOT).getItem();
        addImageButton(x + 153, y + 46, "recipe_transfer", transferRecipe);
        addImageButton(x + 153, y + 62, "recipe_set", () -> BlueprintItem.setRecipe(CraftingStationMenu.BLUEPRINT_SLOT, menu));
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    private void addImageButton(int x, int y, String path, SimpleFunction onClick) {
        addRenderableWidget(new ImageButton(x, y, 20, 20, new WidgetSprites(
                new ResourceLocation("indref", "widget/" + path),
                new ResourceLocation("indref", "widget/" + path),
                new ResourceLocation("indref", "widget/" + path + "_highlighted")
        ), (button) -> onClick.call(), Component.empty()));
    }

    private final SimpleFunction transferRecipe = () -> {
        CompoundTag tag = new CompoundTag();
        PacketDistributor.SERVER.noArg().send(new ItemNbtSyncData(CraftingStationMenu.BLUEPRINT_SLOT, "storedRecipe", tag));
    };
}
