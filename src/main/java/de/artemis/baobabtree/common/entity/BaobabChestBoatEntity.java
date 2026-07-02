package de.artemis.baobabtree.common.entity;

import de.artemis.baobabtree.common.registry.ModEntityTypes;
import de.artemis.baobabtree.common.registry.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class BaobabChestBoatEntity extends ChestBoat {
    public BaobabChestBoatEntity(EntityType<? extends Boat> type, Level level) {
        super(type, level);
        setVariant(Type.ACACIA);
    }

    public BaobabChestBoatEntity(Level level, double x, double y, double z) {
        super(ModEntityTypes.BAOBAB_CHEST_BOAT.get(), level);
        setPos(x, y, z);
        xo = x;
        yo = y;
        zo = z;
        setVariant(Type.ACACIA);
    }

    @Override
    public Item getDropItem() {
        return ModItems.BAOBAB_CHEST_BOAT.get();
    }
}
