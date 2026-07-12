package de.artemis.baobabtree.common.datagen;

import de.artemis.baobabtree.BaobabTree;
import de.artemis.baobabtree.common.registry.ModBlocks;
import de.artemis.baobabtree.common.registry.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.core.Holder;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.Block;

import java.util.stream.Stream;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, BaobabTree.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        blockModels.woodProvider(ModBlocks.BAOBAB_LOG.get())
                .logWithHorizontal(ModBlocks.BAOBAB_LOG.get())
                .wood(ModBlocks.BAOBAB_WOOD.get());
        blockModels.woodProvider(ModBlocks.STRIPPED_BAOBAB_LOG.get())
                .logWithHorizontal(ModBlocks.STRIPPED_BAOBAB_LOG.get())
                .wood(ModBlocks.STRIPPED_BAOBAB_WOOD.get());

        blockModels.createTrivialCube(ModBlocks.BAOBAB_PLANKS.get());
        blockModels.familyWithExistingFullBlock(ModBlocks.BAOBAB_PLANKS.get()).generateFor(baobabPlankFamily());
        blockModels.createHangingSign(
                ModBlocks.BAOBAB_PLANKS.get(),
                ModBlocks.BAOBAB_HANGING_SIGN.get(),
                ModBlocks.BAOBAB_WALL_HANGING_SIGN.get()
        );

        blockModels.createTintedLeaves(ModBlocks.BAOBAB_LEAVES.get(), TexturedModel.LEAVES, FoliageColor.FOLIAGE_DEFAULT);
        blockModels.createPlantWithDefaultItem(
                ModBlocks.BAOBAB_SAPLING.get(),
                ModBlocks.POTTED_BAOBAB_SAPLING.get(),
                BlockModelGenerators.PlantType.NOT_TINTED
        );

        itemModels.generateFlatItem(ModItems.BAOBAB_BOAT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.BAOBAB_CHEST_BOAT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.BAOBAB_FRUIT.get(), ModelTemplates.FLAT_ITEM);
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return super.getKnownBlocks().filter(holder -> !isSpecialBlock(holder.value()));
    }

    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return super.getKnownItems().filter(holder -> !isSpecialItem(holder.value()));
    }

    private static BlockFamily baobabPlankFamily() {
        return new BlockFamily.Builder(ModBlocks.BAOBAB_PLANKS.get())
                .button(ModBlocks.BAOBAB_BUTTON.get())
                .fence(ModBlocks.BAOBAB_FENCE.get())
                .fenceGate(ModBlocks.BAOBAB_FENCE_GATE.get())
                .pressurePlate(ModBlocks.BAOBAB_PRESSURE_PLATE.get())
                .sign(ModBlocks.BAOBAB_SIGN.get(), ModBlocks.BAOBAB_WALL_SIGN.get())
                .slab(ModBlocks.BAOBAB_SLAB.get())
                .stairs(ModBlocks.BAOBAB_STAIRS.get())
                .door(ModBlocks.BAOBAB_DOOR.get())
                .trapdoor(ModBlocks.BAOBAB_TRAPDOOR.get())
                .getFamily();
    }

    private static boolean isSpecialBlock(Block block) {
        return block == ModBlocks.BAOBAB_LITTER.get()
                || block == ModBlocks.TREE_ROOT.get()
                || block == ModBlocks.BAOBAB_FRUIT_POD.get()
                || block == ModBlocks.SMALL_BAOBAB_FRUIT_POD.get()
                || block == ModBlocks.MEDIUM_BAOBAB_FRUIT_POD.get()
                || block == ModBlocks.LARGE_BAOBAB_FRUIT_POD.get();
    }

    private static boolean isSpecialItem(Item item) {
        return item == ModBlocks.BAOBAB_LEAVES.get().asItem()
                || item == ModBlocks.BAOBAB_LITTER.get().asItem()
                || item == ModBlocks.BAOBAB_FRUIT_POD.get().asItem();
    }
}
