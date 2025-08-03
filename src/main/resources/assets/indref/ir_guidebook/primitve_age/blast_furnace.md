---
navigation:
  title: "Blast Furnace"
  icon: "indref:blast_furnace_controller"
  parent: indref:primitive_age.md
  position: 6
item_ids:
  - indref:blast_furnace_controller
  - indref:blast_furnace_hatch
  - indref:blast_furnace_bricks
---

# Blast Furnace

<GameScene zoom="3" interactive={true} fullWidth={true}>
    <MultiblockShape multiblock="indref:blast_furnace"> </MultiblockShape>
</GameScene>

- **Rotatable**
- **Dynamic Size**

The Blast Furnace is a multiblock with a dynamic size that can vary between 3 and 6 layers of brick blocks.
The multiblock preview shows the max size Blast Furnace with 6 layers of brick.

The Blast Furnace is used to produce steel and melt metals that have a rather high melting temperature like bauxite/aluminum.

Just like the <ItemImage id="indref:ceramic_crucible_controller" scale="0.6" /> [Ceramic Crucible](./crucible.md), the Blast Furnace also needs heat from below.
However, since the Blast Furnace is a 2x2 mutliblock, you should use the [Small Firebox](./firebox.md#small-firebox) to heat it.

After assembling the multiblock, items can either be inserted manually or by using a hopper on any of the blocks.

The recipes as well as the heat/time required for a recipe are visible in JEI.

Once the blast furnace is done with a recipe and is filled with molten metal, place a faucet on one of the hatch blocks and a casting basin below it.
Power the faucet with redstone to start casting. For more info look at the [Casting Section](./casting.md)
