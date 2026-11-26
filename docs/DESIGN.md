# Ecologica

Ecologica is a NeoForge mod for Minecraft 1.21.1. Its theme is ecology and geology, built as one system: the ground decides what can live, and what lives changes the ground.

The goal is not a longer list of ores, plants, and mobs. The goal is a world that keeps moving when the player stops placing blocks, and that asks the player to pay attention.

## Guiding philosophy

This design follows Klei Wright's video essay ["How can we make Minecraft feel alive? (It isn't structures)"](https://youtu.be/cBmt2FNP1Ug). The essay's claim is that structures do not make a world feel alive. Populations, weather, and succession do, because they change the place and then change what they do in response. Ecologica treats that essay as the filter for what gets built. A feature that does not serve one of the three principles below waits.

Klei's worked example is a tundra based on Nunavut. That example is adopted below as a first-class design target, not as a pile of references. The wetland slice already in the repository is the same idea at a smaller scale: iron that belongs to a living place.

Three principles, used as acceptance tests:

### 1. Sympathy

A mechanic should resemble what the organism or the rock actually does. This is not a demand for simulation accuracy. Real natural history is already full of strong mechanics, and a player who learns the mod should have learned something true about the world outside the game.

Consequences for Ecologica:

- An ore names its host. Bog iron sits in mud, dirt, and clay, and it is shoveled. Banded iron sits in smooth basalt. Neither pretends to be "stone with spots."
- An animal eats what it eats. Muskox take unsprouted lichen, not "any plant," and not the sprouted lichen whose job is to weather rock.
- A process keeps its real shape when that shape is the interesting part. Bog iron is deposited and can come back. Lichen breaks rock down into gravel, sand, and soil. A predator that has eaten is not hungry.

If a real behavior would be dull in play, we drop it. Sympathy is a source of mechanics, not a constraint that every real detail must appear.

### 2. Feedback loops

Actors have to change the world in a way the player can see, and that change has to change what they do next. A mob that wanders and drops loot is not a loop. A herd that eats a lichen mat down, then leaves because the mat is gone, is a loop. A lichen that turns stone into soil, then stops because the stone is gone, is a loop.

Consequences:

- Distribution is not only a worldgen roll. Worldgen sets the starting condition. Runtime behavior moves it.
- New content has to name both directions of its loop before it is considered done: what it changes, and what that change does to it.
- The first milestone deliberately does not pretend to have this loop yet. Bog iron generates and drops. Renewal, grazing, and weathering are later milestones. Shipping a static ore and calling it an ecosystem would fail this principle.

### 3. Stewardship, not conservation

The player is rewarded for watching, learning, and helping. The mod does not lecture, and it does not punish "bad" play with a debuff, a fine, or a ruined drop table that feels like a slap. Abuse mostly fails to pay, because the system the player was extracting has changed.

Consequences:

- Overcrowded muskox give nothing. They are not deleted and the player is not cursed. A herd with room, sky, and its home biome pays more wool.
- A lichen farm that is not restocked with stone stops, because the lichen ate the stone. Shears are how a player pauses that, on purpose.
- Sustainable hunting can replace itself, because a grazed herd that lost an adult and has no babies can enter a long natural breeding period. Killing every animal removes the animals that would have started that period. The distinction is in the system, not in a message.
- Vanilla's "please do not kill the endangered mob" pattern is the anti-pattern. If a creature needs the player, it needs a job the player can do: replace substrate, leave a breeding adult, keep a predator fed somewhere else, let a fen stay wet.

## Rules that follow

These keep the principles from turning into unrelated content:

- **No tag, no change.** Ecologica does not rewrite biomes it has not been invited into. A datapack or a biome mod opts in by adding a biome to a tag such as `ecologica:has_bog_iron`. Terralith wetlands stay as they are until somebody tags them.
- **One home for a material or a creature, expressed as data.** Iron is not "everywhere below Y=64." Bog iron's home is the wetland tag. Banded iron's home will be the tundra greenstone. Gold already has mesas. We copy that pattern.
- **Worldgen is the start, not the life.** Features place the first concretions, belts, and pioneer colonies. The living part is what those do after the chunk exists.
- **Prefer a datapack hook over a hard dependency.** Create, JEI, and biome mods should see tags and ordinary recipes. Custom recipe types wait until a process cannot be expressed as smelting or crafting.
- **Do not add a structure to carry a system.** A herd does not need a pen, and a fen does not need a hut. If the only way to notice a system is a building, the system is not finished.

