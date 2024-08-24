package net.coxev.raccoon.item;

import net.coxev.raccoon.Raccoon;
import net.coxev.raccoon.entity.ModEntities;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item RACCOON_SPAWN_EGG = registerItem("raccoon_spawn_egg", new SpawnEggItem(ModEntities.RACCOON, 0x51545B, 0x3E3D3C, new FabricItemSettings()));
    public static final Item HAT= registerItem("hat", new Item(new FabricItemSettings().equipmentSlot(new HeadEquippable())));
    public static final Item GLASSES = registerItem("glasses", new Item(new FabricItemSettings().equipmentSlot(new HeadEquippable())));
    public static final Item HAT_AND_GLASSES = registerItem("hat_and_glasses", new Item(new FabricItemSettings().equipmentSlot(new HeadEquippable())));

    private static void addItemsToIngredientItemGroup(FabricItemGroupEntries entries){
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(Raccoon.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Raccoon.LOGGER.info("Registering Mod Items for " + Raccoon.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientItemGroup);
    }
}
