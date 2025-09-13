package com.indref.industrial_reforged.registries;

import com.indref.industrial_reforged.content.blockentities.generators.BasicGeneratorBlockEntity;
import com.indref.industrial_reforged.content.blockentities.machines.CentrifugeBlockEntity;
import com.indref.industrial_reforged.content.blocks.generators.BasicGeneratorBlock;
import com.indref.industrial_reforged.content.blocks.machines.CentrifugeBlock;
import com.indref.industrial_reforged.content.menus.BasicGeneratorMenu;
import com.indref.industrial_reforged.content.menus.CentrifugeMenu;
import com.indref.industrial_reforged.util.machine.AltContainerFactory;
import com.indref.industrial_reforged.util.machine.IRMachine;
import com.indref.industrial_reforged.util.machine.MachineRegistrationHelper;

public final class IRMachines {
    public static final MachineRegistrationHelper HELPER = new MachineRegistrationHelper(IRBlocks.BLOCKS, IRItems.ITEMS, IRBlockEntityTypes.BLOCK_ENTITIES, IRMenuTypes.MENUS);

    public static final IRMachine BASIC_GENERATOR = HELPER.registerMachine("basic_generator", IRMachine.builder(IREnergyTiers.LOW)
            .block(BasicGeneratorBlock::new)
            .blockEntity(BasicGeneratorBlockEntity::new)
            .menu(BasicGeneratorMenu::new));
    public static final IRMachine CENTRIFUGE = HELPER.registerMachine("centrifuge", IRMachine.builder(IREnergyTiers.LOW)
            .block(CentrifugeBlock::new)
            .blockEntity(CentrifugeBlockEntity::new)
            .menu(CentrifugeMenu::new));
}
