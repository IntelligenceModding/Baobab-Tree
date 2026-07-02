package de.artemis.baobabtree.common.datagen;

import de.artemis.baobabtree.common.block.BaobabFruitPodBlock;
import de.artemis.baobabtree.common.registry.ModBlocks;
import de.artemis.baobabtree.common.registry.ModItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    protected ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.BAOBAB_LOG.get());
        dropSelf(ModBlocks.BAOBAB_WOOD.get());
        dropSelf(ModBlocks.STRIPPED_BAOBAB_LOG.get());
        dropSelf(ModBlocks.STRIPPED_BAOBAB_WOOD.get());
        dropSelf(ModBlocks.BAOBAB_PLANKS.get());
        dropSelf(ModBlocks.BAOBAB_STAIRS.get());
        add(ModBlocks.BAOBAB_SLAB.get(), createSlabItemTable(ModBlocks.BAOBAB_SLAB.get()));
        dropSelf(ModBlocks.BAOBAB_FENCE.get());
        dropSelf(ModBlocks.BAOBAB_FENCE_GATE.get());
        dropSelf(ModBlocks.BAOBAB_BUTTON.get());
        dropSelf(ModBlocks.BAOBAB_PRESSURE_PLATE.get());
        add(ModBlocks.BAOBAB_DOOR.get(), createDoorTable(ModBlocks.BAOBAB_DOOR.get()));
        dropSelf(ModBlocks.BAOBAB_TRAPDOOR.get());

        add(ModBlocks.BAOBAB_SIGN.get(), createSingleItemTable(ModItems.BAOBAB_SIGN.get()));
        add(ModBlocks.BAOBAB_WALL_SIGN.get(), createSingleItemTable(ModItems.BAOBAB_SIGN.get()));
        add(ModBlocks.BAOBAB_HANGING_SIGN.get(), createSingleItemTable(ModItems.BAOBAB_HANGING_SIGN.get()));
        add(ModBlocks.BAOBAB_WALL_HANGING_SIGN.get(), createSingleItemTable(ModItems.BAOBAB_HANGING_SIGN.get()));

        add(ModBlocks.BAOBAB_LEAVES.get(), createLeavesDrops(ModBlocks.BAOBAB_LEAVES.get(), ModBlocks.BAOBAB_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
        dropSelf(ModBlocks.BAOBAB_SAPLING.get());
        dropPottedContents(ModBlocks.POTTED_BAOBAB_SAPLING.get());
        dropSelf(ModBlocks.BAOBAB_LEAF_LITTER.get());
        dropSelf(ModBlocks.TREE_ROOT.get());
        add(ModBlocks.BAOBAB_FRUIT_POD.get(), createFruitPodDrops());
    }

    private LootTable.Builder createFruitPodDrops() {
        return LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.BAOBAB_FRUIT_POD.get())
                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BaobabFruitPodBlock.AGE, 4)))
                        .add(applyExplosionCondition(ModBlocks.BAOBAB_FRUIT_POD.get(),
                                LootItem.lootTableItem(ModItems.BAOBAB_FRUIT.get())
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
                                        .apply(ApplyExplosionDecay.explosionDecay()))))
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.BAOBAB_FRUIT_POD.get())
                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BaobabFruitPodBlock.AGE, 4)))
                        .add(applyExplosionCondition(ModBlocks.BAOBAB_FRUIT_POD.get(),
                                LootItem.lootTableItem(ModItems.BAOBAB_SEEDS.get())
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F)))
                                        .apply(ApplyExplosionDecay.explosionDecay()))))
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.BAOBAB_FRUIT_POD.get())
                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BaobabFruitPodBlock.AGE, 0)))
                        .add(applyExplosionCondition(ModBlocks.BAOBAB_FRUIT_POD.get(), LootItem.lootTableItem(ModBlocks.BAOBAB_FRUIT_POD.get()))))
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.BAOBAB_FRUIT_POD.get())
                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BaobabFruitPodBlock.AGE, 1)))
                        .add(applyExplosionCondition(ModBlocks.BAOBAB_FRUIT_POD.get(), LootItem.lootTableItem(ModBlocks.BAOBAB_FRUIT_POD.get()))))
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.BAOBAB_FRUIT_POD.get())
                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BaobabFruitPodBlock.AGE, 2)))
                        .add(applyExplosionCondition(ModBlocks.BAOBAB_FRUIT_POD.get(), LootItem.lootTableItem(ModBlocks.BAOBAB_FRUIT_POD.get()))))
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.BAOBAB_FRUIT_POD.get())
                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BaobabFruitPodBlock.AGE, 3)))
                        .add(applyExplosionCondition(ModBlocks.BAOBAB_FRUIT_POD.get(), LootItem.lootTableItem(ModBlocks.BAOBAB_FRUIT_POD.get()))));
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(block -> (Block) block.get()).toList();
    }
}
