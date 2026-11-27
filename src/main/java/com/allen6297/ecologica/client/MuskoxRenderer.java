package com.allen6297.ecologica.client;

import com.allen6297.ecologica.Ecologica;
import com.allen6297.ecologica.entity.Muskox;

import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

public class MuskoxRenderer extends AgeableMobRenderer<Muskox, LivingEntityRenderState, MuskoxModel> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Ecologica.MOD_ID, "textures/entity/muskox.png");

    public MuskoxRenderer(EntityRendererProvider.Context context) {
        super(context, new MuskoxModel(context.bakeLayer(MuskoxModel.LAYER)), new MuskoxModel(context.bakeLayer(MuskoxModel.BABY_LAYER)), 0.6F);
    }

    @Override
    public Identifier getTextureLocation(LivingEntityRenderState state) {
        return TEXTURE;
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }
}
