package de.artemis.baobabtree.common.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.artemis.baobabtree.BaobabTree;
import net.minecraft.core.Direction;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.FoliageColor;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModSpecialModelProvider implements DataProvider {
    private final PackOutput.PathProvider blockStatePathProvider;
    private final PackOutput.PathProvider itemPathProvider;
    private final PackOutput.PathProvider modelPathProvider;

    public ModSpecialModelProvider(PackOutput output) {
        this.blockStatePathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "blockstates");
        this.itemPathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "items");
        this.modelPathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> writes = new ArrayList<>();

        writes.add(saveBlockState(cache, "tree_root", treeRootBlockState()));
        writes.add(saveBlockState(cache, "baobab_litter", litterBlockState()));
        writes.add(saveBlockState(cache, "baobab_fruit_pod", hangingFruitBlockState()));
        writes.add(saveBlockState(cache, "small_baobab_fruit_pod", directionalBlockState("small_baobab_fruit_pod")));
        writes.add(saveBlockState(cache, "medium_baobab_fruit_pod", directionalBlockState("medium_baobab_fruit_pod")));
        writes.add(saveBlockState(cache, "large_baobab_fruit_pod", directionalBlockState("large_baobab_fruit_pod")));

        writes.add(saveModel(cache, "block/tree_root", cubeColumnModel("baobabtree:block/tree_roots_side", "baobabtree:block/tree_roots_top")));
        writes.add(saveModel(cache, "block/tree_root_horizontal", cubeColumnHorizontalModel("baobabtree:block/tree_roots_side", "baobabtree:block/tree_roots_top")));
        writes.add(saveModel(cache, "block/baobab_fruit_pod_base", crossModel("baobabtree:block/baobab_fruit_pod_stage0")));
        writes.add(saveModel(cache, "block/baobab_fruit_pod_growth_stage1", podGrowthModel(6, 7, 6, 10, 13, 10, "1", 4, 6, 4)));
        writes.add(saveModel(cache, "block/baobab_fruit_pod_growth_stage2", podGrowthModel(4, 3, 4, 12, 13, 12, "2", 8, 10, 8)));
        writes.add(saveModel(cache, "block/baobab_fruit_pod_growth_stage3", podGrowthModel(2, 1, 2, 14, 13, 14, "3", 12, 12, 12)));
        writes.add(saveModel(cache, "block/small_baobab_fruit_pod", podGrowthModel(6, 0, 6, 10, 6, 10, "1", 4, 6, 4)));
        writes.add(saveModel(cache, "block/medium_baobab_fruit_pod", podGrowthModel(4, 0, 4, 12, 10, 12, "2", 8, 10, 8)));
        writes.add(saveModel(cache, "block/large_baobab_fruit_pod", podGrowthModel(2, 0, 2, 14, 12, 14, "3", 12, 12, 12)));
        writes.add(saveModel(cache, "block/baobab_door_bottom_left", cutoutDoorModel("minecraft:block/door_bottom_left")));
        writes.add(saveModel(cache, "block/baobab_door_bottom_left_open", cutoutDoorModel("minecraft:block/door_bottom_left_open")));
        writes.add(saveModel(cache, "block/baobab_door_bottom_right", cutoutDoorModel("minecraft:block/door_bottom_right")));
        writes.add(saveModel(cache, "block/baobab_door_bottom_right_open", cutoutDoorModel("minecraft:block/door_bottom_right_open")));
        writes.add(saveModel(cache, "block/baobab_door_top_left", cutoutDoorModel("minecraft:block/door_top_left")));
        writes.add(saveModel(cache, "block/baobab_door_top_left_open", cutoutDoorModel("minecraft:block/door_top_left_open")));
        writes.add(saveModel(cache, "block/baobab_door_top_right", cutoutDoorModel("minecraft:block/door_top_right")));
        writes.add(saveModel(cache, "block/baobab_door_top_right_open", cutoutDoorModel("minecraft:block/door_top_right_open")));
        writes.add(saveModel(cache, "block/baobab_trapdoor_bottom", cutoutTrapdoorModel("minecraft:block/template_orientable_trapdoor_bottom")));
        writes.add(saveModel(cache, "block/baobab_trapdoor_open", cutoutTrapdoorModel("minecraft:block/template_orientable_trapdoor_open")));
        writes.add(saveModel(cache, "block/baobab_trapdoor_top", cutoutTrapdoorModel("minecraft:block/template_orientable_trapdoor_top")));
        writes.add(saveModel(cache, "block/baobab_sapling", cutoutCrossModel("baobabtree:block/baobab_sapling")));

        writes.add(saveItem(cache, "baobab_leaves", tintedItemModelReference("baobabtree:block/baobab_leaves", FoliageColor.FOLIAGE_DEFAULT)));
        writes.add(saveItem(cache, "baobab_litter", tintedItemModelReference("baobabtree:item/baobab_litter", "baobabtree:foliage")));
        writes.add(saveItem(cache, "baobab_fruit_pod", itemModelReference("baobabtree:item/baobab_fruit_pod")));
        writes.add(saveModel(cache, "item/baobab_litter", flatItemModel("baobabtree:item/baobab_litter")));
        writes.add(saveModel(cache, "item/baobab_fruit_pod", parentedModel("baobabtree:block/large_baobab_fruit_pod")));

        return CompletableFuture.allOf(writes.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Special Model Definitions - " + BaobabTree.MOD_ID;
    }

    private CompletableFuture<?> saveBlockState(CachedOutput cache, String name, JsonObject json) {
        return DataProvider.saveStable(cache, json, resourcePath(blockStatePathProvider, name));
    }

    private CompletableFuture<?> saveItem(CachedOutput cache, String name, JsonObject json) {
        return DataProvider.saveStable(cache, json, resourcePath(itemPathProvider, name));
    }

    private CompletableFuture<?> saveModel(CachedOutput cache, String name, JsonObject json) {
        return DataProvider.saveStable(cache, json, resourcePath(modelPathProvider, name));
    }

    private static Path resourcePath(PackOutput.PathProvider provider, String path) {
        return provider.json(id(path));
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(BaobabTree.MOD_ID, path);
    }

    private static JsonObject treeRootBlockState() {
        JsonObject root = new JsonObject();
        JsonObject variants = new JsonObject();
        variants.add("axis=x", variant("baobabtree:block/tree_root_horizontal", 90, 90));
        variants.add("axis=y", variant("baobabtree:block/tree_root", null, null));
        variants.add("axis=z", variant("baobabtree:block/tree_root_horizontal", 90, null));
        root.add("variants", variants);
        return root;
    }

    private static JsonObject directionalBlockState(String modelName) {
        JsonObject root = new JsonObject();
        JsonObject variants = new JsonObject();
        variants.add("facing=east", variant("baobabtree:block/" + modelName, null, 90));
        variants.add("facing=north", variant("baobabtree:block/" + modelName, null, null));
        variants.add("facing=south", variant("baobabtree:block/" + modelName, null, 180));
        variants.add("facing=west", variant("baobabtree:block/" + modelName, null, 270));
        root.add("variants", variants);
        return root;
    }

    private static JsonObject litterBlockState() {
        JsonObject root = new JsonObject();
        JsonArray multipart = new JsonArray();

        addLitterParts(multipart, "baobab_litter_1", 1);
        addLitterParts(multipart, "baobab_litter_2", 2);
        addLitterParts(multipart, "baobab_litter_2", 3);
        addLitterParts(multipart, "baobab_litter_3", 3);
        addLitterParts(multipart, "baobab_litter_4", 4);

        root.add("multipart", multipart);
        return root;
    }

    private static void addLitterParts(JsonArray multipart, String modelName, int amount) {
        addMultipartEntry(multipart, modelName, Direction.NORTH, 0, "flower_amount", Integer.toString(amount));
        addMultipartEntry(multipart, modelName, Direction.EAST, 90, "flower_amount", Integer.toString(amount));
        addMultipartEntry(multipart, modelName, Direction.SOUTH, 180, "flower_amount", Integer.toString(amount));
        addMultipartEntry(multipart, modelName, Direction.WEST, 270, "flower_amount", Integer.toString(amount));
    }

    private static JsonObject hangingFruitBlockState() {
        JsonObject root = new JsonObject();
        JsonArray multipart = new JsonArray();

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            int rotationY = ((int) direction.toYRot() + 180) % 360;

            for (int age = 0; age <= 3; age++) {
                multipart.add(multipartEntry("baobab_fruit_pod_base", rotationY, direction, age, null));
            }

            multipart.add(multipartEntry("baobab_fruit_pod_growth_stage1", rotationY, direction, 1, 1));
            multipart.add(multipartEntry("baobab_fruit_pod_growth_stage2", rotationY, direction, 2, 2));
            multipart.add(multipartEntry("baobab_fruit_pod_growth_stage3", rotationY, direction, 3, 3));
        }

        root.add("multipart", multipart);
        return root;
    }

    private static JsonObject multipartEntry(String modelName, int rotationY, Direction direction, int age, Integer stageAge) {
        JsonObject entry = new JsonObject();
        JsonObject apply = new JsonObject();
        apply.addProperty("model", "baobabtree:block/" + modelName);
        if (rotationY != 0) {
            apply.addProperty("y", rotationY);
        }
        entry.add("apply", apply);

        JsonObject when = new JsonObject();
        when.addProperty("age", Integer.toString(stageAge == null ? age : stageAge));
        when.addProperty("facing", direction.getSerializedName());
        entry.add("when", when);
        return entry;
    }

    private static void addMultipartEntry(JsonArray multipart, String modelName, Direction facing, int rotationY, String amountProperty, String amount) {
        JsonObject entry = new JsonObject();
        JsonObject apply = new JsonObject();
        apply.addProperty("model", "baobabtree:block/" + modelName);
        if (rotationY != 0) {
            apply.addProperty("y", rotationY);
        }
        entry.add("apply", apply);

        JsonObject when = new JsonObject();
        when.addProperty("facing", facing.getSerializedName());
        when.addProperty(amountProperty, amount);
        entry.add("when", when);

        multipart.add(entry);
    }

    private static JsonObject cubeColumnModel(String sideTexture, String endTexture) {
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:block/cube_column");
        JsonObject textures = new JsonObject();
        textures.addProperty("end", endTexture);
        textures.addProperty("side", sideTexture);
        root.add("textures", textures);
        return root;
    }

    private static JsonObject cubeColumnHorizontalModel(String sideTexture, String endTexture) {
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:block/cube_column_horizontal");
        JsonObject textures = new JsonObject();
        textures.addProperty("end", endTexture);
        textures.addProperty("side", sideTexture);
        root.add("textures", textures);
        return root;
    }

    private static JsonObject crossModel(String texture) {
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:block/cross");
        root.addProperty("render_type", "minecraft:cutout");
        JsonObject textures = new JsonObject();
        textures.addProperty("cross", texture);
        root.add("textures", textures);
        return root;
    }

    private static JsonObject cutoutCrossModel(String texture) {
        return crossModel(texture);
    }

    private static JsonObject cutoutDoorModel(String parent) {
        JsonObject root = new JsonObject();
        root.addProperty("parent", parent);
        root.addProperty("render_type", "minecraft:cutout");
        JsonObject textures = new JsonObject();
        textures.addProperty("bottom", "baobabtree:block/baobab_door_bottom");
        textures.addProperty("top", "baobabtree:block/baobab_door_top");
        root.add("textures", textures);
        return root;
    }

    private static JsonObject cutoutTrapdoorModel(String parent) {
        JsonObject root = new JsonObject();
        root.addProperty("parent", parent);
        root.addProperty("render_type", "minecraft:cutout");
        JsonObject textures = new JsonObject();
        textures.addProperty("texture", "baobabtree:block/baobab_trapdoor");
        root.add("textures", textures);
        return root;
    }

    private static JsonObject podGrowthModel(int fromX, int fromY, int fromZ, int toX, int toY, int toZ, String stage, int uvWidth, int uvHeight, int uvDepth) {
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:block/block");
        root.addProperty("render_type", "minecraft:cutout");

        JsonObject textures = new JsonObject();
        textures.addProperty("particle", "baobabtree:block/baobab_fruit_pod_side_stage" + stage);
        textures.addProperty("side", "baobabtree:block/baobab_fruit_pod_side_stage" + stage);
        textures.addProperty("end", "baobabtree:block/baobab_fruit_pod_top_stage" + stage);
        root.add("textures", textures);

        JsonArray elements = new JsonArray();
        JsonObject element = new JsonObject();
        element.add("from", vector(fromX, fromY, fromZ));
        element.add("to", vector(toX, toY, toZ));

        JsonObject faces = new JsonObject();
        faces.add("north", face("#side", 0, 0, uvWidth, uvHeight));
        faces.add("south", face("#side", 0, 0, uvWidth, uvHeight));
        faces.add("west", face("#side", 0, 0, uvDepth, uvHeight));
        faces.add("east", face("#side", 0, 0, uvDepth, uvHeight));
        faces.add("up", face("#end", 0, 0, uvWidth, uvDepth));
        faces.add("down", face("#end", uvWidth, uvDepth, 0, 0));
        element.add("faces", faces);

        elements.add(element);
        root.add("elements", elements);
        return root;
    }

    private static JsonObject itemModelReference(String model) {
        JsonObject root = new JsonObject();
        JsonObject modelObject = new JsonObject();
        modelObject.addProperty("type", "minecraft:model");
        modelObject.addProperty("model", model);
        root.add("model", modelObject);
        return root;
    }

    private static JsonObject tintedItemModelReference(String model, int color) {
        JsonObject root = itemModelReference(model);
        JsonArray tints = new JsonArray();
        JsonObject tint = new JsonObject();
        tint.addProperty("type", "minecraft:constant");
        tint.addProperty("value", color);
        tints.add(tint);
        root.getAsJsonObject("model").add("tints", tints);
        return root;
    }

    private static JsonObject tintedItemModelReference(String model, String tintType) {
        JsonObject root = itemModelReference(model);
        JsonArray tints = new JsonArray();
        JsonObject tint = new JsonObject();
        tint.addProperty("type", tintType);
        tints.add(tint);
        root.getAsJsonObject("model").add("tints", tints);
        return root;
    }

    private static JsonObject flatItemModel(String layer0) {
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:item/generated");
        JsonObject textures = new JsonObject();
        textures.addProperty("layer0", layer0);
        root.add("textures", textures);
        return root;
    }

    private static JsonObject parentedModel(String parent) {
        JsonObject root = new JsonObject();
        root.addProperty("parent", parent);
        return root;
    }

    private static JsonObject variant(String model, Integer xRotation, Integer yRotation) {
        JsonObject variant = new JsonObject();
        variant.addProperty("model", model);
        if (xRotation != null) {
            variant.addProperty("x", xRotation);
        }
        if (yRotation != null) {
            variant.addProperty("y", yRotation);
        }
        return variant;
    }

    private static JsonArray vector(int x, int y, int z) {
        JsonArray vector = new JsonArray();
        vector.add(x);
        vector.add(y);
        vector.add(z);
        return vector;
    }

    private static JsonObject face(String texture, int u1, int v1, int u2, int v2) {
        JsonObject face = new JsonObject();
        face.addProperty("texture", texture);
        JsonArray uv = new JsonArray();
        uv.add(u1);
        uv.add(v1);
        uv.add(u2);
        uv.add(v2);
        face.add("uv", uv);
        return face;
    }
}
