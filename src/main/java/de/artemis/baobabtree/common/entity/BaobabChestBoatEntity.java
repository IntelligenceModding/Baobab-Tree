package de.artemis.baobabtree.common.entity;

import de.artemis.baobabtree.common.registry.ModEntityTypes;
import de.artemis.baobabtree.common.registry.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.level.Level;

public class BaobabChestBoatEntity extends ChestBoat {
    public BaobabChestBoatEntity(EntityType<? extends ChestBoat> type, Level level) {
        super(type, level, () -> ModItems.BAOBAB_CHEST_BOAT.get());
    }

    public BaobabChestBoatEntity(Level level, double x, double y, double z) {
        this(ModEntityTypes.BAOBAB_CHEST_BOAT.get(), level);
        setInitialPos(x, y, z);
    }
}