## Systems

### Geology

Geology answers: what is this ground, what metal or mineral does it host, and what happens when weather and roots touch it.

Parent material is the rock or sediment under a place. Soil, if it exists at all, is what climate and life have done to that parent. The tundra example has almost no soil: deepslate runs from bedrock to the surface, and the "green" of a greenstone belt is smooth basalt, not grass.

Ores are hosted. The host block, the tool, and the amount dropped are part of the ore's identity.

| Ore | Home | Host | How it reads in the world | Status |
| --- | --- | --- | --- | --- |
| Bog iron | `ecologica:has_bog_iron` (swamp, mangrove swamp) | Dirt, coarse dirt, rooted dirt, mud, clay | Soft concretion in wetland sediment. Shovel. Drops one raw bog iron. Silk touch keeps the block. | Implemented |
| Banded iron | Tundra greenstone, a future tag | Smooth basalt | Richer than vanilla iron. Raw iron blocks sit in the belt. A small vein under each surface belt. Iron's tundra home, the way mesas are gold's. | Designed |
| Later: phosphorite, peat, evaporites | Carbonate, waterlogged organic, dry basins | The rock that would actually hold them | Processing that matches the mineral (lime, fuel, salt), not a second furnace ingot | Later |

Processing stays sympathetic and is allowed to be slow:

- Bog iron's honest process is a bloomery: roast, then work the bloom. The furnace recipe in this milestone is a crude shortcut so the drop is already useful. It is not the interesting machine. A later milestone can make the bloomery the better yield and leave the furnace as the impatient path, without punishing furnace use.
- Lime from carbonate rock (calcite or a future limestone) changes soil. That is a geological input into ecology, not a building block for its own sake.
- Peat is fuel and organic matter. Cutting it is also how a fen dries.

Strata, when they arrive, are tag-driven replacements inside a height band. They are not a new chunk generator. A biome mod that already placed the surface stays in charge; Ecologica only adds a belt where the biome asked for one.

### Ecology

Ecology answers: what can persist here, what it eats, and what it leaves behind.

Plants are roles in a succession, not decorations.

1. Pioneer. Lichen on bare rock. It weathers the block under it.
2. Moss. Follows broken rock and trapped dust.
3. Shrubs. A cold-biome shrub shifts color and stature on tundra soil: stunted, orange, yellow. Same lineage, different niche. That is adaptation the player can see.
4. Dwarf trees. At the tree line, spruce and pine stay short. A spruce sapling grown in the lichen woodlands becomes the dwarf form. Dwarf logs strip to dwarf wood and still yield spruce wood, so the tree line is useful without pretending a tall forest grows in the tundra.

Animals are defined by diet, space, and what they do to the plants.

- Muskox graze unsprouted glow lichen only. They never eat the sprouted form. That one rule keeps a herd from erasing the pioneer that makes soil.
- Herds path weakly toward each other and toward lichen colonies. They clump and settle without a fence. Predators scatter them. They find each other again later.
- Drops from muskox (string and wool) scale with conditions that match a healthy herd: fed, in the open, under sky, in the home biome, with some neighbors but not a crowd. A crowded pen gives nothing. The reward is the reason to learn the conditions. There is no separate punishment.
- If a herd member died nearby and there are no babies, grazing can start a long natural breeding period. Hunting that leaves adults and space replaces itself. Clearing the herd does not.
- Predators, starting with polar bears on muskox, stop hunting for about two Minecraft days after a kill. They prefer babies and other vulnerable animals. The kill does not also drop meat; the predator ate it. Leather may still drop. This is how the mod avoids the vanilla failure mode where wolves and foxes empty a biome and the bodies pile up as free food.

Population is an outcome of those rules, not a spawn-weight number the player never sees.

### Biomes

A biome in Ecologica is a climate crossed with a parent material, plus an invitation tag. The same cold climate on deepslate is not the same place as a cold climate on sediment.

The Nunavut tundra, as the essay describes it:

- **Ground.** No dirt cap. Deepslate from bedrock to the surface. Patches of smooth basalt are the greenstone belts, and they are where the metal is.
- **Air.** A pale sky, snow that drifts rather than only falling straight down, ice and grass pushed toward white. A transition sub-biome does the blending so the tundra does not start on a hard edge.
- **Lichen woodlands.** The tree line. Dwarf spruce and pine, dwarf log walls, dwarf log posts. The posts and walls are strippable and give spruce wood. This is a place plants and herds use, not a structure dungeon.
- **Succession on the rock.** Lichen, then moss, then shrubs, in that order, because each stage makes the next one possible.

