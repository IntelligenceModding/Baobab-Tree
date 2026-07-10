package de.artemis.baobabtree.common.datagen;

import de.artemis.baobabtree.BaobabTree;
import de.artemis.baobabtree.common.registry.ModBlocks;
import de.artemis.baobabtree.common.registry.ModItems;
import de.artemis.baobabtree.common.registry.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, BaobabTree.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(ItemTags.SAPLINGS)
                .add(key(ModBlocks.BAOBAB_SAPLING.get().asItem()));

        tag(ItemTags.LOGS_THAT_BURN)
                .add(key(ModBlocks.BAOBAB_LOG.get().asItem()))
                .add(key(ModBlocks.BAOBAB_WOOD.get().asItem()))
                .add(key(ModBlocks.STRIPPED_BAOBAB_LOG.get().asItem()))
                .add(key(ModBlocks.STRIPPED_BAOBAB_WOOD.get().asItem()));

        tag(ModTags.Items.BAOBAB_LOGS)
                .add(key(ModBlocks.BAOBAB_LOG.get().asItem()))
                .add(key(ModBlocks.BAOBAB_WOOD.get().asItem()))
                .add(key(ModBlocks.STRIPPED_BAOBAB_LOG.get().asItem()))
                .add(key(ModBlocks.STRIPPED_BAOBAB_WOOD.get().asItem()));

        tag(ItemTags.PLANKS)
                .add(key(ModBlocks.BAOBAB_PLANKS.get().asItem()));

        tag(ItemTags.WOODEN_STAIRS)
                .add(key(ModBlocks.BAOBAB_STAIRS.get().asItem()));

        tag(ItemTags.WOODEN_SLABS)
                .add(key(ModBlocks.BAOBAB_SLAB.get().asItem()));

        tag(ItemTags.WOODEN_FENCES)
                .add(key(ModBlocks.BAOBAB_FENCE.get().asItem()));

        tag(ItemTags.FENCE_GATES)
                .add(key(ModBlocks.BAOBAB_FENCE_GATE.get().asItem()));

        tag(ItemTags.WOODEN_BUTTONS)
                .add(key(ModBlocks.BAOBAB_BUTTON.get().asItem()));

        tag(ItemTags.WOODEN_PRESSURE_PLATES)
                .add(key(ModBlocks.BAOBAB_PRESSURE_PLATE.get().asItem()));

        tag(ItemTags.WOODEN_DOORS)
                .add(key(ModBlocks.BAOBAB_DOOR.get().asItem()));

        tag(ItemTags.WOODEN_TRAPDOORS)
                .add(key(ModBlocks.BAOBAB_TRAPDOOR.get().asItem()));

        tag(ItemTags.WOODEN_SHELVES)
                .add(key(ModBlocks.BAOBAB_SHELF.get().asItem()));

        tag(ItemTags.LEAVES)
                .add(key(ModBlocks.BAOBAB_LEAVES.get().asItem()));

        tag(ItemTags.BOATS)
                .add(key(ModItems.BAOBAB_BOAT.get()));

        tag(ItemTags.CHEST_BOATS)
                .add(key(ModItems.BAOBAB_CHEST_BOAT.get()));

        tag(ItemTags.SIGNS)
                .add(key(ModItems.BAOBAB_SIGN.get()));

        tag(ItemTags.HANGING_SIGNS)
                .add(key(ModItems.BAOBAB_HANGING_SIGN.get()));

        tag(Tags.Items.FENCE_GATES_WOODEN)
                .add(key(ModBlocks.BAOBAB_FENCE_GATE.get().asItem()));

        tag(Tags.Items.FENCES_WOODEN)
                .add(key(ModBlocks.BAOBAB_FENCE.get().asItem()));

        tag(Tags.Items.NATURAL_LOGS)
                .add(key(ModBlocks.BAOBAB_LOG.get().asItem()));

        tag(Tags.Items.STRIPPED_LOGS)
                .add(key(ModBlocks.STRIPPED_BAOBAB_LOG.get().asItem()));

        tag(Tags.Items.STRIPPED_WOODS)
                .add(key(ModBlocks.STRIPPED_BAOBAB_WOOD.get().asItem()));

        tag(Tags.Items.NATURAL_WOODS)
                .add(key(ModBlocks.BAOBAB_WOOD.get().asItem()));

        tag(Tags.Items.FOODS_FRUIT)
                .add(key(ModItems.BAOBAB_FRUIT.get()));
    }

    private static ResourceKey<net.minecraft.world.item.Item> key(net.minecraft.world.item.Item item) {
        return item.builtInRegistryHolder().key();
    }
}
