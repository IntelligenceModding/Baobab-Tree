package de.artemis.baobabtree.common.datagen;

import de.artemis.baobabtree.common.registry.ModBlocks;
import de.artemis.baobabtree.common.registry.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModDataMapProvider extends DataMapProvider {
    public ModDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.@NotNull Provider provider) {
        builder(NeoForgeDataMaps.COMPOSTABLES)
                .add(ModBlocks.BAOBAB_LEAVES.getId(), new Compostable(0.3F), false)
                .add(ModBlocks.BAOBAB_SAPLING.getId(), new Compostable(0.3F), false)
                .add(ModBlocks.BAOBAB_LEAF_LITTER.getId(), new Compostable(0.3F), false)
                .add(ModBlocks.BAOBAB_FRUIT_POD.getId(), new Compostable(0.5F), false)
                .add(ModItems.BAOBAB_FRUIT.getId(), new Compostable(0.65F), false)
                .add(ModItems.DRIED_BAOBAB_PULP.getId(), new Compostable(0.85F), false)
                .add(ModItems.BAOBAB_SEEDS.getId(), new Compostable(0.3F), false);

        builder(NeoForgeDataMaps.FURNACE_FUELS)
                .add(ModItems.BAOBAB_BOAT.getId(), new FurnaceFuel(1200), false)
                .add(ModItems.BAOBAB_CHEST_BOAT.getId(), new FurnaceFuel(1200), false)
                .add(ModItems.BAOBAB_SIGN.getId(), new FurnaceFuel(200), false)
                .add(ModItems.BAOBAB_HANGING_SIGN.getId(), new FurnaceFuel(800), false)
                .add(ModBlocks.BAOBAB_LOG.getId(), new FurnaceFuel(300), false)
                .add(ModBlocks.BAOBAB_WOOD.getId(), new FurnaceFuel(300), false)
                .add(ModBlocks.STRIPPED_BAOBAB_LOG.getId(), new FurnaceFuel(300), false)
                .add(ModBlocks.STRIPPED_BAOBAB_WOOD.getId(), new FurnaceFuel(300), false)
                .add(ModBlocks.BAOBAB_PLANKS.getId(), new FurnaceFuel(300), false)
                .add(ModBlocks.BAOBAB_STAIRS.getId(), new FurnaceFuel(300), false)
                .add(ModBlocks.BAOBAB_SLAB.getId(), new FurnaceFuel(150), false)
                .add(ModBlocks.BAOBAB_FENCE.getId(), new FurnaceFuel(300), false)
                .add(ModBlocks.BAOBAB_FENCE_GATE.getId(), new FurnaceFuel(300), false)
                .add(ModBlocks.BAOBAB_DOOR.getId(), new FurnaceFuel(200), false)
                .add(ModBlocks.BAOBAB_TRAPDOOR.getId(), new FurnaceFuel(300), false)
                .add(ModBlocks.BAOBAB_BUTTON.getId(), new FurnaceFuel(100), false)
                .add(ModBlocks.BAOBAB_PRESSURE_PLATE.getId(), new FurnaceFuel(300), false)
                .add(ModBlocks.BAOBAB_LEAF_LITTER.getId(), new FurnaceFuel(100), false)
                .add(ModBlocks.TREE_ROOT.getId(), new FurnaceFuel(300), false);
    }
}
