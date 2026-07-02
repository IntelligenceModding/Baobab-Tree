package de.artemis.baobabtree.common.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class BaobabTreeFeature extends Feature<NoneFeatureConfiguration> {
    private final BaobabTreeGenerator.Variant variant;

    public BaobabTreeFeature(Codec<NoneFeatureConfiguration> codec, BaobabTreeGenerator.Variant variant) {
        super(codec);
        this.variant = variant;
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        return BaobabTreeGenerator.generate(context.level(), context.origin(), context.random(), variant);
    }
}
