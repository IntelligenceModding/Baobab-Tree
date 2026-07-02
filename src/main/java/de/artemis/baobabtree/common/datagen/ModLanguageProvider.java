package de.artemis.baobabtree.common.datagen;

import de.artemis.baobabtree.BaobabTree;
import de.artemis.baobabtree.common.registry.ModBlocks;
import de.artemis.baobabtree.common.registry.ModItems;
import net.minecraft.data.PackOutput;

public class ModLanguageProvider extends net.neoforged.neoforge.common.data.LanguageProvider {
    public ModLanguageProvider(PackOutput output, String locale) {
        super(output, BaobabTree.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.baobabtree", "Baobab Tree");
        add(ModBlocks.BAOBAB_LOG.get(), "Baobab Log");
        add(ModBlocks.BAOBAB_WOOD.get(), "Baobab Wood");
        add(ModBlocks.STRIPPED_BAOBAB_LOG.get(), "Stripped Baobab Log");
        add(ModBlocks.STRIPPED_BAOBAB_WOOD.get(), "Stripped Baobab Wood");
        add(ModBlocks.BAOBAB_PLANKS.get(), "Baobab Planks");
        add(ModBlocks.BAOBAB_STAIRS.get(), "Baobab Stairs");
        add(ModBlocks.BAOBAB_SLAB.get(), "Baobab Slab");
        add(ModBlocks.BAOBAB_FENCE.get(), "Baobab Fence");
        add(ModBlocks.BAOBAB_FENCE_GATE.get(), "Baobab Fence Gate");
        add(ModBlocks.BAOBAB_DOOR.get(), "Baobab Door");
        add(ModBlocks.BAOBAB_TRAPDOOR.get(), "Baobab Trapdoor");
        add(ModBlocks.BAOBAB_BUTTON.get(), "Baobab Button");
        add(ModBlocks.BAOBAB_PRESSURE_PLATE.get(), "Baobab Pressure Plate");
        add(ModBlocks.BAOBAB_SIGN.get(), "Baobab Sign");
        add(ModBlocks.BAOBAB_HANGING_SIGN.get(), "Baobab Hanging Sign");
        add(ModBlocks.BAOBAB_LEAVES.get(), "Baobab Leaves");
        add(ModBlocks.BAOBAB_SAPLING.get(), "Baobab Sapling");
        add(ModBlocks.POTTED_BAOBAB_SAPLING.get(), "Potted Baobab Sapling");
        add(ModBlocks.BAOBAB_LITTER.get(), "Baobab Litter");
        add(ModBlocks.TREE_ROOT.get(), "Tree Root");
        add(ModBlocks.BAOBAB_FRUIT_POD.get(), "Baobab Fruit Pod");
        add(ModBlocks.SMALL_BAOBAB_FRUIT_POD.get(), "Small Baobab Fruit Pod");
        add(ModBlocks.MEDIUM_BAOBAB_FRUIT_POD.get(), "Medium Baobab Fruit Pod");
        add(ModBlocks.LARGE_BAOBAB_FRUIT_POD.get(), "Large Baobab Fruit Pod");
        add(ModItems.BAOBAB_FRUIT_PIECE.get(), "Baobab Fruit Piece");
        add(ModItems.DRIED_BAOBAB_PULP.get(), "Dried Baobab Pulp");
        add(ModItems.BAOBAB_SEEDS.get(), "Baobab Seeds");
        add(ModItems.BAOBAB_BOAT.get(), "Baobab Boat");
        add(ModItems.BAOBAB_CHEST_BOAT.get(), "Baobab Chest Boat");
    }
}
