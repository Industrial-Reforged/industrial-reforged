---
navigation:
  title: "Misc Blocks"
  icon: "indref:crafting_station"
  parent: indref:primitive_age.md
  position: 2
---

# Primitive Machines

## Drain

<ItemImage id="indref:drain" scale="4" />

The drain is a simple block that will drain the block above it, making it useful for collecting infinite amounts of water.
While it does not have a gui, right-clicking it will display the contained fluid and amount.

<GameScene zoom="3" interactive={true} fullWidth={true}>
    <Block id="indref:drain" y="-1" />
    <Block id="minecraft:water" z="1" />
    <Block id="minecraft:water" z="0" />
    <Block id="minecraft:water" z="-1" />
</GameScene>

## Crafting Station

<ItemImage id="indref:crafting_station" scale="4" />

The crafting station is simply a more convenient crafting table that will keep items in the crafting grid.
The block also has a small storage.
