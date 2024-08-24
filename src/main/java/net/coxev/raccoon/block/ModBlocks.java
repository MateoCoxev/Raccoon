package net.coxev.raccoon.block;

import net.coxev.raccoon.Raccoon;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class ModBlocks {

    public static final Block RACCOONMANINOV = registerBlock("raccoonmaninov",
            new RaccoonmaninovBlock(FabricBlockSettings.copyOf(Blocks.GRAY_WOOL).sounds(BlockSoundGroup.WOOL)));

    private static Block registerBlock(String name, Block block){
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(Raccoon.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block){
        return Registry.register(Registries.ITEM, new Identifier(Raccoon.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerModBlocks(){
        Raccoon.LOGGER.info("Registering ModBlocks for " + Raccoon.MOD_ID);
    }

    public static void registerClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(RACCOONMANINOV, RenderLayer.getCutout());
    }
}
