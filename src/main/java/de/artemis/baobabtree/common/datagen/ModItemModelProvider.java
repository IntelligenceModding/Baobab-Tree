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
        modTextureItem(ModItems.BAOBAB_SIGN.get(), "baobab_sign");
        modTextureItem(ModItems.BAOBAB_HANGING_SIGN.get(), "baobab_hanging_sign");
        modTextureItem(ModItems.BAOBAB_BOAT.get(), "baobab_boat");
        modTextureItem(ModItems.BAOBAB_CHEST_BOAT.get(), "baobab_chest_boat");
        modTextureItem(ModItems.BAOBAB_FRUIT.get(), "baobab_fruit");
        modTextureBlockItem(ModBlocks.BAOBAB_SAPLING.get().asItem(), "baobab_sapling");

        modTextureBlockItem(ModBlocks.BAOBAB_LITTER.get().asItem(), "baobab_litter");
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

    private void modTextureBlockItem(Item item, String textureName) {
        String name = item.builtInRegistryHolder().key().location().getPath();
        withExistingParent(name, mcLoc("item/generated"))
                .texture("layer0", modLoc("block/" + textureName));
    }
}
