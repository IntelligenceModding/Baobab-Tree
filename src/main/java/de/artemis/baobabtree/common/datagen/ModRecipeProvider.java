package de.artemis.baobabtree.common.datagen;

import de.artemis.baobabtree.BaobabTree;
import de.artemis.baobabtree.common.registry.ModBlocks;
import de.artemis.baobabtree.common.registry.ModItems;
import de.artemis.baobabtree.common.registry.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider.Runner {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        return new RecipeProvider(registries, output) {
            @Override
            protected void buildRecipes() {
                planksFromLogs(ModBlocks.BAOBAB_PLANKS.get(), ModTags.Items.BAOBAB_LOGS, 2);
                woodFromLogs(ModBlocks.BAOBAB_WOOD.get(), ModBlocks.BAOBAB_LOG.get());
                woodFromLogs(ModBlocks.STRIPPED_BAOBAB_WOOD.get(), ModBlocks.STRIPPED_BAOBAB_LOG.get());

                stairBuilder(ModBlocks.BAOBAB_STAIRS.get(), net.minecraft.world.item.crafting.Ingredient.of(ModBlocks.BAOBAB_PLANKS.get()))
                        .unlockedBy(getHasName(ModBlocks.BAOBAB_PLANKS.get()), has(ModBlocks.BAOBAB_PLANKS.get()))
                        .save(this.output);
                slab(RecipeCategory.BUILDING_BLOCKS, ModBlocks.BAOBAB_SLAB.get(), ModBlocks.BAOBAB_PLANKS.get());
                fenceBuilder(ModBlocks.BAOBAB_FENCE.get(), net.minecraft.world.item.crafting.Ingredient.of(ModBlocks.BAOBAB_PLANKS.get()))
                        .unlockedBy(getHasName(ModBlocks.BAOBAB_PLANKS.get()), has(ModBlocks.BAOBAB_PLANKS.get()))
                        .save(this.output);
                fenceGateBuilder(ModBlocks.BAOBAB_FENCE_GATE.get(), net.minecraft.world.item.crafting.Ingredient.of(ModBlocks.BAOBAB_PLANKS.get()))
                        .unlockedBy(getHasName(ModBlocks.BAOBAB_PLANKS.get()), has(ModBlocks.BAOBAB_PLANKS.get()))
                        .save(this.output);
                doorBuilder(ModBlocks.BAOBAB_DOOR.get(), net.minecraft.world.item.crafting.Ingredient.of(ModBlocks.BAOBAB_PLANKS.get()))
                        .unlockedBy(getHasName(ModBlocks.BAOBAB_PLANKS.get()), has(ModBlocks.BAOBAB_PLANKS.get()))
                        .save(this.output);
                trapdoorBuilder(ModBlocks.BAOBAB_TRAPDOOR.get(), net.minecraft.world.item.crafting.Ingredient.of(ModBlocks.BAOBAB_PLANKS.get()))
                        .unlockedBy(getHasName(ModBlocks.BAOBAB_PLANKS.get()), has(ModBlocks.BAOBAB_PLANKS.get()))
                        .save(this.output);
                ShapedRecipeBuilder.shaped(items, RecipeCategory.DECORATIONS, ModBlocks.BAOBAB_SHELF.get(), 6)
                        .group("shelf")
                        .pattern("###")
                        .pattern("   ")
                        .pattern("###")
                        .define('#', ModBlocks.STRIPPED_BAOBAB_LOG.get())
                        .unlockedBy(getHasName(ModBlocks.STRIPPED_BAOBAB_LOG.get()), has(ModBlocks.STRIPPED_BAOBAB_LOG.get()))
                        .save(this.output);
                buttonBuilder(ModBlocks.BAOBAB_BUTTON.get(), net.minecraft.world.item.crafting.Ingredient.of(ModBlocks.BAOBAB_PLANKS.get()))
                        .unlockedBy(getHasName(ModBlocks.BAOBAB_PLANKS.get()), has(ModBlocks.BAOBAB_PLANKS.get()))
                        .save(this.output);
                pressurePlate(ModBlocks.BAOBAB_PRESSURE_PLATE.get(), ModBlocks.BAOBAB_PLANKS.get());
                signBuilder(ModItems.BAOBAB_SIGN.get(), net.minecraft.world.item.crafting.Ingredient.of(ModBlocks.BAOBAB_PLANKS.get()))
                        .unlockedBy(getHasName(ModBlocks.BAOBAB_PLANKS.get()), has(ModBlocks.BAOBAB_PLANKS.get()))
                        .save(this.output);
                hangingSign(ModItems.BAOBAB_HANGING_SIGN.get(), ModBlocks.STRIPPED_BAOBAB_LOG.get());

                ShapedRecipeBuilder.shaped(items, RecipeCategory.TRANSPORTATION, ModItems.BAOBAB_BOAT.get())
                        .pattern("# #")
                        .pattern("###")
                        .define('#', ModBlocks.BAOBAB_PLANKS.get())
                        .unlockedBy(getHasName(ModBlocks.BAOBAB_PLANKS.get()), has(ModBlocks.BAOBAB_PLANKS.get()))
                        .save(this.output);

                ShapelessRecipeBuilder.shapeless(items, RecipeCategory.TRANSPORTATION, ModItems.BAOBAB_CHEST_BOAT.get())
                        .requires(ModItems.BAOBAB_BOAT.get())
                        .requires(Blocks.CHEST)
                        .unlockedBy(getHasName(ModItems.BAOBAB_BOAT.get()), has(ModItems.BAOBAB_BOAT.get()))
                        .save(this.output);

                ShapelessRecipeBuilder.shapeless(items, RecipeCategory.DECORATIONS, ModBlocks.BAOBAB_SAPLING.get())
                        .requires(ModItems.BAOBAB_FRUIT.get(), 4)
                        .unlockedBy(getHasName(ModItems.BAOBAB_FRUIT.get()), has(ModItems.BAOBAB_FRUIT.get()))
                        .save(this.output, BaobabTree.MOD_ID + ":baobab_sapling_from_fruit");

                ShapelessRecipeBuilder.shapeless(items, RecipeCategory.DECORATIONS, ModBlocks.BAOBAB_LITTER.get(), 2)
                        .requires(ModBlocks.BAOBAB_LEAVES.get())
                        .unlockedBy(getHasName(ModBlocks.BAOBAB_LEAVES.get()), has(ModBlocks.BAOBAB_LEAVES.get()))
                        .save(this.output);

                ShapelessRecipeBuilder.shapeless(items, RecipeCategory.DECORATIONS, ModBlocks.TREE_ROOT.get())
                        .requires(Blocks.DIRT)
                        .requires(Blocks.HANGING_ROOTS)
                        .unlockedBy(getHasName(Blocks.HANGING_ROOTS), has(Blocks.HANGING_ROOTS))
                        .save(this.output);

                ShapelessRecipeBuilder.shapeless(items, RecipeCategory.DECORATIONS, Blocks.HANGING_ROOTS)
                        .requires(ModBlocks.TREE_ROOT.get())
                        .unlockedBy(getHasName(ModBlocks.TREE_ROOT.get()), has(ModBlocks.TREE_ROOT.get()))
                        .save(this.output, BaobabTree.MOD_ID + ":hanging_roots_from_tree_root");

                ShapelessRecipeBuilder.shapeless(items, RecipeCategory.FOOD, ModItems.BAOBAB_FRUIT.get(), 2)
                        .requires(ModBlocks.SMALL_BAOBAB_FRUIT_POD.get())
                        .unlockedBy(getHasName(ModBlocks.SMALL_BAOBAB_FRUIT_POD.get()), has(ModBlocks.SMALL_BAOBAB_FRUIT_POD.get()))
                        .save(this.output, BaobabTree.MOD_ID + ":baobab_fruit_from_small_pod");

                ShapelessRecipeBuilder.shapeless(items, RecipeCategory.FOOD, ModItems.BAOBAB_FRUIT.get(), 4)
                        .requires(ModBlocks.MEDIUM_BAOBAB_FRUIT_POD.get())
                        .unlockedBy(getHasName(ModBlocks.MEDIUM_BAOBAB_FRUIT_POD.get()), has(ModBlocks.MEDIUM_BAOBAB_FRUIT_POD.get()))
                        .save(this.output, BaobabTree.MOD_ID + ":baobab_fruit_from_medium_pod");

                ShapelessRecipeBuilder.shapeless(items, RecipeCategory.FOOD, ModItems.BAOBAB_FRUIT.get(), 6)
                        .requires(ModBlocks.LARGE_BAOBAB_FRUIT_POD.get())
                        .unlockedBy(getHasName(ModBlocks.LARGE_BAOBAB_FRUIT_POD.get()), has(ModBlocks.LARGE_BAOBAB_FRUIT_POD.get()))
                        .save(this.output, BaobabTree.MOD_ID + ":baobab_fruit_from_large_pod");
            }
        };
    }

    @Override
    public String getName() {
        return "Recipes: " + BaobabTree.MOD_ID;
    }
}
