package de.artemis.baobabtree.common.registry;

import de.artemis.baobabtree.BaobabTree;
import de.artemis.baobabtree.common.item.BaobabBoatItem;
import de.artemis.baobabtree.common.item.BaobabSeedsItem;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(BaobabTree.MOD_ID);

    public static final DeferredItem<Item> BAOBAB_SIGN = ITEMS.register("baobab_sign",
            () -> new SignItem(new Item.Properties().stacksTo(16), ModBlocks.BAOBAB_SIGN.get(), ModBlocks.BAOBAB_WALL_SIGN.get()));

    public static final DeferredItem<Item> BAOBAB_HANGING_SIGN = ITEMS.register("baobab_hanging_sign",
            () -> new HangingSignItem(ModBlocks.BAOBAB_HANGING_SIGN.get(), ModBlocks.BAOBAB_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));

    public static final DeferredItem<Item> BAOBAB_BOAT = ITEMS.register("baobab_boat",
            () -> new BaobabBoatItem(false, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> BAOBAB_CHEST_BOAT = ITEMS.register("baobab_chest_boat",
            () -> new BaobabBoatItem(true, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> BAOBAB_FRUIT_PIECE = ITEMS.register("baobab_fruit_piece",
            () -> new Item(new Item.Properties().food(ModFoods.BAOBAB_FRUIT_PIECE)));

    public static final DeferredItem<Item> DRIED_BAOBAB_PULP = ITEMS.register("dried_baobab_pulp",
            () -> new Item(new Item.Properties().food(ModFoods.DRIED_BAOBAB_PULP)));

    public static final DeferredItem<Item> BAOBAB_SEEDS = ITEMS.register("baobab_seeds",
            () -> new BaobabSeedsItem(new Item.Properties()));

    private ModItems() {
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
