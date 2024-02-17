package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.registries.commands.ENetsInfoCommand;
import com.indref.industrial_reforged.registries.commands.ENetsResetCommand;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.server.command.ConfigCommand;

@Mod.EventBusSubscriber(modid = IndustrialReforged.MODID)
public final class IRCommands {
    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        new ENetsResetCommand(event.getDispatcher());
        new ENetsInfoCommand(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());
    }
}
