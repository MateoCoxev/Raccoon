package net.coxev.raccoon.datagen;

import com.supermartijn642.trashcans.TrashCans;
import net.coxev.raccoon.item.ModItems;
import net.coxev.raccoon.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {

    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture){
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(ModTags.Blocks.TRASH_CANS)
                .add(TrashCans.item_trash_can)
                .add(TrashCans.energy_trash_can)
                .add(TrashCans.liquid_trash_can)
                .add(TrashCans.ultimate_trash_can);
    }
}