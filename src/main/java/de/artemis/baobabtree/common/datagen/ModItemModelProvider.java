package de.artemis.baobabtree.common.datagen;

import de.artemis.baobabtree.BaobabTree;
import de.artemis.baobabtree.common.registry.ModBlocks;
import de.artemis.baobabtree.common.registry.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, BaobabTree.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        vanillaTextureItem(ModItems.BAOBAB_SIGN.get(), "acacia_sign");
        vanillaTextureItem(ModItems.BAOBAB_HANGING_SIGN.get(), "acacia_hanging_sign");
        vanillaTextureItem(ModItems.BAOBAB_BOAT.get(), "acacia_boat");
        vanillaTextureItem(ModItems.BAOBAB_CHEST_BOAT.get(), "acacia_chest_boat");
        vanillaTextureItem(ModItems.BAOBAB_FRUIT_PIECE.get(), "melon_slice");
        vanillaTextureItem(ModItems.DRIED_BAOBAB_PULP.get(), "dried_kelp");
        vanillaTextureItem(ModItems.BAOBAB_SEEDS.get(), "pumpkin_seeds");
        vanillaTextureBlockItem(ModBlocks.BAOBAB_SAPLING.get().asItem(), "acacia_sapling");

        modTextureItem(ModBlocks.BAOBAB_LITTER.get().asItem(), "baobab_litter");
    }

    private void vanillaTextureItem(Item item, String textureName) {
        String name = item.builtInRegistryHolder().key().location().getPath();
        withExistingParent(name, mcLoc("item/generated"))
                .texture("layer0", mcLoc("item/" + textureName));
    }

    private void vanillaTextureBlockItem(Item item, String textureName) {
        vanillaTextureBlockItemWithParent(item, textureName, mcLoc("item/generated"));
    }

    private void vanillaTextureBlockItemWithParent(Item item, String textureName, ResourceLocation parent) {
        String name = item.builtInRegistryHolder().key().location().getPath();
        withExistingParent(name, parent)
                .texture("layer0", mcLoc("block/" + textureName));
    }

    private void modTextureItem(Item item, String textureName) {
        String name = item.builtInRegistryHolder().key().location().getPath();
        withExistingParent(name, mcLoc("item/generated"))
                .texture("layer0", modLoc("item/" + textureName));
    }
}
