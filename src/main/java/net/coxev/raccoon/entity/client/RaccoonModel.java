package net.coxev.raccoon.entity.client;

import net.coxev.raccoon.entity.animation.ModAnimations;
import net.coxev.raccoon.entity.custom.RaccoonEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class RaccoonModel<T extends RaccoonEntity> extends AbstractRaccoonModel<T> {

    public RaccoonModel(ModelPart root) {
        super(root);
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData raccoon = modelPartData.addChild("raccoon", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 16.5F, 0.0F));

        ModelPartData body = raccoon.addChild("body", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 7.5F, 0.0F));

        ModelPartData legs = body.addChild("legs", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -3.0F, 5.0F));

        ModelPartData leg_left = legs.addChild("leg_left", ModelPartBuilder.create().uv(0, 8).cuboid(-1.5F, -2.5F, -1.5F, 3.0F, 5.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(3.5F, 0.5F, -0.5F));

        ModelPartData leg_right = legs.addChild("leg_right", ModelPartBuilder.create().uv(0, 0).cuboid(-1.5F, -2.5F, -1.5F, 3.0F, 5.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.5F, 0.5F, -0.5F));

        ModelPartData torso = body.addChild("torso", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -7.0F, 6.0F));

        ModelPartData abdomen = torso.addChild("abdomen", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 1.0F, 1.0F));

        ModelPartData abdomen_r1 = abdomen.addChild("abdomen_r1", ModelPartBuilder.create().uv(0, 0).cuboid(-6.0F, -4.5F, -8.0F, 12.0F, 9.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -1.5F, -7.0F, 0.0436F, 0.0F, 0.0F));

        ModelPartData arms = torso.addChild("arms", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 2.0F, -10.5F));

        ModelPartData arm_left = arms.addChild("arm_left", ModelPartBuilder.create().uv(0, 38).cuboid(-1.5F, -2.5F, -1.5F, 3.0F, 5.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(3.5F, 2.5F, 0.0F));

        ModelPartData arm_right = arms.addChild("arm_right", ModelPartBuilder.create().uv(36, 25).cuboid(-1.5F, -2.5F, -1.5F, 3.0F, 5.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.5F, 2.5F, 0.0F));

        ModelPartData head = torso.addChild("head", ModelPartBuilder.create().uv(0, 25).cuboid(-4.5F, -3.25F, -6.0F, 9.0F, 7.0F, 6.0F, new Dilation(0.0F))
                .uv(36, 33).cuboid(-2.0F, 0.75F, -8.0F, 4.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -4.75F, -13.0F));

        ModelPartData ear_right = head.addChild("ear_right", ModelPartBuilder.create().uv(9, 8).cuboid(-1.0F, -2.0F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.5F, -3.25F, -4.5F));

        ModelPartData ear_left = head.addChild("ear_left", ModelPartBuilder.create().uv(9, 0).cuboid(-1.0F, -2.0F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(3.5F, -3.25F, -4.5F));

        ModelPartData tail = body.addChild("tail", ModelPartBuilder.create(), ModelTransform.pivot(-0.5F, -10.0F, 8.0F));

        ModelPartData tail_r1 = tail.addChild("tail_r1", ModelPartBuilder.create().uv(19, 27).cuboid(-1.5F, 0.5F, -5.5F, 3.0F, 3.0F, 11.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -0.5F, 4.5F, -0.3491F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }
    @Override
    public void setAngles(RaccoonEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.setHeadAngles(netHeadYaw, headPitch);

        this.animateMovement(ModAnimations.RACCOON_WALKING, limbSwing, limbSwingAmount, 2f, 2.5f);
        this.updateAnimation(entity.idleAnimationState, ModAnimations.RACCOON_IDLING, ageInTicks, 1f);
        this.updateAnimation(entity.spinningAnimationState, ModAnimations.RACCOON_SPINNING, ageInTicks, 1f);
        this.updateAnimation(entity.beggingAnimationState, ModAnimations.RACCOON_BEGGING, ageInTicks, 1f);
        this.updateAnimation(entity.sittingAnimationState, ModAnimations.RACCOON_SITTING, ageInTicks, 1f);
        this.updateAnimation(entity.standToBegAnimationState, ModAnimations.RACCOON_STAND_TO_BEG, ageInTicks, 1f);
        this.updateAnimation(entity.standToSitAnimationState, ModAnimations.RACCOON_STAND_TO_SIT, ageInTicks, 1f);
        this.updateAnimation(entity.begToStandAnimationState, ModAnimations.RACCOON_BEG_TO_STAND, ageInTicks, 1f);
        this.updateAnimation(entity.sitToStandAnimationState, ModAnimations.RACCOON_SIT_TO_STAND, ageInTicks, 1f);
        this.updateAnimation(entity.earsWiggleAnimationState, ModAnimations.RACCOON_EARS_WIGGLE, ageInTicks, 1f);
        this.updateAnimation(entity.twerkAnimationState, ModAnimations.RACCOON_TWERK, ageInTicks, 1f);
    }

    private void setHeadAngles(float headYaw, float headPitch){
        headYaw = MathHelper.clamp(headYaw, -20.0f, 20.0f);
        headPitch = MathHelper.clamp(headPitch, -10.f, 10.0f);

        this.head.yaw = headYaw * ((float)Math.PI / 180);
        this.head.pitch = headPitch * ((float)Math.PI / 180);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        raccoon.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart getPart() {
        return raccoon;
    }
}