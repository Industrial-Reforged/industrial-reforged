package com.indref.industrial_reforged.client.widgets;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.client.screen.CrucibleScreen;
import com.indref.industrial_reforged.networking.EmptyCruciblePayload;
import com.portingdeadmods.portingdeadlibs.api.blockentities.ContainerBlockEntity;
import com.portingdeadmods.portingdeadlibs.impl.client.screens.widgets.FluidTankWidget;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class CrucibleFluidTankWidget extends FluidTankWidget {
    private final IFluidHandler fluidHandler;
    private final BlockPos pos;

    public CrucibleFluidTankWidget(int x, int y, TankVariants variant, ContainerBlockEntity entity) {
        super(x, y, variant, entity);
        this.fluidHandler = entity.getFluidHandler();
        this.pos = entity.getBlockPos();
    }

    @Override
    public List<Component> getFluidTooltip() {
        List<Component> fluidTooltip = super.getFluidTooltip();
        if (CrucibleScreen.hasShiftDown() && !fluidHandler.getFluidInTank(0).isEmpty()) {
            fluidTooltip.add(Component.literal("Alt + Shift Click to empty").withStyle(ChatFormatting.YELLOW));
        }
        return fluidTooltip;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (Screen.hasAltDown() && Screen.hasShiftDown()) {
            PacketDistributor.sendToServer(new EmptyCruciblePayload(this.pos));
            IndustrialReforged.LOGGER.debug("Clickeddd");
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
