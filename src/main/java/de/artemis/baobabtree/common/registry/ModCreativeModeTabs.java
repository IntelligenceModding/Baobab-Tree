package de.artemis.baobabtree.common.registry;

import de.artemis.baobabtree.BaobabTree;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
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
                    .icon(() -> new ItemStack(ModBlocks.BAOBAB_SAPLING.get()))
                    .title(Component.translatable("itemGroup.baobabtree"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModBlocks.BAOBAB_LOG);
                        output.accept(ModBlocks.BAOBAB_WOOD);
                        output.accept(ModBlocks.STRIPPED_BAOBAB_LOG);
                        output.accept(ModBlocks.STRIPPED_BAOBAB_WOOD);
                        output.accept(ModBlocks.BAOBAB_PLANKS);
                        output.accept(ModBlocks.BAOBAB_STAIRS);
                        output.accept(ModBlocks.BAOBAB_SLAB);
                        output.accept(ModBlocks.BAOBAB_FENCE);
                        output.accept(ModBlocks.BAOBAB_FENCE_GATE);
                        output.accept(ModBlocks.BAOBAB_DOOR);
                        output.accept(ModBlocks.BAOBAB_TRAPDOOR);
                        output.accept(ModBlocks.BAOBAB_SHELF);
                        output.accept(ModBlocks.BAOBAB_BUTTON);
                        output.accept(ModBlocks.BAOBAB_PRESSURE_PLATE);
                        output.accept(ModItems.BAOBAB_SIGN);
                        output.accept(ModItems.BAOBAB_HANGING_SIGN);
                        output.accept(ModBlocks.BAOBAB_LEAVES);
                        output.accept(ModBlocks.BAOBAB_SAPLING);
                        output.accept(ModBlocks.BAOBAB_LITTER);
                        output.accept(ModBlocks.TREE_ROOT);
                        output.accept(ModBlocks.SMALL_BAOBAB_FRUIT_POD);
                        output.accept(ModBlocks.MEDIUM_BAOBAB_FRUIT_POD);
                        output.accept(ModBlocks.LARGE_BAOBAB_FRUIT_POD);
                        output.accept(ModItems.BAOBAB_FRUIT);
                        output.accept(ModItems.BAOBAB_BOAT);
                        output.accept(ModItems.BAOBAB_CHEST_BOAT);
                    })
                    .build()
    );

    private ModCreativeModeTabs() {
    }

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
