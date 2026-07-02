package de.artemis.baobabtree.common.registry;

import net.minecraft.world.level.block.grower.TreeGrower;

import java.util.Optional;

public final class ModTreeGrowers {
    public static final TreeGrower BAOBAB = new TreeGrower(
            "baobabtree:baobab",
            Optional.empty(),
            Optional.of(ModTreeConfiguredFeatures.YOUNG_BAOBAB_KEY),
            Optional.of(ModTreeConfiguredFeatures.YOUNG_BAOBAB_KEY)
    );

    private ModTreeGrowers() {
    }
}
