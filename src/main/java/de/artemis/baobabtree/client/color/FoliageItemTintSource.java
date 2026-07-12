package de.artemis.baobabtree.client.color;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.FoliageColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class FoliageItemTintSource implements ItemTintSource {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("baobabtree", "foliage");
    public static final MapCodec<FoliageItemTintSource> MAP_CODEC = MapCodec.unit(new FoliageItemTintSource());

    @Override
    public int calculate(ItemStack itemStack, ClientLevel level, LivingEntity owner) {
        if (level != null && owner != null) {
            return BiomeColors.getAverageFoliageColor(level, owner.blockPosition());
        }

        return FoliageColor.FOLIAGE_DEFAULT;
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return MAP_CODEC;
    }
}
