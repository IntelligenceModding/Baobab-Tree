package de.artemis.baobabtree.common.registry;

import de.artemis.baobabtree.BaobabTree;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.registries.Registries;

public final class ModTags {
    private ModTags() {
    }

    public static final class Blocks {
        public static final TagKey<Block> BAOBAB_LOGS = createTag("baobab_logs");

        private Blocks() {
        }

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(Identifier.fromNamespaceAndPath(BaobabTree.MOD_ID, name));
        }
    }

    public static final class Items {
        public static final TagKey<Item> BAOBAB_LOGS = createTag("baobab_logs");

        private Items() {
        }

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(Identifier.fromNamespaceAndPath(BaobabTree.MOD_ID, name));
        }
    }

    public static final class Biomes {
        public static final TagKey<Biome> HAS_BAOBAB_GENERATION = createTag("has_baobab_generation");

        private Biomes() {
        }

        private static TagKey<Biome> createTag(String name) {
            return TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(BaobabTree.MOD_ID, name));
        }
    }
}
