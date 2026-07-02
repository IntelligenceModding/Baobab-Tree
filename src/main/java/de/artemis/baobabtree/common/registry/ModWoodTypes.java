package de.artemis.baobabtree.common.registry;

import de.artemis.baobabtree.BaobabTree;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public final class ModWoodTypes {
    public static final String BAOBAB_NAME = BaobabTree.MOD_ID + ":baobab";

    public static final BlockSetType BAOBAB_SET_TYPE = BlockSetType.register(
            new BlockSetType(BAOBAB_NAME)
    );

    public static final WoodType BAOBAB_WOOD_TYPE = WoodType.register(
            new WoodType(
                    BAOBAB_NAME,
                    BAOBAB_SET_TYPE,
                    SoundType.WOOD,
                    SoundType.HANGING_SIGN,
                    SoundEvents.FENCE_GATE_CLOSE,
                    SoundEvents.FENCE_GATE_OPEN
            )
    );

    private ModWoodTypes() {
    }
}
