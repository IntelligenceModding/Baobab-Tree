package de.artemis.baobabtree.common.datagen;

import de.artemis.baobabtree.BaobabTree;
import de.artemis.baobabtree.common.registry.ModBlocks;
import de.artemis.baobabtree.common.registry.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.jetbrains.annotations.NotNull;

import net.minecraft.world.level.block.Block;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    private static final TagKey<Block> SAPLINGS = BlockTags.create(Identifier.withDefaultNamespace("saplings"));
    private static final TagKey<Block> LOGS_THAT_BURN = BlockTags.create(Identifier.withDefaultNamespace("logs_that_burn"));

    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, BaobabTree.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(key(ModBlocks.BAOBAB_LOG.get()))
                .add(key(ModBlocks.BAOBAB_WOOD.get()))
                .add(key(ModBlocks.STRIPPED_BAOBAB_LOG.get()))
                .add(key(ModBlocks.STRIPPED_BAOBAB_WOOD.get()))
                .add(key(ModBlocks.BAOBAB_PLANKS.get()))
                .add(key(ModBlocks.BAOBAB_STAIRS.get()))
                .add(key(ModBlocks.BAOBAB_SLAB.get()))
                .add(key(ModBlocks.BAOBAB_FENCE.get()))
                .add(key(ModBlocks.BAOBAB_FENCE_GATE.get()))
                .add(key(ModBlocks.BAOBAB_BUTTON.get()))
                .add(key(ModBlocks.BAOBAB_PRESSURE_PLATE.get()))
                .add(key(ModBlocks.BAOBAB_DOOR.get()))
                .add(key(ModBlocks.BAOBAB_TRAPDOOR.get()))
                .add(key(ModBlocks.BAOBAB_SHELF.get()))
                .add(key(ModBlocks.BAOBAB_SIGN.get()))
                .add(key(ModBlocks.BAOBAB_WALL_SIGN.get()))
                .add(key(ModBlocks.BAOBAB_HANGING_SIGN.get()))
                .add(key(ModBlocks.BAOBAB_WALL_HANGING_SIGN.get()))
                .add(key(ModBlocks.BAOBAB_FRUIT_POD.get()))
                .add(key(ModBlocks.SMALL_BAOBAB_FRUIT_POD.get()))
                .add(key(ModBlocks.MEDIUM_BAOBAB_FRUIT_POD.get()))
                .add(key(ModBlocks.LARGE_BAOBAB_FRUIT_POD.get()))
                .add(key(ModBlocks.TREE_ROOT.get()));

        tag(BlockTags.MINEABLE_WITH_HOE)
                .add(key(ModBlocks.BAOBAB_LEAVES.get()))
                .add(key(ModBlocks.BAOBAB_LITTER.get()));

        tag(SAPLINGS)
                .add(key(ModBlocks.BAOBAB_SAPLING.get()));

        tag(BlockTags.LEAVES)
                .add(key(ModBlocks.BAOBAB_LEAVES.get()));

        tag(BlockTags.FLOWER_POTS)
                .add(key(ModBlocks.POTTED_BAOBAB_SAPLING.get()));

        tag(BlockTags.CLIMBABLE)
                .add(key(ModBlocks.TREE_ROOT.get()));

        tag(BlockTags.REPLACEABLE_BY_TREES)
                .add(key(ModBlocks.BAOBAB_LITTER.get()));

        tag(BlockTags.REPLACEABLE)
                .add(key(ModBlocks.BAOBAB_LITTER.get()));

        tag(BlockTags.LOGS)
                .add(key(ModBlocks.BAOBAB_LOG.get()))
                .add(key(ModBlocks.BAOBAB_WOOD.get()))
                .add(key(ModBlocks.STRIPPED_BAOBAB_LOG.get()))
                .add(key(ModBlocks.STRIPPED_BAOBAB_WOOD.get()));

        tag(LOGS_THAT_BURN)
                .add(key(ModBlocks.BAOBAB_LOG.get()))
                .add(key(ModBlocks.BAOBAB_WOOD.get()))
                .add(key(ModBlocks.STRIPPED_BAOBAB_LOG.get()))
                .add(key(ModBlocks.STRIPPED_BAOBAB_WOOD.get()));

        tag(ModTags.Blocks.BAOBAB_LOGS)
                .add(key(ModBlocks.BAOBAB_LOG.get()))
                .add(key(ModBlocks.BAOBAB_WOOD.get()))
                .add(key(ModBlocks.STRIPPED_BAOBAB_LOG.get()))
                .add(key(ModBlocks.STRIPPED_BAOBAB_WOOD.get()));

        tag(Tags.Blocks.FENCE_GATES_WOODEN)
                .add(key(ModBlocks.BAOBAB_FENCE_GATE.get()));

        tag(Tags.Blocks.FENCES_WOODEN)
                .add(key(ModBlocks.BAOBAB_FENCE.get()));

        tag(Tags.Blocks.NATURAL_LOGS)
                .add(key(ModBlocks.BAOBAB_LOG.get()));

        tag(Tags.Blocks.OVERWORLD_NATURAL_LOGS)
                .add(key(ModBlocks.BAOBAB_LOG.get()));

        tag(Tags.Blocks.STRIPPED_LOGS)
                .add(key(ModBlocks.STRIPPED_BAOBAB_LOG.get()));

        tag(Tags.Blocks.STRIPPED_WOODS)
                .add(key(ModBlocks.STRIPPED_BAOBAB_WOOD.get()));

        tag(Tags.Blocks.NATURAL_WOODS)
                .add(key(ModBlocks.BAOBAB_WOOD.get()));

        tag(BlockTags.PLANKS)
                .add(key(ModBlocks.BAOBAB_PLANKS.get()));

        tag(BlockTags.WOODEN_STAIRS)
                .add(key(ModBlocks.BAOBAB_STAIRS.get()));

        tag(BlockTags.WOODEN_SLABS)
                .add(key(ModBlocks.BAOBAB_SLAB.get()));

        tag(BlockTags.WOODEN_FENCES)
                .add(key(ModBlocks.BAOBAB_FENCE.get()));

        tag(BlockTags.FENCES)
                .add(key(ModBlocks.BAOBAB_FENCE.get()));

        tag(BlockTags.FENCE_GATES)
                .add(key(ModBlocks.BAOBAB_FENCE_GATE.get()));

        tag(BlockTags.WOODEN_BUTTONS)
                .add(key(ModBlocks.BAOBAB_BUTTON.get()));

        tag(BlockTags.WOODEN_PRESSURE_PLATES)
                .add(key(ModBlocks.BAOBAB_PRESSURE_PLATE.get()));

        tag(BlockTags.WOODEN_DOORS)
                .add(key(ModBlocks.BAOBAB_DOOR.get()));

        tag(BlockTags.WOODEN_TRAPDOORS)
                .add(key(ModBlocks.BAOBAB_TRAPDOOR.get()));

        tag(BlockTags.WOODEN_SHELVES)
                .add(key(ModBlocks.BAOBAB_SHELF.get()));

        tag(BlockTags.STANDING_SIGNS)
                .add(key(ModBlocks.BAOBAB_SIGN.get()));

        tag(BlockTags.WALL_SIGNS)
                .add(key(ModBlocks.BAOBAB_WALL_SIGN.get()));

        tag(BlockTags.CEILING_HANGING_SIGNS)
                .add(key(ModBlocks.BAOBAB_HANGING_SIGN.get()));

        tag(BlockTags.WALL_HANGING_SIGNS)
                .add(key(ModBlocks.BAOBAB_WALL_HANGING_SIGN.get()));

        tag(BlockTags.SWORD_EFFICIENT)
                .add(key(ModBlocks.BAOBAB_FRUIT_POD.get()))
                .add(key(ModBlocks.SMALL_BAOBAB_FRUIT_POD.get()))
                .add(key(ModBlocks.MEDIUM_BAOBAB_FRUIT_POD.get()))
                .add(key(ModBlocks.LARGE_BAOBAB_FRUIT_POD.get()));
    }

    private static ResourceKey<Block> key(Block block) {
        return block.builtInRegistryHolder().key();
    }
}
