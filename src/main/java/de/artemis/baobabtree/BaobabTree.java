package de.artemis.baobabtree;

import de.artemis.baobabtree.common.datagen.DataGenerators;
import de.artemis.baobabtree.common.registry.ModBlockEntities;
import de.artemis.baobabtree.common.registry.ModBlocks;
import de.artemis.baobabtree.common.registry.ModCreativeModeTabs;
import de.artemis.baobabtree.common.registry.ModItems;
import de.artemis.baobabtree.common.registry.ModMenuTypes;
import de.artemis.baobabtree.common.registry.ModRecipeSerializers;
import de.artemis.baobabtree.common.registry.ModRecipeTypes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(BaobabTree.MOD_ID)
public class BaobabTree {
    public static final String MOD_ID = "baobabtree";

    public BaobabTree(IEventBus modEventBus) {
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipeTypes.register(modEventBus);
        ModRecipeSerializers.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        modEventBus.addListener(DataGenerators::gatherData);
    }
}
