package de.artemis.baobabtree.common.block;

import de.artemis.baobabtree.common.registry.ModTreeGrowers;
import de.artemis.baobabtree.common.worldgen.feature.BaobabTreeGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BaobabSaplingBlock extends SaplingBlock {
    public BaobabSaplingBlock(BlockBehaviour.Properties properties) {
        this(ModTreeGrowers.BAOBAB, properties);
    }

    public BaobabSaplingBlock(TreeGrower treeGrower, BlockBehaviour.Properties properties) {
        super(treeGrower, properties);
    }

    @Override
    public void advanceTree(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        if (state.getValue(STAGE) == 0) {
            level.setBlock(pos, state.cycle(STAGE), 4);
            return;
        }

        BlockPos anchor = findTwoByTwoAnchor(level, pos);
        if (anchor == null) {
            return;
        }

        BlockPos east = anchor.east();
        BlockPos south = anchor.south();
        BlockPos southEast = south.east();

        BlockState anchorState = level.getBlockState(anchor);
        BlockState eastState = level.getBlockState(east);
        BlockState southState = level.getBlockState(south);
        BlockState southEastState = level.getBlockState(southEast);

        level.removeBlock(anchor, false);
        level.removeBlock(east, false);
        level.removeBlock(south, false);
        level.removeBlock(southEast, false);

        BlockPos treeOrigin = southEast;
        if (BaobabTreeGenerator.generateSapling(level, treeOrigin, random)) {
            return;
        }

        level.setBlock(anchor, anchorState, 4);
        level.setBlock(east, eastState, 4);
        level.setBlock(south, southState, 4);
        level.setBlock(southEast, southEastState, 4);
    }

    @Nullable
    private BlockPos findTwoByTwoAnchor(BlockGetter level, BlockPos pos) {
        for (int xOffset = -1; xOffset <= 0; xOffset++) {
            for (int zOffset = -1; zOffset <= 0; zOffset++) {
                BlockPos anchor = pos.offset(xOffset, 0, zOffset);
                if (isSameSapling(level, anchor)
                        && isSameSapling(level, anchor.east())
                        && isSameSapling(level, anchor.south())
                        && isSameSapling(level, anchor.south().east())) {
                    return anchor;
                }
            }
        }
        return null;
    }

    private boolean isSameSapling(BlockGetter level, BlockPos pos) {
        return level.getBlockState(pos).is(this);
    }
}
