package com.allen6297.ecologica.registry;

import com.allen6297.ecologica.Ecologica;
import com.allen6297.ecologica.entity.Muskox;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class EcologicaEntities {
    public static final DeferredRegister.Entities ENTITIES = DeferredRegister.createEntities(Ecologica.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<Muskox>> MUSKOX = ENTITIES.registerEntityType(
            "muskox",
            Muskox::new,
            MobCategory.CREATURE,
            builder -> builder.sized(1.3F, 1.4F).clientTrackingRange(10));

    public static void attributes(EntityAttributeCreationEvent event) {
        event.put(MUSKOX.get(), Muskox.createAttributes().build());
    }

    public static void spawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(
                MUSKOX.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Muskox::checkSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }

    private EcologicaEntities() {}
}
