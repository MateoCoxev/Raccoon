package net.coxev.raccoon.util;

import net.fabricmc.fabric.impl.tag.convention.TagRegistration;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> TRASH_CANS = TagKey.of(RegistryKeys.BLOCK, new Identifier ("c", "trash_cans"));
    }

    public static class Items {
        public static final TagKey<Item> HATS = createTag("hats");
        public static final TagKey<Item> GLASSES = createTag("glasses");

        private static TagKey<Item> createTag(String name) {
            return TagRegistration.ITEM_TAG_REGISTRATION.registerCommon(name);
        }
    }
}
