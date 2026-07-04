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
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BaobabFruitPodBlock extends Block implements BonemealableBlock {
    public static final MapCodec<BaobabFruitPodBlock> CODEC = simpleCodec(BaobabFruitPodBlock::new);
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final int RIPE_FALL_MIN_DELAY_TICKS = 30 * 20;
    private static final int RIPE_FALL_MAX_DELAY_TICKS = 180 * 20;

    private static final VoxelShape[] SHAPES = new VoxelShape[]{
            Block.box(7.0D, 10.0D, 7.0D, 9.0D, 15.0D, 9.0D),
            Block.box(6.0D, 7.0D, 6.0D, 10.0D, 13.0D, 10.0D),
            Block.box(4.0D, 3.0D, 4.0D, 12.0D, 13.0D, 12.0D),
            Block.box(2.0D, 1.0D, 2.0D, 14.0D, 13.0D, 14.0D)
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
                                              @NotNull LevelReader level,
                                              @NotNull ScheduledTickAccess ticks,
                                              @NotNull BlockPos pos,
                                              @NotNull Direction direction,
                                              @NotNull BlockPos neighborPos,
                                              @NotNull BlockState neighborState,
                                              @NotNull RandomSource random) {
        if (direction == Direction.UP && !state.canSurvive(level, pos)) {
            if (level instanceof ServerLevel serverLevel) {
                dropFromBrokenSupport(serverLevel, pos, state);
            }
            return Blocks.AIR.defaultBlockState();
        }

        return super.updateShape(state, level, ticks, pos, direction, neighborPos, neighborState, random);
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
            int newAge = age + 1;
            BlockState grownState = state.setValue(AGE, newAge);
            level.setBlock(pos, grownState, Block.UPDATE_CLIENTS);
            if (newAge == 3) {
                scheduleRipeFallCheck(level, pos);
            }
        }
    }

    @Override
    protected void onPlace(@NotNull BlockState state,
                           @NotNull Level level,
                           @NotNull BlockPos pos,
                           @NotNull BlockState oldState,
                           boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!level.isClientSide() && state.getValue(AGE) == 3) {
            scheduleRipeFallCheck((ServerLevel) level, pos);
        }
    }

    @Override
    protected void tick(@NotNull BlockState state,
                        @NotNull ServerLevel level,
                        @NotNull BlockPos pos,
                        @NotNull RandomSource random) {
        if (state.getValue(AGE) != 3 || !state.canSurvive(level, pos)) {
            return;
        }

        if (hasPodBelow(level, pos) || !hasValidPodLandingBelow(level, pos, state.getValue(FACING))) {
            scheduleRipeFallCheck(level, pos);
            return;
        }

        BlockState fallingPodState = ModBlocks.LARGE_BAOBAB_FRUIT_POD.get().defaultBlockState()
                .setValue(BaobabHangingPodBlock.FACING, state.getValue(FACING));
        playPodDetachFeedback(level, pos, fallingPodState);
        FallingBlockEntity fallingBlock = FallingBlockEntity.fall(level, pos, fallingPodState);
        BaobabHangingPodBlock.configureFallingDamage(fallingBlock, fallingPodState);
        level.setBlock(pos, state.setValue(AGE, 0), Block.UPDATE_CLIENTS);
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

        if (!level.isClientSide()) {
            giveToPlayerOrDrop(level, pos, player, new ItemStack(podItemForAge(age)));
            level.setBlock(pos, state.setValue(AGE, 0), Block.UPDATE_CLIENTS);
            level.playSound(null, pos, SoundEvents.CAVE_VINES_PICK_BERRIES, SoundSource.BLOCKS, 0.9F, 0.9F + level.getRandom().nextFloat() * 0.2F);
        }

        return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
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
            int newAge = age + 1;
            BlockState grownState = state.setValue(AGE, newAge);
            level.setBlock(pos, grownState, Block.UPDATE_CLIENTS);
            if (newAge == 3) {
                scheduleRipeFallCheck(level, pos);
            }
        }
    }

    private void scheduleRipeFallCheck(ServerLevel level, BlockPos pos) {
        int delay = RIPE_FALL_MIN_DELAY_TICKS + level.getRandom().nextInt(RIPE_FALL_MAX_DELAY_TICKS - RIPE_FALL_MIN_DELAY_TICKS + 1);
        level.scheduleTick(pos, this, delay);
    }

    private void dropFromBrokenSupport(ServerLevel level, BlockPos pos, BlockState state) {
        int age = state.getValue(AGE);
        if (age <= 0) {
            Block.popResource(level, pos, new ItemStack(ModItems.BAOBAB_FRUIT.get()));
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
            return;
        }

        BlockState fallingPodState = podItemForAge(age).defaultBlockState()
                .setValue(BaobabHangingPodBlock.FACING, state.getValue(FACING));
        playPodDetachFeedback(level, pos, fallingPodState);
        FallingBlockEntity fallingBlock = FallingBlockEntity.fall(level, pos, fallingPodState);
        BaobabHangingPodBlock.configureFallingDamage(fallingBlock, fallingPodState);
    }

    private void playPodDetachFeedback(ServerLevel level, BlockPos pos, BlockState fallingPodState) {
        level.levelEvent(2001, pos, Block.getId(fallingPodState));
        level.playSound(null, pos, fallingPodState.getSoundType().getBreakSound(), SoundSource.BLOCKS, 0.9F, 0.85F + level.getRandom().nextFloat() * 0.1F);
    }

    private boolean hasPodBelow(LevelReader level, BlockPos pos) {
        for (BlockPos checkPos = pos.below(); checkPos.getY() >= level.getMinY(); checkPos = checkPos.below()) {
            BlockState checkState = level.getBlockState(checkPos);
            if (checkState.is(ModBlocks.SMALL_BAOBAB_FRUIT_POD.get())
                    || checkState.is(ModBlocks.MEDIUM_BAOBAB_FRUIT_POD.get())
                    || checkState.is(ModBlocks.LARGE_BAOBAB_FRUIT_POD.get())) {
                return true;
            }

            if (!checkState.isAir() && !checkState.canBeReplaced()) {
                return false;
            }
        }
        return false;
    }

    private boolean hasValidPodLandingBelow(LevelReader level, BlockPos pos, Direction facing) {
        for (BlockPos checkPos = pos.below(); checkPos.getY() >= level.getMinY(); checkPos = checkPos.below()) {
            BlockState checkState = level.getBlockState(checkPos);
            if (checkState.isAir() || checkState.canBeReplaced()) {
                continue;
            }

            BlockPos landingPos = checkPos.above();
            if (!level.getBlockState(landingPos).canBeReplaced()) {
                return false;
            }

            BlockState landingState = ModBlocks.LARGE_BAOBAB_FRUIT_POD.get().defaultBlockState()
                    .setValue(BaobabHangingPodBlock.FACING, facing);
            return landingState.canSurvive(level, landingPos);
        }

        return false;
    }

    private void giveToPlayerOrDrop(Level level, BlockPos pos, Player player, ItemStack stack) {
        ItemStack remainder = stack.copy();
        boolean fullyInserted = player.addItem(remainder);
        if (!fullyInserted || !remainder.isEmpty()) {
            Block.popResource(level, pos, remainder);
        }
    }
}