Vanilla biomes stay vanilla unless tagged. The first tag in the game is `ecologica:has_bog_iron`. Tundra tags (`has_greenstone`, `has_sprouted_lichen`, the muskox home) will follow the same shape when those systems exist.

### How the systems touch

The essay's tundra is one loop with several actors. Written as the loop Ecologica is aiming at:

```text
bare deepslate and greenstone
        │
        ▼
sprouted glow lichen weathers rock ──► gravel, sand, soil
        │                               (and eventually moss, then shrubs)
        │ spreads ordinary lichen
        ▼
unsprouted lichen mats
        │
        ▼
muskox herds settle and graze
        │
        ├── herd thins the mat ──► herd moves toward another colony
        │
        └── a kill, with adults left and no babies ──► a long breeding period
                    │
                    ▼
            polar bear eats, then rests ~2 days
```

The player is in the loop as a steward:

- Bonemeal on glow lichen sprouts it. Naturally, sprouted lichen belongs to the tundra and the lichen woodlands. Bringing it elsewhere is a choice.
- Sunlight and a compatible block let it either weather that block or spread normal lichen out to about eight blocks.
- Shears stop the effect. Mud does not weather the way stone does.
- Because the lichen consumes its substrate, a farm needs fresh stone. That upkeep is the cost of renewable sand and of the phosphorus the lichen becomes. It is also what keeps the machine from being a chunk-loader with no tending.
- Smelting glow lichen yields phosphorus, the stand-in for the glow-ink role: a luminous material that came from the organism, not from a new dungeon chest.
- Replacing stone, leaving the sprouted lichen the muskox will not eat, and not stacking the whole herd into one hole are the high-yield plays.

The wetland is the same shape of loop, started from the other end (the metal first, because that slice is small enough to ship):

```text
waterlogged organic sediment
        │
        ▼
bog iron concretions          (worldgen, and the seep below)
        │
        ▼
fen moss holds on that sediment
        │
        ├── a patch of 4 or more on mud ──► the mud becomes peat
        ├── a patch of 4 or more on peat ──► slow bog iron, until 3 are already nearby
        └── peat with no moss and no water ──► dried fen soil
                    │
                    └── water against the crust ──► mud again, and the patch can rebuild
```

Renewal is the reward for leaving a working fen. A stripped fen dries. Water brings the crust back to mud. No scolding. Vanilla mud is not dried on its own; only the peat the fen built can crust over, so mangrove mud and mud bricks stay as they are.

Banded iron and bog iron are two homes for the same metal. They should not be collapsed into one "iron ore" with two textures. A player who has learned mesas-mean-gold can learn tundra-means-banded-iron and wetlands-mean-bog-iron.

## Milestone 1, implemented

This repository contains the wetland ore only. It is the smallest thing that proves registration, data generation, and worldgen, and it is already sympathetic about its host rock. It is not yet alive. Nothing in this milestone moves after the chunk is generated, and nothing rewards tending. That gap is intentional and is the next milestone.

What is in the game:

- Block `ecologica:bog_iron_ore`. Packed-mud sound, hardness 1.25, orange terracotta map color. Drops 0–1 experience. Requires a shovel. A pickaxe does not collect it.
- Item `ecologica:raw_bog_iron`. Fortune and silk touch follow the vanilla ore loot pattern: fortune on the raw drop, silk touch keeps the block.
- Veins of size 7, 8 attempts per chunk, uniform between Y=48 and Y=68, only inside `ecologica:bog_iron_replaceables` (dirt, coarse dirt, rooted dirt, mud, clay). Exposed faces are allowed, so a muddy bank can show ore.
- Biomes come from the tag `ecologica:has_bog_iron`. Datagen puts swamp and mangrove swamp in that tag. The biome modifier at `data/ecologica/neoforge/biome_modifier/bog_iron_ore.json` points at the tag, so a pack adds a biome by editing the tag, not by editing Java. The modifier is handwritten for that reason: datagen cannot see a tag it is in the middle of writing.
- Common tags `c:ores/iron`, `c:ore_rates/singular`, and `c:raw_materials/iron`, so other mods can recognize the metal. The block is sediment-hosted, not `c:ores_in_ground/stone`.
- Smelting and blasting recipes turn raw bog iron into an iron ingot, at the same experience and time as raw iron. This is the shortcut described above.

