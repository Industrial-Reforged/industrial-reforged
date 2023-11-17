package com.indref.industrial_reforged.api.blocks;

import com.indref.industrial_reforged.registries.IRItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.neoforged.neoforge.common.IPlantable;
import org.jetbrains.annotations.NotNull;

import static net.neoforged.neoforge.common.CommonHooks.onCropsGrowPost;
import static net.neoforged.neoforge.common.CommonHooks.onCropsGrowPre;

public class TallCropBlock extends CropBlock {
    public static final int FIRST_STAGE_MAX_AGE = 6;
    public static final int SECOND_STAGE_MAX_AGE = 1;
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 7);

    public TallCropBlock(Properties properties) {
        super(properties);
    }

    public int getFirstStageMaxAge() {
        return FIRST_STAGE_MAX_AGE;
    }

    public int getSecondStageMaxAge() {
        return SECOND_STAGE_MAX_AGE;
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel level, BlockPos blockPos, RandomSource randomSource) {
        if (!level.isAreaLoaded(blockPos, 1)) return;
        if (level.getRawBrightness(blockPos, 0) >= 9) {
            int currentAge = this.getAge(blockState);

            if (currentAge < this.getMaxAge()) {
                float growthSpeed = getGrowthSpeed(this, level, blockPos);

                if (onCropsGrowPre(level, blockPos, blockState, randomSource.nextInt((int)(25.0F / growthSpeed) + 1) == 0)) {
                    if(currentAge == getFirstStageMaxAge()) {
                        if(level.getBlockState(blockPos.above(1)).is(Blocks.AIR)) {
                            level.setBlock(blockPos.above(1), this.getStateForAge(currentAge + 1), 2);
                        }
                    } else {
                        level.setBlock(blockPos, this.getStateForAge(currentAge + 1), 2);
                    }

                    onCropsGrowPost(level, blockPos, blockState);
                }
            }
        }
    }

    @Override
    public boolean canSustainPlant(BlockState blockState, BlockGetter world, BlockPos blockPos, Direction facing, IPlantable plantable) {
        return super.mayPlaceOn(blockState, world, blockPos);
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader level, BlockPos blockPos) {
        return super.canSurvive(blockState, level, blockPos) || (level.getBlockState(blockPos.below(1)).is(this) &&
                level.getBlockState(blockPos.below(1)).getValue(AGE) == 7);
    }

    @Override
    public void growCrops(Level level, BlockPos blockPos, BlockState blockState) {
        int nextAge = this.getAge(blockState) + this.getBonemealAgeIncrease(level);
        int maxAge = this.getMaxAge();
        if(nextAge > maxAge) {
            nextAge = maxAge;
        }

        if(this.getAge(blockState) == getFirstStageMaxAge() && level.getBlockState(blockPos.above(1)).is(Blocks.AIR)) {
            level.setBlock(blockPos.above(1), this.getStateForAge(nextAge), 2);
        } else {
            level.setBlock(blockPos, this.getStateForAge(nextAge - getSecondStageMaxAge()), 2);
        }
    }

    @Override
    public int getMaxAge() {
        return FIRST_STAGE_MAX_AGE + SECOND_STAGE_MAX_AGE;
    }

    @Override
    public @NotNull IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(AGE);
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return IRItems.CORN_SEEDS.get();
    }

}
