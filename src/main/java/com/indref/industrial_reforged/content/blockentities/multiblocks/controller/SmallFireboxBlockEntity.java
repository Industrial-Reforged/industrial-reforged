package com.indref.industrial_reforged.content.blockentities.multiblocks.controller;

import com.indref.industrial_reforged.IRConfig;
import com.indref.industrial_reforged.api.capabilities.IRCapabilities;
import com.indref.industrial_reforged.api.capabilities.heat.IHeatStorage;
import com.indref.industrial_reforged.content.multiblocks.SmallFireboxMultiblock;
import com.indref.industrial_reforged.registries.IRBlockEntityTypes;
import com.indref.industrial_reforged.content.multiblocks.tiers.FireboxTiers;
import com.indref.industrial_reforged.translations.IRTranslations;
import com.portingdeadmods.portingdeadlibs.api.blockentities.multiblocks.FakeBlockEntity;
import com.portingdeadmods.portingdeadlibs.api.blockentities.multiblocks.SavesControllerPosBlockEntity;
import com.portingdeadmods.portingdeadlibs.utils.BlockUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class SmallFireboxBlockEntity extends FireboxBlockEntity implements FakeBlockEntity, SavesControllerPosBlockEntity {
    private BlockPos mainControllerPos;
    private final Map<BlockPos, BlockCapabilityCache<IHeatStorage, Direction>> aboveBlockCapCache;

    public SmallFireboxBlockEntity(BlockPos pos, BlockState state) {
        super(IRBlockEntityTypes.SMALL_FIREBOX.get(), pos, state, FireboxTiers.SMALL, IRConfig.smallFireboxHeatCapacity);
        this.aboveBlockCapCache = new HashMap<>();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        initCapCache();
    }

    public void initCapCache() {
        if (level instanceof ServerLevel serverLevel) {
            for (BlockPos pos : BlockUtils.getBlocksAroundSelf3x3(worldPosition)) {
                if (level.getBlockEntity(pos) instanceof SmallFireboxBlockEntity be && worldPosition.equals(be.getActualBlockEntityPos())) {
                    this.aboveBlockCapCache.put(pos.above(), BlockCapabilityCache.create(
                            IRCapabilities.HeatStorage.BLOCK,
                            serverLevel,
                            pos.above(),
                            Direction.DOWN,
                            () -> !this.isRemoved(),
                            this::initCapCache
                    ));
                }
            }
            this.aboveBlockCapCache.put(worldPosition.above(), BlockCapabilityCache.create(
                    IRCapabilities.HeatStorage.BLOCK,
                    serverLevel,
                    worldPosition.above(),
                    Direction.DOWN,
                    () -> !this.isRemoved(),
                    this::initCapCache
            ));
        }
    }

    @Override
    public void commonTick() {
        if (actualBlockEntity()) {
            getHeatStorage().setLastHeatStored(getHeatStorage().getHeatStored());

            tickRecipe();

            tickIO();

            if (getBlockState().getValue(SmallFireboxMultiblock.FORMED) && this.getBurnTime() > 0 && level.isClientSide() && level.random.nextInt(0, 35) == 0) {
                level.playLocalSound(worldPosition, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 2.0F, 1.0F, false);
            }

            if (!level.isClientSide()) {
                tickHeat();
            }
        }

    }

    @Override
    protected void tickIO() {
        if (!level.isClientSide()) {
            // Only export heat to block directly above
            if (aboveBlockCapCache != null) {
                for (BlockPos pos : aboveBlockCapCache.keySet()) {
                    IHeatStorage aboveHeatStorage = aboveBlockCapCache.get(pos).getCapability();
                    if (aboveHeatStorage != null && level != null) {
                        IHeatStorage thisHeatStorage = getHeatStorage();
                        float deltaT = (float) ((thisHeatStorage.getHeatStored() - aboveHeatStorage.getHeatStored()) * 0.1); // Spread factor
                        aboveHeatStorage.fill(deltaT, false);
                        thisHeatStorage.drain(deltaT, false);
                    }
                }
            }
        }
    }

    public float getHeatDecay() {
        return IRConfig.smallFireboxHeatDecay;
    }

    public float getProductionAmount() {
        return IRConfig.smallFireboxHeatProduction;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return IRTranslations.Menus.SMALL_FIREBOX.component();
    }

    @Override
    public boolean actualBlockEntity() {
        return worldPosition.equals(mainControllerPos);
    }

    @Override
    public @Nullable BlockPos getActualBlockEntityPos() {
        return mainControllerPos;
    }

    @Override
    public void setBlockActive(boolean value) {
        level.setBlockAndUpdate(worldPosition, getBlockState().setValue(SmallFireboxMultiblock.ACTIVE, value));
        for (BlockPos pos : BlockUtils.getBlocksAroundSelf3x3(worldPosition)) {
            if (level.getBlockEntity(pos) instanceof SmallFireboxBlockEntity be) {
                if (be.getActualBlockEntityPos() != null && be.getActualBlockEntityPos().equals(worldPosition)) {
                    level.setBlockAndUpdate(pos, level.getBlockState(pos).setValue(SmallFireboxMultiblock.ACTIVE, value));
                }
            }
        }
    }

    @Override
    public void setControllerPos(BlockPos blockPos) {
        this.mainControllerPos = blockPos;
    }

    @Override
    protected void saveData(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveData(tag, provider);
        BlockPos controllerPos = getActualBlockEntityPos();
        if (controllerPos != null) {
            tag.putLong("mainControllerPos", controllerPos.asLong());
        }
    }

    @Override
    protected void loadData(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadData(tag, provider);
        long mainControllerPos1 = tag.getLong("mainControllerPos");
        this.mainControllerPos = BlockPos.of(mainControllerPos1);
    }
}
