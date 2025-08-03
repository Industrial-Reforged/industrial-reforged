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
Just like the <ItemImage id="indref:ceramic_crucible_controller" scale="0.6" /> <ItemLink id="indref:ceramic_crucible_controller" />, the Blast Furnace also needs heat from below.
However, since the Blast Furnace is a 2x2 mutliblock, you should use the [Small Firebox](./firebox.md#small-firebox) to heat the Blast Furnace.
