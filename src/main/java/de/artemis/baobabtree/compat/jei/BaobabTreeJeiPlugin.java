package de.artemis.baobabtree.compat.jei;

import de.artemis.baobabtree.BaobabTree;
import de.artemis.baobabtree.common.registry.ModBlocks;
import de.artemis.baobabtree.common.registry.ModItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

@JeiPlugin
public class BaobabTreeJeiPlugin implements IModPlugin {
    private static final Identifier PLUGIN_UID = Identifier.fromNamespaceAndPath(BaobabTree.MOD_ID, "jei_plugin");

    @Override
    public Identifier getPluginUid() {
        return PLUGIN_UID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addIngredientInfo(
                ModBlocks.BAOBAB_SAPLING.get(),
                Component.translatable("jei.baobabtree.baobab_sapling")
        );

        registration.addIngredientInfo(
                ModItems.BAOBAB_FRUIT.get(),
                Component.translatable("jei.baobabtree.baobab_fruit")
        );

        registration.addIngredientInfo(
                ModBlocks.BAOBAB_LITTER.get(),
                Component.translatable("jei.baobabtree.baobab_litter")
        );

        registration.addIngredientInfo(
                ModBlocks.TREE_ROOT.get(),
                Component.translatable("jei.baobabtree.tree_root")
        );

        registration.addIngredientInfo(
                ModBlocks.BAOBAB_FRUIT_POD.get(),
                Component.translatable("jei.baobabtree.baobab_fruit_pod")
        );

        registration.addIngredientInfo(
                ModBlocks.SMALL_BAOBAB_FRUIT_POD.get(),
                Component.translatable("jei.baobabtree.small_baobab_fruit_pod")
        );

        registration.addIngredientInfo(
                ModBlocks.MEDIUM_BAOBAB_FRUIT_POD.get(),
                Component.translatable("jei.baobabtree.medium_baobab_fruit_pod")
        );

        registration.addIngredientInfo(
                ModBlocks.LARGE_BAOBAB_FRUIT_POD.get(),
                Component.translatable("jei.baobabtree.large_baobab_fruit_pod")
        );
    }
}
