# Ecologica

Ecologica is a NeoForge mod for Minecraft 1.21.1 (mod id `ecologica`). Geology and ecology are one system: the ground decides what can live there, and living things change the ground.

The design, including the three principles borrowed from Klei Wright's essay ["How can we make Minecraft feel alive? (It isn't structures)"](https://youtu.be/cBmt2FNP1Ug), is in [`docs/DESIGN.md`](docs/DESIGN.md).

This repository currently contains the first milestone only: **bog iron**, a soft iron concretion in wetland mud. It generates in swamps and mangrove swamps, is mined with a shovel, and the raw item smelts to an iron ingot. The tundra, lichen, and muskox described in the design doc are not implemented yet.

## Requirements

- JDK 21, 64-bit. NeoForge supports the Microsoft build of OpenJDK; other JDK 21 builds work too.
- A display is required for `runClient`. The data generator and `build` do not need one.

## Build and run

The Gradle wrapper is included. The first run downloads Minecraft, NeoForge, and the toolchain. That takes a while.

```bash
./gradlew build
```

Produces `build/libs/ecologica-0.1.0.jar`.

```bash
./gradlew runData
```

Regenerates models, language, loot, recipes, tags, and worldgen JSON into `src/generated/resources`. Those files are committed. Run this after changing anything under `datagen` or `worldgen`.

```bash
./gradlew runClient
```

Launches a Minecraft client with the mod loaded. Bog iron is on the **Ecologica** creative tab. In a survival world, look in a swamp or mangrove swamp between Y=48 and Y=68 and dig dirt, mud, or clay with a shovel. A pickaxe will not collect the block.

```bash
./gradlew runServer
```

Launches a dedicated server. `runGameTestServer` is the MDK's game-test harness. This project does not register game tests yet, so that task exits as a failure until tests exist.

Import the repository as a Gradle project in IntelliJ IDEA or Eclipse. If libraries look missing, run `./gradlew --refresh-dependencies`.

Mojang's official names are used for Minecraft methods and fields. Those names are under Mojang's mapping license: <https://github.com/NeoForged/NeoForm/blob/main/Mojang.md>.

The project was started from the [NeoForge 1.21.1 ModDevGradle MDK](https://github.com/NeoForgeMDKs/MDK-1.21.1-ModDevGradle) (ModDevGradle 2.0.148, NeoForge 21.1.267, Java 21).

## License

No license has been chosen for Ecologica. Until the owner picks one, no permission is granted to use, copy, modify, or distribute this project beyond whatever the host already allows for viewing the repository.

`mod_license` in `gradle.properties` is `All Rights Reserved` for that reason. It is a placeholder, not a license selection. After choosing a license at <https://choosealicense.com/>, replace this section and set `mod_license` to the short name so `neoforge.mods.toml` matches.

The unmodified MDK template is covered by the template license NeoForged ships with it (MIT, copyright NeoForged). That grant covers the template, not Ecologica as a whole.

Textures and models from Klei Wright's [Tundra Game Drop Assets](https://www.planetminecraft.com/texture-pack/tundra-game-drop-assets/) are not included. They are published under the same license as Matcha Flavoured (CC BY-NC-SA 4.0). Whether that fits this project is an open decision; see the design doc before adding any of those files.
