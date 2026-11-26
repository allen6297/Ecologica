# Ecologica

Ecologica is a NeoForge mod for Minecraft 1.21.1 (mod id `ecologica`). Geology and ecology are one system: the ground decides what can live there, and living things change the ground.

The design, including the three principles borrowed from Klei Wright's essay ["How can we make Minecraft feel alive? (It isn't structures)"](https://youtu.be/cBmt2FNP1Ug), is in [`docs/DESIGN.md`](docs/DESIGN.md).

What is in the game:

- **Bog iron**, a soft iron concretion in wetland mud. It generates in swamps and mangrove swamps, is mined with a shovel, and the raw item smelts to an iron ingot.
- **Fen moss**, a living skin on that mud. A patch of four or more turns the mud under it into peat, and turns peat into bog iron, until three concretions are nearby. Strip the patch and the seep stops.
- **Peat**, the fen's organic layer. Shovel it up and it burns in a furnace for 800 ticks, four items. Leave a placed peat block with no moss and no water against it and it becomes dried fen soil. Water against that crust turns it back into mud.

The tundra, lichen, and muskox described in the design doc are not implemented yet.

## Requirements

- JDK 21, 64-bit. NeoForge supports the Microsoft build of OpenJDK; other JDK 21 builds work too.
- A display is required for `runClient`. The data generator and `build` do not need one.

## Build and run

The Gradle wrapper is included. The first run downloads Minecraft, NeoForge, and the toolchain. That takes a while.

```bash
./gradlew build
```

Produces `build/libs/ecologica-0.3.0.jar`.

```bash
./gradlew runData
```

Regenerates models, language, loot, recipes, tags, and worldgen JSON into `src/generated/resources`. Those files are committed. Run this after changing anything under `datagen` or `worldgen`.

```bash
./gradlew runClient
```

Launches a Minecraft client with the mod loaded. Bog iron, fen moss, peat, and dried fen soil are on the **Ecologica** creative tab.

In a survival world, look in a swamp or mangrove swamp. Bog iron is between Y=48 and Y=68 in dirt, mud, clay, or peat, and it is dug with a shovel. A pickaxe does not collect it. Peat generates a little higher, about Y=60 to Y=72, in mud, dirt, and clay. Fen moss generates as a carpet on those sediments.

To watch the loop without waiting, run `/gamerule randomTickSpeed 300` and place four or more fen moss on mud. The mud becomes peat, then the peat becomes bog iron, and new iron pauses once three concretions are nearby. Break the moss and leave the peat in the air: it dries into dried fen soil. Put water against that crust and it becomes mud again. Peat in a furnace smelts four items.

```bash
./gradlew runServer
```

Launches a dedicated server.

```bash
./gradlew runGameTestServer
```

Runs the fen-moss game tests (the seep, the patch size, the full-neighborhood pause, stripping, and the biome gate on spread).

Import the repository as a Gradle project in IntelliJ IDEA or Eclipse. If libraries look missing, run `./gradlew --refresh-dependencies`.

Mojang's official names are used for Minecraft methods and fields. Those names are under Mojang's mapping license: <https://github.com/NeoForged/NeoForm/blob/main/Mojang.md>.

The project was started from the [NeoForge 1.21.1 ModDevGradle MDK](https://github.com/NeoForgeMDKs/MDK-1.21.1-ModDevGradle) (ModDevGradle 2.0.148, NeoForge 21.1.267, Java 21).

## License

No license has been chosen for Ecologica. Until the owner picks one, no permission is granted to use, copy, modify, or distribute this project beyond whatever the host already allows for viewing the repository.

`mod_license` in `gradle.properties` is `All Rights Reserved` for that reason. It is a placeholder, not a license selection. After choosing a license at <https://choosealicense.com/>, replace this section and set `mod_license` to the short name so `neoforge.mods.toml` matches.

The unmodified MDK template is covered by the template license NeoForged ships with it (MIT, copyright NeoForged). That grant covers the template, not Ecologica as a whole.

Textures and models from Klei Wright's [Tundra Game Drop Assets](https://www.planetminecraft.com/texture-pack/tundra-game-drop-assets/) are not included. They are published under the same license as Matcha Flavoured (CC BY-NC-SA 4.0). Whether that fits this project is an open decision; see the design doc before adding any of those files.
