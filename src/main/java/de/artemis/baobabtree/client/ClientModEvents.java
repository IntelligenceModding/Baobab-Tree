package de.artemis.baobabtree.client;

import de.artemis.baobabtree.client.renderer.BaobabBoatRenderer;
import de.artemis.baobabtree.client.renderer.BaobabLitterBlockEntityRenderer;
import de.artemis.baobabtree.common.particle.BaobabLitterFluffParticle;
import de.artemis.baobabtree.common.registry.ModBlockEntities;
import de.artemis.baobabtree.common.registry.ModBlocks;
import de.artemis.baobabtree.common.registry.ModEntityTypes;
import de.artemis.baobabtree.common.registry.ModParticles;
import net.minecraft.client.model.object.boat.BoatModel;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.StandingSignRenderer;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

import java.util.List;

public final class ClientModEvents {
    private ClientModEvents() {
    }

    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.BAOBAB_BOAT.get(),
                context -> new BaobabBoatRenderer(context, false));
        event.registerEntityRenderer(ModEntityTypes.BAOBAB_CHEST_BOAT.get(),
                context -> new BaobabBoatRenderer(context, true));
    }

    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BaobabBoatRenderer.BOAT_LAYER, BoatModel::createBoatModel);
        event.registerLayerDefinition(BaobabBoatRenderer.CHEST_BOAT_LAYER, BoatModel::createChestBoatModel);
    }

    public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.BAOBAB_SIGN.get(), StandingSignRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.BAOBAB_HANGING_SIGN.get(), HangingSignRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.BAOBAB_LITTER.get(), BaobabLitterBlockEntityRenderer::new);
    }

    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.BAOBAB_LITTER_FLUFF.get(), BaobabLitterFluffParticle.Provider::new);
    }

    public static void registerBlockTintSources(RegisterColorHandlersEvent.BlockTintSources event) {
        event.register(List.of(BlockTintSources.foliage()),
                ModBlocks.BAOBAB_LEAVES.get(),
                ModBlocks.BAOBAB_LITTER.get());
    }
}
