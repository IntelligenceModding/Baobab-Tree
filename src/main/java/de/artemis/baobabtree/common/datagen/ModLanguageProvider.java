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
        add(ModBlocks.BAOBAB_SHELF.get(), "Baobab Shelf");
        add(ModBlocks.BAOBAB_BUTTON.get(), "Baobab Button");
        add(ModBlocks.BAOBAB_PRESSURE_PLATE.get(), "Baobab Pressure Plate");
        add(ModBlocks.BAOBAB_SIGN.get(), "Baobab Sign");
        add(ModBlocks.BAOBAB_WALL_SIGN.get(), "Baobab Wall Sign");
        add(ModBlocks.BAOBAB_HANGING_SIGN.get(), "Baobab Hanging Sign");
        add(ModBlocks.BAOBAB_WALL_HANGING_SIGN.get(), "Baobab Wall Hanging Sign");
        add(ModBlocks.BAOBAB_LEAVES.get(), "Baobab Leaves");
        add(ModBlocks.BAOBAB_SAPLING.get(), "Baobab Sapling");
        add(ModBlocks.POTTED_BAOBAB_SAPLING.get(), "Potted Baobab Sapling");
        add(ModBlocks.BAOBAB_LITTER.get(), "Baobab Litter");
        add(ModBlocks.TREE_ROOT.get(), "Tree Root");
        add(ModBlocks.BAOBAB_FRUIT_POD.get(), "Baobab Fruit Pod");
        add(ModBlocks.SMALL_BAOBAB_FRUIT_POD.get(), "Small Baobab Fruit Pod");
        add(ModBlocks.MEDIUM_BAOBAB_FRUIT_POD.get(), "Medium Baobab Fruit Pod");
        add(ModBlocks.LARGE_BAOBAB_FRUIT_POD.get(), "Large Baobab Fruit Pod");
        add(ModItems.BAOBAB_SIGN.get(), "Baobab Sign");
        add(ModItems.BAOBAB_HANGING_SIGN.get(), "Baobab Hanging Sign");
        add(ModItems.BAOBAB_FRUIT.get(), "Baobab Fruit");
        add(ModItems.BAOBAB_BOAT.get(), "Baobab Boat");
        add(ModItems.BAOBAB_CHEST_BOAT.get(), "Baobab Boat with Chest");
        add("entity.baobabtree.baobab_boat", "Baobab Boat");
        add("entity.baobabtree.baobab_chest_boat", "Baobab Boat with Chest");
        add("advancements.baobabtree.root.title", "Under the Baobab");
        add("advancements.baobabtree.root.description", "Find the first signs of a baobab's life.");
        add("advancements.baobabtree.taste_of_the_savanna.title", "Taste of the Savanna");
        add("advancements.baobabtree.taste_of_the_savanna.description", "Eat a baobab fruit.");
        add("advancements.baobabtree.plant_the_future.title", "Plant the Future");
        add("advancements.baobabtree.plant_the_future.description", "Acquire a baobab sapling.");
        add("advancements.baobabtree.baobab_timber.title", "Baobab Timber");
        add("advancements.baobabtree.baobab_timber.description", "Bring down wood from a grown baobab.");
        add("advancements.baobabtree.savanna_joinery.title", "Savanna Joinery");
        add("advancements.baobabtree.savanna_joinery.description", "Work baobab timber into planks.");
        add("advancements.baobabtree.settle_under_shade.title", "Settle Under Shade");
        add("advancements.baobabtree.settle_under_shade.description", "Craft a baobab shelf, a sign, and a hanging sign.");
        add("advancements.baobabtree.river_crossing.title", "River Crossing");
        add("advancements.baobabtree.river_crossing.description", "Set out in a baobab boat.");
        add("advancements.baobabtree.packed_crossing.title", "Packed Crossing");
        add("advancements.baobabtree.packed_crossing.description", "Upgrade your baobab boat with a chest.");
        add("advancements.baobabtree.forest_floor_findings.title", "Forest Floor Findings");
        add("advancements.baobabtree.forest_floor_findings.description", "Pick up a pile of baobab litter.");
        add("advancements.baobabtree.root_cause.title", "Root Cause");
        add("advancements.baobabtree.root_cause.description", "Recover one of the tree's roots.");
        add("advancements.baobabtree.fallen_orchard.title", "Fallen Orchard");
        add("advancements.baobabtree.fallen_orchard.description", "Collect a fallen baobab pod.");
        add("advancements.baobabtree.heavy_harvest.title", "Heavy Harvest");
        add("advancements.baobabtree.heavy_harvest.description", "Bring down a large baobab fruit pod.");
        add("advancements.baobabtree.pod_taxonomist.title", "Pod Taxonomist");
        add("advancements.baobabtree.pod_taxonomist.description", "Collect the small, medium, and large fallen pods.");

        add("jei.baobabtree.baobab_sapling", "Needs a 2x2 to grow. A grown sapling cluster only creates the young tree, without the natural ground clutter.");
        add("jei.baobabtree.baobab_fruit", "Can be eaten or planted on the underside of baobab leaves to start a hanging fruit pod.");
        add("jei.baobabtree.baobab_litter", "Rummage it by hand to clear the whole pile at once. Thicker piles have better loot odds. Pickaxes strip it layer by layer instead.");
        add("jei.baobabtree.tree_root", "Acts like a climbable root block. Axe harvests it into loose material, while a shovel turns it into dirt and drops hanging roots.");
        add("jei.baobabtree.baobab_fruit_pod", "Grows under baobab leaves from planted fruit. Right-click a grown pod to harvest the pod itself and leave the base to regrow.");
        add("jei.baobabtree.small_baobab_fruit_pod", "Harvest with an axe or sword for 2 baobab fruit. Small fallen pods deal the least impact damage.");
        add("jei.baobabtree.medium_baobab_fruit_pod", "Harvest with an axe or sword for 4 baobab fruit. Medium fallen pods hit harder than small ones.");
        add("jei.baobabtree.large_baobab_fruit_pod", "Harvest with an axe or sword for 6 baobab fruit. Ripe hanging pods can drop on their own, and large fallen pods hit the hardest.");
    }
}
