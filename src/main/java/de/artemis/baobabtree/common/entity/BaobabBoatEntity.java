package de.artemis.baobabtree.common.entity;

import de.artemis.baobabtree.common.registry.ModEntityTypes;
import de.artemis.baobabtree.common.registry.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.boat.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class BaobabBoatEntity extends Boat {
    public BaobabBoatEntity(EntityType<? extends Boat> type, Level level) {
        super(type, level, () -> ModItems.BAOBAB_BOAT.get());
    }

    public BaobabBoatEntity(Level level, double x, double y, double z) {
        this(ModEntityTypes.BAOBAB_BOAT.get(), level);
        setInitialPos(x, y, z);
    }
}
