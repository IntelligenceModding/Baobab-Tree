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
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
        planksFromLogs(recipeOutput, ModBlocks.BAOBAB_PLANKS.get(), ModTags.Items.BAOBAB_LOGS, 4);
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
                .requires(ModItems.BAOBAB_SEEDS.get(), 4)
                .unlockedBy(getHasName(ModItems.BAOBAB_SEEDS.get()), has(ModItems.BAOBAB_SEEDS.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(BaobabTree.MOD_ID, "baobab_sapling_from_seeds"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, ModBlocks.BAOBAB_LEAF_LITTER.get(), 2)
                .requires(ModBlocks.BAOBAB_LEAVES.get())
                .unlockedBy(getHasName(ModBlocks.BAOBAB_LEAVES.get()), has(ModBlocks.BAOBAB_LEAVES.get()))
                .save(recipeOutput);

        foodSmelting(recipeOutput, ModItems.BAOBAB_FRUIT.get(), ModItems.DRIED_BAOBAB_PULP.get(), 0.1F, 200);
        foodSmoking(recipeOutput, ModItems.BAOBAB_FRUIT.get(), ModItems.DRIED_BAOBAB_PULP.get(), 0.1F, 100);
        foodCampfireCooking(recipeOutput, ModItems.BAOBAB_FRUIT.get(), ModItems.DRIED_BAOBAB_PULP.get(), 0.1F, 600);
    }

    private static void foodSmelting(RecipeOutput recipeOutput, ItemLike input, ItemLike output, float exp, int time) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(input), RecipeCategory.FOOD, output, exp, time)
                .unlockedBy(getHasName(input), has(input))
                .save(recipeOutput, BaobabTree.MOD_ID + ":" + getItemName(output) + "_from_smelting");
    }

    private static void foodSmoking(RecipeOutput recipeOutput, ItemLike input, ItemLike output, float exp, int time) {
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(input), RecipeCategory.FOOD, output, exp, time)
                .unlockedBy(getHasName(input), has(input))
                .save(recipeOutput, BaobabTree.MOD_ID + ":" + getItemName(output) + "_from_smoking");
    }

    private static void foodCampfireCooking(RecipeOutput recipeOutput, ItemLike input, ItemLike output, float exp, int time) {
        SimpleCookingRecipeBuilder.generic(Ingredient.of(input), RecipeCategory.FOOD, output, exp, time, RecipeSerializer.CAMPFIRE_COOKING_RECIPE, net.minecraft.world.item.crafting.CampfireCookingRecipe::new)
                .unlockedBy(getHasName(input), has(input))
                .save(recipeOutput, BaobabTree.MOD_ID + ":" + getItemName(output) + "_from_campfire_cooking");
    }
}
