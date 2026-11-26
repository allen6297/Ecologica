package com.allen6297.ecologica.gametest;

import java.util.List;
import java.util.function.Consumer;

import com.allen6297.ecologica.Ecologica;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.BuiltinTestFunctions;
import net.minecraft.gametest.framework.FunctionGameTestInstance;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.gametest.framework.TestFunctionLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;

/**
 * 26.3 game tests are registry objects: a function plus a test instance that points at the platform structure.
 */
public final class EcologicaGameTests {
    private static final Identifier STRUCTURE = Identifier.fromNamespaceAndPath(Ecologica.MOD_ID, "platform");
    private static boolean functionsRegistered;

    /**
     * Must run before {@link BuiltinTestFunctions#bootstrap} calls {@link TestFunctionLoader#runLoaders}.
     * The mod constructor is too late: the test-function registry is already built by then.
     */
    public static void registerFunctions() {
        if (functionsRegistered) {
            return;
        }
        functionsRegistered = true;
        TestFunctionLoader.registerLoader(output -> {
            registerFunction(output, "seep_forms_one", FenMossTests::seepFormsOneConcretionAndLeavesTheMoss);
            registerFunction(output, "seep_needs_a_patch", FenMossTests::seepDoesNothingBelowThePatchSize);
            registerFunction(output, "seep_pauses_when_full", FenMossTests::seepPausesWhenTheNeighborhoodIsFull);
            registerFunction(output, "stripping_stops_the_seep", FenMossTests::strippingThePatchStopsTheSeep);
            registerFunction(output, "digging_sediment_returns_moss", FenMossTests::diggingTheSedimentReturnsTheMoss);
            registerFunction(output, "spread_follows_the_fen_tag", FenMossTests::spreadFollowsTheFenTag);
            registerFunction(output, "patch_builds_peat", FenSoilTests::livingPatchTurnsMudIntoPeat);
            registerFunction(output, "mud_does_not_become_iron", FenSoilTests::mudDoesNotBecomeBogIronDirectly);
            registerFunction(output, "small_patch_does_not_build_peat", FenSoilTests::smallPatchDoesNotAccumulate);
            registerFunction(output, "exposed_peat_dries", FenSoilTests::exposedPeatDries);
            registerFunction(output, "moss_keeps_peat_wet", FenSoilTests::mossKeepsPeatFromDrying);
            registerFunction(output, "water_keeps_peat_wet", FenSoilTests::waterKeepsPeatFromDrying);
            registerFunction(output, "water_rewets_dried_soil", FenSoilTests::waterTurnsDriedSoilBackIntoMud);
            registerFunction(output, "moss_pops_off_dried_soil", FenSoilTests::mossPopsOffDriedSoil);
            registerFunction(output, "peat_is_furnace_fuel", FenSoilTests::peatBurnsLongerThanAPlankAndShorterThanCoal);
        });
    }

    public static void register(RegisterGameTestsEvent event) {
        Holder<TestEnvironmentDefinition<?>> environment = event.registerEnvironment(
                Identifier.fromNamespaceAndPath(Ecologica.MOD_ID, "default"),
                new TestEnvironmentDefinition.AllOf(List.of()));
        TestData<Holder<TestEnvironmentDefinition<?>>> data = new TestData<>(environment, STRUCTURE, 100, 0, true);
        for (String name : List.of(
                "seep_forms_one",
                "seep_needs_a_patch",
                "seep_pauses_when_full",
                "stripping_stops_the_seep",
                "digging_sediment_returns_moss",
                "spread_follows_the_fen_tag",
                "patch_builds_peat",
                "mud_does_not_become_iron",
                "small_patch_does_not_build_peat",
                "exposed_peat_dries",
                "moss_keeps_peat_wet",
                "water_keeps_peat_wet",
                "water_rewets_dried_soil",
                "moss_pops_off_dried_soil",
                "peat_is_furnace_fuel")) {
            Identifier id = Identifier.fromNamespaceAndPath(Ecologica.MOD_ID, name);
            event.registerTest(id, new FunctionGameTestInstance(ResourceKey.create(Registries.TEST_FUNCTION, id), data));
        }
    }

    private static void registerFunction(
            java.util.function.BiConsumer<ResourceKey<Consumer<GameTestHelper>>, Consumer<GameTestHelper>> output,
            String name,
            Consumer<GameTestHelper> test) {
        output.accept(ResourceKey.create(Registries.TEST_FUNCTION, Identifier.fromNamespaceAndPath(Ecologica.MOD_ID, name)), test);
    }

    private EcologicaGameTests() {}
}
