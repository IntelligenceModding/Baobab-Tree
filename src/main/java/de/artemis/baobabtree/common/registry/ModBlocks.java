package de.artemis.baobabtree.common.registry;

import de.artemis.baobabtree.BaobabTree;
import de.artemis.baobabtree.common.block.BaobabCeilingHangingSignBlock;
import de.artemis.baobabtree.common.block.BaobabFruitPodBlock;
import de.artemis.baobabtree.common.block.BaobabHangingPodBlock;
import de.artemis.baobabtree.common.block.BaobabLitterBlock;
import de.artemis.baobabtree.common.block.BaobabSaplingBlock;
import de.artemis.baobabtree.common.block.BaobabStandingSignBlock;
import de.artemis.baobabtree.common.block.BaobabStrippableLogBlock;
import de.artemis.baobabtree.common.block.BaobabWallHangingSignBlock;
import de.artemis.baobabtree.common.block.BaobabWallSignBlock;
import de.artemis.baobabtree.common.block.TreeRootBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

@SuppressWarnings("deprecation")
public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(BaobabTree.MOD_ID);

    public static final DeferredBlock<Block> BAOBAB_LOG = registerBlock("baobab_log",
            properties -> new BaobabStrippableLogBlock(properties, () -> ModBlocks.STRIPPED_BAOBAB_LOG.get()),
            BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_LOG));

    public static final DeferredBlock<Block> BAOBAB_WOOD = registerBlock("baobab_wood",
            properties -> new BaobabStrippableLogBlock(properties, () -> ModBlocks.STRIPPED_BAOBAB_WOOD.get()),
            BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_WOOD));

    public static final DeferredBlock<Block> STRIPPED_BAOBAB_LOG = registerBlock("stripped_baobab_log",
            RotatedPillarBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_ACACIA_LOG));

    public static final DeferredBlock<Block> STRIPPED_BAOBAB_WOOD = registerBlock("stripped_baobab_wood",
            RotatedPillarBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_ACACIA_WOOD));

    public static final DeferredBlock<Block> BAOBAB_PLANKS = registerBlock("baobab_planks",
            Block::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_PLANKS));

    public static final DeferredBlock<Block> BAOBAB_STAIRS = registerBlock("baobab_stairs",
            properties -> new StairBlock(BAOBAB_PLANKS.get().defaultBlockState(), properties),
            BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_STAIRS));

    public static final DeferredBlock<Block> BAOBAB_SLAB = registerBlock("baobab_slab",
            SlabBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_SLAB));

    public static final DeferredBlock<Block> BAOBAB_FENCE = registerBlock("baobab_fence",
            FenceBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_FENCE));

    public static final DeferredBlock<Block> BAOBAB_FENCE_GATE = registerBlock("baobab_fence_gate",
            properties -> new FenceGateBlock(ModWoodTypes.BAOBAB_WOOD_TYPE, properties),
            BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_FENCE_GATE));

    public static final DeferredBlock<Block> BAOBAB_BUTTON = registerBlock("baobab_button",
            properties -> new ButtonBlock(ModWoodTypes.BAOBAB_SET_TYPE, 30, properties.noCollission()),
            BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_BUTTON));

    public static final DeferredBlock<Block> BAOBAB_PRESSURE_PLATE = registerBlock("baobab_pressure_plate",
            properties -> new PressurePlateBlock(ModWoodTypes.BAOBAB_SET_TYPE, properties),
            BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_PRESSURE_PLATE));

    public static final DeferredBlock<Block> BAOBAB_DOOR = registerBlock("baobab_door",
            properties -> new DoorBlock(ModWoodTypes.BAOBAB_SET_TYPE, properties.noOcclusion()),
            BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_DOOR));

    public static final DeferredBlock<Block> BAOBAB_TRAPDOOR = registerBlock("baobab_trapdoor",
            properties -> new TrapDoorBlock(ModWoodTypes.BAOBAB_SET_TYPE, properties.noOcclusion()),
            BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_TRAPDOOR));

    public static final DeferredBlock<Block> BAOBAB_SIGN = registerBlockWithoutItem("baobab_sign",
            properties -> new BaobabStandingSignBlock(properties.noCollission().strength(1.0F)),
            BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_SIGN));

    public static final DeferredBlock<Block> BAOBAB_WALL_SIGN = registerBlockWithoutItem("baobab_wall_sign",
            properties -> new BaobabWallSignBlock(properties.noCollission().strength(1.0F)),
            BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_WALL_SIGN));

    public static final DeferredBlock<Block> BAOBAB_HANGING_SIGN = registerBlockWithoutItem("baobab_hanging_sign",
            properties -> new BaobabCeilingHangingSignBlock(properties.noCollission().strength(1.0F)),
            BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_HANGING_SIGN));

    public static final DeferredBlock<Block> BAOBAB_WALL_HANGING_SIGN = registerBlockWithoutItem("baobab_wall_hanging_sign",
            properties -> new BaobabWallHangingSignBlock(properties.noCollission().strength(1.0F)),
            BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_WALL_HANGING_SIGN));

    public static final DeferredBlock<Block> BAOBAB_LEAVES = registerBlock("baobab_leaves",
            properties -> new LeavesBlock(properties.randomTicks()),
            BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_LEAVES));

    public static final DeferredBlock<Block> BAOBAB_SAPLING = registerBlock("baobab_sapling",
            BaobabSaplingBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_SAPLING));

    public static final DeferredBlock<Block> POTTED_BAOBAB_SAPLING = registerBlockWithoutItem("potted_baobab_sapling",
            properties -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, BAOBAB_SAPLING, properties.noOcclusion()),
            BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_ACACIA_SAPLING));

    public static final DeferredBlock<Block> BAOBAB_LITTER = registerBlock("baobab_litter",
            BaobabLitterBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.PINK_PETALS));

    public static final DeferredBlock<Block> TREE_ROOT = registerBlock("tree_root",
            properties -> new TreeRootBlock(properties.sound(SoundType.ROOTED_DIRT)),
            BlockBehaviour.Properties.ofFullCopy(Blocks.MUDDY_MANGROVE_ROOTS));

    public static final DeferredBlock<Block> BAOBAB_FRUIT_POD = registerBlock("baobab_fruit_pod",
            properties -> new BaobabFruitPodBlock(properties.randomTicks().sound(SoundType.CROP)),
            BlockBehaviour.Properties.ofFullCopy(Blocks.COCOA));

    public static final DeferredBlock<Block> SMALL_BAOBAB_FRUIT_POD = registerBlock("small_baobab_fruit_pod",
            properties -> new BaobabHangingPodBlock(properties.sound(SoundType.CROP), 0),
            BlockBehaviour.Properties.ofFullCopy(Blocks.COCOA));

    public static final DeferredBlock<Block> MEDIUM_BAOBAB_FRUIT_POD = registerBlock("medium_baobab_fruit_pod",
            properties -> new BaobabHangingPodBlock(properties.sound(SoundType.CROP), 1),
            BlockBehaviour.Properties.ofFullCopy(Blocks.COCOA));

    public static final DeferredBlock<Block> LARGE_BAOBAB_FRUIT_POD = registerBlock("large_baobab_fruit_pod",
            properties -> new BaobabHangingPodBlock(properties.sound(SoundType.CROP), 2),
            BlockBehaviour.Properties.ofFullCopy(Blocks.COCOA));

    private ModBlocks() {
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> blockFactory, BlockBehaviour.Properties properties) {
        DeferredBlock<T> block = BLOCKS.registerBlock(name, blockFactory, properties);
        ModItems.ITEMS.registerSimpleBlockItem(block);
        return block;
    }

    private static <T extends Block> DeferredBlock<T> registerBlockWithoutItem(String name, Function<BlockBehaviour.Properties, T> blockFactory, BlockBehaviour.Properties properties) {
        return BLOCKS.registerBlock(name, blockFactory, properties);
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