Code map:

| Piece | Path |
| --- | --- |
| Mod entry, registers the data generator | `src/main/java/com/allen6297/ecologica/Ecologica.java` |
| Block, item, creative tab | `src/main/java/com/allen6297/ecologica/registry/` |
| Tag keys | `src/main/java/com/allen6297/ecologica/tag/EcologicaTags.java` |
| Vein size, height, feature keys | `src/main/java/com/allen6297/ecologica/worldgen/EcologicaWorldgen.java` |
| Models, language, loot, recipes, tags, features | `src/main/java/com/allen6297/ecologica/datagen/` |
| Generated JSON | `src/generated/resources/` |
| Fen moss block and the seep | `src/main/java/com/allen6297/ecologica/block/FenMossBlock.java` |
| Biome modifiers | `src/main/resources/data/ecologica/neoforge/biome_modifier/` |

## Milestone 2, implemented

Fen moss is the first thing in the mod that changes the world after the chunk exists.

- Block `ecologica:fen_moss`. A carpet. It survives only on `ecologica:fen_moss_supports`: the bog-iron sediments, plus bog iron ore so the seep does not uproot itself. Anywhere else it pops, and the item drops, so a dug-up transplant can be replanted.
- It generates on the surface in `ecologica:has_bog_iron`, two patches a chunk, and only where the block below is sediment. Grass and open water do not take it.
- On a random tick inside that tag, it sometimes spreads one block onto neighboring sediment. Outside the tag, a placed moss stays where you put it and does not colonize.
- The seep, as of milestone 2b: four or more moss blocks in a 5×3×5 neighborhood turn mineral sediment under the moss into peat, and turn peat into bog iron until that neighborhood already holds three concretions. A smaller patch does nothing. Removing the moss is the off switch. There is no debuff and no message.
- Peat (`ecologica:peat`) also generates on its own between Y=60 and Y=72, is mined with a shovel, and burns for 800 ticks. With no moss above it and no water against it, a random tick turns it into dried fen soil. Water against that crust turns it back into mud. Vanilla mud is not part of this drying.

Game tests in `FenMossTests` and `FenSoilTests` drive the seep, the peat step, drying, and rewetting directly. They do not wait on random ticks.

## Compatibility

