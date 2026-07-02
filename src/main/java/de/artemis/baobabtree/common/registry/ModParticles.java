package de.artemis.baobabtree.common.registry;

import de.artemis.baobabtree.BaobabTree;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, BaobabTree.MOD_ID);

    public static final Supplier<SimpleParticleType> BAOBAB_LITTER_FLUFF =
            PARTICLE_TYPES.register("baobab_litter_fluff", () -> new SimpleParticleType(true));

    private ModParticles() {
    }

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
