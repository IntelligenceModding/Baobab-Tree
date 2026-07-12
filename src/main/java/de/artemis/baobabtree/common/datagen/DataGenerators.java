package de.artemis.baobabtree.common.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class DataGenerators {
    private DataGenerators() {
    }

    public static void gatherClientData(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();

        event.addProvider(new ModModelProvider(packOutput));
        event.addProvider(new ModSpecialModelProvider(packOutput));
        event.addProvider(new ModLanguageProvider(packOutput, "en_us"));
    }

    public static void gatherServerData(GatherDataEvent.Server event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        event.addProvider(new LootTableProvider(
                packOutput,
                Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(ModBlockLootTableProvider::new, LootContextParamSets.BLOCK)),
                lookupProvider
        ));
        event.addProvider(new ModDatapackProvider(packOutput, lookupProvider));
        event.addProvider(new ModRecipeProvider(packOutput, lookupProvider));

        ModBlockTagProvider blockTagsProvider = event.addProvider(new ModBlockTagProvider(packOutput, lookupProvider));
        event.addProvider(new ModItemTagProvider(packOutput, lookupProvider, blockTagsProvider.contentsGetter()));
        event.addProvider(new ModDataMapProvider(packOutput, lookupProvider));
    }
}
