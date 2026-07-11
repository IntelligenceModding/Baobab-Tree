package de.artemis.baobabtree.common.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.artemis.baobabtree.BaobabTree;
import de.artemis.baobabtree.common.registry.ModBlocks;
import de.artemis.baobabtree.common.registry.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public final class ModModelProvider extends ModelProvider {
    private static final int DEFAULT_FOLIAGE_TINT = -12012264;
    private static final int DEFAULT_ACACIA_FOLIAGE_TINT = -5331926;
    private static final Set<Block> CUSTOM_BLOCKSTATES = Set.of(
            ModBlocks.BAOBAB_SHELF.get(),
            ModBlocks.BAOBAB_LITTER.get(),
            ModBlocks.BAOBAB_FRUIT_POD.get(),
            ModBlocks.SMALL_BAOBAB_FRUIT_POD.get(),
            ModBlocks.MEDIUM_BAOBAB_FRUIT_POD.get(),
            ModBlocks.LARGE_BAOBAB_FRUIT_POD.get()
    );

    private static final TexturedModel.Provider TREE_ROOT_VERTICAL =
            block -> new TexturedModel(treeRootMapping(), net.minecraft.client.data.models.model.ModelTemplates.CUBE_COLUMN);
    private static final TexturedModel.Provider TREE_ROOT_HORIZONTAL =
            block -> new TexturedModel(treeRootMapping(), net.minecraft.client.data.models.model.ModelTemplates.CUBE_COLUMN_HORIZONTAL);

    private final PackOutput.PathProvider blockstatesPathProvider;
    private final PackOutput.PathProvider blockModelPathProvider;
    private final PackOutput.PathProvider itemDefinitionPathProvider;
    public ModModelProvider(PackOutput output) {
        super(output, BaobabTree.MOD_ID);
        this.blockstatesPathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "blockstates");
        this.blockModelPathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models/block");
        this.itemDefinitionPathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "items");
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return super.getKnownBlocks().filter(holder -> !CUSTOM_BLOCKSTATES.contains(holder.value()));
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        blockModels.woodProvider(ModBlocks.BAOBAB_LOG.get())
                .logWithHorizontal(ModBlocks.BAOBAB_LOG.get())
                .wood(ModBlocks.BAOBAB_WOOD.get());
        blockModels.woodProvider(ModBlocks.STRIPPED_BAOBAB_LOG.get())
                .logWithHorizontal(ModBlocks.STRIPPED_BAOBAB_LOG.get())
                .wood(ModBlocks.STRIPPED_BAOBAB_WOOD.get());

        blockModels.family(ModBlocks.BAOBAB_PLANKS.get()).generateFor(new BlockFamily.Builder(ModBlocks.BAOBAB_PLANKS.get())
                .button(ModBlocks.BAOBAB_BUTTON.get())
                .door(ModBlocks.BAOBAB_DOOR.get())
                .fence(ModBlocks.BAOBAB_FENCE.get())
                .fenceGate(ModBlocks.BAOBAB_FENCE_GATE.get())
                .pressurePlate(ModBlocks.BAOBAB_PRESSURE_PLATE.get())
                .sign(ModBlocks.BAOBAB_SIGN.get(), ModBlocks.BAOBAB_WALL_SIGN.get())
                .slab(ModBlocks.BAOBAB_SLAB.get())
                .stairs(ModBlocks.BAOBAB_STAIRS.get())
                .trapdoor(ModBlocks.BAOBAB_TRAPDOOR.get())
                .getFamily());
        blockModels.createHangingSign(ModBlocks.BAOBAB_PLANKS.get(), ModBlocks.BAOBAB_HANGING_SIGN.get(), ModBlocks.BAOBAB_WALL_HANGING_SIGN.get());

        blockModels.createTrivialBlock(ModBlocks.BAOBAB_LEAVES.get(), TexturedModel.LEAVES);
        blockModels.createPlantWithDefaultItem(ModBlocks.BAOBAB_SAPLING.get(), ModBlocks.POTTED_BAOBAB_SAPLING.get(), BlockModelGenerators.PlantType.NOT_TINTED);
        blockModels.createRotatedPillarWithHorizontalVariant(ModBlocks.TREE_ROOT.get(), TREE_ROOT_VERTICAL, TREE_ROOT_HORIZONTAL);

        blockModels.registerSimpleItemModel(ModBlocks.BAOBAB_FRUIT_POD.get(), modModel("large_baobab_fruit_pod"));
        blockModels.registerSimpleItemModel(ModBlocks.SMALL_BAOBAB_FRUIT_POD.get(), modModel("small_baobab_fruit_pod"));
        blockModels.registerSimpleItemModel(ModBlocks.MEDIUM_BAOBAB_FRUIT_POD.get(), modModel("medium_baobab_fruit_pod"));
        blockModels.registerSimpleItemModel(ModBlocks.LARGE_BAOBAB_FRUIT_POD.get(), modModel("large_baobab_fruit_pod"));

        itemModels.itemModelOutput.accept(
                ModBlocks.BAOBAB_LEAVES.get().asItem(),
                ItemModelUtils.tintedModel(modModel("baobab_leaves"), foliageTint())
        );
        Identifier litterItemModel = itemModels.createFlatItemModel(ModBlocks.BAOBAB_LITTER.get().asItem(), ModelTemplates.FLAT_ITEM);
        itemModels.itemModelOutput.accept(
                ModBlocks.BAOBAB_LITTER.get().asItem(),
                ItemModelUtils.tintedModel(litterItemModel, acaciaFoliageTint())
        );
        itemModels.generateFlatItem(ModItems.BAOBAB_BOAT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.BAOBAB_CHEST_BOAT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.BAOBAB_FRUIT.get(), ModelTemplates.FLAT_ITEM);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return super.run(output).thenCompose(ignored -> writeCustomAssets(output));
    }

    private CompletableFuture<?> writeCustomAssets(CachedOutput output) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        futures.add(saveBlockState(output, "baobab_litter", litterBlockState()));
        futures.add(saveBlockState(output, "baobab_fruit_pod", fruitPodBlockState()));
        futures.add(saveBlockState(output, "small_baobab_fruit_pod", horizontalFacingBlockState("small_baobab_fruit_pod")));
        futures.add(saveBlockState(output, "medium_baobab_fruit_pod", horizontalFacingBlockState("medium_baobab_fruit_pod")));
        futures.add(saveBlockState(output, "large_baobab_fruit_pod", horizontalFacingBlockState("large_baobab_fruit_pod")));
        futures.add(saveBlockState(output, "baobab_shelf", shelfBlockState()));

        futures.add(saveBlockModel(output, "baobab_fruit_pod_base", crossModel("baobab_fruit_pod_stage0")));
        futures.add(saveBlockModel(output, "baobab_fruit_pod_growth_stage1", podModel("1", 6.0, 7.0, 6.0, 10.0, 13.0, 10.0, 4.0, 6.0, 4.0)));
        futures.add(saveBlockModel(output, "baobab_fruit_pod_growth_stage2", podModel("2", 4.0, 3.0, 4.0, 12.0, 13.0, 12.0, 8.0, 10.0, 8.0)));
        futures.add(saveBlockModel(output, "baobab_fruit_pod_growth_stage3", podModel("3", 2.0, 1.0, 2.0, 14.0, 13.0, 14.0, 12.0, 12.0, 12.0)));
        futures.add(saveBlockModel(output, "small_baobab_fruit_pod", podModel("1", 6.0, 0.0, 6.0, 10.0, 6.0, 10.0, 4.0, 6.0, 4.0)));
        futures.add(saveBlockModel(output, "medium_baobab_fruit_pod", podModel("2", 4.0, 0.0, 4.0, 12.0, 10.0, 12.0, 8.0, 10.0, 8.0)));
        futures.add(saveBlockModel(output, "large_baobab_fruit_pod", podModel("3", 2.0, 0.0, 2.0, 14.0, 12.0, 14.0, 12.0, 12.0, 12.0)));
        futures.add(saveBlockModel(output, "baobab_shelf", shelfModel("acacia_shelf")));
        futures.add(saveBlockModel(output, "baobab_shelf_unpowered", shelfModel("acacia_shelf_unpowered")));
        futures.add(saveBlockModel(output, "baobab_shelf_unconnected", shelfModel("acacia_shelf_unconnected")));
        futures.add(saveBlockModel(output, "baobab_shelf_left", shelfModel("acacia_shelf_left")));
        futures.add(saveBlockModel(output, "baobab_shelf_center", shelfModel("acacia_shelf_center")));
        futures.add(saveBlockModel(output, "baobab_shelf_right", shelfModel("acacia_shelf_right")));
        futures.add(saveBlockModel(output, "baobab_shelf_inventory", shelfModel("acacia_shelf_inventory")));
        futures.add(saveItemDefinition(output, "baobab_shelf", shelfItemDefinition()));

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    private CompletableFuture<?> saveBlockState(CachedOutput output, String name, JsonObject json) {
        return DataProvider.saveStable(output, json, blockstatesPathProvider.json(id(name)));
    }

    private CompletableFuture<?> saveBlockModel(CachedOutput output, String name, JsonObject json) {
        return DataProvider.saveStable(output, json, blockModelPathProvider.json(id(name)));
    }

    private CompletableFuture<?> saveItemDefinition(CachedOutput output, String name, JsonObject json) {
        return DataProvider.saveStable(output, json, itemDefinitionPathProvider.json(id(name)));
    }

    private static TextureMapping treeRootMapping() {
        return new TextureMapping()
                .put(TextureSlot.SIDE, new Material(Identifier.fromNamespaceAndPath(BaobabTree.MOD_ID, "block/tree_roots_side")))
                .put(TextureSlot.END, new Material(Identifier.fromNamespaceAndPath(BaobabTree.MOD_ID, "block/tree_roots_top")));
    }

    private static JsonObject fruitPodBlockState() {
        JsonArray multipart = new JsonArray();

        for (Direction facing : Direction.Plane.HORIZONTAL) {
            int rotation = rotationFor(facing);
            for (int age = 0; age <= 3; age++) {
                multipart.add(multipartEntry("baobab_fruit_pod_base", rotation, age, facing));
            }
            for (int age = 1; age <= 3; age++) {
                multipart.add(multipartEntry("baobab_fruit_pod_growth_stage" + age, rotation, age, facing));
            }
        }

        JsonObject root = new JsonObject();
        root.add("multipart", multipart);
        return root;
    }

    private static JsonObject shelfBlockState() {
        JsonArray multipart = new JsonArray();

        for (Direction facing : Direction.Plane.HORIZONTAL) {
            int rotation = rotationFor(facing);
            multipart.add(shelfMultipartEntry("baobab_shelf", rotation, facing, null, null));
            multipart.add(shelfMultipartEntry("baobab_shelf_unpowered", rotation, facing, Boolean.FALSE, null));
            multipart.add(shelfMultipartEntry("baobab_shelf_unconnected", rotation, facing, Boolean.TRUE, "unconnected"));
            multipart.add(shelfMultipartEntry("baobab_shelf_left", rotation, facing, Boolean.TRUE, "left"));
            multipart.add(shelfMultipartEntry("baobab_shelf_center", rotation, facing, Boolean.TRUE, "center"));
            multipart.add(shelfMultipartEntry("baobab_shelf_right", rotation, facing, Boolean.TRUE, "right"));
        }

        JsonObject root = new JsonObject();
        root.add("multipart", multipart);
        return root;
    }

    private static JsonObject litterBlockState() {
        JsonArray multipart = new JsonArray();

        addLitterEntries(multipart, "baobab_litter_1", 1);
        addLitterEntries(multipart, "baobab_litter_2", 2);
        addLitterEntries(multipart, "baobab_litter_2", 3);
        addLitterEntries(multipart, "baobab_litter_3", 3);
        addLitterEntries(multipart, "baobab_litter_4", 4);

        JsonObject root = new JsonObject();
        root.add("multipart", multipart);
        return root;
    }

    private static JsonObject horizontalFacingBlockState(String modelName) {
        JsonObject variants = new JsonObject();
        for (Direction facing : Direction.Plane.HORIZONTAL) {
            JsonObject variant = new JsonObject();
            variant.addProperty("model", modelId("block/" + modelName));
            int rotation = rotationFor(facing);
            if (rotation != 0) {
                variant.addProperty("y", rotation);
            }
            variants.add("facing=" + facing.getSerializedName(), variant);
        }

        JsonObject root = new JsonObject();
        root.add("variants", variants);
        return root;
    }

    private static JsonObject shelfMultipartEntry(String modelName,
                                                  int yRotation,
                                                  Direction facing,
                                                  Boolean powered,
                                                  String sideChainPart) {
        JsonObject entry = new JsonObject();
        JsonObject apply = new JsonObject();
        apply.addProperty("model", modelId("block/" + modelName));
        if (yRotation != 0) {
            apply.addProperty("y", yRotation);
        }
        entry.add("apply", apply);

        JsonObject when = new JsonObject();
        if (powered == null && sideChainPart == null) {
            when.addProperty("facing", facing.getSerializedName());
        } else {
            JsonArray and = new JsonArray();
            and.add(propertyCondition("facing", facing.getSerializedName()));
            if (powered != null) {
                and.add(propertyCondition("powered", powered.toString()));
            }
            if (sideChainPart != null) {
                and.add(propertyCondition("side_chain", sideChainPart));
            }
            when.add("AND", and);
        }
        entry.add("when", when);
        return entry;
    }

    private static JsonObject multipartEntry(String modelName, int yRotation, int age, Direction facing) {
        JsonObject entry = new JsonObject();
        JsonObject apply = new JsonObject();
        apply.addProperty("model", modelId("block/" + modelName));
        if (yRotation != 0) {
            apply.addProperty("y", yRotation);
        }
        entry.add("apply", apply);

        JsonObject when = new JsonObject();
        when.addProperty("age", Integer.toString(age));
        when.addProperty("facing", facing.getSerializedName());
        entry.add("when", when);
        return entry;
    }

    private static void addLitterEntries(JsonArray multipart, String modelName, int amount) {
        for (Direction facing : Direction.Plane.HORIZONTAL) {
            JsonObject entry = new JsonObject();
            JsonObject apply = new JsonObject();
            apply.addProperty("model", modelId("block/" + modelName));
            int rotation = rotationFor(facing);
            if (rotation != 0) {
                apply.addProperty("y", rotation);
            }
            entry.add("apply", apply);

            JsonObject when = new JsonObject();
            when.addProperty("facing", facing.getSerializedName());
            when.addProperty("amount", Integer.toString(amount));
            entry.add("when", when);
            multipart.add(entry);
        }
    }

    private static JsonObject crossModel(String textureName) {
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:block/cross");
        root.addProperty("render_type", "minecraft:cutout");

        JsonObject textures = new JsonObject();
        textures.addProperty("cross", modelId("block/" + textureName));
        textures.addProperty("particle", modelId("block/" + textureName));
        root.add("textures", textures);
        return root;
    }

    private static JsonObject podModel(String stage,
                                       double fromX,
                                       double fromY,
                                       double fromZ,
                                       double toX,
                                       double toY,
                                       double toZ,
                                       double uvWidth,
                                       double uvHeight,
                                       double uvDepth) {
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:block/block");
        root.addProperty("render_type", "minecraft:cutout");

        JsonObject textures = new JsonObject();
        textures.addProperty("particle", modelId("block/baobab_fruit_pod_side_stage" + stage));
        textures.addProperty("side", modelId("block/baobab_fruit_pod_side_stage" + stage));
        textures.addProperty("end", modelId("block/baobab_fruit_pod_top_stage" + stage));
        root.add("textures", textures);

        JsonObject element = new JsonObject();
        element.add("from", point(fromX, fromY, fromZ));
        element.add("to", point(toX, toY, toZ));

        JsonObject faces = new JsonObject();
        faces.add("north", face("#side", 0.0, 0.0, uvWidth, uvHeight));
        faces.add("south", face("#side", 0.0, 0.0, uvWidth, uvHeight));
        faces.add("west", face("#side", 0.0, 0.0, uvDepth, uvHeight));
        faces.add("east", face("#side", 0.0, 0.0, uvDepth, uvHeight));
        faces.add("up", face("#end", 0.0, 0.0, uvWidth, uvDepth));
        faces.add("down", face("#end", uvWidth, uvDepth, 0.0, 0.0));
        element.add("faces", faces);

        JsonArray elements = new JsonArray();
        elements.add(element);
        root.add("elements", elements);
        return root;
    }

    private static JsonArray point(double x, double y, double z) {
        JsonArray point = new JsonArray();
        point.add(x);
        point.add(y);
        point.add(z);
        return point;
    }

    private static JsonObject propertyCondition(String key, String value) {
        JsonObject condition = new JsonObject();
        condition.addProperty(key, value);
        return condition;
    }

    private static JsonObject face(String texture, double u0, double v0, double u1, double v1) {
        JsonObject face = new JsonObject();
        face.addProperty("texture", texture);
        JsonArray uv = new JsonArray();
        uv.add(u0);
        uv.add(v0);
        uv.add(u1);
        uv.add(v1);
        face.add("uv", uv);
        return face;
    }

    private static JsonObject shelfModel(String parentModelName) {
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:block/" + parentModelName);

        JsonObject textures = new JsonObject();
        textures.addProperty("all", modelId("block/baobab_shelf"));
        textures.addProperty("particle", modelId("block/stripped_baobab_log"));
        root.add("textures", textures);
        return root;
    }

    private static JsonObject shelfItemDefinition() {
        JsonObject root = new JsonObject();
        JsonObject model = new JsonObject();
        model.addProperty("type", "minecraft:model");
        model.addProperty("model", modelId("block/baobab_shelf_inventory"));
        root.add("model", model);
        return root;
    }

    private static int rotationFor(Direction facing) {
        return switch (facing) {
            case EAST -> 90;
            case SOUTH -> 180;
            case WEST -> 270;
            default -> 0;
        };
    }

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(BaobabTree.MOD_ID, path);
    }

    private static Identifier modModel(String path) {
        return Identifier.fromNamespaceAndPath(BaobabTree.MOD_ID, "block/" + path);
    }

    private static String modelId(String path) {
        return BaobabTree.MOD_ID + ":" + path;
    }

    private static ItemTintSource foliageTint() {
        return ItemModelUtils.constantTint(DEFAULT_FOLIAGE_TINT);
    }

    private static ItemTintSource acaciaFoliageTint() {
        return ItemModelUtils.constantTint(DEFAULT_ACACIA_FOLIAGE_TINT);
    }
}
