package de.artemis.baobabtree.common.item;

import de.artemis.baobabtree.common.entity.BaobabBoatEntity;
import de.artemis.baobabtree.common.entity.BaobabChestBoatEntity;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class BaobabBoatItem extends Item {
    private final boolean chestBoat;

    public BaobabBoatItem(boolean chestBoat, Properties properties) {
        super(properties);
        this.chestBoat = chestBoat;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);

        if (hit.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(stack);
        }

        if (!level.isClientSide) {
            double x = hit.getLocation().x;
            double y = hit.getLocation().y;
            double z = hit.getLocation().z;

            if (chestBoat) {
                BaobabChestBoatEntity boat = new BaobabChestBoatEntity(level, x, y, z);
                boat.setYRot(player.getYRot());
                level.addFreshEntity(boat);
            } else {
                BaobabBoatEntity boat = new BaobabBoatEntity(level, x, y, z);
                boat.setYRot(player.getYRot());
                level.addFreshEntity(boat);
            }

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}
