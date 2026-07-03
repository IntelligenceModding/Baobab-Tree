package de.artemis.baobabtree.common.block;

import com.mojang.serialization.MapCodec;
import de.artemis.baobabtree.common.registry.ModItems;
import de.artemis.baobabtree.common.util.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.ItemAbilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BaobabHangingPodBlock extends Block implements Fallable {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape[] GROUND_SHAPES = new VoxelShape[]{
            Block.box(6.0D, 0.0D, 6.0D, 10.0D, 6.0D, 10.0D),
            Block.box(4.0D, 0.0D, 4.0D, 12.0D, 10.0D, 12.0D),
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D)
    };

    private final int sizeIndex;
    private final MapCodec<BaobabHangingPodBlock> codec;

    public BaobabHangingPodBlock(BlockBehaviour.Properties properties) {
        this(properties, 0);
    }

    public BaobabHangingPodBlock(BlockBehaviour.Properties properties, int sizeIndex) {
        super(properties);
        this.sizeIndex = sizeIndex;
        this.codec = simpleCodec(p -> new BaobabHangingPodBlock(p, sizeIndex));
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected @NotNull MapCodec<? extends Block> codec() {
        return codec;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    protected boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        BlockPos belowPos = pos.below();
        return level.getBlockState(belowPos).isFaceSturdy(level, belowPos, Direction.UP);
    }

    @Override
    protected @NotNull BlockState updateShape(@NotNull BlockState state,
                                              @NotNull Direction direction,
                                              @NotNull BlockState neighborState,
                                              @NotNull LevelAccessor level,
                                              @NotNull BlockPos pos,
                                              @NotNull BlockPos neighborPos) {
        return direction == Direction.DOWN && !state.canSurvive(level, pos)
                ? net.minecraft.world.level.block.Blocks.AIR.defaultBlockState()
                : super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
        return state.canSurvive(context.getLevel(), context.getClickedPos()) ? state : null;
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state,
                                           @NotNull BlockGetter level,
                                           @NotNull BlockPos pos,
                                           @NotNull CollisionContext context) {
        return GROUND_SHAPES[sizeIndex];
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack,
                                                       @NotNull BlockState state,
                                                       @NotNull Level level,
                                                       @NotNull BlockPos pos,
                                                       @NotNull Player player,
                                                       @NotNull InteractionHand hand,
                                                       @NotNull BlockHitResult hitResult) {
        if (!stack.canPerformAction(ItemAbilities.AXE_DIG) && !stack.canPerformAction(ItemAbilities.SWORD_DIG)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (level.isClientSide()) {
            return ItemInteractionResult.SUCCESS;
        }

        harvestPod(level, pos, state, player, stack, hand, hitResult);
        return ItemInteractionResult.CONSUME;
    }

    @Override
    public void onLand(Level level, BlockPos pos, BlockState state, BlockState replaceableState, FallingBlockEntity fallingBlock) {
        level.levelEvent(2001, pos, Block.getId(state));
        level.playSound(null, pos, state.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 0.9F, 0.9F + level.random.nextFloat() * 0.15F);
    }

    @Override
    public void onBrokenAfterFall(Level level, BlockPos pos, FallingBlockEntity fallingBlock) {
        BlockState state = defaultBlockState();
        level.levelEvent(2001, pos, Block.getId(state));
        level.playSound(null, pos, state.getSoundType().getBreakSound(), SoundSource.BLOCKS, 0.9F, 0.9F + level.random.nextFloat() * 0.15F);
    }

    public static void configureFallingDamage(FallingBlockEntity entity, BlockState state) {
        if (!(state.getBlock() instanceof BaobabHangingPodBlock podBlock)) {
            return;
        }

        switch (podBlock.sizeIndex) {
            case 0 -> entity.setHurtsEntities(1.5F, 12);
            case 1 -> entity.setHurtsEntities(2.5F, 20);
            default -> entity.setHurtsEntities(3.5F, 30);
        }
    }

    private void harvestPod(Level level,
                            BlockPos pos,
                            BlockState state,
                            Player player,
                            ItemStack tool,
                            InteractionHand hand,
                            BlockHitResult hitResult) {
        level.levelEvent(2001, pos, Block.getId(state));
        level.playSound(null, pos, state.getSoundType().getBreakSound(), SoundSource.BLOCKS, 0.9F, 0.95F + level.random.nextFloat() * 0.1F);
        level.removeBlock(pos, false);

        ModUtils.spawnItemAtClickedSide(level, pos, hitResult, new ItemStack(ModItems.BAOBAB_FRUIT.get(), (sizeIndex + 1) * 2));
        ModUtils.awardBlockMinedStat(player, this);
        tool.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
    }
}
