package net.coxev.raccoon.datagen;

import net.coxev.raccoon.Raccoon;
import net.coxev.raccoon.block.ModBlocks;
import net.coxev.raccoon.advancement.ChonkyCriterion;
import net.coxev.raccoon.entity.ModEntities;
import net.coxev.raccoon.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.advancement.criterion.PlayerInteractedWithEntityCriterion;
import net.minecraft.advancement.criterion.TameAnimalCriterion;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.NbtPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class ModAdvancementProvider extends FabricAdvancementProvider {

    public ModAdvancementProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        Advancement raccoonTamed = Advancement.Builder.create()
                .display(
                        ModBlocks.RACCOONMANINOV,
                        Text.translatable("advancements.raccoon.title"),
                        Text.translatable("advancements.raccoon.description"),
                        new Identifier("textures/gui/advancements/backgrounds/adventure.png"),
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion("raccoon_tamed", TameAnimalCriterion.Conditions.create(EntityPredicate.Builder.create().type(ModEntities.RACCOON).build()))
                .build(consumer, Raccoon.MOD_ID + "/raccoon_tamed");
        Advancement gotRaccoonmaninov = Advancement.Builder.create()
                .display(
                        ModBlocks.RACCOONMANINOV,
                        Text.translatable("advancements.got_raccoonmaninov.title"),
                        Text.translatable("advancements.got_raccoonmaninov.description"),
                        new Identifier("textures/gui/advancements/backgrounds/adventure.png"),
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion("got_raccoonmaninov", InventoryChangedCriterion.Conditions.items(ModBlocks.RACCOONMANINOV))
                .parent(raccoonTamed)
                .build(consumer, Raccoon.MOD_ID + "/got_raccoonmaninov");
        Advancement raccoonWearingHatAndGlasses = Advancement.Builder.create()
                .display(
                        ModItems.HAT_AND_GLASSES,
                        Text.translatable("advancements.raccoon_wearing_hat_and_glasses.title"),
                        Text.translatable("advancements.raccoon_wearing_hat_and_glasses.description"),
                        new Identifier("textures/gui/advancements/backgrounds/adventure.png"),
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion("raccoon_wearing_hat_and_glasses", PlayerInteractedWithEntityCriterion.Conditions.create(ItemPredicate.Builder.create().items(ModItems.HAT_AND_GLASSES), EntityPredicate.asLootContextPredicate(EntityPredicate.Builder.create().type(ModEntities.RACCOON).build())))
                .parent(raccoonTamed)
                .build(consumer, Raccoon.MOD_ID + "/raccoon_wearing_hat_and_glasses");
        Advancement chonkyRaccoonTamed = Advancement.Builder.create()
                .display(
                        ModItems.RACCOON_SPAWN_EGG,
                        Text.translatable("advancements.chonky_raccoon_tamed.title"),
                        Text.translatable("advancements.chonky_raccoon_tamed.description"),
                        new Identifier("textures/gui/advancements/backgrounds/adventure.png"),
                        AdvancementFrame.CHALLENGE,
                        true,
                        true,
                        true
                )
                .criterion("chonky_raccoon_tamed", TameAnimalCriterion.Conditions.create(EntityPredicate.Builder.create().type(ModEntities.RACCOON).nbt(new NbtPredicate(createChonkyNbtPredicate())).build()))
                .parent(raccoonTamed)
                .build(consumer, Raccoon.MOD_ID + "/chonky_raccoon_tamed");
        Advancement chonked = Advancement.Builder.create()
                .display(
                        Items.CAKE,
                        Text.translatable("advancements.chonked.title"),
                        Text.translatable("advancements.chonked.description"),
                        new Identifier("textures/gui/advancements/backgrounds/adventure.png"),
                        AdvancementFrame.CHALLENGE,
                        true,
                        true,
                        true
                )
                .criterion("chonked", ChonkyCriterion.Conditions.create())
                .parent(raccoonTamed)
                .build(consumer, Raccoon.MOD_ID + "/chonked");
    }

    private static NbtCompound createChonkyNbtPredicate() {
        NbtCompound nbtPredicate = new NbtCompound();
        nbtPredicate.putBoolean("Chonky", true);
        return nbtPredicate;
    }
}