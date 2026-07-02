package de.artemis.baobabtree.common.block;

import com.mojang.serialization.MapCodec;
import de.artemis.baobabtree.common.block.entity.BaobabHangingSignBlockEntity;
import de.artemis.baobabtree.common.registry.ModWoodTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unchecked")
public class BaobabWallHangingSignBlock extends WallHangingSignBlock {
    public static final MapCodec<WallHangingSignBlock> CODEC =
            (MapCodec<WallHangingSignBlock>) (MapCodec<?>) simpleCodec(BaobabWallHangingSignBlock::new);

    public BaobabWallHangingSignBlock(BlockBehaviour.Properties properties) {
        super(ModWoodTypes.BAOBAB_WOOD_TYPE, properties);
    }

    @Override
    public @NotNull MapCodec<WallHangingSignBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new BaobabHangingSignBlockEntity(pos, state);
    }
}
