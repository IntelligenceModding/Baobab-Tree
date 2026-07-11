package de.artemis.baobabtree.common.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class BaobabLitterFluffParticle extends TextureSheetParticle {
    private final float driftStrength;
    private final float swirlPhase;

    protected BaobabLitterFluffParticle(ClientLevel level,
                                        double x,
                                        double y,
                                        double z,
                                        SpriteSet spriteSet,
                                        double xSpeed,
                                        double ySpeed,
                                        double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);

        double angle = random.nextDouble() * Math.PI * 2.0D;
        double launch = 0.016D + random.nextDouble() * 0.012D;
        this.xd = xSpeed * 0.35D + Math.cos(angle) * launch;
        this.yd = 0.02D + random.nextDouble() * 0.014D;
        this.zd = zSpeed * 0.35D + Math.sin(angle) * launch;

        this.friction = 0.96F;
        this.gravity = 0.007F;
        this.quadSize = 0.18F + random.nextFloat() * 0.08F;
        this.lifetime = 30 + random.nextInt(18);
        this.driftStrength = 0.0035F + random.nextFloat() * 0.0025F;
        this.swirlPhase = random.nextFloat() * ((float) Math.PI * 2.0F);
        this.roll = random.nextFloat() * ((float) Math.PI * 2.0F);
        this.oRoll = this.roll;

        int tint = BiomeColors.getAverageFoliageColor(level, BlockPos.containing(x, y, z));
        float red = ((tint >> 16) & 0xFF) / 255.0F;
        float green = ((tint >> 8) & 0xFF) / 255.0F;
        float blue = (tint & 0xFF) / 255.0F;
        this.setColor(red, green, blue);

        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.oRoll = this.roll;

        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }

        float ageRatio = (float) this.age / (float) this.lifetime;
        double breezeX = Math.sin(this.age * 0.34D + this.swirlPhase) * this.driftStrength;
        double breezeZ = Math.cos(this.age * 0.29D + this.swirlPhase) * this.driftStrength;
        this.xd += breezeX;
        this.zd += breezeZ;

        this.yd -= this.gravity * (0.5F + ageRatio * 0.9F);
        this.move(this.xd, this.yd, this.zd);

        this.xd *= 0.965D;
        this.zd *= 0.965D;
        this.yd *= 0.91D;
        this.roll += 0.045F + this.driftStrength * 5.0F;

        if (this.onGround) {
            this.xd *= 0.76D;
            this.zd *= 0.76D;
            this.yd = 0.0D;
            this.roll *= 0.97F;
        }

        if (ageRatio > 0.55F) {
            this.alpha = 1.0F - (ageRatio - 0.55F) / 0.45F;
        }
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull SimpleParticleType type,
                                       @NotNull ClientLevel level,
                                       double x,
                                       double y,
                                       double z,
                                       double xSpeed,
                                       double ySpeed,
                                       double zSpeed) {
            return new BaobabLitterFluffParticle(level, x, y, z, this.spriteSet, xSpeed, ySpeed, zSpeed);
        }
    }
}
