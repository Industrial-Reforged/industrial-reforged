---
navigation:
  title: "Firebox"
  icon: "indref:firebox_controller"
  parent: indref:primitive_age.md
  position: 3
---

# Firebox

> Multiblock previews show the unformed multiblock on the left and the formed multiblock on the right.
> The green box indicates the controller block.

## Refractory Firebox

<GameScene zoom="3" interactive={true} fullWidth={true}>
    <MultiblockShape multiblock="indref:refractory_firebox"> </MultiblockShape>
</GameScene>

- **Not Rotatable**
- **Static Size**

The refractory firebox or just firebox is the first multiblock that you will build in Industrial Reforged.
It is built as shown above and consists of a <ItemImage id="indref:coil" scale="0.6" /> **Copper Coil** and <ItemImage id="indref:refractory_brick" scale="0.6" /> **8 Refractory Bricks**.
To assemble the multiblock, click on the block highlighted with a green box (the copper coil) using a <ItemImage id="indref:hammer" scale="0.6" /> [Hammer](./tools.md#hammer).

The firebox can be used to burn any furnace fuel to produce heat.
The firebox has a gui that can be used to manually insert fuel and also shows the current amount of heat.
Fuel can also be inserted using a hopper or any item pipe connected to any of the firebox's blocks.

While the firebox can spread heat to any of the blocks above it.
**[WIP]** The center block will spread **40%** of the firebox's heat while the other blocks will each spread **7.5%** of the firebox's heat, adding up to 100%.
While this means that the firebox is most efficient when all 3x3 blocks above it are covered, it can also be used for any other block using heat.

<GameScene zoom="3" interactive={true} fullWidth={true}>
<MultiblockShape multiblock="indref:refractory_firebox" unformed={false} showController={false}> </MultiblockShape>
<BoxAnnotation min="-1 0 -1" max="2 1 2" color="#AAAAAA">
Center: 40%
Surrounding: 7.5%
</BoxAnnotation>
<BlockAnnotation x="0" y="0" z="0" color="#FFFFFF" />
</GameScene>

The first use for the firebox is heating the <ItemImage id="indref:ceramic_crucible_controller" scale="0.6" /> [Crucible](./crucible.md).

## Small Firebox

<GameScene zoom="3" interactive={true} fullWidth={true}>
    <MultiblockShape multiblock="indref:small_firebox"> </MultiblockShape>
</GameScene>

- **Rotatable**
- **Static Size**

The small firebox is rather inefficient and expensive for heating the crucible.
However, when used for heating the <ItemImage id="indref:blast_furnace_controller" scale="0.6" /> [Blast Furnace](./blast_furnace.md) it is a lot more efficient that the regular firebox.

The multiblock is built with <ItemImage id="indref:small_firebox_hatch" scale="0.6" /> **4 Small Firebox Hatches** and assembled by clicking on any of them using a <ItemImage id="indref:hammer" scale="0.6" /> [Hammer](./tools.md#hammer).
