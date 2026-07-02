package de.artemis.baobabtree.common.block.entity;

import de.artemis.baobabtree.common.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BaobabLitterBlockEntity extends BlockEntity {
    public BaobabLitterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BAOBAB_LITTER.get(), pos, state);
    }
}
