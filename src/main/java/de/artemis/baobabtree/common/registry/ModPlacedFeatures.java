package de.artemis.baobabtree.common.registry;

import de.artemis.baobabtree.BaobabTree;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.HeightmapPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

import java.util.List;

public final class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> YOUNG_BAOBAB_PLACED_KEY = registerKey("young_baobab_placed");
    public static final ResourceKey<PlacedFeature> MATURE_BAOBAB_PLACED_KEY = registerKey("mature_baobab_placed");
    public static final ResourceKey<PlacedFeature> ANCIENT_BAOBAB_PLACED_KEY = registerKey("ancient_baobab_placed");
    public static final ResourceKey<PlacedFeature> FALLEN_BAOBAB_PLACED_KEY = registerKey("fallen_baobab_placed");
    public static final ResourceKey<PlacedFeature> WINDSWEPT_YOUNG_BAOBAB_PLACED_KEY = registerKey("windswept_young_baobab_placed");
    public static final ResourceKey<PlacedFeature> WINDSWEPT_MATURE_BAOBAB_PLACED_KEY = registerKey("windswept_mature_baobab_placed");
    public static final ResourceKey<PlacedFeature> WINDSWEPT_ANCIENT_BAOBAB_PLACED_KEY = registerKey("windswept_ancient_baobab_placed");
    public static final ResourceKey<PlacedFeature> WINDSWEPT_FALLEN_BAOBAB_PLACED_KEY = registerKey("windswept_fallen_baobab_placed");

    private ModPlacedFeatures() {
    }

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, YOUNG_BAOBAB_PLACED_KEY, configuredFeatures.getOrThrow(ModTreeConfiguredFeatures.YOUNG_BAOBAB_KEY),
                List.of(
                        RarityFilter.onAverageOnceEvery(30),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG),
                        BiomeFilter.biome()
                ));

        register(context, MATURE_BAOBAB_PLACED_KEY, configuredFeatures.getOrThrow(ModTreeConfiguredFeatures.MATURE_BAOBAB_KEY),
                List.of(
                        RarityFilter.onAverageOnceEvery(90),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG),
                        BiomeFilter.biome()
                ));

        register(context, ANCIENT_BAOBAB_PLACED_KEY, configuredFeatures.getOrThrow(ModTreeConfiguredFeatures.ANCIENT_BAOBAB_KEY),
                List.of(
                        RarityFilter.onAverageOnceEvery(250),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG),
                        BiomeFilter.biome()
                ));

        register(context, FALLEN_BAOBAB_PLACED_KEY, configuredFeatures.getOrThrow(ModTreeConfiguredFeatures.FALLEN_BAOBAB_KEY),
                List.of(
                        RarityFilter.onAverageOnceEvery(220),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG),
                        BiomeFilter.biome()
                ));

        register(context, WINDSWEPT_YOUNG_BAOBAB_PLACED_KEY, configuredFeatures.getOrThrow(ModTreeConfiguredFeatures.YOUNG_BAOBAB_KEY),
                List.of(
                        RarityFilter.onAverageOnceEvery(60),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG),
                        BiomeFilter.biome()
                ));

        register(context, WINDSWEPT_MATURE_BAOBAB_PLACED_KEY, configuredFeatures.getOrThrow(ModTreeConfiguredFeatures.MATURE_BAOBAB_KEY),
                List.of(
                        RarityFilter.onAverageOnceEvery(180),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG),
                        BiomeFilter.biome()
                ));

        register(context, WINDSWEPT_ANCIENT_BAOBAB_PLACED_KEY, configuredFeatures.getOrThrow(ModTreeConfiguredFeatures.ANCIENT_BAOBAB_KEY),
                List.of(
                        RarityFilter.onAverageOnceEvery(400),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG),
                        BiomeFilter.biome()
                ));

        register(context, WINDSWEPT_FALLEN_BAOBAB_PLACED_KEY, configuredFeatures.getOrThrow(ModTreeConfiguredFeatures.FALLEN_BAOBAB_KEY),
                List.of(
                        RarityFilter.onAverageOnceEvery(350),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG),
                        BiomeFilter.biome()
                ));
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(BaobabTree.MOD_ID, name));
    }

    private static void register(BootstrapContext<PlacedFeature> context,
                                 ResourceKey<PlacedFeature> key,
                                 Holder<net.minecraft.world.level.levelgen.feature.ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
