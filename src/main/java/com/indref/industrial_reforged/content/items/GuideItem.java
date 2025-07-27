package com.indref.industrial_reforged.content.items;

import com.indref.industrial_reforged.compat.guideme.IRGuide;
import com.indref.industrial_reforged.translations.IRTranslations;
import com.indref.industrial_reforged.util.CompatUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GuideItem extends Item {
    public GuideItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (level.isClientSide) {
            if (CompatUtils.isGuideMELoaded()) {
                player.swing(usedHand);
                IRGuide.openGuide(player);
            } else {
                player.sendSystemMessage(IRTranslations.Messages.GUIDE_ME_MISSING.component().withStyle(ChatFormatting.RED));
            }
        }
        return super.use(level, player, usedHand);
    }
}
