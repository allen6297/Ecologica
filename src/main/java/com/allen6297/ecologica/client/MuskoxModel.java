package com.allen6297.ecologica.client;

import com.allen6297.ecologica.Ecologica;

import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

/** A stocky quadruped until a real muskox model is licensed. */
public class MuskoxModel extends QuadrupedModel<LivingEntityRenderState> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(Ecologica.MOD_ID, "muskox"), "main");
    public static final ModelLayerLocation BABY_LAYER = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(Ecologica.MOD_ID, "muskox"), "baby");

    public MuskoxModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        return LayerDefinition.create(QuadrupedModel.createBodyMesh(12, false, true, CubeDeformation.NONE), 64, 32);
    }
}