- **Create and other processing mods.** Iron tags are the compatibility surface. Crushing wheels and similar machines that iterate `c:ores/iron` can see bog iron ore. Those machines often assume a pickaxe and a stone host. Bog iron is a shovel block in mud. If that mismatch misbehaves, the tag is the thing to drop, not the block. A Create-specific crushing recipe stays optional and data-driven; the mod loads without Create.
- **Terralith, Tectonic, Lithostitched, and other worldgen.** Ecologica does not replace their biomes or their stone. Invitation is a tag. Pack makers add Terralith fens to `ecologica:has_bog_iron`, and later add their cold biomes to the tundra tags. Our placed features are our own, so we do not re-add a vanilla placed feature and trip a feature-cycle crash. Replaceable-block tags are how a new soil or a new mud joins bog iron.
- **JEI and EMI.** Milestone 1 uses vanilla smelting, which both already show. When a bloomery or a lichen-weathering recipe cannot be a vanilla recipe, the viewer plugin is an optional runtime dependency (the MDK's `localRuntime` pattern), never a required mod.
- **Seasons and food mods.** Later growth and moisture should read vanilla farmland and common tags first. No hard dependency on Serene Seasons or Farmer's Delight.

## Assets from the essay

Klei Wright published the tundra textures and models as [Tundra Game Drop Assets](https://www.planetminecraft.com/texture-pack/tundra-game-drop-assets/) on Planet Minecraft. The post says the pack is raw material to build from, not a normal resource pack, and that it uses the same license as [Matcha Flavoured](https://modrinth.com/project/QI0EmgZ1): [CC BY-NC-SA 4.0](https://creativecommons.org/licenses/by-nc-sa/4.0/). The stated reason for that license is to keep the work off the Bedrock Marketplace and out of other commercial sales. The muskox model was built starting from Parrie43's [Prettier Cows](https://www.planetminecraft.com/texture-pack/prettier-cows/), which needs credit if those rigs are reused. The Planet Minecraft page lists the pack against a much newer game version than 1.21.1, so the models would have to be checked before they would load here.

None of those files are in this repository. Ecologica has not chosen a license (see the README). CC BY-NC-SA is share-alike and non-commercial. Bringing the assets in would obligate attribution, the same license on derivatives, and no commercial use. That may be the right call, and it may not. The owner decides before any texture, model, or sound from that pack is copied.

## Roadmap

Each milestone should be playable on its own. A milestone is done when a player can see the loop it claims, not when the assets exist.

**Milestone 1 — Bog iron. Done in this repository.** One sympathetic ore, tag-invited worldgen, a shortcut smelt. Proves the project, the data generator, and the "no tag, no change" rule. Does not yet feed back.

**Milestone 2 — A fen that can heal. Done.** Fen moss sits only on wetland sediment (and on bog iron, so a new concretion does not tear it up). It spreads onto nearby sediment inside `ecologica:has_bog_iron` and nowhere else. A patch of 4 or more turns the mineral sediment under it into peat, and turns peat into bog iron until that neighborhood already holds 3 concretions. Dig those and the seep can work again. Drop the patch below 4 moss blocks and it stops. Dig the sediment out from under a moss and the moss item pops, so it can be replanted. No debuff.

**Milestone 2b — Peat and a fen that dries. Done.** Peat is the organic layer. It generates in the fen, a shovel mines it, and it fuels a furnace for 800 ticks (four items: more than a plank, less than coal). Peat under moss stays peat until the seep uses it. Peat with neither moss nor water against it becomes dried fen soil, which does not support moss and does not host bog iron. Water against that crust turns it back into mud, and a living patch can build peat there again. Vanilla mud is left alone.

**Milestone 3 — Tundra ground, iron's other home.** Deepslate to the surface in a tagged cold biome. Smooth basalt patches as greenstone belts. `ecologica:smooth_basalt_iron_ore` (banded iron) drops more iron than vanilla ore, with raw iron blocks in the belt and a short vein under each belt that reaches the surface. This is the essay's ore. It lands here, after bog iron, because the belt wants the tundra's rock column and should arrive with that column, not as a second lonely ore in vanilla stone.

**Milestone 4 — Succession.** Sprouted glow lichen, obtained with bonemeal, generating on its own only in the tundra and the lichen woodlands. In sunlight on a block that accepts it, it either weathers that block (stone toward gravel, sand, or soil, which makes sand renewable) or spreads ordinary lichen within about eight blocks. Shears halt it. Mud is left alone. It consumes substrate, so the farm is a chore unless the player restocks stone. Smelting glow lichen gives phosphorus, used where glow ink sacs are used. Moss follows weathered rock. A shrub from another cold biome gains stunted orange and yellow forms on the new soil.

**Milestone 5 — The tree line.** Lichen woodlands as a transition biome. Dwarf spruce and pine; spruce saplings grown there become the dwarf tree. Dwarf log wall and dwarf log post, strippable, yielding spruce wood. Pale sky, drifting snow, white-shifted ice and grass, with the transition biome doing the blend. This is atmosphere in service of the place the herds will use, not a shader demo.

**Milestone 6 — Muskox.** A goat-cousin. Grazes unsprouted lichen only. Herd pathing toward nearby animals and toward lichen colonies, scatter from predators, rejoin later. Wool and string after feeding, much more when the herd is in open sky in its home biome with neighbors but not a crowd; a packed group gives nothing. If an adult died nearby and there are no babies, grazing can start a long natural breeding period.

**Milestone 7 — Predators that get full.** Polar bears hunt muskox, prefer calves and stragglers, then ignore prey for about two Minecraft days. The carcass does not drop meat. Leather can. The same satiation pattern is the template for any later hunter, including a fix for vanilla wolves and foxes emptying a pen, if we touch those mobs at all.

After that: the bloomery as the better bog-iron path, lime and soil pH, and optional Create recipes. Those are processing, and they wait until the loops they feed exist.

## What we are not building first

- Villages, ruins, or other structures whose job is to make the biome feel occupied. The essay's point is that this does not work.
- A catalog of mobs with no diet and no effect on plants.
- Punishment mechanics: negative effects for killing an animal, for mining, or for "playing wrong."
- A rewritten overworld. Tagged invitation only.
