package de.artemis.baobabtree.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import de.artemis.baobabtree.common.block.BaobabHangingPodBlock;
import de.artemis.baobabtree.common.block.BaobabLitterBlock;
import de.artemis.baobabtree.common.block.entity.BaobabLitterBlockEntity;
import de.artemis.baobabtree.common.registry.ModBlocks;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class BaobabLitterBlockEntityRenderer implements BlockEntityRenderer<BaobabLitterBlockEntity, BaobabLitterBlockEntityRenderer.RenderState> {
    private static final float[][] QUADRANT_BOUNDS = new float[][]{
            {0.5F, 1.0F, 0.5F, 1.0F},
            {0.5F, 1.0F, 0.0F, 0.5F},
            {0.0F, 0.5F, 0.0F, 0.5F},
            {0.0F, 0.5F, 0.5F, 1.0F}
    };

    private final BlockModelResolver blockModelResolver;

    public BaobabLitterBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.blockModelResolver = context.blockModelResolver();
    }

    @Override
    public RenderState createRenderState() {
        return new RenderState();
    }

    @Override
    public void extractRenderState(
            BaobabLitterBlockEntity blockEntity,
            RenderState state,
            float partialTicks,
            Vec3 cameraPosition,
            ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress
    ) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);

        BlockState litterState = blockEntity.getBlockState();
        Block podBlock = BaobabLitterBlock.podBlockForState(litterState);
        state.visible = podBlock != null;
        state.podModel.clear();
        if (podBlock == null) {
            return;
        }

        RandomSource random = RandomSource.create(blockEntity.getBlockPos().asLong() * 31L + 17L);
        float scale = podScale(podBlock);
        OccupiedArea occupiedArea = occupiedArea(litterState);
        float halfFootprint = podHalfFootprint(podBlock, scale);

        state.xOffset = randomBetween(random, occupiedArea.minX() + halfFootprint, occupiedArea.maxX() - halfFootprint) - (scale * 0.5F);
        state.zOffset = randomBetween(random, occupiedArea.minZ() + halfFootprint, occupiedArea.maxZ() - halfFootprint) - (scale * 0.5F);
        state.yOffset = groundedYOffset(litterState, podBlock);
        state.scale = scale;
        state.tiltX = (random.nextFloat() - 0.5F) * 18.0F;
        state.tiltZ = (random.nextFloat() - 0.5F) * 18.0F;

        BlockState podState = podBlock.defaultBlockState().setValue(
                BaobabHangingPodBlock.FACING,
                Direction.Plane.HORIZONTAL.getRandomDirection(random)
        );
        this.blockModelResolver.update(state.podModel, podState, BlockDisplayContext.create());
    }

    @Override
    public void submit(RenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (!state.visible || state.podModel.isEmpty()) {
            return;
        }

        poseStack.pushPose();
        poseStack.translate(state.xOffset, state.yOffset, state.zOffset);
        poseStack.scale(state.scale, state.scale, state.scale);
        poseStack.translate(0.5F, 0.0F, 0.5F);
        poseStack.mulPose(Axis.XP.rotationDegrees(state.tiltX));
        poseStack.mulPose(Axis.ZP.rotationDegrees(state.tiltZ));
        poseStack.translate(-0.5F, 0.0F, -0.5F);
        state.podModel.submitMultiLayer(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }

    private static float podScale(Block podBlock) {
        if (podBlock == ModBlocks.SMALL_BAOBAB_FRUIT_POD.get()) {
            return 0.72F;
        }
        if (podBlock == ModBlocks.MEDIUM_BAOBAB_FRUIT_POD.get()) {
            return 0.62F;
        }
        return 0.54F;
    }

    private static float groundedYOffset(BlockState litterState, Block podBlock) {
        float litterLift = (litterState.getValue(BaobabLitterBlock.AMOUNT) - 1) * 0.003F;
        if (podBlock == ModBlocks.SMALL_BAOBAB_FRUIT_POD.get()) {
            return -0.115F + litterLift;
        }
        if (podBlock == ModBlocks.MEDIUM_BAOBAB_FRUIT_POD.get()) {
            return -0.135F + litterLift;
        }
        return -0.155F + litterLift;
    }

    private static float podHalfFootprint(Block podBlock, float scale) {
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

    private static float randomBetween(RandomSource random, float min, float max) {
        if (max <= min) {
            return (min + max) * 0.5F;
        }
        return min + random.nextFloat() * (max - min);
    }

    private static OccupiedArea occupiedArea(BlockState state) {
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

    public static class RenderState extends BlockEntityRenderState {
        public final BlockModelRenderState podModel = new BlockModelRenderState();
        public boolean visible;
        public float xOffset;
        public float yOffset;
        public float zOffset;
        public float scale;
        public float tiltX;
        public float tiltZ;
    }
}
