package de.artemis.baobabtree.common.registry;

import de.artemis.baobabtree.BaobabTree;
import de.artemis.baobabtree.common.block.BaobabCeilingHangingSignBlock;
import de.artemis.baobabtree.common.block.BaobabFruitPodBlock;
import de.artemis.baobabtree.common.block.BaobabStandingSignBlock;
import de.artemis.baobabtree.common.block.BaobabStrippableLogBlock;
import de.artemis.baobabtree.common.block.BaobabWallHangingSignBlock;
import de.artemis.baobabtree.common.block.BaobabWallSignBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.PinkPetalsBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(BaobabTree.MOD_ID);

    public static final DeferredBlock<Block> BAOBAB_LOG = registerBlock("baobab_log",
            () -> new BaobabStrippableLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_LOG), () -> ModBlocks.STRIPPED_BAOBAB_LOG.get()));

    public static final DeferredBlock<Block> BAOBAB_WOOD = registerBlock("baobab_wood",
            () -> new BaobabStrippableLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_WOOD), () -> ModBlocks.STRIPPED_BAOBAB_WOOD.get()));

    public static final DeferredBlock<Block> STRIPPED_BAOBAB_LOG = registerBlock("stripped_baobab_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_ACACIA_LOG)));

    public static final DeferredBlock<Block> STRIPPED_BAOBAB_WOOD = registerBlock("stripped_baobab_wood",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_ACACIA_WOOD)));

    public static final DeferredBlock<Block> BAOBAB_PLANKS = registerBlock("baobab_planks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_PLANKS)));

    public static final DeferredBlock<Block> BAOBAB_STAIRS = registerBlock("baobab_stairs",
            () -> new StairBlock(BAOBAB_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_STAIRS)));

    public static final DeferredBlock<Block> BAOBAB_SLAB = registerBlock("baobab_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_SLAB)));

    public static final DeferredBlock<Block> BAOBAB_FENCE = registerBlock("baobab_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_FENCE)));

    public static final DeferredBlock<Block> BAOBAB_FENCE_GATE = registerBlock("baobab_fence_gate",
            () -> new FenceGateBlock(ModWoodTypes.BAOBAB_WOOD_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_FENCE_GATE)));

    public static final DeferredBlock<Block> BAOBAB_BUTTON = registerBlock("baobab_button",
            () -> new ButtonBlock(ModWoodTypes.BAOBAB_SET_TYPE, 30, BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_BUTTON).noCollission()));

    public static final DeferredBlock<Block> BAOBAB_PRESSURE_PLATE = registerBlock("baobab_pressure_plate",
            () -> new PressurePlateBlock(ModWoodTypes.BAOBAB_SET_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_PRESSURE_PLATE)));

    public static final DeferredBlock<Block> BAOBAB_DOOR = registerBlock("baobab_door",
            () -> new DoorBlock(ModWoodTypes.BAOBAB_SET_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_DOOR).noOcclusion()));

    public static final DeferredBlock<Block> BAOBAB_TRAPDOOR = registerBlock("baobab_trapdoor",
            () -> new TrapDoorBlock(ModWoodTypes.BAOBAB_SET_TYPE, BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_TRAPDOOR).noOcclusion()));

    public static final DeferredBlock<Block> BAOBAB_SIGN = BLOCKS.register("baobab_sign",
            () -> new BaobabStandingSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_SIGN).noCollission().strength(1.0F)));

    public static final DeferredBlock<Block> BAOBAB_WALL_SIGN = BLOCKS.register("baobab_wall_sign",
            () -> new BaobabWallSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_WALL_SIGN).noCollission().strength(1.0F).lootFrom(BAOBAB_SIGN)));

    public static final DeferredBlock<Block> BAOBAB_HANGING_SIGN = BLOCKS.register("baobab_hanging_sign",
            () -> new BaobabCeilingHangingSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_HANGING_SIGN).noCollission().strength(1.0F)));

    public static final DeferredBlock<Block> BAOBAB_WALL_HANGING_SIGN = BLOCKS.register("baobab_wall_hanging_sign",
            () -> new BaobabWallHangingSignBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_WALL_HANGING_SIGN).noCollission().strength(1.0F).lootFrom(BAOBAB_HANGING_SIGN)));

    public static final DeferredBlock<Block> BAOBAB_LEAVES = registerBlock("baobab_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_LEAVES).randomTicks()));

    public static final DeferredBlock<Block> BAOBAB_SAPLING = registerBlock("baobab_sapling",
            () -> new SaplingBlock(ModTreeGrowers.BAOBAB, BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_SAPLING)));

    public static final DeferredBlock<Block> POTTED_BAOBAB_SAPLING = BLOCKS.register("potted_baobab_sapling",
            () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, BAOBAB_SAPLING, BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_ACACIA_SAPLING).noOcclusion()));

    public static final DeferredBlock<Block> BAOBAB_LEAF_LITTER = registerBlock("baobab_leaf_litter",
            () -> new PinkPetalsBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.PINK_PETALS)));

    public static final DeferredBlock<Block> TREE_ROOT = registerBlock("tree_root",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MUDDY_MANGROVE_ROOTS)));

    public static final DeferredBlock<Block> BAOBAB_FRUIT_POD = registerBlock("baobab_fruit_pod",
            () -> new BaobabFruitPodBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COCOA).randomTicks()));

    private ModBlocks() {
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> blockSupplier) {
        DeferredBlock<T> block = BLOCKS.register(name, blockSupplier);
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
