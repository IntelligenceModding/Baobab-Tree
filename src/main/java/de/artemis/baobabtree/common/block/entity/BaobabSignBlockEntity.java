package de.artemis.baobabtree.common.block.entity;

import de.artemis.baobabtree.common.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BaobabSignBlockEntity extends SignBlockEntity {
    public BaobabSignBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BAOBAB_SIGN.get(), pos, state);
    }
}
