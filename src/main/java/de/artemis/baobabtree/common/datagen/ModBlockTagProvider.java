package de.artemis.baobabtree.common.datagen;

import de.artemis.baobabtree.BaobabTree;
import de.artemis.baobabtree.common.registry.ModBlocks;
import de.artemis.baobabtree.common.registry.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, BaobabTree.MOD_ID, existingFileHelper);
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
