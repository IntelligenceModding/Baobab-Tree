package de.artemis.baobabtree.common.datagen;

import de.artemis.baobabtree.BaobabTree;
import de.artemis.baobabtree.common.registry.ModBlocks;
import de.artemis.baobabtree.common.registry.ModItems;
import de.artemis.baobabtree.common.registry.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, BaobabTree.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(ItemTags.SAPLINGS)
                .add(ModBlocks.BAOBAB_SAPLING.get().asItem());

        tag(ItemTags.LOGS_THAT_BURN)
                .add(ModBlocks.BAOBAB_LOG.get().asItem())
                .add(ModBlocks.BAOBAB_WOOD.get().asItem())
                .add(ModBlocks.STRIPPED_BAOBAB_LOG.get().asItem())
                .add(ModBlocks.STRIPPED_BAOBAB_WOOD.get().asItem());

        tag(ModTags.Items.BAOBAB_LOGS)
                .add(ModBlocks.BAOBAB_LOG.get().asItem())
                .add(ModBlocks.BAOBAB_WOOD.get().asItem())
                .add(ModBlocks.STRIPPED_BAOBAB_LOG.get().asItem())
                .add(ModBlocks.STRIPPED_BAOBAB_WOOD.get().asItem());

        tag(ItemTags.PLANKS)
                .add(ModBlocks.BAOBAB_PLANKS.get().asItem());

        tag(ItemTags.BOATS)
                .add(ModItems.BAOBAB_BOAT.get());

        tag(ItemTags.CHEST_BOATS)
                .add(ModItems.BAOBAB_CHEST_BOAT.get());

        tag(ItemTags.SIGNS)
                .add(ModItems.BAOBAB_SIGN.get());

        tag(ItemTags.HANGING_SIGNS)
                .add(ModItems.BAOBAB_HANGING_SIGN.get());
    }
}
