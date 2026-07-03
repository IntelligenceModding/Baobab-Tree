package de.artemis.baobabtree.common.worldgen.feature;

import de.artemis.baobabtree.common.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.PinkPetalsBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class BaobabTreeGenerator {
    public enum Variant {
        YOUNG(11, 12, 2.2F, 1.9F, 1.35F, 5, 6, 5, 6, 1.0F, 6, 8, 3, 4, 0, 6, 2),
        MATURE(17, 18, 3.4F, 2.9F, 2.0F, 6, 7, 6, 7, 1.0F, 10, 12, 4, 5, 0, 10, 3),
        ANCIENT(22, 24, 4.3F, 3.6F, 2.5F, 6, 7, 8, 9, 1.05F, 12, 15, 4, 5, 0, 13, 4),
        FALLEN(8, 10, 3.2F, 2.7F, 1.9F, 3, 4, 4, 5, 1.0F, 5, 7, 3, 4, 0, 12, 3);

        final int minHeight;
        final int maxHeight;
        final float baseRadius;
        final float bodyRadius;
        final float neckRadius;
        final int minBranches;
        final int maxBranches;
        final int minBranchLength;
        final int maxBranchLength;
        final float branchThickness;
        final int minLeafClusters;
        final int maxLeafClusters;
        final int minLeafRadius;
        final int maxLeafRadius;
        final int maxDrift;
        final int clearanceRadius;
        final int maxSlope;

        Variant(int minHeight,
                int maxHeight,
                float baseRadius,
                float bodyRadius,
                float neckRadius,
                int minBranches,
                int maxBranches,
                int minBranchLength,
                int maxBranchLength,
                float branchThickness,
                int minLeafClusters,
                int maxLeafClusters,
                int minLeafRadius,
                int maxLeafRadius,
                int maxDrift,
                int clearanceRadius,
                int maxSlope) {
            this.minHeight = minHeight;
            this.maxHeight = maxHeight;
            this.baseRadius = baseRadius;
            this.bodyRadius = bodyRadius;
            this.neckRadius = neckRadius;
            this.minBranches = minBranches;
            this.maxBranches = maxBranches;
            this.minBranchLength = minBranchLength;
            this.maxBranchLength = maxBranchLength;
            this.branchThickness = branchThickness;
            this.minLeafClusters = minLeafClusters;
            this.maxLeafClusters = maxLeafClusters;
            this.minLeafRadius = minLeafRadius;
            this.maxLeafRadius = maxLeafRadius;
            this.maxDrift = maxDrift;
            this.clearanceRadius = clearanceRadius;
            this.maxSlope = maxSlope;
        }
    }

    private BaobabTreeGenerator() {
    }

    public static boolean generate(WorldGenLevel level, BlockPos origin, RandomSource random, Variant variant) {
        return generate(level, origin, random, variant, true);
    }

    public static boolean generateSapling(WorldGenLevel level, BlockPos origin, RandomSource random) {
        return generate(level, origin, random, Variant.YOUNG, false);
    }

    private static boolean generate(WorldGenLevel level, BlockPos origin, RandomSource random, Variant variant, boolean withGroundDecoration) {
        if (origin.getY() <= level.getMinBuildHeight() + 1) {
            return false;
        }

        if (variant == Variant.FALLEN) {
            return generateFallen(level, origin, random, withGroundDecoration);
        }

        int trunkHeight = Mth.nextInt(random, variant.minHeight, variant.maxHeight);
        if (!isValidGround(level, origin, variant) || !hasClearance(level, origin, trunkHeight, variant)) {
            return false;
        }

        GenerationState state = new GenerationState(level, random, origin, variant, trunkHeight, withGroundDecoration);
        if (withGroundDecoration) {
            state.placeGroundPatch();
            state.placeRootSystem();
        }
        state.placeTrunk();
        state.anchorTrunkBase();
        state.carveAncientHollow();
        state.placeBranches();
        state.placeLeafClusters();
        state.finalizeLeaves();
        if (withGroundDecoration) {
            state.placeFruitPods();
        }
        if (withGroundDecoration) {
            state.placeGroundCover();
        }
        return true;
    }

    private static boolean isValidGround(WorldGenLevel level, BlockPos origin, Variant variant) {
        int sampleRadius = Mth.ceil(variant.baseRadius) + 2;
        int minSurface = Integer.MAX_VALUE;
        int maxSurface = Integer.MIN_VALUE;
        int centerSurfaceY = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, origin.getX(), origin.getZ()) - 1;

        if (centerSurfaceY != origin.getY() - 1) {
            return false;
        }

        for (int dx = -sampleRadius; dx <= sampleRadius; dx++) {
            for (int dz = -sampleRadius; dz <= sampleRadius; dz++) {
                if (dx * dx + dz * dz > (sampleRadius + 0.5F) * (sampleRadius + 0.5F)) {
                    continue;
                }

                int surfaceY = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, origin.getX() + dx, origin.getZ() + dz) - 1;
                BlockPos surfacePos = new BlockPos(origin.getX() + dx, surfaceY, origin.getZ() + dz);
                BlockState groundState = level.getBlockState(surfacePos);
                if (!isAllowedSoil(groundState) || !level.getFluidState(surfacePos).isEmpty()) {
                    return false;
                }

                minSurface = Math.min(minSurface, surfaceY);
                maxSurface = Math.max(maxSurface, surfaceY);
            }
        }

        return maxSurface - minSurface <= variant.maxSlope;
    }

    private static boolean hasClearance(WorldGenLevel level, BlockPos origin, int trunkHeight, Variant variant) {
        for (int dx = -variant.clearanceRadius; dx <= variant.clearanceRadius; dx++) {
            for (int dz = -variant.clearanceRadius; dz <= variant.clearanceRadius; dz++) {
                if (dx * dx + dz * dz > variant.clearanceRadius * variant.clearanceRadius + 6) {
                    continue;
                }

                for (int y = 0; y <= trunkHeight + variant.maxLeafRadius + 6; y++) {
                    BlockPos pos = origin.offset(dx, y, dz);
                    BlockState state = level.getBlockState(pos);
                    if (!canReplace(state) && !state.is(BlockTags.LOGS) && !state.is(BlockTags.LEAVES)) {
                        return false;
                    }
                }
            }
        }

        return origin.getY() + trunkHeight + variant.maxLeafRadius + 6 < level.getMaxBuildHeight();
    }

    private static boolean generateFallen(WorldGenLevel level, BlockPos origin, RandomSource random, boolean withGroundDecoration) {
        if (!isValidGround(level, origin, Variant.FALLEN)) {
            return false;
        }

        Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        int stumpHeight = Mth.nextInt(random, 5, 7);
        int trunkLength = Mth.nextInt(random, 10, 13);
        float stumpBaseRadius = 3.0F + (random.nextFloat() - 0.5F) * 0.2F;
        float stumpTopRadius = 2.15F + (random.nextFloat() - 0.5F) * 0.15F;
        float trunkBaseRadius = 2.55F + (random.nextFloat() - 0.5F) * 0.15F;
        float trunkTipRadius = 1.55F + (random.nextFloat() - 0.5F) * 0.1F;

        if (!hasFallenClearance(level, origin, direction, trunkLength, stumpHeight, stumpBaseRadius, trunkBaseRadius)) {
            return false;
        }

        Set<BlockPos> logPositions = new HashSet<>();
        Set<BlockPos> leafPositions = new HashSet<>();
        List<LeafPadSeed> leafPadSeeds = new ArrayList<>();
        List<BlockPos> trunkCenters = new ArrayList<>();

        if (withGroundDecoration) {
            placeFallenGroundBlend(level, random, origin, direction, trunkLength, stumpBaseRadius, trunkBaseRadius);
        }

        for (int y = 0; y < stumpHeight; y++) {
            float t = y / (float) Math.max(1, stumpHeight - 1);
            float radius = Mth.lerp(t, stumpBaseRadius, stumpTopRadius);
            placeVerticalCircularLayer(level, origin, origin.getY() + y, radius, logPositions);
        }

        for (int y = 1; y < stumpHeight; y++) {
            float t = y / (float) Math.max(1, stumpHeight - 1);
            float outerRadius = Mth.lerp(t, stumpBaseRadius, stumpTopRadius);
            float hollowRadius = Math.max(0.95F, outerRadius - 1.15F);
            carveVerticalCircularLayer(level, origin, origin.getY() + y, hollowRadius, logPositions);
        }

        for (int step = 0; step < trunkLength; step++) {
            float t = step / (float) Math.max(1, trunkLength - 1);
            float radius = Mth.lerp(t, trunkBaseRadius, trunkTipRadius) + Mth.sin(t * Mth.PI) * 0.12F;
            int x = origin.getX() + direction.getStepX() * (step + 1);
            int z = origin.getZ() + direction.getStepZ() * (step + 1);
            int surfaceY = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, x, z) - 1;
            int centerY = surfaceY + Mth.floor(radius);
            if (step == 0) {
                centerY = Math.max(centerY, origin.getY() + 2);
            } else if (step == 1) {
                centerY = Math.max(centerY, origin.getY() + 1);
            }

            BlockPos center = new BlockPos(x, centerY, z);
            trunkCenters.add(center);
            placeHorizontalCircularLayer(level, center, direction.getAxis(), radius, logPositions);
            carveHorizontalCircularLayer(level, center, direction.getAxis(), Math.max(0.95F, radius - 1.1F), logPositions);
        }

        anchorFallenWood(level, logPositions, 4);
        placeFallenCanopy(level, random, direction, trunkCenters, leafPadSeeds, logPositions, leafPositions);
        finalizeGeneratedLeaves(level, logPositions, leafPositions);

        if (withGroundDecoration) {
            placeFallenFruitPods(level, random, origin, leafPositions);
            placeFallenGroundCover(level, random, origin, direction, trunkCenters, stumpBaseRadius);
        }

        return true;
    }

    private static boolean hasFallenClearance(WorldGenLevel level,
                                              BlockPos origin,
                                              Direction direction,
                                              int trunkLength,
                                              int stumpHeight,
                                              float stumpRadius,
                                              float trunkRadius) {
        int stumpExtent = Mth.ceil(stumpRadius) + 3;
        for (int dx = -stumpExtent; dx <= stumpExtent; dx++) {
            for (int dz = -stumpExtent; dz <= stumpExtent; dz++) {
                for (int y = 0; y <= stumpHeight + 5; y++) {
                    BlockPos pos = origin.offset(dx, y, dz);
                    BlockState state = level.getBlockState(pos);
                    if (!canReplace(state) && !state.is(BlockTags.LOGS) && !state.is(BlockTags.LEAVES)) {
                        return false;
                    }
                }
            }
        }

        int lateralExtent = Mth.ceil(trunkRadius) + 3;
        for (int step = 0; step < trunkLength; step++) {
            int x = origin.getX() + direction.getStepX() * (step + 1);
            int z = origin.getZ() + direction.getStepZ() * (step + 1);
            int surfaceY = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, x, z) - 1;
            if (Math.abs(surfaceY - (origin.getY() - 1)) > Variant.FALLEN.maxSlope) {
                return false;
            }

            int centerY = surfaceY + Mth.floor(trunkRadius);
            for (int lateral = -lateralExtent; lateral <= lateralExtent; lateral++) {
                for (int dy = -1; dy <= lateralExtent + 5; dy++) {
                    BlockPos pos = direction.getAxis() == Direction.Axis.X
                            ? new BlockPos(x, centerY + dy, z + lateral)
                            : new BlockPos(x + lateral, centerY + dy, z);
                    BlockState state = level.getBlockState(pos);
                    if (!canReplace(state) && !state.is(BlockTags.LOGS) && !state.is(BlockTags.LEAVES)) {
                        return false;
                    }
                }
            }
        }

        return origin.getY() + stumpHeight + 8 < level.getMaxBuildHeight();
    }

    private static boolean isAllowedSoil(BlockState state) {
        return state.is(BlockTags.DIRT)
                || state.is(Blocks.GRASS_BLOCK)
                || state.is(Blocks.COARSE_DIRT)
                || state.is(Blocks.SAND)
                || state.is(Blocks.RED_SAND)
                || state.is(Blocks.TERRACOTTA)
                || state.is(Blocks.ORANGE_TERRACOTTA);
    }

    private static boolean canReplace(BlockState state) {
        return state.isAir()
                || state.canBeReplaced()
                || state.is(BlockTags.LEAVES)
                || state.is(Blocks.VINE)
                || state.is(ModBlocks.BAOBAB_LITTER.get());
    }

    private static void placeFallenGroundBlend(WorldGenLevel level,
                                               RandomSource random,
                                               BlockPos origin,
                                               Direction direction,
                                               int trunkLength,
                                               float stumpRadius,
                                               float trunkRadius) {
        int stumpExtent = Mth.ceil(stumpRadius) + 2;
        for (int dx = -stumpExtent; dx <= stumpExtent; dx++) {
            for (int dz = -stumpExtent; dz <= stumpExtent; dz++) {
                double distance = Math.sqrt(dx * dx + dz * dz);
                if (distance > stumpRadius + 1.2D) {
                    continue;
                }

                BlockPos soilPos = surfacePosAt(level, origin.getX() + dx, origin.getZ() + dz);
                BlockState state = level.getBlockState(soilPos);
                if (state.is(BlockTags.DIRT) || state.is(Blocks.GRASS_BLOCK)) {
                    setGeneratedBlock(level, soilPos, random.nextFloat() < 0.6F ? Blocks.COARSE_DIRT.defaultBlockState() : Blocks.DIRT.defaultBlockState());
                }
                if (distance > stumpRadius - 0.4D && random.nextFloat() < 0.45F) {
                    Direction.Axis axis = Math.abs(dx) >= Math.abs(dz) ? Direction.Axis.X : Direction.Axis.Z;
                    tryPlaceSoilBlock(level, soilPos, ModBlocks.TREE_ROOT.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, axis));
                } else if (random.nextFloat() < 0.35F) {
                    tryPlaceSoilBlock(level, soilPos.below(), Blocks.ROOTED_DIRT.defaultBlockState());
                }
            }
        }

        int width = Mth.ceil(trunkRadius) + 1;
        for (int step = 0; step < trunkLength; step++) {
            int x = origin.getX() + direction.getStepX() * (step + 1);
            int z = origin.getZ() + direction.getStepZ() * (step + 1);
            for (int lateral = -width; lateral <= width; lateral++) {
                int sampleX = direction.getAxis() == Direction.Axis.X ? x : x + lateral;
                int sampleZ = direction.getAxis() == Direction.Axis.X ? z + lateral : z;
                BlockPos soilPos = surfacePosAt(level, sampleX, sampleZ);
                BlockState state = level.getBlockState(soilPos);
                if (state.is(BlockTags.DIRT) || state.is(Blocks.GRASS_BLOCK)) {
                    float coarseChance = step < trunkLength / 3 ? 0.45F : 0.28F;
                    setGeneratedBlock(level, soilPos, random.nextFloat() < coarseChance ? Blocks.COARSE_DIRT.defaultBlockState() : Blocks.DIRT.defaultBlockState());
                }
                if (random.nextFloat() < 0.22F) {
                    tryPlaceSoilBlock(level, soilPos.below(), Blocks.ROOTED_DIRT.defaultBlockState());
                }
            }
        }
    }

    private static void placeVerticalCircularLayer(WorldGenLevel level, BlockPos origin, int worldY, float radius, Set<BlockPos> logPositions) {
        int extent = Mth.ceil(radius) + 1;
        for (int dx = -extent; dx <= extent; dx++) {
            for (int dz = -extent; dz <= extent; dz++) {
                if (Math.sqrt(dx * dx + dz * dz) <= radius + 0.08D) {
                    placeWood(level, origin.offset(dx, worldY - origin.getY(), dz), logPositions);
                }
            }
        }
    }

    private static void carveVerticalCircularLayer(WorldGenLevel level, BlockPos origin, int worldY, float radius, Set<BlockPos> logPositions) {
        int extent = Mth.ceil(radius);
        for (int dx = -extent; dx <= extent; dx++) {
            for (int dz = -extent; dz <= extent; dz++) {
                if (Math.sqrt(dx * dx + dz * dz) > radius) {
                    continue;
                }
                BlockPos pos = origin.offset(dx, worldY - origin.getY(), dz);
                if (level.getBlockState(pos).is(ModBlocks.BAOBAB_WOOD.get())) {
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                    logPositions.remove(pos);
                }
            }
        }
    }

    private static void placeHorizontalCircularLayer(WorldGenLevel level,
                                                     BlockPos center,
                                                     Direction.Axis axis,
                                                     float radius,
                                                     Set<BlockPos> logPositions) {
        int extent = Mth.ceil(radius) + 1;
        for (int lateral = -extent; lateral <= extent; lateral++) {
            for (int dy = -extent; dy <= extent; dy++) {
                if (Math.sqrt(lateral * lateral + dy * dy) > radius + 0.08D) {
                    continue;
                }
                BlockPos pos = axis == Direction.Axis.X
                        ? center.offset(0, dy, lateral)
                        : center.offset(lateral, dy, 0);
                placeWood(level, pos, logPositions);
            }
        }
    }

    private static void carveHorizontalCircularLayer(WorldGenLevel level,
                                                     BlockPos center,
                                                     Direction.Axis axis,
                                                     float radius,
                                                     Set<BlockPos> logPositions) {
        int extent = Mth.ceil(radius);
        for (int lateral = -extent; lateral <= extent; lateral++) {
            for (int dy = -extent; dy <= extent; dy++) {
                if (Math.sqrt(lateral * lateral + dy * dy) > radius) {
                    continue;
                }
                BlockPos pos = axis == Direction.Axis.X
                        ? center.offset(0, dy, lateral)
                        : center.offset(lateral, dy, 0);
                if (level.getBlockState(pos).is(ModBlocks.BAOBAB_WOOD.get())) {
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                    logPositions.remove(pos);
                }
            }
        }
    }

    private static void anchorFallenWood(WorldGenLevel level, Set<BlockPos> logPositions, int maxDepth) {
        List<BlockPos> anchors = new ArrayList<>(logPositions);
        anchors.sort(Comparator.comparingInt(BlockPos::getY));
        for (BlockPos pos : anchors) {
            int surfaceY = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, pos.getX(), pos.getZ()) - 1;
            if (pos.getY() > surfaceY + 2) {
                continue;
            }
            for (int depth = 1; depth <= maxDepth; depth++) {
                BlockPos fillPos = pos.below(depth);
                BlockState state = level.getBlockState(fillPos);
                if (supportsFoundation(state)) {
                    break;
                }
                if (!canReplace(state) && !state.is(BlockTags.LEAVES)) {
                    break;
                }
                setGeneratedBlock(level, fillPos, ModBlocks.BAOBAB_WOOD.get().defaultBlockState());
                logPositions.add(fillPos.immutable());
            }
        }
    }

    private static void placeFallenCanopy(WorldGenLevel level,
                                          RandomSource random,
                                          Direction direction,
                                          List<BlockPos> trunkCenters,
                                          List<LeafPadSeed> leafPadSeeds,
                                          Set<BlockPos> logPositions,
                                          Set<BlockPos> leafPositions) {
        if (trunkCenters.isEmpty()) {
            return;
        }

        double trunkAngle = Math.atan2(direction.getStepZ(), direction.getStepX());
        int branchCount = 3 + random.nextInt(2);
        for (int i = 0; i < branchCount; i++) {
            int anchorIndex = Mth.clamp(Mth.floor((0.45F + random.nextFloat() * 0.35F) * (trunkCenters.size() - 1)), 0, trunkCenters.size() - 1);
            BlockPos baseCenter = trunkCenters.get(anchorIndex);
            Direction side = random.nextBoolean() ? direction.getClockWise() : direction.getCounterClockWise();
            BlockPos anchor = baseCenter.relative(side).above(2 + random.nextInt(2));
            double angle = trunkAngle + (side == direction.getClockWise() ? Math.PI / 2.0D : -Math.PI / 2.0D) + (random.nextDouble() - 0.5D) * 0.45D;
            List<BlockPos> branch = placeFallenBranch(level, random, anchor, angle, 4 + random.nextInt(3), logPositions);
            if (!branch.isEmpty()) {
                leafPadSeeds.add(new LeafPadSeed(branch.get(branch.size() - 1), angle, 3 + random.nextInt(2)));
            }
        }

        BlockPos tipCenter = trunkCenters.get(trunkCenters.size() - 1).above(2);
        leafPadSeeds.add(new LeafPadSeed(tipCenter, trunkAngle + (random.nextDouble() - 0.5D) * 0.35D, 4));
        leafPadSeeds.add(new LeafPadSeed(trunkCenters.get(Math.max(0, trunkCenters.size() / 2)).above(3), trunkAngle, 3));

        for (LeafPadSeed seed : leafPadSeeds) {
            placeFallenLeafCluster(level, random, seed, leafPositions, logPositions);
        }
    }

    private static List<BlockPos> placeFallenBranch(WorldGenLevel level,
                                                    RandomSource random,
                                                    BlockPos start,
                                                    double angle,
                                                    int length,
                                                    Set<BlockPos> logPositions) {
        Vec3iLike p0 = new Vec3iLike(start.getX(), start.getY(), start.getZ());
        Vec3iLike p1 = new Vec3iLike(
                start.getX() + Mth.floor(Math.cos(angle) * Math.max(1.5D, length * 0.25D)),
                start.getY() + 1,
                start.getZ() + Mth.floor(Math.sin(angle) * Math.max(1.5D, length * 0.25D))
        );
        Vec3iLike p2 = new Vec3iLike(
                start.getX() + Mth.floor(Math.cos(angle) * (length * 0.6D)),
                start.getY() + 2 + random.nextInt(2),
                start.getZ() + Mth.floor(Math.sin(angle) * (length * 0.6D))
        );
        Vec3iLike p3 = new Vec3iLike(
                start.getX() + Mth.floor(Math.cos(angle) * length),
                start.getY() + 2 + random.nextInt(2),
                start.getZ() + Mth.floor(Math.sin(angle) * length)
        );

        List<BlockPos> samples = new ArrayList<>();
        BlockPos previous = null;
        int steps = Math.max(8, length * 4);
        for (int step = 0; step <= steps; step++) {
            float t = step / (float) steps;
            Vec3Like point = bezier(p0, p1, p2, p3, t);
            BlockPos current = BlockPos.containing(point.x(), point.y(), point.z());
            if (previous != null && previous.equals(current)) {
                continue;
            }
            placeWood(level, current, logPositions);
            samples.add(current.immutable());
            previous = current;
        }

        return samples;
    }

    private static void placeFallenLeafCluster(WorldGenLevel level,
                                               RandomSource random,
                                               LeafPadSeed seed,
                                               Set<BlockPos> leafPositions,
                                               Set<BlockPos> logPositions) {
        int radius = Math.max(3, seed.radius());
        BlockPos center = seed.tip().offset(
                Mth.floor(Math.cos(seed.angle()) * Math.max(1, radius - 2)),
                0,
                Mth.floor(Math.sin(seed.angle()) * Math.max(1, radius - 2))
        );
        placeWood(level, center.below(), logPositions);
        placeLeafTransition(level, seed.tip(), seed.angle(), leafPositions);
        placeLeafPad(level, random, center, radius, 1, leafPositions);
        if (random.nextFloat() < 0.65F) {
            double sideAngle = seed.angle() + (random.nextBoolean() ? 0.75D : -0.75D);
            BlockPos linked = center.offset(
                    Mth.floor(Math.cos(sideAngle) * Math.max(1, radius - 2)),
                    0,
                    Mth.floor(Math.sin(sideAngle) * Math.max(1, radius - 2))
            );
            placeWood(level, linked.below(), logPositions);
            placeLeafPad(level, random, linked, Math.max(2, radius - 1), 1, leafPositions);
        }
    }

    private static void placeLeafTransition(WorldGenLevel level, BlockPos tip, double angle, Set<BlockPos> leafPositions) {
        placeLeaf(level, tip.above(), leafPositions);
        placeLeaf(level, tip.relative(sideDirection(angle, true)), leafPositions);
        placeLeaf(level, tip.relative(sideDirection(angle, false)), leafPositions);
        placeLeaf(level, tip.offset(Mth.floor(Math.cos(angle)), 0, Mth.floor(Math.sin(angle))), leafPositions);
    }

    private static Direction sideDirection(double angle, boolean positive) {
        double sideAngle = angle + (positive ? Math.PI / 2.0D : -Math.PI / 2.0D);
        return Math.abs(Math.cos(sideAngle)) > Math.abs(Math.sin(sideAngle))
                ? (Math.cos(sideAngle) >= 0 ? Direction.EAST : Direction.WEST)
                : (Math.sin(sideAngle) >= 0 ? Direction.SOUTH : Direction.NORTH);
    }

    private static void placeLeafPad(WorldGenLevel level,
                                     RandomSource random,
                                     BlockPos center,
                                     int radius,
                                     int verticalRadius,
                                     Set<BlockPos> leafPositions) {
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                for (int dy = -verticalRadius; dy <= verticalRadius; dy++) {
                    double horizontal = Math.sqrt(dx * dx + dz * dz);
                    if (horizontal > radius + 0.2D) {
                        continue;
                    }
                    if (verticalRadius > 0 && horizontal > radius - 0.35D && Math.abs(dy) > 0 && random.nextFloat() < 0.45F) {
                        continue;
                    }
                    if (horizontal > radius - 0.2D && random.nextFloat() < 0.28F) {
                        continue;
                    }

                    BlockPos pos = center.offset(dx, dy, dz);
                    placeLeaf(level, pos, leafPositions);
                }
            }
        }
    }

    private static void finalizeGeneratedLeaves(WorldGenLevel level, Set<BlockPos> logPositions, Set<BlockPos> leafPositions) {
        Map<BlockPos, Integer> distances = new HashMap<>();
        ArrayDeque<BlockPos> queue = new ArrayDeque<>(logPositions);

        while (!queue.isEmpty()) {
            BlockPos current = queue.removeFirst();
            int distance = distances.getOrDefault(current, 0);
            if (distance >= 6) {
                continue;
            }

            for (Direction direction : Direction.values()) {
                BlockPos neighbor = current.relative(direction);
                if (!leafPositions.contains(neighbor)) {
                    continue;
                }

                int next = distance + 1;
                int existing = distances.getOrDefault(neighbor, Integer.MAX_VALUE);
                if (next < existing) {
                    distances.put(neighbor, next);
                    queue.addLast(neighbor);
                }
            }
        }

        for (BlockPos leafPos : leafPositions) {
            BlockState state = level.getBlockState(leafPos);
            if (state.is(ModBlocks.BAOBAB_LEAVES.get())) {
                int distance = distances.getOrDefault(leafPos, 7);
                setGeneratedBlock(level, leafPos, state.setValue(LeavesBlock.DISTANCE, Math.min(7, distance)).setValue(LeavesBlock.PERSISTENT, false));
            }
        }
    }

    private static void placeFallenFruitPods(WorldGenLevel level, RandomSource random, BlockPos origin, Set<BlockPos> leafPositions) {
        List<BlockPos> candidates = new ArrayList<>();
        for (BlockPos leafPos : leafPositions) {
            BlockPos podPos = leafPos.below();
            BlockState belowState = level.getBlockState(podPos);
            if ((!belowState.isAir() && !belowState.canBeReplaced()) || leafPos.getY() < origin.getY() + 3) {
                continue;
            }
            candidates.add(leafPos.immutable());
        }

        shufflePositions(random, candidates);
        int targetCount = 2 + random.nextInt(3);
        int placed = 0;
        for (BlockPos leafPos : candidates) {
            if (placed >= targetCount) {
                break;
            }
            BlockPos podPos = leafPos.below();
            BlockState state = ModBlocks.BAOBAB_FRUIT_POD.get().defaultBlockState()
                    .setValue(de.artemis.baobabtree.common.block.BaobabFruitPodBlock.AGE, random.nextInt(4))
                    .setValue(de.artemis.baobabtree.common.block.BaobabFruitPodBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(random));
            if (!state.canSurvive(level, podPos)) {
                continue;
            }
            setGeneratedBlock(level, podPos, state);
            placed++;
        }
    }

    private static void placeFallenGroundCover(WorldGenLevel level,
                                               RandomSource random,
                                               BlockPos origin,
                                               Direction direction,
                                               List<BlockPos> trunkCenters,
                                               float stumpRadius) {
        int radius = 7;
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                double distance = Math.sqrt(dx * dx + dz * dz);
                if (distance > radius + 0.35D || distance < stumpRadius - 0.15D) {
                    continue;
                }

                BlockPos supportPos = surfacePosAt(level, origin.getX() + dx, origin.getZ() + dz);
                BlockPos placePos = supportPos.above();
                BlockState supportState = level.getBlockState(supportPos);
                if (!canPlaceDecorationOn(supportState) || !canReplace(level.getBlockState(placePos))) {
                    continue;
                }

                if (random.nextFloat() < 0.42F) {
                    setGeneratedBlock(level, placePos, ModBlocks.BAOBAB_LITTER.get().defaultBlockState()
                            .setValue(PinkPetalsBlock.AMOUNT, 1 + random.nextInt(4))
                            .setValue(PinkPetalsBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(random)));
                } else if (random.nextFloat() < 0.38F) {
                    BlockState foliage = random.nextInt(10) < 6
                            ? Blocks.SHORT_GRASS.defaultBlockState()
                            : random.nextInt(10) < 8 ? Blocks.FERN.defaultBlockState() : Blocks.DEAD_BUSH.defaultBlockState();
                    if (foliage.canSurvive(level, placePos)) {
                        setGeneratedBlock(level, placePos, foliage);
                    }
                }
            }
        }

        if (trunkCenters.isEmpty()) {
            return;
        }

        for (BlockPos center : trunkCenters) {
            for (Direction side : Direction.Plane.HORIZONTAL) {
                if (side == direction || side == direction.getOpposite()) {
                    continue;
                }
                if (random.nextFloat() >= 0.22F) {
                    continue;
                }
                BlockPos supportPos = surfacePosAt(level, center.getX() + side.getStepX(), center.getZ() + side.getStepZ());
                BlockPos placePos = supportPos.above();
                if (!canPlaceDecorationOn(level.getBlockState(supportPos)) || !canReplace(level.getBlockState(placePos))) {
                    continue;
                }
                setGeneratedBlock(level, placePos, ModBlocks.BAOBAB_LITTER.get().defaultBlockState()
                        .setValue(PinkPetalsBlock.AMOUNT, 1 + random.nextInt(3))
                        .setValue(PinkPetalsBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(random)));
            }
        }
    }

    private static boolean canPlaceDecorationOn(BlockState state) {
        return state.is(BlockTags.DIRT)
                || state.is(Blocks.GRASS_BLOCK)
                || state.is(Blocks.COARSE_DIRT)
                || state.is(Blocks.DIRT)
                || state.is(Blocks.ROOTED_DIRT)
                || state.is(Blocks.SAND)
                || state.is(Blocks.RED_SAND)
                || state.is(ModBlocks.TREE_ROOT.get());
    }

    private static void shufflePositions(RandomSource random, List<BlockPos> positions) {
        for (int i = positions.size() - 1; i > 0; i--) {
            int swapIndex = random.nextInt(i + 1);
            BlockPos tmp = positions.get(i);
            positions.set(i, positions.get(swapIndex));
            positions.set(swapIndex, tmp);
        }
    }

    private static BlockPos surfacePosAt(WorldGenLevel level, int x, int z) {
        return new BlockPos(x, level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, x, z) - 1, z);
    }

    private static void placeLeaf(WorldGenLevel level, BlockPos pos, Set<BlockPos> leafPositions) {
        BlockState state = level.getBlockState(pos);
        if (canReplace(state) || state.is(ModBlocks.BAOBAB_LEAVES.get())) {
            setGeneratedBlock(level, pos, ModBlocks.BAOBAB_LEAVES.get().defaultBlockState().setValue(LeavesBlock.PERSISTENT, false));
            leafPositions.add(pos.immutable());
        }
    }

    private static void placeWood(WorldGenLevel level, BlockPos pos, Set<BlockPos> logPositions) {
        BlockState state = level.getBlockState(pos);
        if (canReplace(state) || state.is(ModBlocks.BAOBAB_LEAVES.get())) {
            setGeneratedBlock(level, pos, ModBlocks.BAOBAB_WOOD.get().defaultBlockState());
            logPositions.add(pos.immutable());
        }
    }

    private static boolean supportsFoundation(BlockState state) {
        return isAllowedSoil(state)
                || state.is(Blocks.ROOTED_DIRT)
                || state.is(ModBlocks.TREE_ROOT.get())
                || (!canReplace(state) && !state.is(BlockTags.LEAVES));
    }

    private static void tryPlaceSoilBlock(WorldGenLevel level, BlockPos pos, BlockState state) {
        BlockState existing = level.getBlockState(pos);
        if (existing.is(BlockTags.DIRT)
                || existing.is(Blocks.GRASS_BLOCK)
                || existing.is(Blocks.COARSE_DIRT)
                || existing.is(Blocks.DIRT)
                || existing.is(Blocks.ROOTED_DIRT)
                || existing.is(Blocks.SAND)
                || existing.is(Blocks.RED_SAND)) {
            setGeneratedBlock(level, pos, state);
        }
    }

    private static void setGeneratedBlock(WorldGenLevel level, BlockPos pos, BlockState newState) {
        clearDoublePlantAt(level, pos);
        level.setBlock(pos, newState, 2);
    }

    private static void clearDoublePlantAt(WorldGenLevel level, BlockPos pos) {
        BlockState existing = level.getBlockState(pos);
        if (!existing.hasProperty(DoublePlantBlock.HALF)) {
            return;
        }

        DoubleBlockHalf half = existing.getValue(DoublePlantBlock.HALF);
        BlockPos otherPos = half == DoubleBlockHalf.LOWER ? pos.above() : pos.below();
        BlockState otherState = level.getBlockState(otherPos);
        if (otherState.is(existing.getBlock()) && otherState.hasProperty(DoublePlantBlock.HALF)) {
            level.setBlock(otherPos, Blocks.AIR.defaultBlockState(), 2);
        }
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
    }

    private static final class GenerationState {
        private final WorldGenLevel level;
        private final RandomSource random;
        private final BlockPos origin;
        private final Variant variant;
        private final int trunkHeight;
        private final int branchCount;
        private final int desiredLeafClusters;
        private final int leafRadius;
        private final float baseRadius;
        private final float bodyRadius;
        private final float neckRadius;
        @SuppressWarnings("unused")
        private final boolean withGroundDecoration;
        private final Set<BlockPos> logPositions = new HashSet<>();
        private final Set<BlockPos> leafPositions = new HashSet<>();
        private final List<LeafPadSeed> leafPadSeeds = new ArrayList<>();

        private GenerationState(WorldGenLevel level, RandomSource random, BlockPos origin, Variant variant, int trunkHeight, boolean withGroundDecoration) {
            this.level = level;
            this.random = random;
            this.origin = origin;
            this.variant = variant;
            this.trunkHeight = trunkHeight;
            this.withGroundDecoration = withGroundDecoration;
            this.branchCount = Mth.nextInt(random, variant.minBranches, variant.maxBranches);
            this.desiredLeafClusters = Mth.nextInt(random, variant.minLeafClusters, variant.maxLeafClusters);
            this.leafRadius = Mth.nextInt(random, variant.minLeafRadius, variant.maxLeafRadius);
            this.baseRadius = variant.baseRadius + (random.nextFloat() - 0.5F) * 0.25F;
            this.bodyRadius = variant.bodyRadius + (random.nextFloat() - 0.5F) * 0.2F;
            this.neckRadius = variant.neckRadius + (random.nextFloat() - 0.5F) * 0.15F;
        }

        private void placeGroundPatch() {
            int radius = Mth.ceil(baseRadius) + 2;
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    if (dx * dx + dz * dz > (radius + 0.75F) * (radius + 0.75F)) {
                        continue;
                    }

                    BlockPos soilPos = surfacePosAt(origin.getX() + dx, origin.getZ() + dz);
                    BlockState state = level.getBlockState(soilPos);
                    if (Math.abs(soilPos.getY() - (origin.getY() - 1)) > variant.maxSlope) {
                        continue;
                    }
                    if (!isAncientInterior(dx, dz) && random.nextFloat() < 0.18F) {
                        continue;
                    }
                    if (state.is(BlockTags.DIRT) || state.is(Blocks.GRASS_BLOCK)) {
                        setGeneratedBlock(soilPos, random.nextFloat() < 0.55F ? Blocks.COARSE_DIRT.defaultBlockState() : Blocks.DIRT.defaultBlockState());
                    }
                }
            }
        }

        private void placeRootSystem() {
            enrichCentralSoil();

            int armCount = switch (variant) {
                case YOUNG -> 3 + random.nextInt(2);
                case MATURE -> 4 + random.nextInt(2);
                case ANCIENT -> 5 + random.nextInt(2);
                case FALLEN -> 4 + random.nextInt(2);
            };
            int armLength = switch (variant) {
                case YOUNG -> 2 + random.nextInt(2);
                case MATURE -> 3 + random.nextInt(2);
                case ANCIENT -> 4 + random.nextInt(2);
                case FALLEN -> 3 + random.nextInt(2);
            };
            double startAngle = random.nextDouble() * Mth.TWO_PI;

            for (int armIndex = 0; armIndex < armCount; armIndex++) {
                double angle = startAngle + armIndex * (Mth.TWO_PI / armCount) + (random.nextDouble() - 0.5D) * 0.55D;
                Direction.Axis axis = Math.abs(Math.cos(angle)) > Math.abs(Math.sin(angle)) ? Direction.Axis.X : Direction.Axis.Z;
                double startDistance = Math.max(1.5D, baseRadius - 0.3D);

                for (int step = 0; step < armLength; step++) {
                    double distance = startDistance + step * 0.95D;
                    int x = origin.getX() + Mth.floor(Math.cos(angle) * distance);
                    int z = origin.getZ() + Mth.floor(Math.sin(angle) * distance);
                    int surfaceY = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, x, z) - 1;
                    if (Math.abs(surfaceY - (origin.getY() - 1)) > 1) {
                        continue;
                    }

                    BlockPos surfacePos = new BlockPos(x, surfaceY, z);
                    BlockPos belowPos = surfacePos.below();
                    BlockPos deeperPos = belowPos.below();

                    if (step == 0 || (step == 1 && random.nextFloat() < 0.45F)) {
                        placeSurfaceRoot(surfacePos, axis);
                    } else {
                        placeRootedDirt(surfacePos);
                    }

                    if (random.nextFloat() < 0.9F) {
                        placeBuriedRoot(belowPos, axis);
                    } else {
                        placeRootedDirt(belowPos);
                    }

                    if (step > 0 && random.nextFloat() < 0.65F) {
                        if (random.nextFloat() < 0.45F) {
                            placeBuriedRoot(deeperPos, axis);
                        } else {
                            placeRootedDirt(deeperPos);
                        }
                    }

                    if (random.nextFloat() < 0.55F) {
                        BlockPos sidePos = surfacePos.relative(sideDirection(angle, random.nextBoolean()));
                        placeRootedDirt(sidePos);
                    }
                }
            }
        }

        private void enrichCentralSoil() {
            int radius = Mth.ceil(baseRadius) + 1;
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    double distance = Math.sqrt(dx * dx + dz * dz);
                    if (distance > baseRadius + 0.65D) {
                        continue;
                    }

                    BlockPos topPos = surfacePosAt(origin.getX() + dx, origin.getZ() + dz);
                    if (Math.abs(topPos.getY() - (origin.getY() - 1)) > variant.maxSlope) {
                        continue;
                    }
                    BlockPos belowPos = topPos.below();
                    if (isAncientInterior(dx, dz)) {
                        if (level.getBlockState(topPos).is(BlockTags.DIRT) || level.getBlockState(topPos).is(Blocks.GRASS_BLOCK)) {
                            setGeneratedBlock(topPos, random.nextFloat() < 0.6F ? Blocks.COARSE_DIRT.defaultBlockState() : Blocks.DIRT.defaultBlockState());
                        }
                        if (random.nextFloat() < 0.5F) {
                            placeRootedDirt(belowPos);
                        }
                        continue;
                    }
                    if (distance > baseRadius - 0.45D && random.nextFloat() < 0.22F) {
                        Direction.Axis axis = radialAxis(dx, dz);
                        placeSurfaceRoot(topPos, axis);
                    } else if (random.nextFloat() < 0.55F) {
                        placeRootedDirt(topPos);
                    }

                    if (random.nextFloat() < 0.65F) {
                        placeRootedDirt(belowPos);
                    }
                }
            }
        }

        private void anchorTrunkBase() {
            int radius = Mth.ceil(baseRadius + 0.7F);
            int maxDepth = variant.maxSlope + 3;
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    double distance = Math.sqrt(dx * dx + dz * dz);
                    if (distance > baseRadius + 0.15D) {
                        continue;
                    }

                    BlockPos trunkPos = origin.offset(dx, 0, dz);
                    if (!logPositions.contains(trunkPos)) {
                        continue;
                    }

                    for (int depth = 1; depth <= maxDepth; depth++) {
                        BlockPos fillPos = trunkPos.below(depth);
                        BlockState fillState = level.getBlockState(fillPos);
                        if (supportsFoundation(fillState)) {
                            break;
                        }
                        if (!canReplace(fillState) && !fillState.is(BlockTags.LEAVES)) {
                            break;
                        }

                        setGeneratedBlock(fillPos, ModBlocks.BAOBAB_WOOD.get().defaultBlockState());
                        logPositions.add(fillPos.immutable());
                    }
                }
            }
        }

        private void placeSurfaceRoot(BlockPos pos, Direction.Axis axis) {
            if (!canReplaceSoilAt(pos)) {
                return;
            }
            BlockPos abovePos = pos.above();
            BlockState aboveState = level.getBlockState(abovePos);
            if (!aboveState.isAir() && !aboveState.canBeReplaced()) {
                return;
            }
            setGeneratedBlock(pos, ModBlocks.TREE_ROOT.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, axis));
        }

        private void placeBuriedRoot(BlockPos pos, Direction.Axis axis) {
            if (!canReplaceSoilAt(pos)) {
                return;
            }
            setGeneratedBlock(pos, ModBlocks.TREE_ROOT.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, axis));
        }

        private void placeRootedDirt(BlockPos pos) {
            if (!canReplaceSoilAt(pos)) {
                return;
            }
            setGeneratedBlock(pos, Blocks.ROOTED_DIRT.defaultBlockState());
        }

        private Direction.Axis radialAxis(int dx, int dz) {
            if (Math.abs(dx) == Math.abs(dz)) {
                return random.nextBoolean() ? Direction.Axis.X : Direction.Axis.Z;
            }
            return Math.abs(dx) > Math.abs(dz) ? Direction.Axis.X : Direction.Axis.Z;
        }

        private boolean canReplaceSoilAt(BlockPos pos) {
            BlockState state = level.getBlockState(pos);
            return state.is(BlockTags.DIRT)
                    || state.is(Blocks.GRASS_BLOCK)
                    || state.is(Blocks.COARSE_DIRT)
                    || state.is(Blocks.DIRT)
                    || state.is(Blocks.ROOTED_DIRT)
                    || state.is(Blocks.RED_SAND)
                    || state.is(Blocks.SAND);
        }

        private boolean supportsFoundation(BlockState state) {
            return isAllowedSoil(state)
                    || state.is(Blocks.ROOTED_DIRT)
                    || state.is(ModBlocks.TREE_ROOT.get())
                    || (!canReplace(state) && !state.is(BlockTags.LEAVES));
        }

        private BlockPos surfacePosAt(int x, int z) {
            int surfaceY = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, x, z) - 1;
            return new BlockPos(x, surfaceY, z);
        }

        private void placeGroundCover() {
            int radius = switch (variant) {
                case YOUNG -> 4;
                case MATURE -> 6;
                case ANCIENT -> 7;
                case FALLEN -> 6;
            };

            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    double distance = Math.sqrt(dx * dx + dz * dz);
                    if (distance > radius + 0.35D) {
                        continue;
                    }
                    if (distance < Math.max(1.6D, baseRadius - 0.6D) && random.nextFloat() < 0.65F) {
                        continue;
                    }

                    int x = origin.getX() + dx;
                    int z = origin.getZ() + dz;
                    int surfaceY = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, x, z) - 1;
                    if (Math.abs(surfaceY - (origin.getY() - 1)) > 2) {
                        continue;
                    }

                    BlockPos supportPos = new BlockPos(x, surfaceY, z);
                    BlockPos placePos = supportPos.above();
                    BlockState supportState = level.getBlockState(supportPos);
                    BlockState placeState = level.getBlockState(placePos);

                    if (!canPlaceGroundCoverOn(supportState) || !canPlaceDecorationAt(placeState)) {
                        continue;
                    }
                    if (isAncientInterior(dx, dz)) {
                        if (supportState.is(Blocks.GRASS_BLOCK)) {
                            setGeneratedBlock(supportPos, random.nextFloat() < 0.6F ? Blocks.COARSE_DIRT.defaultBlockState() : Blocks.DIRT.defaultBlockState());
                        }
                        continue;
                    }
                    if (supportState.is(ModBlocks.TREE_ROOT.get()) && random.nextFloat() < 0.75F) {
                        continue;
                    }

                    float normalized = (float) (distance / Math.max(1.0D, radius));
                    float litterChance = 0.74F - normalized * 0.24F;
                    if (random.nextFloat() < litterChance) {
                        placeLitter(placePos, supportPos);
                        continue;
                    }

                    float foliageChance = 0.22F + normalized * 0.24F;
                    if (variant == Variant.ANCIENT && normalized > 0.45F) {
                        foliageChance += 0.12F;
                    }
                    if (random.nextFloat() < foliageChance) {
                        placeFoliage(placePos, supportPos);
                    }
                }
            }
        }

        private boolean isAncientInterior(int dx, int dz) {
            if (variant != Variant.ANCIENT) {
                return false;
            }
            float interiorRadius = Math.max(1.35F, radiusAtHeight(0.0F) - 1.45F);
            return dx * dx + dz * dz <= interiorRadius * interiorRadius;
        }

        private boolean canPlaceGroundCoverOn(BlockState state) {
            return state.is(BlockTags.DIRT)
                    || state.is(Blocks.GRASS_BLOCK)
                    || state.is(Blocks.COARSE_DIRT)
                    || state.is(Blocks.DIRT)
                    || state.is(Blocks.ROOTED_DIRT)
                    || state.is(Blocks.SAND)
                    || state.is(Blocks.RED_SAND)
                    || state.is(ModBlocks.TREE_ROOT.get());
        }

        private boolean canPlaceDecorationAt(BlockState state) {
            return state.isAir() || state.canBeReplaced();
        }

        private void placeLitter(BlockPos placePos, BlockPos supportPos) {
            if (!level.getBlockState(supportPos).isFaceSturdy(level, supportPos, Direction.UP)) {
                return;
            }

            int amountRoll = random.nextInt(10);
            int amount = switch (amountRoll) {
                case 0, 1, 2, 3 -> 1;
                case 4, 5, 6 -> 2;
                case 7, 8 -> 3;
                default -> 4;
            };
            Direction facing = Direction.Plane.HORIZONTAL.getRandomDirection(random);
            BlockState litterState = de.artemis.baobabtree.common.block.BaobabLitterBlock.clearPodState(ModBlocks.BAOBAB_LITTER.get().defaultBlockState())
                    .setValue(PinkPetalsBlock.AMOUNT, amount)
                    .setValue(PinkPetalsBlock.FACING, facing);
            if (random.nextFloat() < amount * 0.07F) {
                float podRoll = random.nextFloat();
                litterState = litterState.setValue(de.artemis.baobabtree.common.block.BaobabLitterBlock.HAS_SMALL, podRoll < 0.55F);
                litterState = litterState.setValue(de.artemis.baobabtree.common.block.BaobabLitterBlock.HAS_MEDIUM, podRoll >= 0.55F && podRoll < 0.85F);
                litterState = litterState.setValue(de.artemis.baobabtree.common.block.BaobabLitterBlock.HAS_LARGE, podRoll >= 0.85F);
            }
            if (litterState.canSurvive(level, placePos)) {
                setGeneratedBlock(placePos, litterState);
            }
        }

        private void placeFoliage(BlockPos placePos, BlockPos supportPos) {
            if (!level.getBlockState(supportPos).isFaceSturdy(level, supportPos, Direction.UP)) {
                return;
            }

            BlockState supportState = level.getBlockState(supportPos);
            BlockState foliageState;
            if (supportState.is(Blocks.SAND) || supportState.is(Blocks.RED_SAND)) {
                foliageState = Blocks.DEAD_BUSH.defaultBlockState();
            } else {
                int roll = random.nextInt(10);
                foliageState = roll < 6 ? Blocks.SHORT_GRASS.defaultBlockState()
                        : roll < 8 ? Blocks.FERN.defaultBlockState()
                        : Blocks.DEAD_BUSH.defaultBlockState();
            }

            if (foliageState.canSurvive(level, placePos)) {
                setGeneratedBlock(placePos, foliageState);
            }
        }

        private void placeTrunk() {
            int flareHeight = switch (variant) {
                case YOUNG -> 2;
                case MATURE, FALLEN -> 3;
                case ANCIENT -> 4;
            };
            for (int y = 0; y < trunkHeight; y++) {
                float t = y / (float) Math.max(1, trunkHeight - 1);
                float radius = radiusAtHeight(t);
                float flareExtra = y < flareHeight ? (1.0F - y / (float) flareHeight) * 0.18F : 0.0F;
                placeCircularLayer(origin.getY() + y, radius + flareExtra);
            }
        }

        private void carveAncientHollow() {
            if (variant != Variant.ANCIENT) {
                return;
            }

            for (int y = 0; y < trunkHeight; y++) {
                float t = y / (float) Math.max(1, trunkHeight - 1);
                float outerRadius = radiusAtHeight(t);
                float hollowRadius = Math.max(1.35F, outerRadius - 1.45F);
                carveCircularLayer(origin.getY() + y, hollowRadius);
            }
        }

        private float radiusAtHeight(float t) {
            if (t < 0.18F) {
                return Mth.lerp(t / 0.18F, baseRadius + 0.6F, bodyRadius + 0.2F);
            }
            if (t < 0.72F) {
                return Mth.lerp((t - 0.18F) / 0.54F, bodyRadius + 0.2F, bodyRadius);
            }
            if (t < 0.92F) {
                return Mth.lerp((t - 0.72F) / 0.2F, bodyRadius, neckRadius);
            }
            return Mth.lerp((t - 0.92F) / 0.08F, neckRadius, Math.max(neckRadius, bodyRadius * 0.92F));
        }

        private void placeCircularLayer(int worldY, float radius) {
            int extent = Mth.ceil(radius) + 1;
            for (int dx = -extent; dx <= extent; dx++) {
                for (int dz = -extent; dz <= extent; dz++) {
                    double distance = Math.sqrt(dx * dx + dz * dz);
                    if (distance <= radius + 0.08D) {
                        placeLog(origin.offset(dx, worldY - origin.getY(), dz), Direction.Axis.Y, false);
                    }
                }
            }
        }

        private void carveCircularLayer(int worldY, float radius) {
            int extent = Mth.ceil(radius);
            for (int dx = -extent; dx <= extent; dx++) {
                for (int dz = -extent; dz <= extent; dz++) {
                    double distance = Math.sqrt(dx * dx + dz * dz);
                    if (distance > radius) {
                        continue;
                    }

                    BlockPos pos = origin.offset(dx, worldY - origin.getY(), dz);
                    if (!level.getBlockState(pos).is(ModBlocks.BAOBAB_WOOD.get())) {
                        continue;
                    }

                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                    logPositions.remove(pos);
                }
            }
        }

        private void placeBranches() {
            double startAngle = random.nextDouble() * Mth.TWO_PI;
            int branchStartBase = Mth.floor(trunkHeight * (variant == Variant.YOUNG ? 0.76F : 0.74F));

            for (int branchIndex = 0; branchIndex < branchCount; branchIndex++) {
                double angle = startAngle + branchIndex * (Mth.TWO_PI / branchCount) + (random.nextDouble() - 0.5D) * 0.7D;
                int startY = branchStartBase + random.nextInt(Math.max(1, trunkHeight / 10));
                float startRadius = radiusAtHeight(startY / (float) Math.max(1, trunkHeight - 1));
                BlockPos start = BlockPos.containing(
                        origin.getX() + Math.cos(angle) * Math.max(1.2D, startRadius - 0.45D),
                        origin.getY() + startY,
                        origin.getZ() + Math.sin(angle) * Math.max(1.2D, startRadius - 0.45D)
                );

                int length = Mth.nextInt(random, variant.minBranchLength, variant.maxBranchLength);
                float thickness = variant.branchThickness + (random.nextFloat() - 0.5F) * 0.08F;
                List<BlockPos> primary = placeBranch(start, angle, length, thickness, false);

                if (!primary.isEmpty()) {
                    addLeafSeed(primary.get(primary.size() - 1), angle, leafRadius);
                }

                int secondaryCount = variant == Variant.YOUNG ? 2 + random.nextInt(2) : 3 + random.nextInt(2);
                for (int i = 0; i < secondaryCount; i++) {
                    if (primary.isEmpty()) {
                        break;
                    }
                    int anchorIndex = Mth.clamp(Mth.floor((0.58F + random.nextFloat() * 0.22F) * (primary.size() - 1)), 0, primary.size() - 1);
                    BlockPos anchor = primary.get(anchorIndex);
                    double secondaryAngle = angle + (random.nextBoolean() ? 1.0D : -1.0D) * (0.38D + random.nextDouble() * 0.42D);
                    int secondaryLength = Math.max(3, Mth.floor(length * (0.36F + random.nextFloat() * 0.06F)));
                    List<BlockPos> secondary = placeBranch(anchor, secondaryAngle, secondaryLength, Math.max(0.95F, thickness - 0.28F), true);

                    if (!secondary.isEmpty()) {
                        addLeafSeed(secondary.get(secondary.size() - 1), secondaryAngle, Math.max(2, leafRadius - 1));
                        if (random.nextFloat() < 0.7F) {
                            int tertiaryAnchorIndex = Mth.clamp(Mth.floor((0.62F + random.nextFloat() * 0.2F) * (secondary.size() - 1)), 0, secondary.size() - 1);
                            BlockPos tertiaryAnchor = secondary.get(tertiaryAnchorIndex);
                            double tertiaryAngle = secondaryAngle + (random.nextBoolean() ? 1.0D : -1.0D) * (0.32D + random.nextDouble() * 0.28D);
                            int tertiaryLength = Math.max(2, Mth.floor(secondaryLength * (0.32F + random.nextFloat() * 0.06F)));
                            List<BlockPos> tertiary = placeBranch(tertiaryAnchor, tertiaryAngle, tertiaryLength, 0.9F, true);
                            if (!tertiary.isEmpty()) {
                                addLeafSeed(tertiary.get(tertiary.size() - 1), tertiaryAngle, Math.max(2, leafRadius - 2));
                            }
                        }
                    }
                }
            }
        }

        private List<BlockPos> placeBranch(BlockPos start, double angle, int length, float thickness, boolean lighter) {
            int effectiveLength = length;
            if (length >= 8) {
                effectiveLength -= 1;
            }
            double branchReach = switch (variant) {
                case YOUNG -> Math.min(effectiveLength, 4.6D);
                case MATURE, ANCIENT, FALLEN -> effectiveLength;
            };

            int rise = lighter ? random.nextInt(2) : 1;
            double bend = (random.nextDouble() - 0.5D) * Math.max(0.6D, branchReach * 0.08D);

            Vec3iLike p0 = new Vec3iLike(start.getX(), start.getY(), start.getZ());
            Vec3iLike p1 = new Vec3iLike(
                    start.getX() + Mth.floor(Math.cos(angle) * Math.max(2.0D, branchReach * 0.22D)),
                    start.getY(),
                    start.getZ() + Mth.floor(Math.sin(angle) * Math.max(2.0D, branchReach * 0.22D))
            );
            Vec3iLike p2 = new Vec3iLike(
                    start.getX() + Mth.floor(Math.cos(angle) * (branchReach * 0.58D) - Math.sin(angle) * bend),
                    start.getY() + rise,
                    start.getZ() + Mth.floor(Math.sin(angle) * (branchReach * 0.58D) + Math.cos(angle) * bend)
            );
            Vec3iLike p3 = new Vec3iLike(
                    start.getX() + Mth.floor(Math.cos(angle) * branchReach),
                    start.getY() + rise + (lighter ? 0 : 1),
                    start.getZ() + Mth.floor(Math.sin(angle) * branchReach)
            );

            List<BlockPos> samples = new ArrayList<>();
            BlockPos previous = null;
            int steps = Math.max(8, effectiveLength * 4);
            for (int step = 0; step <= steps; step++) {
                float t = step / (float) steps;
                Vec3Like point = bezier(p0, p1, p2, p3, t);
                BlockPos current = BlockPos.containing(point.x(), point.y(), point.z());
                if (previous != null && previous.equals(current)) {
                    continue;
                }

                Direction.Axis axis = previous == null
                        ? (Math.abs(Math.cos(angle)) > Math.abs(Math.sin(angle)) ? Direction.Axis.X : Direction.Axis.Z)
                        : (Math.abs(current.getX() - previous.getX()) > Math.abs(current.getZ() - previous.getZ()) ? Direction.Axis.X : Direction.Axis.Z);
                placeBranchSection(current, axis, 0);

                samples.add(current.immutable());
                previous = current;
            }

            return samples;
        }

        private void addLeafSeed(BlockPos tip, double angle, int radius) {
            leafPadSeeds.add(new LeafPadSeed(tip, angle, radius));
        }

        private void placeLeafClusters() {
            if (leafPadSeeds.isEmpty()) {
                return;
            }

            addCentralCrownSeed();
            leafPadSeeds.sort(Comparator.comparingInt((LeafPadSeed seed) -> seed.tip().getY()).reversed());
            for (LeafPadSeed seed : leafPadSeeds) {
                int baseRadius = Math.max(2, Math.min(3, seed.radius() - 1));
                BlockPos baseCenter = seed.tip().offset(
                        Mth.floor(Math.cos(seed.angle()) * Math.max(1, baseRadius - 1)),
                        0,
                        Mth.floor(Math.sin(seed.angle()) * Math.max(1, baseRadius - 1))
                );
                placeCircularLeafPad(baseCenter, baseRadius, 0);
            }

            int count = leafPadSeeds.size();
            for (int i = 0; i < count; i++) {
                LeafPadSeed seed = leafPadSeeds.get(i);
                placeLeafCluster(seed);
            }
        }

        private void addCentralCrownSeed() {
            int radius = Math.max(3, leafRadius);
            double angle = random.nextDouble() * Mth.TWO_PI;
            BlockPos tip = origin.offset(0, trunkHeight, 0);
            leafPadSeeds.add(new LeafPadSeed(tip, angle, radius));
        }

        private void placeLeafCluster(LeafPadSeed seed) {
            int radius = Math.max(2, seed.radius());
            int verticalRadius = switch (variant) {
                case YOUNG -> 1;
                case MATURE -> 1;
                case ANCIENT -> 1 + random.nextInt(2);
                case FALLEN -> 1;
            };
            BlockPos center = seed.tip().offset(
                    Mth.floor(Math.cos(seed.angle()) * Math.max(0, radius - 2)),
                    0,
                    Mth.floor(Math.sin(seed.angle()) * Math.max(0, radius - 2))
            );

            placeTipTransition(seed.tip(), seed.angle());
            placeLeafSupport(center, verticalRadius);
            placeCircularLeafPad(center, radius, verticalRadius);
            if (random.nextFloat() < 0.7F) {
                int sideOffset = Math.max(1, radius - 2);
                double sideAngle = seed.angle() + (random.nextBoolean() ? 0.7D : -0.7D);
                BlockPos linkedCenter = center.offset(
                        Mth.floor(Math.cos(sideAngle) * sideOffset),
                        random.nextInt(Math.max(1, verticalRadius + 1)) - (verticalRadius > 1 ? 1 : 0),
                        Mth.floor(Math.sin(sideAngle) * sideOffset)
                );
                placeLeafSupport(linkedCenter, Math.max(0, verticalRadius - 1));
                placeCircularLeafPad(linkedCenter, Math.max(2, radius - 1), Math.max(0, verticalRadius - 1));
            }
        }

        private void placeLeafSupport(BlockPos center, int verticalRadius) {
            placeWood(center.below(), true);
            if (verticalRadius >= 2) {
                placeWood(center, true);
            }
        }

        private void placeTipTransition(BlockPos tip, double angle) {
            placeLeafIfPossible(tip.above());
            placeLeafIfPossible(tip.relative(sideDirection(angle, true)));
            placeLeafIfPossible(tip.relative(sideDirection(angle, false)));
            placeLeafIfPossible(tip.offset(
                    Mth.floor(Math.cos(angle)),
                    0,
                    Mth.floor(Math.sin(angle))
            ));
        }

        private Direction sideDirection(double angle, boolean positive) {
            double sideAngle = angle + (positive ? Math.PI / 2.0D : -Math.PI / 2.0D);
            return Math.abs(Math.cos(sideAngle)) > Math.abs(Math.sin(sideAngle))
                    ? (Math.cos(sideAngle) >= 0 ? Direction.EAST : Direction.WEST)
                    : (Math.sin(sideAngle) >= 0 ? Direction.SOUTH : Direction.NORTH);
        }

        private void placeLeafIfPossible(BlockPos pos) {
            if (canReplace(level.getBlockState(pos))) {
                setGeneratedBlock(pos, ModBlocks.BAOBAB_LEAVES.get().defaultBlockState());
                leafPositions.add(pos.immutable());
            }
        }

        private void placeCircularLeafPad(BlockPos center, int radius, int verticalRadius) {
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    for (int dy = -verticalRadius; dy <= verticalRadius; dy++) {
                        double horizontal = Math.sqrt(dx * dx + dz * dz);
                        if (horizontal > radius + 0.2D) {
                            continue;
                        }
                        if (Math.abs(dy) > verticalRadius) {
                            continue;
                        }
                        double verticalNormalized = verticalRadius == 0 ? 0.0D : Math.abs(dy) / (double) (verticalRadius + 0.25D);
                        if (verticalRadius > 0 && horizontal > radius - 0.35D && verticalNormalized > 0.55D) {
                            continue;
                        }
                        if (horizontal > radius - 0.2D && random.nextFloat() < 0.32F) {
                            continue;
                        }

                        BlockPos pos = center.offset(dx, dy, dz);
                        if (canReplace(level.getBlockState(pos))) {
                            setGeneratedBlock(pos, ModBlocks.BAOBAB_LEAVES.get().defaultBlockState().setValue(LeavesBlock.PERSISTENT, false));
                            leafPositions.add(pos.immutable());
                        }
                    }
                }
            }
        }

        private void finalizeLeaves() {
            Map<BlockPos, Integer> distances = new HashMap<>();
            ArrayDeque<BlockPos> queue = new ArrayDeque<>(logPositions);

            while (!queue.isEmpty()) {
                BlockPos current = queue.removeFirst();
                int distance = distances.getOrDefault(current, 0);
                if (distance >= 6) {
                    continue;
                }

                for (Direction direction : Direction.values()) {
                    BlockPos neighbor = current.relative(direction);
                    if (!leafPositions.contains(neighbor)) {
                        continue;
                    }

                    int next = distance + 1;
                    int existing = distances.getOrDefault(neighbor, Integer.MAX_VALUE);
                    if (next < existing) {
                        distances.put(neighbor, next);
                        queue.addLast(neighbor);
                    }
                }
            }

            for (BlockPos leafPos : leafPositions) {
                BlockState state = level.getBlockState(leafPos);
                if (state.is(ModBlocks.BAOBAB_LEAVES.get())) {
                    int distance = distances.getOrDefault(leafPos, 7);
                    setGeneratedBlock(leafPos, state.setValue(LeavesBlock.DISTANCE, Math.min(7, distance)).setValue(LeavesBlock.PERSISTENT, false));
                }
            }
        }

        private void placeFruitPods() {
            List<BlockPos> candidates = new ArrayList<>();
            for (BlockPos leafPos : leafPositions) {
                BlockPos podPos = leafPos.below();
                BlockState belowState = level.getBlockState(podPos);
                if (!belowState.isAir() && !belowState.canBeReplaced()) {
                    continue;
                }
                if (leafPos.getY() < origin.getY() + trunkHeight - 5) {
                    continue;
                }
                if (Math.abs(leafPos.getX() - origin.getX()) <= 1 && Math.abs(leafPos.getZ() - origin.getZ()) <= 1) {
                    continue;
                }
                candidates.add(leafPos.immutable());
            }

            if (candidates.isEmpty()) {
                return;
            }

            shuffleCandidates(candidates);
            int targetCount = switch (variant) {
                case YOUNG -> 3 + random.nextInt(4);
                case MATURE -> 6 + random.nextInt(7);
                case ANCIENT -> 9 + random.nextInt(7);
                case FALLEN -> 4 + random.nextInt(4);
            };

            int placed = 0;
            for (BlockPos leafPos : candidates) {
                if (placed >= targetCount) {
                    break;
                }

                BlockPos podPos = leafPos.below();
                BlockState state = ModBlocks.BAOBAB_FRUIT_POD.get().defaultBlockState()
                        .setValue(de.artemis.baobabtree.common.block.BaobabFruitPodBlock.AGE, random.nextInt(4))
                        .setValue(de.artemis.baobabtree.common.block.BaobabFruitPodBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(random));
                if (!state.canSurvive(level, podPos)) {
                    continue;
                }

                setGeneratedBlock(podPos, state);
                placed++;
            }
        }

        private void shuffleCandidates(List<BlockPos> positions) {
            for (int i = positions.size() - 1; i > 0; i--) {
                int swapIndex = random.nextInt(i + 1);
                BlockPos tmp = positions.get(i);
                positions.set(i, positions.get(swapIndex));
                positions.set(swapIndex, tmp);
            }
        }

        private void placeBranchSection(BlockPos center, Direction.Axis axis, int extraHeight) {
            placeLog(center, axis, false);
        }

        private void placeLog(BlockPos pos, Direction.Axis axis, boolean branchWood) {
            BlockState state = ModBlocks.BAOBAB_WOOD.get().defaultBlockState();

            if (canReplace(level.getBlockState(pos)) || level.getBlockState(pos).is(ModBlocks.BAOBAB_LEAVES.get())) {
                setGeneratedBlock(pos, state);
                logPositions.add(pos.immutable());
            }
        }

        private void placeWood(BlockPos pos, boolean branchWood) {
            BlockState state = ModBlocks.BAOBAB_WOOD.get().defaultBlockState();

            if (canReplace(level.getBlockState(pos)) || level.getBlockState(pos).is(ModBlocks.BAOBAB_LEAVES.get())) {
                setGeneratedBlock(pos, state);
                logPositions.add(pos.immutable());
            }
        }

        private void setGeneratedBlock(BlockPos pos, BlockState newState) {
            clearDoublePlantAt(pos);
            level.setBlock(pos, newState, 2);
        }

        private void clearDoublePlantAt(BlockPos pos) {
            BlockState existing = level.getBlockState(pos);
            if (!existing.hasProperty(DoublePlantBlock.HALF)) {
                return;
            }

            DoubleBlockHalf half = existing.getValue(DoublePlantBlock.HALF);
            BlockPos otherPos = half == DoubleBlockHalf.LOWER ? pos.above() : pos.below();
            BlockState otherState = level.getBlockState(otherPos);
            if (otherState.is(existing.getBlock()) && otherState.hasProperty(DoublePlantBlock.HALF)) {
                level.setBlock(otherPos, Blocks.AIR.defaultBlockState(), 2);
            }
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
        }
    }

    private static Vec3Like bezier(Vec3iLike p0, Vec3iLike p1, Vec3iLike p2, Vec3iLike p3, float t) {
        float inv = 1.0F - t;
        float inv2 = inv * inv;
        float inv3 = inv2 * inv;
        float t2 = t * t;
        float t3 = t2 * t;

        double x = inv3 * p0.x() + 3.0D * inv2 * t * p1.x() + 3.0D * inv * t2 * p2.x() + t3 * p3.x();
        double y = inv3 * p0.y() + 3.0D * inv2 * t * p1.y() + 3.0D * inv * t2 * p2.y() + t3 * p3.y();
        double z = inv3 * p0.z() + 3.0D * inv2 * t * p1.z() + 3.0D * inv * t2 * p2.z() + t3 * p3.z();
        return new Vec3Like(x, y, z);
    }

    private record Vec3Like(double x, double y, double z) {
    }

    private record Vec3iLike(int x, int y, int z) {
    }

    private record LeafPadSeed(BlockPos tip, double angle, int radius) {
    }
}
