package de.artemis.baobabtree.common.block;

import com.mojang.serialization.MapCodec;
import de.artemis.baobabtree.common.util.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.ItemAbilities;
import org.jetbrains.annotations.NotNull;

public class TreeRootBlock extends RotatedPillarBlock {
    public static final MapCodec<TreeRootBlock> CODEC = simpleCodec(TreeRootBlock::new);
    private static final VoxelShape FULL_SUPPORT = Shapes.block();

    public TreeRootBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull MapCodec<TreeRootBlock> codec() {
        return CODEC;
    }

    @Override
    protected @NotNull VoxelShape getCollisionShape(@NotNull BlockState state,
                                                    @NotNull BlockGetter level,
                                                    @NotNull BlockPos pos,
                                                    @NotNull CollisionContext context) {
        return FULL_SUPPORT;
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state,
                                           @NotNull BlockGetter level,
                                           @NotNull BlockPos pos,
                                           @NotNull CollisionContext context) {
        return FULL_SUPPORT;
    }

    @Override
    protected @NotNull VoxelShape getBlockSupportShape(@NotNull BlockState state,
                                                       @NotNull BlockGetter level,
                                                       @NotNull BlockPos pos) {
        return FULL_SUPPORT;
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack,
                                                       @NotNull BlockState state,
                                                       @NotNull Level level,
                                                       @NotNull BlockPos pos,
                                                       @NotNull Player player,
                                                       @NotNull InteractionHand hand,
                                                       @NotNull BlockHitResult hitResult) {
        if (stack.canPerformAction(ItemAbilities.SHOVEL_DIG)) {
            if (level.isClientSide()) {
                return ItemInteractionResult.SUCCESS;
            }

            shovelRoot((ServerLevel) level, pos, player, hand, stack, hitResult);
            return ItemInteractionResult.CONSUME;
        }

        if (!stack.canPerformAction(ItemAbilities.AXE_DIG)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (level.isClientSide()) {
            return ItemInteractionResult.SUCCESS;
        }

        harvestRoot((ServerLevel) level, pos, state, player, hand, stack);
        return ItemInteractionResult.CONSUME;
    }

    private void shovelRoot(ServerLevel level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack, BlockHitResult hitResult) {
        level.setBlock(pos, Blocks.DIRT.defaultBlockState(), 3);
        ModUtils.spawnItemAtClickedSide(level, pos, hitResult, new ItemStack(Blocks.HANGING_ROOTS));
        stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
        level.playSound(null, pos, SoundEvents.ROOTED_DIRT_BREAK, SoundSource.BLOCKS, 0.9F, 0.95F + level.random.nextFloat() * 0.15F);
    }

    private void harvestRoot(ServerLevel level, BlockPos pos, BlockState state, Player player, InteractionHand hand, ItemStack stack) {
        level.destroyBlock(pos, false, player);
        ModUtils.awardBlockMinedStat(player, this);

        int hangingRoots = level.random.nextInt(3);
        int dirt = level.random.nextInt(2);

        if (hangingRoots > 0) {
            Block.popResource(level, pos, new ItemStack(Blocks.HANGING_ROOTS, hangingRoots));
        }
        if (dirt > 0) {
            Block.popResource(level, pos, new ItemStack(Blocks.DIRT, dirt));
        }

        stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
    }
}
