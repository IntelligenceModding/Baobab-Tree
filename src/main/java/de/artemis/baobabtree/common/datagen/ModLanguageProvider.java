package de.artemis.baobabtree.common.datagen;

import de.artemis.baobabtree.BaobabTree;
import net.minecraft.data.PackOutput;

public class ModLanguageProvider extends net.neoforged.neoforge.common.data.LanguageProvider {
    public ModLanguageProvider(PackOutput output, String locale) {
        super(output, BaobabTree.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.baobabtree", "Baobab Tree");
    }
}
