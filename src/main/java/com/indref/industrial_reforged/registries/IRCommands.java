package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.IndustrialReforged;
import com.indref.industrial_reforged.registries.commands.ENetsInfoCommand;
import com.indref.industrial_reforged.registries.commands.ENetsResetCommand;
import com.indref.industrial_reforged.registries.commands.TagInfoCommand;
import com.mojang.datafixers.types.templates.Tag;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.server.command.ConfigCommand;

@EventBusSubscriber(modid = IndustrialReforged.MODID)
public final class IRCommands {
    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        new ENetsResetCommand(event.getDispatcher());
        new ENetsInfoCommand(event.getDispatcher());
        new TagInfoCommand(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());
    }
}
