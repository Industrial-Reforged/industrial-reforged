package com.indref.industrial_reforged.registries.multiblocks;

import com.indref.industrial_reforged.api.multiblocks.IMultiblock;
import com.indref.industrial_reforged.api.multiblocks.MultiblockDirection;
import com.indref.industrial_reforged.api.tiers.CrucibleTiers;
import com.indref.industrial_reforged.api.tiers.templates.CrucibleTier;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record CrucibleMultiblock(CrucibleTier tier) implements IMultiblock {
    public static final CrucibleMultiblock CERAMIC = new CrucibleMultiblock(CrucibleTiers.CERAMIC);

    @Override
    public Block getController() {
        return tier.getController();
    }

    @Override
    public List<List<Integer>> getLayout() {
        return List.of(
                List.of(
                        0, 0, 0,
                        0, 2, 0,
                        0, 0, 0
                ),
                List.of(
                        0, 0, 0,
                        0, 1, 0,
                        0, 0, 0
                )
        );
    }

    @Override
    public Map<Integer, @Nullable Block> getDefinition() {
        Map<Integer, Block> def = new HashMap<>();
        def.put(0, tier.getCrucibleWallBlock());
        def.put(1, null);
        def.put(2, tier.getController());
        return def;
    }

    @Override
    public void formBlock(Level level, MultiblockDirection direction, BlockPos blockPos, int index, int indexY) {
        Minecraft.getInstance().player.sendSystemMessage(Component.literal("Forming..."));
    }

    @Override
    public void unformBlock(Level level, BlockPos blockPos) {

    }
}
