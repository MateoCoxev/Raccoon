package net.coxev.raccoon.item;

import net.coxev.raccoon.Raccoon;
import net.coxev.raccoon.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup RACCOON_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(Raccoon.MOD_ID, "raccoon"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.raccoon"))
                    .icon(() -> new ItemStack(ModItems.RACCOON_SPAWN_EGG)).entries((displayContext, entries) -> {
                        entries.add(ModItems.RACCOON_SPAWN_EGG);

                        entries.add(ModBlocks.RACCOONMANINOV);

                        entries.add(ModItems.HAT);
                        entries.add(ModItems.GLASSES);
                        entries.add(ModItems.HAT_AND_GLASSES);
                    }).build());

    public static void registerItemGroups(){
        Raccoon.LOGGER.info("Registering Item Groups for " + Raccoon.MOD_ID);
    }
}
