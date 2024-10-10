package com.indref.industrial_reforged.content.commands;

import com.indref.industrial_reforged.util.EnergyNetUtils;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

public class ENetsInfoCommand {
    public ENetsInfoCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("indref")
                .then(Commands.literal("energyNets")
                        .then(Commands.literal("info")
                                .executes(commandContext -> infoOnENets(commandContext.getSource())))));
    }

    private int infoOnENets(CommandSourceStack source) {
        ServerLevel level = source.getLevel();
        Player player = source.getPlayer();
        player.sendSystemMessage(Component.literal("EnergyNet amount: " + EnergyNetUtils.getEnergyNets(level).getEnets().getNetworks().size()));
        player.sendSystemMessage(Component.literal("EnergyNets: " + EnergyNetUtils.getEnergyNets(level).getEnets().getNetworks()));
        return 1;
    }
}
