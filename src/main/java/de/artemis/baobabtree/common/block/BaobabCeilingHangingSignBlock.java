package de.artemis.baobabtree.common.block;

import com.mojang.serialization.MapCodec;
import de.artemis.baobabtree.common.block.entity.BaobabHangingSignBlockEntity;
import de.artemis.baobabtree.common.registry.ModWoodTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unchecked")
public class BaobabCeilingHangingSignBlock extends CeilingHangingSignBlock {
    public static final MapCodec<CeilingHangingSignBlock> CODEC =
            (MapCodec<CeilingHangingSignBlock>) (MapCodec<?>) simpleCodec(BaobabCeilingHangingSignBlock::new);

    public BaobabCeilingHangingSignBlock(BlockBehaviour.Properties properties) {
        super(ModWoodTypes.BAOBAB_WOOD_TYPE, properties);
    }

    @Override
    public @NotNull MapCodec<CeilingHangingSignBlock> codec() {
        return CODEC;
    }

    @Override
    public @NotNull BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new BaobabHangingSignBlockEntity(pos, state);
    }
}
