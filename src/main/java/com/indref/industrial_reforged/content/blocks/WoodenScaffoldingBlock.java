package com.indref.industrial_reforged.content.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WoodenScaffoldingBlock extends Block {
    private static final VoxelShape COLLISION_SHAPE = box(1, 0, 1, 15, 16, 15);
    private static final VoxelShape FULL_SHAPE = Shapes.or(
            box(0, 0, 0, 16, 4, 16),
            box(0, 12, 0, 16, 16, 16)
    );
    public static final VoxelShape CHECK_SHAPE = Shapes.box(0, -20, 0, 1, -19, 1);

    public WoodenScaffoldingBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        boolean checkForClimbing = context.isAbove(CHECK_SHAPE, pos, false);
        if (checkForClimbing)
            return COLLISION_SHAPE;
        else
            return FULL_SHAPE;
    }

    @Override
    public boolean isLadder(BlockState state, LevelReader level, BlockPos pos, LivingEntity entity) {
        return true;
    }

    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        super.entityInside(state, worldIn, pos, entityIn);
        if (entityIn instanceof LivingEntity && isLadder(state, worldIn, pos, (LivingEntity) entityIn))
            applyLadderLogic(entityIn);
    }

    public static void applyLadderLogic(Entity entityIn) {
        if (entityIn instanceof LivingEntity livingEntity && !livingEntity.onClimbable()) {
            Vec3 motion = entityIn.getDeltaMovement();
            float maxMotion = 0.15F;
            motion = new Vec3(
                    Mth.clamp(motion.x, -maxMotion, maxMotion),
                    Math.max(motion.y, -maxMotion),
                    Mth.clamp(motion.z, -maxMotion, maxMotion)
            );

            entityIn.fallDistance = 0.0F;

            if (motion.y < 0 && entityIn instanceof Player && entityIn.isShiftKeyDown())
                motion = new Vec3(motion.x, 0, motion.z);
            else if (entityIn.horizontalCollision)
                motion = new Vec3(motion.x, 0.2, motion.z);
            entityIn.setDeltaMovement(motion);
        }
    }
}
