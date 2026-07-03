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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
        planksFromLogs(recipeOutput, ModBlocks.BAOBAB_PLANKS.get(), ModTags.Items.BAOBAB_LOGS, 2);
        woodFromLogs(recipeOutput, ModBlocks.BAOBAB_WOOD.get(), ModBlocks.BAOBAB_LOG.get());
        woodFromLogs(recipeOutput, ModBlocks.STRIPPED_BAOBAB_WOOD.get(), ModBlocks.STRIPPED_BAOBAB_LOG.get());

        stairBuilder(ModBlocks.BAOBAB_STAIRS.get(), Ingredient.of(ModBlocks.BAOBAB_PLANKS.get()))
                .unlockedBy(getHasName(ModBlocks.BAOBAB_PLANKS.get()), has(ModBlocks.BAOBAB_PLANKS.get()))
                .save(recipeOutput);
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BAOBAB_SLAB.get(), ModBlocks.BAOBAB_PLANKS.get());
        fenceBuilder(ModBlocks.BAOBAB_FENCE.get(), Ingredient.of(ModBlocks.BAOBAB_PLANKS.get()))
                .unlockedBy(getHasName(ModBlocks.BAOBAB_PLANKS.get()), has(ModBlocks.BAOBAB_PLANKS.get()))
                .save(recipeOutput);
        fenceGateBuilder(ModBlocks.BAOBAB_FENCE_GATE.get(), Ingredient.of(ModBlocks.BAOBAB_PLANKS.get()))
                .unlockedBy(getHasName(ModBlocks.BAOBAB_PLANKS.get()), has(ModBlocks.BAOBAB_PLANKS.get()))
                .save(recipeOutput);
        doorBuilder(ModBlocks.BAOBAB_DOOR.get(), Ingredient.of(ModBlocks.BAOBAB_PLANKS.get()))
                .unlockedBy(getHasName(ModBlocks.BAOBAB_PLANKS.get()), has(ModBlocks.BAOBAB_PLANKS.get()))
                .save(recipeOutput);
        trapdoorBuilder(ModBlocks.BAOBAB_TRAPDOOR.get(), Ingredient.of(ModBlocks.BAOBAB_PLANKS.get()))
                .unlockedBy(getHasName(ModBlocks.BAOBAB_PLANKS.get()), has(ModBlocks.BAOBAB_PLANKS.get()))
                .save(recipeOutput);
        buttonBuilder(ModBlocks.BAOBAB_BUTTON.get(), Ingredient.of(ModBlocks.BAOBAB_PLANKS.get()))
                .unlockedBy(getHasName(ModBlocks.BAOBAB_PLANKS.get()), has(ModBlocks.BAOBAB_PLANKS.get()))
                .save(recipeOutput);
        pressurePlate(recipeOutput, ModBlocks.BAOBAB_PRESSURE_PLATE.get(), ModBlocks.BAOBAB_PLANKS.get());
        signBuilder(ModItems.BAOBAB_SIGN.get(), Ingredient.of(ModBlocks.BAOBAB_PLANKS.get()))
                .unlockedBy(getHasName(ModBlocks.BAOBAB_PLANKS.get()), has(ModBlocks.BAOBAB_PLANKS.get()))
                .save(recipeOutput);
        hangingSign(recipeOutput, ModItems.BAOBAB_HANGING_SIGN.get(), ModBlocks.STRIPPED_BAOBAB_LOG.get());

        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, ModItems.BAOBAB_BOAT.get())
                .pattern("# #")
                .pattern("###")
                .define('#', ModBlocks.BAOBAB_PLANKS.get())
                .unlockedBy(getHasName(ModBlocks.BAOBAB_PLANKS.get()), has(ModBlocks.BAOBAB_PLANKS.get()))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, ModItems.BAOBAB_CHEST_BOAT.get())
                .requires(ModItems.BAOBAB_BOAT.get())
                .requires(Blocks.CHEST)
                .unlockedBy(getHasName(ModItems.BAOBAB_BOAT.get()), has(ModItems.BAOBAB_BOAT.get()))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModBlocks.BAOBAB_SAPLING.get())
                .requires(ModItems.BAOBAB_FRUIT.get(), 4)
                .unlockedBy(getHasName(ModItems.BAOBAB_FRUIT.get()), has(ModItems.BAOBAB_FRUIT.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(BaobabTree.MOD_ID, "baobab_sapling_from_fruit"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModBlocks.BAOBAB_LITTER.get(), 2)
                .requires(ModBlocks.BAOBAB_LEAVES.get())
                .unlockedBy(getHasName(ModBlocks.BAOBAB_LEAVES.get()), has(ModBlocks.BAOBAB_LEAVES.get()))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModBlocks.TREE_ROOT.get())
                .requires(Blocks.DIRT)
                .requires(Blocks.HANGING_ROOTS)
                .unlockedBy(getHasName(Blocks.HANGING_ROOTS), has(Blocks.HANGING_ROOTS))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, Blocks.HANGING_ROOTS)
                .requires(ModBlocks.TREE_ROOT.get())
                .unlockedBy(getHasName(ModBlocks.TREE_ROOT.get()), has(ModBlocks.TREE_ROOT.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(BaobabTree.MOD_ID, "hanging_roots_from_tree_root"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.BAOBAB_FRUIT.get(), 2)
                .requires(ModBlocks.SMALL_BAOBAB_FRUIT_POD.get())
                .unlockedBy(getHasName(ModBlocks.SMALL_BAOBAB_FRUIT_POD.get()), has(ModBlocks.SMALL_BAOBAB_FRUIT_POD.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(BaobabTree.MOD_ID, "baobab_fruit_from_small_pod"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.BAOBAB_FRUIT.get(), 4)
                .requires(ModBlocks.MEDIUM_BAOBAB_FRUIT_POD.get())
                .unlockedBy(getHasName(ModBlocks.MEDIUM_BAOBAB_FRUIT_POD.get()), has(ModBlocks.MEDIUM_BAOBAB_FRUIT_POD.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(BaobabTree.MOD_ID, "baobab_fruit_from_medium_pod"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.BAOBAB_FRUIT.get(), 6)
                .requires(ModBlocks.LARGE_BAOBAB_FRUIT_POD.get())
                .unlockedBy(getHasName(ModBlocks.LARGE_BAOBAB_FRUIT_POD.get()), has(ModBlocks.LARGE_BAOBAB_FRUIT_POD.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(BaobabTree.MOD_ID, "baobab_fruit_from_large_pod"));
    }
}
