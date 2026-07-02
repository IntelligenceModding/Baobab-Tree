package de.artemis.baobabtree.common.entity;

import de.artemis.baobabtree.common.registry.ModEntityTypes;
import de.artemis.baobabtree.common.registry.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class BaobabBoatEntity extends Boat {
    public BaobabBoatEntity(EntityType<? extends Boat> type, Level level) {
        super(type, level);
        setVariant(Type.ACACIA);
    }

    public BaobabBoatEntity(Level level, double x, double y, double z) {
        super(ModEntityTypes.BAOBAB_BOAT.get(), level);
        setPos(x, y, z);
        xo = x;
        yo = y;
        zo = z;
        setVariant(Type.ACACIA);
    }

    @Override
    public Item getDropItem() {
        return ModItems.BAOBAB_BOAT.get();
    }
}
