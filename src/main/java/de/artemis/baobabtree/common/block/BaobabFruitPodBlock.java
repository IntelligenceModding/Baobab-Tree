package de.artemis.baobabtree.common.block;

import com.mojang.serialization.MapCodec;
import de.artemis.baobabtree.common.registry.ModBlocks;
import de.artemis.baobabtree.common.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BaobabFruitPodBlock extends Block implements BonemealableBlock {
    public static final MapCodec<BaobabFruitPodBlock> CODEC = simpleCodec(BaobabFruitPodBlock::new);
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape[] SHAPES = new VoxelShape[]{
            Block.box(7.0D, 10.0D, 7.0D, 9.0D, 15.0D, 9.0D),
            Block.box(6.0D, 8.0D, 6.0D, 10.0D, 14.0D, 10.0D),
            Block.box(4.0D, 4.0D, 4.0D, 12.0D, 14.0D, 12.0D),
            Block.box(2.0D, 2.0D, 2.0D, 14.0D, 14.0D, 14.0D)
    };

    public BaobabFruitPodBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(AGE, 0)
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected @NotNull MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, FACING);
    }

    @Override
    protected boolean isRandomlyTicking(@NotNull BlockState state) {
        return state.getValue(AGE) < 3;
    }

    @Override
    protected boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        return level.getBlockState(pos.above()).is(ModBlocks.BAOBAB_LEAVES.get());
    }

    @Override
    protected @NotNull BlockState updateShape(@NotNull BlockState state,
                                              @NotNull Direction direction,
                                              @NotNull BlockState neighborState,
                                              @NotNull LevelAccessor level,
                                              @NotNull BlockPos pos,
                                              @NotNull BlockPos neighborPos) {
        return direction == Direction.UP && !state.canSurvive(level, pos)
                ? net.minecraft.world.level.block.Blocks.AIR.defaultBlockState()
                : super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
        return state.canSurvive(context.getLevel(), context.getClickedPos()) ? state : null;
    }

    @Override
    protected void randomTick(@NotNull BlockState state,
                              @NotNull ServerLevel level,
                              @NotNull BlockPos pos,
                              @NotNull RandomSource random) {
        int age = state.getValue(AGE);
        if (age < 3 && random.nextInt(5) == 0) {
            level.setBlock(pos, state.setValue(AGE, age + 1), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state,
                                           @NotNull BlockGetter level,
                                           @NotNull BlockPos pos,
                                           @NotNull CollisionContext context) {
        return SHAPES[state.getValue(AGE)];
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state,
                                                        @NotNull Level level,
                                                        @NotNull BlockPos pos,
                                                        @NotNull Player player,
                                                        @NotNull BlockHitResult hitResult) {
        int age = state.getValue(AGE);
        if (age == 0) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide) {
            giveToPlayerOrDrop(level, pos, player, new ItemStack(podItemForAge(age)));
            level.setBlock(pos, state.setValue(AGE, 0), Block.UPDATE_CLIENTS);
            level.playSound(null, pos, SoundEvents.CAVE_VINES_PICK_BERRIES, SoundSource.BLOCKS, 0.9F, 0.9F + level.random.nextFloat() * 0.2F);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private Block podItemForAge(int age) {
        return switch (age) {
            case 1 -> ModBlocks.SMALL_BAOBAB_FRUIT_POD.get();
            case 2 -> ModBlocks.MEDIUM_BAOBAB_FRUIT_POD.get();
            default -> ModBlocks.LARGE_BAOBAB_FRUIT_POD.get();
        };
    }

    @Override
    public boolean isValidBonemealTarget(@NotNull LevelReader level,
                                         @NotNull BlockPos pos,
                                         @NotNull BlockState state) {
        return state.getValue(AGE) < 3;
    }

    @Override
    public boolean isBonemealSuccess(@NotNull Level level,
                                     @NotNull RandomSource random,
                                     @NotNull BlockPos pos,
                                     @NotNull BlockState state) {
        return state.getValue(AGE) < 3;
    }

    @Override
    public void performBonemeal(@NotNull ServerLevel level,
                                @NotNull RandomSource random,
                                @NotNull BlockPos pos,
                                @NotNull BlockState state) {
        int age = state.getValue(AGE);
        if (age < 3) {
            level.setBlock(pos, state.setValue(AGE, age + 1), Block.UPDATE_CLIENTS);
        }
    }

    private void giveToPlayerOrDrop(Level level, BlockPos pos, Player player, ItemStack stack) {
        ItemStack remainder = stack.copy();
        boolean fullyInserted = player.addItem(remainder);
        if (!fullyInserted || !remainder.isEmpty()) {
            Block.popResource(level, pos, remainder);
        }
    }
}
