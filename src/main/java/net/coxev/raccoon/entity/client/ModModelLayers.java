package net.coxev.raccoon.entity.client;

import net.coxev.raccoon.Raccoon;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class ModModelLayers {
    public static final EntityModelLayer RACCOON =
            new EntityModelLayer(new Identifier(Raccoon.MOD_ID, "raccoon"), "main");
    public static final EntityModelLayer CHONKY_RACCOON =
            new EntityModelLayer(new Identifier(Raccoon.MOD_ID, "chonky_raccoon"), "main");
    public static final EntityModelLayer BABY_RACCOON =
            new EntityModelLayer(new Identifier(Raccoon.MOD_ID, "baby_raccoon"), "main");
    public static final EntityModelLayer RACCOON_BANDANA =
            new EntityModelLayer(new Identifier(Raccoon.MOD_ID, "raccoon_bandana"), "main");
    public static final EntityModelLayer RACCOONMANINOV =
            new EntityModelLayer(new Identifier(Raccoon.MOD_ID, "raccoon"), "layers");
}
