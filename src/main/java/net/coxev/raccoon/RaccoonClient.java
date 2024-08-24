package net.coxev.raccoon;

import net.coxev.raccoon.block.ModBlocks;
import net.coxev.raccoon.entity.ModEntities;
import net.coxev.raccoon.entity.client.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.block.entity.BedBlockEntityRenderer;

public class RaccoonClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.RACCOON, RaccoonRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.RACCOON, RaccoonModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.CHONKY_RACCOON, ChonkyRaccoonModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.BABY_RACCOON, BabyRaccoonModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.RACCOONMANINOV, BedBlockEntityRenderer::getHeadTexturedModelData);

        ModBlocks.registerClient();
    }
}
