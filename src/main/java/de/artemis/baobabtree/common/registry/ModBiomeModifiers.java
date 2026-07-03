package de.artemis.baobabtree.common.registry;

import de.artemis.baobabtree.BaobabTree;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class ModBiomeModifiers {
    public static final ResourceKey<BiomeModifier> ADD_YOUNG_BAOBABS = registerKey("add_young_baobabs");
    public static final ResourceKey<BiomeModifier> ADD_MATURE_BAOBABS = registerKey("add_mature_baobabs");
    public static final ResourceKey<BiomeModifier> ADD_ANCIENT_BAOBABS = registerKey("add_ancient_baobabs");
    public static final ResourceKey<BiomeModifier> ADD_FALLEN_BAOBABS = registerKey("add_fallen_baobabs");
    public static final ResourceKey<BiomeModifier> ADD_WINDSWEPT_YOUNG_BAOBABS = registerKey("add_windswept_young_baobabs");
    public static final ResourceKey<BiomeModifier> ADD_WINDSWEPT_MATURE_BAOBABS = registerKey("add_windswept_mature_baobabs");
    public static final ResourceKey<BiomeModifier> ADD_WINDSWEPT_ANCIENT_BAOBABS = registerKey("add_windswept_ancient_baobabs");
    public static final ResourceKey<BiomeModifier> ADD_WINDSWEPT_FALLEN_BAOBABS = registerKey("add_windswept_fallen_baobabs");

    private ModBiomeModifiers() {
    }

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        context.register(ADD_YOUNG_BAOBABS, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(ModTags.Biomes.HAS_BAOBAB_GENERATION),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.YOUNG_BAOBAB_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));

        context.register(ADD_MATURE_BAOBABS, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(ModTags.Biomes.HAS_BAOBAB_GENERATION),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.MATURE_BAOBAB_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));

        context.register(ADD_ANCIENT_BAOBABS, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(ModTags.Biomes.HAS_BAOBAB_GENERATION),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.ANCIENT_BAOBAB_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));

        context.register(ADD_FALLEN_BAOBABS, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(ModTags.Biomes.HAS_BAOBAB_GENERATION),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.FALLEN_BAOBAB_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));

        HolderSet<Biome> windsweptSavanna = HolderSet.direct(biomes.getOrThrow(Biomes.WINDSWEPT_SAVANNA));

        context.register(ADD_WINDSWEPT_YOUNG_BAOBABS, new BiomeModifiers.AddFeaturesBiomeModifier(
                windsweptSavanna,
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.WINDSWEPT_YOUNG_BAOBAB_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));

        context.register(ADD_WINDSWEPT_MATURE_BAOBABS, new BiomeModifiers.AddFeaturesBiomeModifier(
                windsweptSavanna,
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.WINDSWEPT_MATURE_BAOBAB_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));

        context.register(ADD_WINDSWEPT_ANCIENT_BAOBABS, new BiomeModifiers.AddFeaturesBiomeModifier(
                windsweptSavanna,
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.WINDSWEPT_ANCIENT_BAOBAB_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));

        context.register(ADD_WINDSWEPT_FALLEN_BAOBABS, new BiomeModifiers.AddFeaturesBiomeModifier(
                windsweptSavanna,
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.WINDSWEPT_FALLEN_BAOBAB_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION
        ));
    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(BaobabTree.MOD_ID, name));
    }
}
