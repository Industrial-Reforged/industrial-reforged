package com.indref.industrial_reforged.commands;

import com.indref.industrial_reforged.util.Util;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class EnetsInfoCommand {
    public EnetsInfoCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("indref")
                .then(Commands.literal("energyNets")
                        .then(Commands.literal("info")
                                .executes(commandContext -> infoOnENets(commandContext.getSource())))));
    }

    private int infoOnENets(CommandSourceStack source) {
        Level level = source.getLevel();
        Player player = source.getPlayer();
        player.sendSystemMessage(Component.literal("EnergyNet amount: "+Util.getEnergyNets(level).getNetworks().size()));
        player.sendSystemMessage(Component.literal("EnergyNets: "+Util.getEnergyNets(level).getNetworks()));
        return 1;
    }
}
