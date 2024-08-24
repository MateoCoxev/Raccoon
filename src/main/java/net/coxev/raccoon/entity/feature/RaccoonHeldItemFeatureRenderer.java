package net.coxev.raccoon.entity.feature;

import net.coxev.raccoon.entity.client.AbstractRaccoonModel;
import net.coxev.raccoon.entity.client.RaccoonModel;
import net.coxev.raccoon.entity.custom.RaccoonEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;


public class RaccoonHeldItemFeatureRenderer extends FeatureRenderer<RaccoonEntity, AbstractRaccoonModel<RaccoonEntity>> {
    private final HeldItemRenderer heldItemRenderer;

    public RaccoonHeldItemFeatureRenderer(FeatureRendererContext<RaccoonEntity, AbstractRaccoonModel<RaccoonEntity>> context, HeldItemRenderer heldItemRenderer) {
        super(context);
        this.heldItemRenderer = heldItemRenderer;
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, RaccoonEntity entity, float limbAngle, float limbDistance, float age, float tickDelta, float headYaw, float headPitch) {
        float m;
        if(!entity.getEquippedStack(EquipmentSlot.MAINHAND).isEmpty()){
            ItemStack heldItem = entity.getEquippedStack(EquipmentSlot.MAINHAND);
            matrixStack.push();
            matrixStack.translate(this.getContextModel().raccoon.pivotX / 16.0f, this.getContextModel().raccoon.pivotY / 16.0f, this.getContextModel().raccoon.pivotZ / 16.0f);

            matrixStack.translate(this.getContextModel().body.pivotX / 16.0f, this.getContextModel().body.pivotY / 16.0f, this.getContextModel().body.pivotZ / 16.0f);
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(this.getContextModel().body.getTransform().yaw * 180.0f / (float) Math.PI));

            matrixStack.translate(this.getContextModel().torso.pivotX / 16.0f, this.getContextModel().torso.pivotY / 16.0f, this.getContextModel().torso.pivotZ / 16.0f);

            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(this.getContextModel().torso.getTransform().yaw * 180.0f / (float) Math.PI));
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(this.getContextModel().torso.getTransform().pitch * 180.0f / (float) Math.PI));

            matrixStack.translate(this.getContextModel().head.pivotX / 16.0f, this.getContextModel().head.pivotY / 16.0f, this.getContextModel().head.pivotZ / 16.0f);

            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(this.getContextModel().head.getTransform().yaw * 180.0f / (float) Math.PI));
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(this.getContextModel().head.getTransform().pitch * 180.0f / (float) Math.PI));

            matrixStack.translate(0f, 0.2f, -0.35f);
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0f));
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0f));
            this.heldItemRenderer.renderItem(entity, heldItem, ModelTransformationMode.GROUND, false, matrixStack, vertexConsumerProvider, light);
            matrixStack.pop();
        }
    }
}
