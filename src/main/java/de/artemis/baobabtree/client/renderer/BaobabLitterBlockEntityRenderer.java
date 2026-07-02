package de.artemis.baobabtree.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import de.artemis.baobabtree.common.block.BaobabLitterBlock;
import de.artemis.baobabtree.common.block.entity.BaobabLitterBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.RandomSource;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class BaobabLitterBlockEntityRenderer implements BlockEntityRenderer<BaobabLitterBlockEntity> {
    private static final float[][] QUADRANT_CENTERS = new float[][]{
            {0.75F, 0.75F}, // southeast
            {0.75F, 0.25F}, // northeast
            {0.25F, 0.25F}, // northwest
            {0.25F, 0.75F}  // southwest
    };

    private final BlockRenderDispatcher blockRenderDispatcher;

    public BaobabLitterBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderDispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(BaobabLitterBlockEntity blockEntity,
                       float partialTick,
                       PoseStack poseStack,
                       MultiBufferSource bufferSource,
                       int packedLight,
                       int packedOverlay) {
        BlockState state = blockEntity.getBlockState();
        if (!state.hasProperty(BaobabLitterBlock.HAS_FRUIT) || !state.getValue(BaobabLitterBlock.HAS_FRUIT)) {
            return;
        }

        RandomSource random = RandomSource.create(blockEntity.getBlockPos().asLong() * 31L + 17L);
        int quadrantIndex = occupiedQuadrantIndex(state, random);
        float xOffset = QUADRANT_CENTERS[quadrantIndex][0] + (random.nextFloat() - 0.5F) * 0.08F;
        float zOffset = QUADRANT_CENTERS[quadrantIndex][1] + (random.nextFloat() - 0.5F) * 0.08F;
        float yOffset = -0.07F + (state.getValue(BaobabLitterBlock.AMOUNT) - 1) * 0.004F;
        Direction facing = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        BlockState cocoaState = Blocks.COCOA.defaultBlockState()
                .setValue(CocoaBlock.AGE, 2)
                .setValue(CocoaBlock.FACING, facing);

        poseStack.pushPose();
        poseStack.translate(xOffset, yOffset, zOffset);
        poseStack.mulPose(Axis.YP.rotationDegrees(random.nextFloat() * 360.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F + (random.nextFloat() - 0.5F) * 28.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees((random.nextFloat() - 0.5F) * 18.0F));
        poseStack.scale(0.42F, 0.42F, 0.42F);
        blockRenderDispatcher.renderSingleBlock(
                cocoaState,
                poseStack,
                bufferSource,
                packedLight,
                net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY
        );
        poseStack.popPose();
    }

    private int occupiedQuadrantIndex(BlockState state, RandomSource random) {
        int amount = state.getValue(BaobabLitterBlock.AMOUNT);
        int[] occupied = new int[amount];
        int facingIndex = state.getValue(BaobabLitterBlock.FACING).get2DDataValue();
        for (int i = 0; i < amount; i++) {
            occupied[i] = Math.floorMod(i - facingIndex, 4);
        }
        return occupied[random.nextInt(amount)];
    }
}
