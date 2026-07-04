package de.artemis.baobabtree.common.registry;

import de.artemis.baobabtree.BaobabTree;
import de.artemis.baobabtree.common.block.entity.BaobabLitterBlockEntity;
import de.artemis.baobabtree.common.block.entity.BaobabHangingSignBlockEntity;
import de.artemis.baobabtree.common.block.entity.BaobabSignBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, BaobabTree.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BaobabSignBlockEntity>> BAOBAB_SIGN =
            BLOCK_ENTITIES.register("baobab_sign",
                    () -> new BlockEntityType<>(
                            BaobabSignBlockEntity::new,
                            ModBlocks.BAOBAB_SIGN.get(),
                            ModBlocks.BAOBAB_WALL_SIGN.get()
                    ));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BaobabHangingSignBlockEntity>> BAOBAB_HANGING_SIGN =
            BLOCK_ENTITIES.register("baobab_hanging_sign",
                    () -> new BlockEntityType<>(
                            BaobabHangingSignBlockEntity::new,
                            ModBlocks.BAOBAB_HANGING_SIGN.get(),
                            ModBlocks.BAOBAB_WALL_HANGING_SIGN.get()
                    ));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BaobabLitterBlockEntity>> BAOBAB_LITTER =
            BLOCK_ENTITIES.register("baobab_litter",
                    () -> new BlockEntityType<>(
                            BaobabLitterBlockEntity::new,
                            ModBlocks.BAOBAB_LITTER.get()
                    ));

    private ModBlockEntities() {
    }

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
