package de.artemis.baobabtree.common.registry;

import de.artemis.baobabtree.BaobabTree;
import de.artemis.baobabtree.common.entity.BaobabBoatEntity;
import de.artemis.baobabtree.common.entity.BaobabChestBoatEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, BaobabTree.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<BaobabBoatEntity>> BAOBAB_BOAT =
            ENTITY_TYPES.register("baobab_boat",
                    () -> EntityType.Builder.<BaobabBoatEntity>of(BaobabBoatEntity::new, MobCategory.MISC)
                            .sized(1.375F, 0.5625F)
                            .clientTrackingRange(10)
                            .updateInterval(1)
                            .build("baobab_boat"));

    public static final DeferredHolder<EntityType<?>, EntityType<BaobabChestBoatEntity>> BAOBAB_CHEST_BOAT =
            ENTITY_TYPES.register("baobab_chest_boat",
                    () -> EntityType.Builder.<BaobabChestBoatEntity>of(BaobabChestBoatEntity::new, MobCategory.MISC)
                            .sized(1.375F, 0.5625F)
                            .clientTrackingRange(10)
                            .updateInterval(1)
                            .build("baobab_chest_boat"));

    private ModEntityTypes() {
    }

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
