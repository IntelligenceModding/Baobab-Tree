package de.artemis.baobabtree.client.renderer;

import de.artemis.baobabtree.BaobabTree;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class BaobabBoatRenderer extends BoatRenderer {
    public static final ModelLayerLocation BOAT_LAYER = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(BaobabTree.MOD_ID, "boat/baobab"),
            "main"
    );
    public static final ModelLayerLocation CHEST_BOAT_LAYER = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(BaobabTree.MOD_ID, "chest_boat/baobab"),
            "main"
    );

    public BaobabBoatRenderer(EntityRendererProvider.Context context, boolean chestBoat) {
        super(context, chestBoat ? CHEST_BOAT_LAYER : BOAT_LAYER);
    }
}
