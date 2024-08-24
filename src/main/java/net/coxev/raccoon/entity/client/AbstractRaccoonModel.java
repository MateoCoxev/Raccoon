package net.coxev.raccoon.entity.client;

import net.coxev.raccoon.entity.custom.RaccoonEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;

public abstract class AbstractRaccoonModel<T extends RaccoonEntity> extends SinglePartEntityModel<T> {
    public final ModelPart raccoon;
    public final ModelPart body;
    public final ModelPart torso;
    public final ModelPart head;

    public AbstractRaccoonModel(ModelPart root) {
        this.raccoon = root.getChild("raccoon");
        this.body = raccoon.getChild("body");
        this.torso = raccoon.getChild("body").getChild("torso");
        this.head = raccoon.getChild("body").getChild("torso").getChild("head");
    }

    @Override
    public abstract void setAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch);

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        this.raccoon.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }
}