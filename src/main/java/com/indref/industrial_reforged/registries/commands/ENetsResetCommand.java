package com.indref.industrial_reforged.registries.commands;

import com.indref.industrial_reforged.util.EnergyNetUtils;
import com.indref.industrial_reforged.util.Utils;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

public class ENetsResetCommand {
    public ENetsResetCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("indref")
                .then(Commands.literal("energyNets")
                        .then(Commands.literal("reset")
                .executes(commandContext -> resetENets(commandContext.getSource())))));
    }

    private int resetENets(CommandSourceStack source) {
        ServerLevel level = source.getLevel();
        EnergyNetUtils.resetNets(level);
        source.sendSuccess(() -> Component.literal("Reset energy nets"), true);
        return 1;
    }
}
