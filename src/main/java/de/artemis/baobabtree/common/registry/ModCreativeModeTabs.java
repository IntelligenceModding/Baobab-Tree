package de.artemis.baobabtree.common.registry;

import de.artemis.baobabtree.BaobabTree;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BaobabTree.MOD_ID);

    @SuppressWarnings("unused")
    public static final Supplier<CreativeModeTab> BAOBAB_TREE = CREATIVE_MODE_TABS.register(
            "baobab_tree",
            () -> CreativeModeTab.builder()
                    .title(Component.literal("Baobab Tree"))
                    .icon(() -> Items.ACACIA_SAPLING.getDefaultInstance())
                    .displayItems((parameters, output) -> {
                    })
                    .build()
    );

    private ModCreativeModeTabs() {
    }

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
