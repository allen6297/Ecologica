package com.allen6297.ecologica.mixin;

import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.allen6297.ecologica.gametest.EcologicaGameTests;

import net.minecraft.core.Registry;
import net.minecraft.gametest.framework.BuiltinTestFunctions;
import net.minecraft.gametest.framework.GameTestHelper;

/**
 * Test functions are gathered once, inside {@link BuiltinTestFunctions#bootstrap}, before any mod constructor runs.
 */
@Mixin(BuiltinTestFunctions.class)
public class BuiltinTestFunctionsMixin {
    @Inject(method = "bootstrap", at = @At("HEAD"))
    private static void ecologica$registerFunctions(
            Registry<Consumer<GameTestHelper>> registry, CallbackInfoReturnable<Consumer<GameTestHelper>> callback) {
        EcologicaGameTests.registerFunctions();
    }
}
