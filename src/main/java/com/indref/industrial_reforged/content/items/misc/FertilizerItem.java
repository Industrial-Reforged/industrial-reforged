package com.indref.industrial_reforged.content.items.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.BaseCoralWallFanBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import javax.annotation.Nullable;
import java.util.Random;

public class FertilizerItem extends BoneMealItem {

    public FertilizerItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        Level level = useOnContext.getLevel();
        BlockPos blockpos = useOnContext.getClickedPos();
        BlockPos blockpos1 = blockpos.relative(useOnContext.getClickedFace());
        if (applyFertilizer(useOnContext.getItemInHand(), level, blockpos, useOnContext.getPlayer())) {
            if (!level.isClientSide) {
                useOnContext.getPlayer().gameEvent(GameEvent.ITEM_INTERACT_FINISH);
                level.levelEvent(1505, blockpos, 0);
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            BlockState blockstate = level.getBlockState(blockpos);
            boolean flag = blockstate.isFaceSturdy(level, blockpos, useOnContext.getClickedFace());
            if (flag && growWaterPlant(useOnContext.getItemInHand(), level, blockpos1, useOnContext.getClickedFace())) {
                if (!level.isClientSide) {
                    useOnContext.getPlayer().gameEvent(GameEvent.ITEM_INTERACT_FINISH);
                    level.levelEvent(1505, blockpos1, 0);
                }

                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    public static boolean applyFertilizer(ItemStack itemStack, Level level, BlockPos blockPos, Player player) {
        Random random = new Random();
        int randomNumber = random.nextInt(0, 4);
        BlockState blockstate = level.getBlockState(blockPos);
        int hook = net.neoforged.neoforge.event.EventHooks.onApplyBonemeal(player, level, blockPos, blockstate, itemStack);
        if (hook != 0) return hook > 0;
        Block block = blockstate.getBlock();
        if (block instanceof BonemealableBlock bonemealableblock && bonemealableblock.isValidBonemealTarget(level, blockPos, blockstate)) {
            if (level instanceof ServerLevel) {
                if (bonemealableblock.isBonemealSuccess(level, level.random, blockPos, blockstate)) {
                    bonemealableblock.performBonemeal((ServerLevel)level, level.random, blockPos, blockstate);
                }

                if (randomNumber == 0) {
                    itemStack.shrink(1);
                }
            }

            return true;
        }

        return false;
    }

    public static boolean growWaterPlant(ItemStack itemStack, Level level, BlockPos blockPos, @Nullable Direction direction) {
        Random random = new Random();
        int randomNumber = random.nextInt(0, 4);
        if (level.getBlockState(blockPos).is(Blocks.WATER) && level.getFluidState(blockPos).getAmount() == 8) {
            if (!level.isClientSide()) {
                RandomSource randomsource = level.getRandom();

                outer:
                for (int i = 0; i < 128; ++i) {
                    BlockPos blockpos = blockPos;
                    BlockState blockstate = Blocks.SEAGRASS.defaultBlockState();

                    for (int j = 0; j < i / 16; ++j) {
                        blockpos = blockpos.offset(
                                randomsource.nextInt(3) - 1, (randomsource.nextInt(3) - 1) * randomsource.nextInt(3) / 2, randomsource.nextInt(3) - 1
                        );
                        if (level.getBlockState(blockpos).isCollisionShapeFullBlock(level, blockpos)) {
                            continue outer;
                        }
                    }

                    Holder<Biome> holder = level.getBiome(blockpos);
                    if (holder.is(BiomeTags.PRODUCES_CORALS_FROM_BONEMEAL)) {
                        if (i == 0 && direction != null && direction.getAxis().isHorizontal()) {
                            blockstate = BuiltInRegistries.BLOCK
                                    .getTag(BlockTags.WALL_CORALS)
                                    .flatMap(p_204098_ -> p_204098_.getRandomElement(level.random))
                                    .map(p_204100_ -> p_204100_.value().defaultBlockState())
                                    .orElse(blockstate);
                            if (blockstate.hasProperty(BaseCoralWallFanBlock.FACING)) {
                                blockstate = blockstate.setValue(BaseCoralWallFanBlock.FACING, direction);
                            }
                        } else if (randomsource.nextInt(4) == 0) {
                            blockstate = BuiltInRegistries.BLOCK
                                    .getTag(BlockTags.UNDERWATER_BONEMEALS)
                                    .flatMap(p_204091_ -> p_204091_.getRandomElement(level.random))
                                    .map(p_204095_ -> p_204095_.value().defaultBlockState())
                                    .orElse(blockstate);
                        }
                    }

                    if (blockstate.is(BlockTags.WALL_CORALS, p_204093_ -> p_204093_.hasProperty(BaseCoralWallFanBlock.FACING))) {
                        for (int k = 0; !blockstate.canSurvive(level, blockpos) && k < 4; ++k) {
                            blockstate = blockstate.setValue(BaseCoralWallFanBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(randomsource));
                        }
                    }

                    if (blockstate.canSurvive(level, blockpos)) {
                        BlockState blockstate1 = level.getBlockState(blockpos);
                        if (blockstate1.is(Blocks.WATER) && level.getFluidState(blockpos).getAmount() == 8) {
                            level.setBlock(blockpos, blockstate, 3);
                        } else if (blockstate1.is(Blocks.SEAGRASS) && randomsource.nextInt(10) == 0) {
                            ((BonemealableBlock) Blocks.SEAGRASS).performBonemeal((ServerLevel) level, randomsource, blockpos, blockstate1);
                        }
                    }
                }


                if (randomNumber == 0) {
                    itemStack.shrink(1);
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
