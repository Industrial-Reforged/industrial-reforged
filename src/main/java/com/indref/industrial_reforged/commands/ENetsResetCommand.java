package com.indref.industrial_reforged.commands;

import com.indref.industrial_reforged.capabilities.energy.network.IEnergyNets;
import com.indref.industrial_reforged.util.Util;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public class ENetsResetCommand {
    public ENetsResetCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("indref")
                .then(Commands.literal("energyNets")
                        .then(Commands.literal("reset")
                .executes(commandContext -> resetENets(commandContext.getSource())))));
    }

    private int resetENets(CommandSourceStack source) {
        Level level = source.getLevel();
        Util.getEnergyNets(level).resetNets();
        source.sendSuccess(() -> Component.literal("Reset energy nets"), true);
        return 1;
    }
}
