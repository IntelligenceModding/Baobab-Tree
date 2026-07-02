package de.artemis.baobabtree.common.datagen;

import de.artemis.baobabtree.BaobabTree;
import de.artemis.baobabtree.common.block.BaobabFruitPodBlock;
import de.artemis.baobabtree.common.registry.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.PinkPetalsBlock;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, BaobabTree.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        logBlock(ModBlocks.BAOBAB_LOG.get(), mcLoc("block/acacia_log"), mcLoc("block/acacia_log_top"));
        woodBlock(ModBlocks.BAOBAB_WOOD.get(), mcLoc("block/acacia_log"));
        logBlock(ModBlocks.STRIPPED_BAOBAB_LOG.get(), mcLoc("block/stripped_acacia_log"), mcLoc("block/stripped_acacia_log_top"));
        woodBlock(ModBlocks.STRIPPED_BAOBAB_WOOD.get(), mcLoc("block/stripped_acacia_log"));

        planksBlock(ModBlocks.BAOBAB_PLANKS.get(), mcLoc("block/acacia_planks"));
        stairsBlockFromTexture(ModBlocks.BAOBAB_STAIRS.get(), mcLoc("block/acacia_planks"));
        slabBlockFromTexture(ModBlocks.BAOBAB_SLAB.get(), mcLoc("block/acacia_planks"));
        fenceBlockFromTexture(ModBlocks.BAOBAB_FENCE.get(), mcLoc("block/acacia_planks"));
        fenceGateBlockFromTexture(ModBlocks.BAOBAB_FENCE_GATE.get(), mcLoc("block/acacia_planks"));
        buttonBlockFromTexture(ModBlocks.BAOBAB_BUTTON.get(), mcLoc("block/acacia_planks"));
        pressurePlateBlockFromTexture(ModBlocks.BAOBAB_PRESSURE_PLATE.get(), mcLoc("block/acacia_planks"));
        doorBlockFromTexture(ModBlocks.BAOBAB_DOOR.get(), mcLoc("block/acacia_door_bottom"), mcLoc("block/acacia_door_top"));
        trapdoorBlockFromTexture(ModBlocks.BAOBAB_TRAPDOOR.get(), mcLoc("block/acacia_trapdoor"));

        signBlock(
                (StandingSignBlock) ModBlocks.BAOBAB_SIGN.get(),
                (WallSignBlock) ModBlocks.BAOBAB_WALL_SIGN.get(),
                mcLoc("block/acacia_planks")
        );
        hangingSignBlock(
                (CeilingHangingSignBlock) ModBlocks.BAOBAB_HANGING_SIGN.get(),
                (WallHangingSignBlock) ModBlocks.BAOBAB_WALL_HANGING_SIGN.get(),
                mcLoc("block/acacia_planks")
        );

        leavesBlock(ModBlocks.BAOBAB_LEAVES.get(), mcLoc("block/acacia_leaves"));
        saplingBlock(ModBlocks.BAOBAB_SAPLING.get(), mcLoc("block/acacia_sapling"));
        pottedPlantBlock(ModBlocks.POTTED_BAOBAB_SAPLING.get(), mcLoc("block/acacia_sapling"));

        leafLitterBlock();

        logBlock(ModBlocks.TREE_ROOT.get(), mcLoc("block/muddy_mangrove_roots_side"), mcLoc("block/muddy_mangrove_roots_top"));
        fruitPodBlock();
    }

    private void logBlock(Block block, ResourceLocation side, ResourceLocation end) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        ModelFile vertical = models().cubeColumn(name, side, end);
        ModelFile horizontal = models().cubeColumnHorizontal(name + "_horizontal", side, end);
        axisBlock((RotatedPillarBlock) block, vertical, horizontal);
        simpleBlockItem(block, vertical);
    }

    private void woodBlock(Block block, ResourceLocation bark) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        ModelFile vertical = models().cubeColumn(name, bark, bark);
        ModelFile horizontal = models().cubeColumnHorizontal(name + "_horizontal", bark, bark);
        axisBlock((RotatedPillarBlock) block, vertical, horizontal);
        simpleBlockItem(block, vertical);
    }

    private void planksBlock(Block block, ResourceLocation texture) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        ModelFile model = models().cubeAll(name, texture);
        simpleBlock(block, model);
        simpleBlockItem(block, model);
    }

    private void stairsBlockFromTexture(Block block, ResourceLocation texture) {
        stairsBlock((StairBlock) block, texture);
        simpleBlockItem(block, models().getExistingFile(modLoc(BuiltInRegistries.BLOCK.getKey(block).getPath())));
    }

    private void slabBlockFromTexture(Block block, ResourceLocation texture) {
        slabBlock((SlabBlock) block, texture, texture);
        simpleBlockItem(block, models().getExistingFile(modLoc(BuiltInRegistries.BLOCK.getKey(block).getPath())));
    }

    private void fenceBlockFromTexture(Block block, ResourceLocation texture) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        fenceBlock((FenceBlock) block, texture);
        simpleBlockItem(block, models().fenceInventory(name + "_inventory", texture));
    }

    private void fenceGateBlockFromTexture(Block block, ResourceLocation texture) {
        fenceGateBlock((FenceGateBlock) block, texture);
        simpleBlockItem(block, models().getExistingFile(modLoc(BuiltInRegistries.BLOCK.getKey(block).getPath())));
    }

    private void buttonBlockFromTexture(Block block, ResourceLocation texture) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        buttonBlock((ButtonBlock) block, texture);
        simpleBlockItem(block, models().buttonInventory(name + "_inventory", texture));
    }

    private void pressurePlateBlockFromTexture(Block block, ResourceLocation texture) {
        pressurePlateBlock((PressurePlateBlock) block, texture);
        simpleBlockItem(block, models().getExistingFile(modLoc(BuiltInRegistries.BLOCK.getKey(block).getPath())));
    }

    private void doorBlockFromTexture(Block block, ResourceLocation bottom, ResourceLocation top) {
        doorBlockWithRenderType((DoorBlock) block, bottom, top, "cutout");
        simpleBlockItem(block, models().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath(), mcLoc("item/generated"))
                .texture("layer0", mcLoc("item/acacia_door")));
    }

    private void trapdoorBlockFromTexture(Block block, ResourceLocation texture) {
        trapdoorBlockWithRenderType((TrapDoorBlock) block, texture, true, "cutout");
        simpleBlockItem(block, models().getExistingFile(modLoc(BuiltInRegistries.BLOCK.getKey(block).getPath() + "_bottom")));
    }

    private void leavesBlock(Block block, ResourceLocation texture) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        BlockModelBuilder model = models()
                .withExistingParent(name, mcLoc("block/leaves"))
                .texture("all", texture)
                .renderType("cutout_mipped");
        simpleBlock(block, model);
        simpleBlockItem(block, model);
    }

    private void saplingBlock(Block block, ResourceLocation texture) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        ModelFile model = models().cross(name, texture).renderType("cutout");
        simpleBlock(block, model);
    }

    private void pottedPlantBlock(Block block, ResourceLocation texture) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        ModelFile model = models()
                .withExistingParent(name, mcLoc("block/flower_pot_cross"))
                .texture("plant", texture)
                .texture("dirt", mcLoc("block/dirt"))
                .texture("flowerpot", mcLoc("block/flower_pot"));
        simpleBlock(block, model);
    }

    private void crossBlock(Block block, ResourceLocation texture) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        ModelFile model = models().cross(name, texture).renderType("cutout");
        simpleBlock(block, model);
        simpleBlockItem(block, model);
    }

    private void cutoutLogBlock(Block block, ResourceLocation side, ResourceLocation end) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        ModelFile vertical = models().cubeColumn(name, side, end).renderType("cutout");
        ModelFile horizontal = models().cubeColumnHorizontal(name + "_horizontal", side, end).renderType("cutout");
        axisBlock((RotatedPillarBlock) block, vertical, horizontal);
        simpleBlockItem(block, vertical);
    }

    private void leafLitterBlock() {
        String name = BuiltInRegistries.BLOCK.getKey(ModBlocks.BAOBAB_LEAF_LITTER.get()).getPath();
        ModelFile one = models().getExistingFile(modLoc("block/" + name + "_1"));
        ModelFile two = models().getExistingFile(modLoc("block/" + name + "_2"));
        ModelFile three = models().getExistingFile(modLoc("block/" + name + "_3"));
        ModelFile four = models().getExistingFile(modLoc("block/" + name + "_4"));
        MultiPartBlockStateBuilder builder = getMultipartBuilder(ModBlocks.BAOBAB_LEAF_LITTER.get());

        addLeafLitterPart(builder, one, Direction.NORTH, 0, 1);
        addLeafLitterPart(builder, one, Direction.EAST, 90, 1);
        addLeafLitterPart(builder, one, Direction.SOUTH, 180, 1);
        addLeafLitterPart(builder, one, Direction.WEST, 270, 1);

        addLeafLitterPart(builder, two, Direction.NORTH, 0, 2);
        addLeafLitterPart(builder, two, Direction.EAST, 90, 2);
        addLeafLitterPart(builder, two, Direction.SOUTH, 180, 2);
        addLeafLitterPart(builder, two, Direction.WEST, 270, 2);
        addLeafLitterPart(builder, two, Direction.NORTH, 0, 3);
        addLeafLitterPart(builder, two, Direction.EAST, 90, 3);
        addLeafLitterPart(builder, two, Direction.SOUTH, 180, 3);
        addLeafLitterPart(builder, two, Direction.WEST, 270, 3);

        addLeafLitterPart(builder, three, Direction.NORTH, 0, 3);
        addLeafLitterPart(builder, three, Direction.EAST, 90, 3);
        addLeafLitterPart(builder, three, Direction.SOUTH, 180, 3);
        addLeafLitterPart(builder, three, Direction.WEST, 270, 3);

        addLeafLitterPart(builder, four, Direction.NORTH, 0, 4);
        addLeafLitterPart(builder, four, Direction.EAST, 90, 4);
        addLeafLitterPart(builder, four, Direction.SOUTH, 180, 4);
        addLeafLitterPart(builder, four, Direction.WEST, 270, 4);
    }

    private void addLeafLitterPart(MultiPartBlockStateBuilder builder, ModelFile model, Direction facing, int rotationY, int amount) {
        builder.part()
                .modelFile(model)
                .rotationY(rotationY)
                .addModel()
                .condition(PinkPetalsBlock.FACING, facing)
                .condition(PinkPetalsBlock.AMOUNT, amount);
    }

    private void fruitPodBlock() {
        BlockModelBuilder[] stages = new BlockModelBuilder[]{
                models().cross("baobab_fruit_pod_stage0", mcLoc("block/acacia_sapling")).renderType("cutout"),
                models().cross("baobab_fruit_pod_stage1", mcLoc("block/acacia_sapling")).renderType("cutout"),
                models().cross("baobab_fruit_pod_stage2", mcLoc("block/hanging_roots")).renderType("cutout"),
                models().cross("baobab_fruit_pod_stage3", mcLoc("block/hanging_roots")).renderType("cutout"),
                models().cross("baobab_fruit_pod_stage4", mcLoc("block/hanging_roots")).renderType("cutout")
        };

        getVariantBuilder(ModBlocks.BAOBAB_FRUIT_POD.get()).forAllStates(state -> {
            int age = state.getValue(BaobabFruitPodBlock.AGE);
            return ConfiguredModel.builder().modelFile(stages[age]).build();
        });

        simpleBlockItem(ModBlocks.BAOBAB_FRUIT_POD.get(), stages[4]);
    }
}
