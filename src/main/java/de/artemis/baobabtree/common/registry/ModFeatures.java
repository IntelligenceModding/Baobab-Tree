package de.artemis.baobabtree.common.registry;

import de.artemis.baobabtree.BaobabTree;
import de.artemis.baobabtree.common.worldgen.feature.BaobabTreeFeature;
import de.artemis.baobabtree.common.worldgen.feature.BaobabTreeGenerator;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(BuiltInRegistries.FEATURE, BaobabTree.MOD_ID);

    public static final Supplier<Feature<NoneFeatureConfiguration>> YOUNG_BAOBAB_FEATURE =
            FEATURES.register("young_baobab_feature", () -> new BaobabTreeFeature(NoneFeatureConfiguration.CODEC, BaobabTreeGenerator.Variant.YOUNG));
    public static final Supplier<Feature<NoneFeatureConfiguration>> MATURE_BAOBAB_FEATURE =
            FEATURES.register("mature_baobab_feature", () -> new BaobabTreeFeature(NoneFeatureConfiguration.CODEC, BaobabTreeGenerator.Variant.MATURE));
    public static final Supplier<Feature<NoneFeatureConfiguration>> ANCIENT_BAOBAB_FEATURE =
            FEATURES.register("ancient_baobab_feature", () -> new BaobabTreeFeature(NoneFeatureConfiguration.CODEC, BaobabTreeGenerator.Variant.ANCIENT));
    public static final Supplier<Feature<NoneFeatureConfiguration>> FALLEN_BAOBAB_FEATURE =
            FEATURES.register("fallen_baobab_feature", () -> new BaobabTreeFeature(NoneFeatureConfiguration.CODEC, BaobabTreeGenerator.Variant.FALLEN));

    private ModFeatures() {
    }

    public static void register(IEventBus eventBus) {
        FEATURES.register(eventBus);
    }
}
