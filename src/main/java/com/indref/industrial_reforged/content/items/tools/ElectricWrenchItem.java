package com.indref.industrial_reforged.content.items.tools;

public class ElectricWrenchItem {}//extends SimpleEnergyItem {
//    public ElectricWrenchItem(Item.Properties properties, Supplier<EnergyTier> energyTier, int energyUsage, IntSupplier defaultEnergyCapacity) {
//        super(properties, energyTier, defaultEnergyCapacity);
//    }
//
//    @Override
//    public boolean isDamageable(ItemStack stack) {
//        return false;
//    }
//
//    @Override
//    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {
//        Player player = useOnContext.getPlayer();
//        Level level = useOnContext.getLevel();
//        BlockPos clickPos = useOnContext.getClickedPos();
//        BlockState wrenchableBlock = level.getBlockState(clickPos);
//        ItemStack itemInHand = useOnContext.getItemInHand();
//        IEnergyStorage energyStorage = getEnergyCap(itemInHand);
//
//        // only on the server side
//        if (!level.isClientSide) {
//            // check if block can be wrenched
//            if (wrenchableBlock instanceof CustomWrenchableBlock iWrenchableBlockBlock
//                    && iWrenchableBlockBlock.canWrench(level, clickPos, wrenchableBlock)
//                    && player.isCrouching()
//                    && energyStorage.getEnergyStored() >= 10) {
//                // Drop the block itself instead of custom drop
//                if (iWrenchableBlockBlock.getCustomDropItem().isEmpty()) {
//                    ItemStack dropItem = wrenchableBlock.getBlock().asItem().getDefaultInstance();
//                    ItemHandlerHelper.giveItemToPlayer(player, dropItem);
//                }
//                // Drop the custom drop
//                else {
//                    ItemStack dropItem = iWrenchableBlockBlock.getCustomDropItem().get().getDefaultInstance();
//                    ItemHandlerHelper.giveItemToPlayer(player, dropItem);
//                }
//                energyStorage.tryDrainEnergy(10, false);
//                level.removeBlock(clickPos, false);
//                return InteractionResult.SUCCESS;
//            }
//        }
//        return InteractionResult.FAIL;
//    }
//
//    @Override
//    public int getDefaultEnergyCapacity() {
//        return 10000;
//    }
//}

