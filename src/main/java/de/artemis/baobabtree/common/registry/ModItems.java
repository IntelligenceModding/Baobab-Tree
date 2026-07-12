package de.artemis.baobabtree.common.registry;

import de.artemis.baobabtree.BaobabTree;
import de.artemis.baobabtree.common.item.BaobabBoatItem;
import de.artemis.baobabtree.common.item.BaobabFruitItem;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(BaobabTree.MOD_ID);

    public static final DeferredItem<Item> BAOBAB_SIGN = ITEMS.registerItem("baobab_sign",
            properties -> new SignItem(ModBlocks.BAOBAB_SIGN.get(), ModBlocks.BAOBAB_WALL_SIGN.get(), properties),
            new Item.Properties().stacksTo(16));

    public static final DeferredItem<Item> BAOBAB_HANGING_SIGN = ITEMS.registerItem("baobab_hanging_sign",
            properties -> new HangingSignItem(ModBlocks.BAOBAB_HANGING_SIGN.get(), ModBlocks.BAOBAB_WALL_HANGING_SIGN.get(), properties),
            new Item.Properties().stacksTo(16));

    public static final DeferredItem<Item> BAOBAB_BOAT = ITEMS.registerItem("baobab_boat",
            properties -> new BaobabBoatItem(false, properties),
            new Item.Properties().stacksTo(1));

    public static final DeferredItem<Item> BAOBAB_CHEST_BOAT = ITEMS.registerItem("baobab_chest_boat",
            properties -> new BaobabBoatItem(true, properties),
            new Item.Properties().stacksTo(1));

    public static final DeferredItem<Item> BAOBAB_FRUIT = ITEMS.registerItem("baobab_fruit",
            properties -> new BaobabFruitItem(properties.food(ModFoods.BAOBAB_FRUIT)));

    private ModItems() {
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
