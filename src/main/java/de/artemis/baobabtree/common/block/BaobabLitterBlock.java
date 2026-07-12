package de.artemis.baobabtree.common.block;

import com.mojang.serialization.MapCodec;
import de.artemis.baobabtree.common.block.entity.BaobabLitterBlockEntity;
import de.artemis.baobabtree.common.registry.ModBlocks;
import de.artemis.baobabtree.common.registry.ModItems;
import de.artemis.baobabtree.common.registry.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.PinkPetalsBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.ItemAbilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BaobabLitterBlock extends PinkPetalsBlock implements EntityBlock {
    public static final MapCodec<BaobabLitterBlock> CODEC = simpleCodec(BaobabLitterBlock::new);
    public static final BooleanProperty HAS_SMALL = BooleanProperty.create("has_small");
    public static final BooleanProperty HAS_MEDIUM = BooleanProperty.create("has_medium");
    public static final BooleanProperty HAS_LARGE = BooleanProperty.create("has_large");

    public BaobabLitterBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(HAS_SMALL, false)
                .setValue(HAS_MEDIUM, false)
                .setValue(HAS_LARGE, false));
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull MapCodec<PinkPetalsBlock> codec() {
        return (MapCodec<PinkPetalsBlock>) (MapCodec<?>) CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HAS_SMALL, HAS_MEDIUM, HAS_LARGE);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new BaobabLitterBlockEntity(pos, state);
    }

    @Override
    public boolean canBeReplaced(@NotNull BlockState state, @NotNull BlockPlaceContext useContext) {
        return !useContext.isSecondaryUseActive()
                && useContext.getItemInHand().is(this.asItem())
                && state.getValue(AMOUNT) < 4;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        BlockState existing = context.getLevel().getBlockState(context.getClickedPos());
        if (existing.is(this)) {
            return existing.setValue(AMOUNT, Math.min(4, existing.getValue(AMOUNT) + 1));
        }

        BlockState state = super.getStateForPlacement(context);
        return state == null ? null : clearPodState(state);
    }

    @Override
    protected @NotNull InteractionResult useItemOn(@NotNull ItemStack stack,
                                                   @NotNull BlockState state,
                                                   @NotNull Level level,
                                                   @NotNull BlockPos pos,
                                                   @NotNull Player player,
                                                   @NotNull InteractionHand hand,
                                                   @NotNull BlockHitResult hitResult) {
        if (stack.isEmpty()) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }

        if (stack.canPerformAction(ItemAbilities.PICKAXE_DIG)) {
            if (!level.isClientSide()) {
                harvestOneLayer(level, pos, state);
                level.playSound(null, pos, SoundEvents.GRASS_BREAK, SoundSource.BLOCKS, 0.75F, 0.95F + level.getRandom().nextFloat() * 0.1F);
            }
            return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
        }

        InteractionResult itemUseResult = stack.useOn(new UseOnContext(player, hand, hitResult));
        if (itemUseResult == InteractionResult.PASS || itemUseResult == InteractionResult.TRY_WITH_EMPTY_HAND) {
            return rummageAndRemoveWithItem(state, level, pos, player);
        }

        return itemUseResult;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state,
                                                        @NotNull Level level,
                                                        @NotNull BlockPos pos,
                                                        @NotNull Player player,
                                                        @NotNull BlockHitResult hitResult) {
        return rummageAndRemove(state, level, pos, player);
    }

    private InteractionResult rummageAndRemove(BlockState state, Level level, BlockPos pos, Player player) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        int amountBefore = state.getValue(AMOUNT);
        rummage((ServerLevel) level, pos, state, player, amountBefore);
        level.removeBlock(pos, false);
        level.playSound(null, pos, SoundEvents.GRASS_BREAK, SoundSource.BLOCKS, 0.75F, 0.9F + level.getRandom().nextFloat() * 0.2F);
        return InteractionResult.CONSUME;
    }

    private InteractionResult rummageAndRemoveWithItem(BlockState state, Level level, BlockPos pos, Player player) {
        rummageAndRemove(state, level, pos, player);
        return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
    }

    private void rummage(ServerLevel level, BlockPos pos, BlockState state, Player player, int amountBefore) {
        RandomSource random = level.getRandom();
        Block podBlock = podBlockForState(state);
        if (podBlock != null) {
            giveToPlayerOrDrop(level, pos, player, new ItemStack(podBlock));
        }
        float successChance = Math.min(0.8F, amountBefore * 0.2F);
        if (random.nextFloat() < successChance) {
            ItemStack loot = createLoot(random);
            if (!loot.isEmpty()) {
                giveToPlayerOrDrop(level, pos, player, loot);
            }
        }
    }

    private ItemStack createLoot(RandomSource random) {
        int roll = random.nextInt(100);
        if (roll < 42) {
            return new ItemStack(net.minecraft.world.item.Items.STICK, 1 + random.nextInt(2));
        }
        if (roll < 72) {
            return new ItemStack(ModItems.BAOBAB_FRUIT.get(), 1);
        }
        if (roll < 87) {
            return new ItemStack(ModItems.BAOBAB_FRUIT.get(), 2);
        }
        if (roll < 96) {
            return new ItemStack(ModBlocks.BAOBAB_SAPLING.get(), 1);
        }
        return new ItemStack(net.minecraft.world.item.Items.WHEAT_SEEDS, 1);
    }

    private void harvestOneLayer(Level level, BlockPos pos, BlockState state) {
        int amountBefore = state.getValue(AMOUNT);
        Block.popResource(level, pos, new ItemStack(ModBlocks.BAOBAB_LITTER.get()));
        Block podBlock = podBlockForState(state);

        if (amountBefore <= 1) {
            level.removeBlock(pos, false);
        } else {
            level.setBlock(pos, clearPodState(state).setValue(AMOUNT, amountBefore - 1), 2);
        }

        if (podBlock != null) {
            Block.popResource(level, pos, new ItemStack(podBlock));
        }
    }

    public static BlockState clearPodState(BlockState state) {
        return state.setValue(HAS_SMALL, false)
                .setValue(HAS_MEDIUM, false)
                .setValue(HAS_LARGE, false);
    }

    public static boolean hasAnyPod(BlockState state) {
        return state.getValue(HAS_SMALL) || state.getValue(HAS_MEDIUM) || state.getValue(HAS_LARGE);
    }

    public static Block podBlockForState(BlockState state) {
        if (state.getValue(HAS_LARGE)) {
            return ModBlocks.LARGE_BAOBAB_FRUIT_POD.get();
        }
        if (state.getValue(HAS_MEDIUM)) {
            return ModBlocks.MEDIUM_BAOBAB_FRUIT_POD.get();
        }
        if (state.getValue(HAS_SMALL)) {
            return ModBlocks.SMALL_BAOBAB_FRUIT_POD.get();
        }
        return null;
    }

    private void giveToPlayerOrDrop(Level level, BlockPos pos, Player player, ItemStack stack) {
        ItemStack remainder = stack.copy();
        boolean fullyInserted = player.addItem(remainder);
        if (!fullyInserted || !remainder.isEmpty()) {
            Block.popResource(level, pos, remainder);
        }
    }

    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        for (Player player : level.players()) {
            if (!player.onGround() || !player.blockPosition().equals(pos)) {
                continue;
            }

            double dx = player.getX() - player.xOld;
            double dz = player.getZ() - player.zOld;
            double speedSq = dx * dx + dz * dz;
            if (speedSq <= 0.0003D || random.nextFloat() >= 0.65F) {
                continue;
            }

            spawnFootstepParticles(level, pos, state, random);
            playFootstepSound(level, pos, random);
        }
    }

    private void spawnFootstepParticles(Level level, BlockPos pos, BlockState state, RandomSource random) {
        int count = 1 + random.nextInt(2 + state.getValue(AMOUNT));

        for (int i = 0; i < count; i++) {
            double x = pos.getX() + 0.15D + random.nextDouble() * 0.7D;
            double z = pos.getZ() + 0.15D + random.nextDouble() * 0.7D;
            double y = pos.getY() + 0.03D + random.nextDouble() * 0.08D;
            double angle = random.nextDouble() * Math.PI * 2.0D;
            double speed = 0.015D + random.nextDouble() * 0.012D;
            double vx = Math.cos(angle) * speed;
            double vz = Math.sin(angle) * speed;
            double vy = 0.004D + random.nextDouble() * 0.008D;

            level.addParticle(ModParticles.BAOBAB_LITTER_FLUFF.get(), x, y, z, vx, vy, vz);
        }
    }

    private void playFootstepSound(Level level, BlockPos pos, RandomSource random) {
        level.playLocalSound(
                pos.getX() + 0.5D,
                pos.getY() + 0.5D,
                pos.getZ() + 0.5D,
                SoundEvents.HANGING_ROOTS_STEP,
                SoundSource.BLOCKS,
                0.75F + random.nextFloat() * 0.15F,
                0.9F + random.nextFloat() * 0.25F,
                false
        );
    }
}
