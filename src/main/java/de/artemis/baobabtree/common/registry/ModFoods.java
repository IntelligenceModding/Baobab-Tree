package de.artemis.baobabtree.common.registry;

import net.minecraft.world.food.FoodProperties;

public final class ModFoods {
    public static final FoodProperties BAOBAB_FRUIT = new FoodProperties.Builder()
            .nutrition(4)
            .saturationModifier(0.4F)
            .build();

    private ModFoods() {
    }
}
