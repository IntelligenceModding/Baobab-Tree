package de.artemis.baobabtree;

import de.artemis.baobabtree.client.ClientModEvents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(value = BaobabTree.MOD_ID, dist = Dist.CLIENT)
public class BaobabTreeClient {
    public BaobabTreeClient(IEventBus modEventBus) {
        modEventBus.addListener(ClientModEvents::onClientSetup);
        modEventBus.addListener(ClientModEvents::registerEntityRenderers);
        modEventBus.addListener(ClientModEvents::registerBlockEntityRenderers);
        modEventBus.addListener(ClientModEvents::registerBlockColors);
        modEventBus.addListener(ClientModEvents::registerItemColors);
    }
}
