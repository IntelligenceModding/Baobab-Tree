package de.artemis.baobabtree.common.registry;

import de.artemis.baobabtree.BaobabTree;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public final class ModTreeConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> YOUNG_BAOBAB_KEY = registerKey("young_baobab");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MATURE_BAOBAB_KEY = registerKey("mature_baobab");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ANCIENT_BAOBAB_KEY = registerKey("ancient_baobab");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_BAOBAB_KEY = registerKey("fallen_baobab");

    private ModTreeConfiguredFeatures() {
    }

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        register(context, YOUNG_BAOBAB_KEY, ModFeatures.YOUNG_BAOBAB_FEATURE.get(), NoneFeatureConfiguration.NONE);
        register(context, MATURE_BAOBAB_KEY, ModFeatures.MATURE_BAOBAB_FEATURE.get(), NoneFeatureConfiguration.NONE);
        register(context, ANCIENT_BAOBAB_KEY, ModFeatures.ANCIENT_BAOBAB_FEATURE.get(), NoneFeatureConfiguration.NONE);
        register(context, FALLEN_BAOBAB_KEY, ModFeatures.FALLEN_BAOBAB_FEATURE.get(), NoneFeatureConfiguration.NONE);
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(BaobabTree.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(
            BootstrapContext<ConfiguredFeature<?, ?>> context,
            ResourceKey<ConfiguredFeature<?, ?>> key,
            F feature,
            FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
