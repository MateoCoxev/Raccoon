package net.coxev.raccoon.datagen;

import net.coxev.raccoon.block.ModBlocks;
import net.coxev.raccoon.item.ModItems;
import net.coxev.raccoon.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModRecipeProvider extends FabricRecipeProvider {

    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, ModItems.HAT, 1)
                .pattern(" W ")
                .pattern("WWW")
                .input('W', Items.WHEAT)
                .criterion(hasItem(Items.WHEAT), conditionsFromItem(Items.WHEAT))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, ModItems.GLASSES, 1)
                .pattern("GSG")
                .input('G', Items.BLACK_STAINED_GLASS_PANE)
                .input('S', Items.STICK)
                .criterion(hasItem(Items.BLACK_STAINED_GLASS_PANE), conditionsFromItem(Items.BLACK_STAINED_GLASS_PANE))
                .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, ModItems.HAT_AND_GLASSES, 1)
                .input(ModTags.Items.HATS)
                .input(ModTags.Items.GLASSES)
                .criterion(hasItem(ModItems.HAT), conditionsFromItem(ModItems.GLASSES))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, ModBlocks.RACCOONMANINOV, 1)
                .pattern("GGG")
                .pattern("RGR")
                .pattern("GGG")
                .input('G', Items.GRAY_WOOL)
                .input('R', Items.RED_WOOL)
                .criterion(hasItem(Items.GRAY_WOOL), conditionsFromItem(Items.GRAY_WOOL))
                .criterion(hasItem(Items.RED_WOOL), conditionsFromItem(Items.RED_WOOL))
                .offerTo(exporter);
    }
}