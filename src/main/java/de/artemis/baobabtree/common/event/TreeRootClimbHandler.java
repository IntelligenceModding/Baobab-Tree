package de.artemis.baobabtree.common.event;

import de.artemis.baobabtree.common.registry.ModBlocks;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public final class TreeRootClimbHandler {
    private TreeRootClimbHandler() {
    }

    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        Level level = player.level();
        if (player.isSpectator() || player.isPassenger()) {
            return;
        }

        boolean touchingSideRoot = player.horizontalCollision && hasTreeRoot(level, player.getBoundingBox().inflate(0.08D, 0.0D, 0.08D));
        boolean touchingCeilingRoot = hasTreeRoot(level, player.getBoundingBox().move(0.0D, 0.12D, 0.0D).inflate(-0.02D, 0.0D, -0.02D));
        if (!touchingSideRoot && !touchingCeilingRoot) {
            return;
        }

        boolean movingIntoRoot = Math.abs(player.zza) > 0.01F || Math.abs(player.xxa) > 0.01F || player.horizontalCollision;
        Vec3 movement = player.getDeltaMovement();
        double x = Mth.clamp(movement.x, -0.15D, 0.15D);
        double z = Mth.clamp(movement.z, -0.15D, 0.15D);
        double y = Math.max(movement.y, -0.15D);
        boolean activeClimb = false;

        player.fallDistance = 0.0F;

        if (player.isShiftKeyDown()) {
            if (touchingSideRoot && y < 0.0D) {
                y = 0.0D;
            }
        } else {
            if (touchingSideRoot && movingIntoRoot) {
                y = Math.max(y, 0.18D);
                activeClimb = true;
            }
            if (touchingCeilingRoot) {
                y = Math.max(y, movingIntoRoot ? 0.08D : 0.02D);
                activeClimb = true;
            }
        }

        player.setDeltaMovement(x, y, z);
        player.hasImpulse = true;

        if (activeClimb && player.tickCount % 4 == 0) {
            playClimbFeedback(player, level, touchingCeilingRoot);
        }
    }

    private static boolean hasTreeRoot(Level level, AABB area) {
        int minX = Mth.floor(area.minX);
        int maxX = Mth.floor(area.maxX);
        int minY = Mth.floor(area.minY);
        int maxY = Mth.floor(area.maxY);
        int minZ = Mth.floor(area.minZ);
        int maxZ = Mth.floor(area.maxZ);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    if (level.getBlockState(new BlockPos(x, y, z)).is(ModBlocks.TREE_ROOT.get())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private static void playClimbFeedback(Player player, Level level, boolean ceilingClimb) {
        if (!level.isClientSide) {
            return;
        }

        level.addParticle(
                new BlockParticleOption(ParticleTypes.BLOCK, Blocks.ROOTED_DIRT.defaultBlockState()),
                player.getX() + (level.random.nextDouble() - 0.5D) * 0.3D,
                player.getY() + (ceilingClimb ? 1.7D : 0.8D),
                player.getZ() + (level.random.nextDouble() - 0.5D) * 0.3D,
                (level.random.nextDouble() - 0.5D) * 0.02D,
                ceilingClimb ? -0.01D : 0.015D,
                (level.random.nextDouble() - 0.5D) * 0.02D
        );
        if (level.random.nextFloat() < 0.55F) {
            level.playLocalSound(
                player.getX(),
                player.getY() + 0.5D,
                player.getZ(),
                Blocks.ROOTED_DIRT.defaultBlockState().getSoundType().getStepSound(),
                SoundSource.PLAYERS,
                0.16F,
                0.9F + level.random.nextFloat() * 0.18F,
                false
            );
        }
    }
}
