package de.artemis.baobabtree.common.datagen;

import de.artemis.baobabtree.BaobabTree;
import de.artemis.baobabtree.common.registry.ModBlocks;
import de.artemis.baobabtree.common.registry.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, BaobabTree.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(ModBlocks.BAOBAB_LOG.get())
                .add(ModBlocks.BAOBAB_WOOD.get())
                .add(ModBlocks.STRIPPED_BAOBAB_LOG.get())
                .add(ModBlocks.STRIPPED_BAOBAB_WOOD.get())
                .add(ModBlocks.BAOBAB_PLANKS.get())
                .add(ModBlocks.BAOBAB_STAIRS.get())
                .add(ModBlocks.BAOBAB_SLAB.get())
                .add(ModBlocks.BAOBAB_FENCE.get())
                .add(ModBlocks.BAOBAB_FENCE_GATE.get())
                .add(ModBlocks.BAOBAB_BUTTON.get())
                .add(ModBlocks.BAOBAB_PRESSURE_PLATE.get())
                .add(ModBlocks.BAOBAB_DOOR.get())
                .add(ModBlocks.BAOBAB_TRAPDOOR.get())
                .add(ModBlocks.BAOBAB_SHELF.get())
                .add(ModBlocks.BAOBAB_SIGN.get())
                .add(ModBlocks.BAOBAB_WALL_SIGN.get())
                .add(ModBlocks.BAOBAB_HANGING_SIGN.get())
                .add(ModBlocks.BAOBAB_WALL_HANGING_SIGN.get())
                .add(ModBlocks.TREE_ROOT.get());

        tag(BlockTags.MINEABLE_WITH_HOE)
                .add(ModBlocks.BAOBAB_LEAVES.get())
                .add(ModBlocks.BAOBAB_LITTER.get())
                .add(ModBlocks.BAOBAB_FRUIT_POD.get())
                .add(ModBlocks.SMALL_BAOBAB_FRUIT_POD.get())
                .add(ModBlocks.MEDIUM_BAOBAB_FRUIT_POD.get())
                .add(ModBlocks.LARGE_BAOBAB_FRUIT_POD.get());

        tag(BlockTags.SAPLINGS)
                .add(ModBlocks.BAOBAB_SAPLING.get());

        tag(BlockTags.LEAVES)
                .add(ModBlocks.BAOBAB_LEAVES.get());

        tag(BlockTags.FLOWER_POTS)
                .add(ModBlocks.POTTED_BAOBAB_SAPLING.get());

        tag(BlockTags.CLIMBABLE)
                .add(ModBlocks.TREE_ROOT.get());

        tag(BlockTags.REPLACEABLE_BY_TREES)
                .add(ModBlocks.BAOBAB_LITTER.get());

        tag(BlockTags.LOGS)
                .add(ModBlocks.BAOBAB_LOG.get())
                .add(ModBlocks.BAOBAB_WOOD.get())
                .add(ModBlocks.STRIPPED_BAOBAB_LOG.get())
                .add(ModBlocks.STRIPPED_BAOBAB_WOOD.get());

        tag(BlockTags.LOGS_THAT_BURN)
                .add(ModBlocks.BAOBAB_LOG.get())
                .add(ModBlocks.BAOBAB_WOOD.get())
                .add(ModBlocks.STRIPPED_BAOBAB_LOG.get())
                .add(ModBlocks.STRIPPED_BAOBAB_WOOD.get());

        tag(ModTags.Blocks.BAOBAB_LOGS)
                .add(ModBlocks.BAOBAB_LOG.get())
                .add(ModBlocks.BAOBAB_WOOD.get())
                .add(ModBlocks.STRIPPED_BAOBAB_LOG.get())
                .add(ModBlocks.STRIPPED_BAOBAB_WOOD.get());

        tag(Tags.Blocks.FENCE_GATES_WOODEN)
                .add(ModBlocks.BAOBAB_FENCE_GATE.get());

        tag(Tags.Blocks.STRIPPED_LOGS)
                .add(ModBlocks.STRIPPED_BAOBAB_LOG.get());

        tag(Tags.Blocks.STRIPPED_WOODS)
                .add(ModBlocks.STRIPPED_BAOBAB_WOOD.get());

        tag(Tags.Blocks.NATURAL_WOODS)
                .add(ModBlocks.BAOBAB_WOOD.get());

        tag(BlockTags.PLANKS)
                .add(ModBlocks.BAOBAB_PLANKS.get());

        tag(BlockTags.WOODEN_STAIRS)
                .add(ModBlocks.BAOBAB_STAIRS.get());

        tag(BlockTags.WOODEN_SLABS)
                .add(ModBlocks.BAOBAB_SLAB.get());

        tag(BlockTags.WOODEN_FENCES)
                .add(ModBlocks.BAOBAB_FENCE.get());

        tag(BlockTags.FENCE_GATES)
                .add(ModBlocks.BAOBAB_FENCE_GATE.get());

        tag(BlockTags.WOODEN_BUTTONS)
                .add(ModBlocks.BAOBAB_BUTTON.get());

        tag(BlockTags.WOODEN_PRESSURE_PLATES)
                .add(ModBlocks.BAOBAB_PRESSURE_PLATE.get());

        tag(BlockTags.WOODEN_DOORS)
                .add(ModBlocks.BAOBAB_DOOR.get());

        tag(BlockTags.WOODEN_TRAPDOORS)
                .add(ModBlocks.BAOBAB_TRAPDOOR.get());

        tag(BlockTags.WOODEN_SHELVES)
                .add(ModBlocks.BAOBAB_SHELF.get());

        tag(BlockTags.STANDING_SIGNS)
                .add(ModBlocks.BAOBAB_SIGN.get());

        tag(BlockTags.WALL_SIGNS)
                .add(ModBlocks.BAOBAB_WALL_SIGN.get());

        tag(BlockTags.CEILING_HANGING_SIGNS)
                .add(ModBlocks.BAOBAB_HANGING_SIGN.get());

        tag(BlockTags.WALL_HANGING_SIGNS)
                .add(ModBlocks.BAOBAB_WALL_HANGING_SIGN.get());
    }
}
