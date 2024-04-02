package com.indref.industrial_reforged.registries.commands;

import com.indref.industrial_reforged.util.Utils;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.stream.Stream;

public class TagInfoCommand {
    public TagInfoCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("indref")
                .then(Commands.literal("getTags")
                                .executes(commandContext -> getTagsForItem(commandContext.getSource()))));
    }

    private int getTagsForItem(CommandSourceStack source) {
        Player player = source.getPlayer();
        List<TagKey<Item>> tags = player.getMainHandItem().getTags().toList();
        player.sendSystemMessage(Component.literal(player.getMainHandItem().getItem()+", has the following tags:"));
        player.sendSystemMessage(Component.literal(tags.toString()));
        return 1;
    }
}
