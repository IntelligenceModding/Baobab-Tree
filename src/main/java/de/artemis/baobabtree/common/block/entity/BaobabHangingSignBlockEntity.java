package de.artemis.baobabtree.common.block.entity;

import de.artemis.baobabtree.common.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BaobabHangingSignBlockEntity extends SignBlockEntity {
    public BaobabHangingSignBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BAOBAB_HANGING_SIGN.get(), pos, state);
    }
}
