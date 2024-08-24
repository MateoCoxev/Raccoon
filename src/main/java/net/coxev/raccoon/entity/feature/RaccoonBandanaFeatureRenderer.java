package net.coxev.raccoon.entity.feature;

import net.coxev.raccoon.Raccoon;
import net.coxev.raccoon.entity.client.AbstractRaccoonModel;
import net.coxev.raccoon.entity.client.ModModelLayers;
import net.coxev.raccoon.entity.client.RaccoonModel;
import net.coxev.raccoon.entity.custom.RaccoonEntity;
import net.coxev.raccoon.item.ModItems;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class RaccoonBandanaFeatureRenderer extends FeatureRenderer<RaccoonEntity, AbstractRaccoonModel<RaccoonEntity>> {
    private static final Identifier SKIN = new Identifier(Raccoon.MOD_ID, "textures/entity/raccoon/raccoon_bandana.png");

    public RaccoonBandanaFeatureRenderer(FeatureRendererContext<RaccoonEntity, AbstractRaccoonModel<RaccoonEntity>> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, RaccoonEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (entity.isInvisible()) {
            return;
        }
        if(entity.isTamed()){
            float[] fs = entity.getBandanaColor().getColorComponents();
            renderModel(this.getContextModel(), SKIN, matrices, vertexConsumers, light, entity, fs[0], fs[1], fs[2]);
        }
    }
}
