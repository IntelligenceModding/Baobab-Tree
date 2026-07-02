package de.artemis.baobabtree.common.registry;

import net.minecraft.world.food.FoodProperties;

public final class ModFoods {
    public static final FoodProperties BAOBAB_FRUIT_PIECE = new FoodProperties.Builder()
            .nutrition(4)
            .saturationModifier(0.4F)
            .build();

    public static final FoodProperties DRIED_BAOBAB_PULP = new FoodProperties.Builder()
            .nutrition(5)
            .saturationModifier(0.6F)
            .build();

    private ModFoods() {
    }
}
