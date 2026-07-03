package de.artemis.baobabtree.common.item;

import de.artemis.baobabtree.common.block.BaobabFruitPodBlock;
import de.artemis.baobabtree.common.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class BaobabFruitItem extends Item {
    public BaobabFruitItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        BlockState clickedState = level.getBlockState(clickedPos);

        if (!clickedState.is(ModBlocks.BAOBAB_LEAVES.get()) || context.getClickedFace() != Direction.DOWN) {
            return InteractionResult.PASS;
        }

        BlockPos podPos = clickedPos.below();
        if (!level.getBlockState(podPos).canBeReplaced()) {
            return InteractionResult.PASS;
        }

        BlockState podState = ModBlocks.BAOBAB_FRUIT_POD.get().defaultBlockState()
                .setValue(BaobabFruitPodBlock.FACING, context.getHorizontalDirection().getOpposite());

        if (!podState.canSurvive(level, podPos) || !level.setBlock(podPos, podState, Block.UPDATE_ALL)) {
            return InteractionResult.PASS;
        }

        ItemStack stack = context.getItemInHand();
        Player player = context.getPlayer();
        if (player == null || !player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        SoundType soundType = podState.getSoundType(level, podPos, player);
        level.playSound(player, podPos, soundType.getPlaceSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
