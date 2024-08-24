package net.coxev.raccoon.util;

import net.coxev.raccoon.Raccoon;
import net.fabricmc.fabric.impl.tag.convention.TagRegistration;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> TRASH_CANS = createTag("trash_cans");

        private static TagKey<Block> createTag(String name) {
            return TagRegistration.BLOCK_TAG_REGISTRATION.registerCommon(name);
        }
    }

    public static class Items {
        public static final TagKey<Item> TRASH_CANS = createTag("trash_cans");
        public static final TagKey<Item> HATS = createTag("hats");
        public static final TagKey<Item> GLASSES = createTag("glasses");

        private static TagKey<Item> createTag(String name) {
            return TagRegistration.ITEM_TAG_REGISTRATION.registerCommon(name);
        }
    }
}
