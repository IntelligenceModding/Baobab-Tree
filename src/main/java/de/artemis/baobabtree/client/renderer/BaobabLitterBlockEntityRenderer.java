package de.artemis.baobabtree.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import de.artemis.baobabtree.common.block.BaobabLitterBlock;
import de.artemis.baobabtree.common.block.BaobabHangingPodBlock;
import de.artemis.baobabtree.common.block.entity.BaobabLitterBlockEntity;
import de.artemis.baobabtree.common.registry.ModBlocks;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BaobabLitterBlockEntityRenderer implements BlockEntityRenderer<BaobabLitterBlockEntity> {
    private static final float[][] QUADRANT_BOUNDS = new float[][]{
            {0.5F, 1.0F, 0.5F, 1.0F}, // southeast
            {0.5F, 1.0F, 0.0F, 0.5F}, // northeast
            {0.0F, 0.5F, 0.0F, 0.5F}, // northwest
            {0.0F, 0.5F, 0.5F, 1.0F}  // southwest
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
        Block podBlock = BaobabLitterBlock.podBlockForState(state);
        if (podBlock == null) {
            return;
        }

        RandomSource random = RandomSource.create(blockEntity.getBlockPos().asLong() * 31L + 17L);
        float scale = podScale(podBlock);
        OccupiedArea occupiedArea = occupiedArea(state);
        float halfFootprint = podHalfFootprint(podBlock, scale);
        float xCenter = randomBetween(random, occupiedArea.minX() + halfFootprint, occupiedArea.maxX() - halfFootprint);
        float zCenter = randomBetween(random, occupiedArea.minZ() + halfFootprint, occupiedArea.maxZ() - halfFootprint);
        float yOffset = groundedYOffset(state, podBlock);
        Direction facing = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        BlockState podState = podBlock.defaultBlockState().setValue(BaobabHangingPodBlock.FACING, facing);
        float tiltX = (random.nextFloat() - 0.5F) * 18.0F;
        float tiltZ = (random.nextFloat() - 0.5F) * 18.0F;

        poseStack.pushPose();
        poseStack.translate(xCenter - (scale * 0.5F), yOffset, zCenter - (scale * 0.5F));
        poseStack.scale(scale, scale, scale);
        poseStack.translate(0.5F, 0.0F, 0.5F);
        poseStack.mulPose(Axis.XP.rotationDegrees(tiltX));
        poseStack.mulPose(Axis.ZP.rotationDegrees(tiltZ));
        poseStack.translate(-0.5F, 0.0F, -0.5F);
        blockRenderDispatcher.renderSingleBlock(
                podState,
                poseStack,
                bufferSource,
                packedLight,
                net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY
        );
        poseStack.popPose();
    }

    private float podScale(Block podBlock) {
        if (podBlock == ModBlocks.SMALL_BAOBAB_FRUIT_POD.get()) {
            return 0.72F;
        }
        if (podBlock == ModBlocks.MEDIUM_BAOBAB_FRUIT_POD.get()) {
            return 0.62F;
        }
        return 0.54F;
    }

    private float groundedYOffset(BlockState litterState, Block podBlock) {
        float litterLift = (litterState.getValue(BaobabLitterBlock.AMOUNT) - 1) * 0.003F;
        if (podBlock == ModBlocks.SMALL_BAOBAB_FRUIT_POD.get()) {
            return -0.115F + litterLift;
        }
        if (podBlock == ModBlocks.MEDIUM_BAOBAB_FRUIT_POD.get()) {
            return -0.135F + litterLift;
        }
        return -0.155F + litterLift;
    }

    private float podHalfFootprint(Block podBlock, float scale) {
        float widthFraction;
        if (podBlock == ModBlocks.SMALL_BAOBAB_FRUIT_POD.get()) {
            widthFraction = 4.0F / 16.0F;
        } else if (podBlock == ModBlocks.MEDIUM_BAOBAB_FRUIT_POD.get()) {
            widthFraction = 8.0F / 16.0F;
        } else {
            widthFraction = 12.0F / 16.0F;
        }
        return (scale * widthFraction) * 0.5F + 0.01F;
    }

    private float randomBetween(RandomSource random, float min, float max) {
        if (max <= min) {
            return (min + max) * 0.5F;
        }
        return min + random.nextFloat() * (max - min);
    }

    private OccupiedArea occupiedArea(BlockState state) {
        int amount = state.getValue(BaobabLitterBlock.AMOUNT);
        int[] occupied = new int[amount];
        int facingIndex = state.getValue(BaobabLitterBlock.FACING).get2DDataValue();
        for (int i = 0; i < amount; i++) {
            occupied[i] = Math.floorMod(i - facingIndex, 4);
        }

        float minX = 1.0F;
        float maxX = 0.0F;
        float minZ = 1.0F;
        float maxZ = 0.0F;
        for (int quadrant : occupied) {
            float[] bounds = QUADRANT_BOUNDS[quadrant];
            minX = Math.min(minX, bounds[0]);
            maxX = Math.max(maxX, bounds[1]);
            minZ = Math.min(minZ, bounds[2]);
            maxZ = Math.max(maxZ, bounds[3]);
        }

        return new OccupiedArea(minX, maxX, minZ, maxZ);
    }

    private record OccupiedArea(float minX, float maxX, float minZ, float maxZ) {
    }
}
