package net.coxev.raccoon;

import net.coxev.raccoon.block.ModBlocks;
import net.coxev.raccoon.advancement.ModAdvancements;
import net.coxev.raccoon.entity.ModEntities;
import net.coxev.raccoon.entity.custom.RaccoonEntity;
import net.coxev.raccoon.item.ModItemGroups;
import net.coxev.raccoon.item.ModItems;
import net.coxev.raccoon.sound.ModSounds;
import net.coxev.raccoon.world.gen.ModWorldGen;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Raccoon implements ModInitializer {
	public static final String MOD_ID = "raccoon";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModEntities.registerModEntities();
		ModItemGroups.registerItemGroups();

		FabricDefaultAttributeRegistry.register(ModEntities.RACCOON, RaccoonEntity.createRaccoonAttributes());

		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModSounds.registerSounds();

		ModAdvancements.registerCriterions();

		ModWorldGen.generateModWorldGen();
	}
}