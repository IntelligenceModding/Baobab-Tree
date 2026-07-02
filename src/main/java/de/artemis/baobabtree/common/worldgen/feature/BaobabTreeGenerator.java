package de.artemis.baobabtree.common.worldgen.feature;

import de.artemis.baobabtree.common.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
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
        ANCIENT(22, 24, 4.3F, 3.6F, 2.5F, 6, 7, 8, 9, 1.05F, 12, 15, 4, 5, 0, 13, 4);

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
        if (origin.getY() <= level.getMinBuildHeight() + 1) {
            return false;
        }

        int trunkHeight = Mth.nextInt(random, variant.minHeight, variant.maxHeight);
        if (!isValidGround(level, origin, variant) || !hasClearance(level, origin, trunkHeight, variant)) {
            return false;
        }

        GenerationState state = new GenerationState(level, random, origin, variant, trunkHeight);
        state.placeGroundPatch();
        state.placeRootSystem();
        state.placeTrunk();
        state.placeBranches();
        state.placeLeafClusters();
        state.finalizeLeaves();
        return true;
    }

    private static boolean isValidGround(WorldGenLevel level, BlockPos origin, Variant variant) {
        int sampleRadius = Mth.ceil(variant.baseRadius) + 2;
        int minSurface = Integer.MAX_VALUE;
        int maxSurface = Integer.MIN_VALUE;

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

                for (int y = 1; y <= trunkHeight + variant.maxLeafRadius + 6; y++) {
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
                || state.is(ModBlocks.BAOBAB_LEAF_LITTER.get());
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
        private final Set<BlockPos> logPositions = new HashSet<>();
        private final Set<BlockPos> leafPositions = new HashSet<>();
        private final List<LeafPadSeed> leafPadSeeds = new ArrayList<>();

        private GenerationState(WorldGenLevel level, RandomSource random, BlockPos origin, Variant variant, int trunkHeight) {
            this.level = level;
            this.random = random;
            this.origin = origin;
            this.variant = variant;
            this.trunkHeight = trunkHeight;
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
                    if (dx * dx + dz * dz > (radius + 0.75F) * (radius + 0.75F) || random.nextFloat() < 0.18F) {
                        continue;
                    }

                    BlockPos soilPos = origin.offset(dx, -1, dz);
                    BlockState state = level.getBlockState(soilPos);
                    if (state.is(BlockTags.DIRT) || state.is(Blocks.GRASS_BLOCK)) {
                        level.setBlock(soilPos, random.nextFloat() < 0.55F ? Blocks.COARSE_DIRT.defaultBlockState() : Blocks.DIRT.defaultBlockState(), 2);
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
            };
            int armLength = switch (variant) {
                case YOUNG -> 2 + random.nextInt(2);
                case MATURE -> 3 + random.nextInt(2);
                case ANCIENT -> 4 + random.nextInt(2);
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

                    BlockPos topPos = origin.offset(dx, -1, dz);
                    BlockPos belowPos = topPos.below();
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

        private void placeSurfaceRoot(BlockPos pos, Direction.Axis axis) {
            if (!canReplaceSoilAt(pos)) {
                return;
            }
            BlockPos abovePos = pos.above();
            BlockState aboveState = level.getBlockState(abovePos);
            if (!aboveState.isAir() && !aboveState.canBeReplaced()) {
                return;
            }
            level.setBlock(pos, ModBlocks.TREE_ROOT.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, axis), 2);
        }

        private void placeBuriedRoot(BlockPos pos, Direction.Axis axis) {
            if (!canReplaceSoilAt(pos)) {
                return;
            }
            level.setBlock(pos, ModBlocks.TREE_ROOT.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, axis), 2);
        }

        private void placeRootedDirt(BlockPos pos) {
            if (!canReplaceSoilAt(pos)) {
                return;
            }
            level.setBlock(pos, Blocks.ROOTED_DIRT.defaultBlockState(), 2);
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

        private void placeTrunk() {
            int flareHeight = variant == Variant.YOUNG ? 2 : variant == Variant.MATURE ? 3 : 4;
            for (int y = 0; y < trunkHeight; y++) {
                float t = y / (float) Math.max(1, trunkHeight - 1);
                float radius = radiusAtHeight(t);
                float flareExtra = y < flareHeight ? (1.0F - y / (float) flareHeight) * 0.18F : 0.0F;
                placeCircularLayer(origin.getY() + y, radius + flareExtra);
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
                case MATURE, ANCIENT -> effectiveLength;
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
                level.setBlock(pos, ModBlocks.BAOBAB_LEAVES.get().defaultBlockState(), 2);
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
                            level.setBlock(pos, ModBlocks.BAOBAB_LEAVES.get().defaultBlockState().setValue(LeavesBlock.PERSISTENT, false), 2);
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
                    level.setBlock(leafPos, state.setValue(LeavesBlock.DISTANCE, Math.min(7, distance)).setValue(LeavesBlock.PERSISTENT, false), 2);
                }
            }
        }

        private void placeBranchSection(BlockPos center, Direction.Axis axis, int extraHeight) {
            placeLog(center, axis, false);
        }

        private void placeLog(BlockPos pos, Direction.Axis axis, boolean branchWood) {
            BlockState state = ModBlocks.BAOBAB_WOOD.get().defaultBlockState();

            if (canReplace(level.getBlockState(pos)) || level.getBlockState(pos).is(ModBlocks.BAOBAB_LEAVES.get())) {
                level.setBlock(pos, state, 2);
                logPositions.add(pos.immutable());
            }
        }

        private void placeWood(BlockPos pos, boolean branchWood) {
            BlockState state = ModBlocks.BAOBAB_WOOD.get().defaultBlockState();

            if (canReplace(level.getBlockState(pos)) || level.getBlockState(pos).is(ModBlocks.BAOBAB_LEAVES.get())) {
                level.setBlock(pos, state, 2);
                logPositions.add(pos.immutable());
            }
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
