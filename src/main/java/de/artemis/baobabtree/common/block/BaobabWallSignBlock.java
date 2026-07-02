package de.artemis.baobabtree.common.block;

import com.mojang.serialization.MapCodec;
import de.artemis.baobabtree.common.block.entity.BaobabSignBlockEntity;
import de.artemis.baobabtree.common.registry.ModWoodTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unchecked")
public class BaobabWallSignBlock extends WallSignBlock {
    public static final MapCodec<WallSignBlock> CODEC =
            (MapCodec<WallSignBlock>) (MapCodec<?>) simpleCodec(BaobabWallSignBlock::new);

    public BaobabWallSignBlock(BlockBehaviour.Properties properties) {
        super(ModWoodTypes.BAOBAB_WOOD_TYPE, properties);
    }

    @Override
    public @NotNull MapCodec<WallSignBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new BaobabSignBlockEntity(pos, state);
    }
}
