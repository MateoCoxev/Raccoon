package net.coxev.raccoon.entity.client;

import net.coxev.raccoon.Raccoon;
import net.coxev.raccoon.entity.custom.RaccoonEntity;
import net.coxev.raccoon.entity.feature.RaccoonBandanaFeatureRenderer;
import net.coxev.raccoon.entity.feature.RaccoonHeldItemFeatureRenderer;
import net.coxev.raccoon.entity.feature.RaccoonMexicanFeatureRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.CatCollarFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.FoxHeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.MediumPufferfishEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.PufferfishEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RaccoonRenderer extends MobEntityRenderer<RaccoonEntity, AbstractRaccoonModel<RaccoonEntity>> {
    public static final Identifier TEXTURE = new Identifier(Raccoon.MOD_ID, "textures/entity/raccoon.png");
    private final EntityModel<RaccoonEntity> babyModel;
    private final EntityModel<RaccoonEntity> mediumModel = this.getModel();
    private final EntityModel<RaccoonEntity> chonkyModel;

    public RaccoonRenderer(EntityRendererFactory.Context context) {
        super(context, new RaccoonModel<>(context.getPart(ModModelLayers.RACCOON)), 0.6f);
        this.chonkyModel = new ChonkyRaccoonModel<>(context.getPart(ModModelLayers.CHONKY_RACCOON));
        this.babyModel = new BabyRaccoonModel<>(context.getPart(ModModelLayers.BABY_RACCOON));

        this.addFeature(new RaccoonHeldItemFeatureRenderer(this, context.getHeldItemRenderer()));
        this.addFeature(new RaccoonMexicanFeatureRenderer(this, context.getHeldItemRenderer()));
        this.addFeature(new RaccoonBandanaFeatureRenderer(this));
    }

    @Override
    public Identifier getTexture(RaccoonEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(RaccoonEntity raccoon, float f, float g, MatrixStack matrixStack,
                       VertexConsumerProvider vertexConsumerProvider, int i) {
        float scaling = raccoon.isBaby() ? 0.5f : (raccoon.isChonky() ? 1.2f : 1f);
        this.model = (AbstractRaccoonModel<RaccoonEntity>) (raccoon.isBaby() ? this.babyModel : (raccoon.isChonky() ? this.chonkyModel : this.mediumModel));
        matrixStack.scale(scaling, scaling, scaling);
        super.render(raccoon, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
