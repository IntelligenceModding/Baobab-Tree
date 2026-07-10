package de.artemis.baobabtree;

import de.artemis.baobabtree.common.command.BaobabTreeCommands;
import de.artemis.baobabtree.common.datagen.DataGenerators;
import de.artemis.baobabtree.common.event.TreeRootClimbHandler;
import de.artemis.baobabtree.common.registry.ModBlockEntities;
import de.artemis.baobabtree.common.registry.ModBlocks;
import de.artemis.baobabtree.common.registry.ModCreativeModeTabs;
import de.artemis.baobabtree.common.registry.ModEntityTypes;
import de.artemis.baobabtree.common.registry.ModFeatures;
import de.artemis.baobabtree.common.registry.ModItems;
import de.artemis.baobabtree.common.registry.ModParticles;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;

@Mod(BaobabTree.MOD_ID)
public class BaobabTree {
    public static final String MOD_ID = "baobabtree";

    public BaobabTree(IEventBus modEventBus) {
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModEntityTypes.register(modEventBus);
        ModFeatures.register(modEventBus);
        ModParticles.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        modEventBus.addListener(DataGenerators::gatherClientData);
        modEventBus.addListener(DataGenerators::gatherServerData);
        modEventBus.addListener(BaobabTree::onBlockEntityTypeAddBlocks);
        NeoForge.EVENT_BUS.addListener(BaobabTreeCommands::register);
        NeoForge.EVENT_BUS.addListener(TreeRootClimbHandler::onPlayerTick);
    }

    private static void onBlockEntityTypeAddBlocks(BlockEntityTypeAddBlocksEvent event) {
        event.modify(BlockEntityTypes.SHELF, ModBlocks.BAOBAB_SHELF.get());
    }
}
