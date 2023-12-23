package com.indref.industrial_reforged.registries.blocks.multiblocks;

import com.indref.industrial_reforged.api.multiblocks.IMultiBlockController;
import com.indref.industrial_reforged.api.multiblocks.IMultiblock;
import com.indref.industrial_reforged.api.tiers.CrucibleTiers;
import com.indref.industrial_reforged.api.tiers.templates.CrucibleTier;
import com.indref.industrial_reforged.capabilities.IRCapabilities;
import com.indref.industrial_reforged.registries.IRItems;
import com.indref.industrial_reforged.registries.blockentities.CrucibleBlockEntity;
import com.indref.industrial_reforged.registries.multiblocks.CrucibleMultiblock;
import com.indref.industrial_reforged.util.BlockUtils;
import com.indref.industrial_reforged.util.Util;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.attachment.AttachmentHolder;
import net.neoforged.neoforge.attachment.AttachmentInternals;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class CrucibleControllerBlock extends BaseEntityBlock implements IMultiBlockController {
    public static final MapCodec<CrucibleControllerBlock> CODEC = simpleCodec((properties1) -> new CrucibleControllerBlock(properties1, CrucibleTiers.CERAMIC));
    private final CrucibleTier tier;

    public CrucibleControllerBlock(Properties properties, CrucibleTier crucibleTier) {
        super(properties);
        tier = crucibleTier;
    }

    public CrucibleTier getTier() {
        return tier;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        return tier.getController().asItem().getDefaultInstance();
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (!level.isClientSide()) {
            IFluidHandler fluidHandler = BlockUtils.getBlockEntityCapability(Capabilities.FluidHandler.BLOCK, level, level.getBlockEntity(blockPos));
            if (player.getMainHandItem().is(IRItems.ALUMINUM_INGOT.get())) {
                fluidHandler.fill(new FluidStack(Fluids.WATER, 100), IFluidHandler.FluidAction.EXECUTE);
            }
            NetworkHooks.openScreen((ServerPlayer) player, (CrucibleBlockEntity) level.getBlockEntity(blockPos), blockPos);
            player.sendSystemMessage(Component.literal(Util.fluidStackToString(fluidHandler.getFluidInTank(0))));
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new CrucibleBlockEntity(p_153215_, p_153216_);
    }

    @Override
    public IMultiblock getMultiblock() {
        return CrucibleMultiblock.CERAMIC;
    }
}
