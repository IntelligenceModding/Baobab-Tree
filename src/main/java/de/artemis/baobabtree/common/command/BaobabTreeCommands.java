package de.artemis.baobabtree.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import de.artemis.baobabtree.common.worldgen.feature.BaobabTreeGenerator;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public final class BaobabTreeCommands {
    private static final int DEFAULT_SPACING = 30;

    private BaobabTreeCommands() {
    }

    public static void register(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("baobabtree")
                .requires(source -> source.hasPermission(4))
                .then(Commands.literal("spawn")
                        .then(Commands.literal("young")
                                .executes(context -> spawnVariant(context, BaobabTreeGenerator.Variant.YOUNG, sourcePos(context)))
                                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                        .executes(context -> spawnVariant(context, BaobabTreeGenerator.Variant.YOUNG, BlockPosArgument.getLoadedBlockPos(context, "pos")))))
                        .then(Commands.literal("mature")
                                .executes(context -> spawnVariant(context, BaobabTreeGenerator.Variant.MATURE, sourcePos(context)))
                                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                        .executes(context -> spawnVariant(context, BaobabTreeGenerator.Variant.MATURE, BlockPosArgument.getLoadedBlockPos(context, "pos")))))
                        .then(Commands.literal("ancient")
                                .executes(context -> spawnVariant(context, BaobabTreeGenerator.Variant.ANCIENT, sourcePos(context)))
                                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                        .executes(context -> spawnVariant(context, BaobabTreeGenerator.Variant.ANCIENT, BlockPosArgument.getLoadedBlockPos(context, "pos")))))
                        .then(Commands.literal("fallen")
                                .executes(context -> spawnVariant(context, BaobabTreeGenerator.Variant.FALLEN, sourcePos(context)))
                                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                        .executes(context -> spawnVariant(context, BaobabTreeGenerator.Variant.FALLEN, BlockPosArgument.getLoadedBlockPos(context, "pos")))))
                        .then(Commands.literal("all")
                                .executes(context -> spawnAll(context, sourcePos(context), DEFAULT_SPACING))
                                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                        .executes(context -> spawnAll(context, BlockPosArgument.getLoadedBlockPos(context, "pos"), DEFAULT_SPACING))
                                        .then(Commands.argument("spacing", IntegerArgumentType.integer(18, 64))
                                                .executes(context -> spawnAll(
                                                        context,
                                                        BlockPosArgument.getLoadedBlockPos(context, "pos"),
                                                        IntegerArgumentType.getInteger(context, "spacing"))))))));
    }

    private static int spawnVariant(CommandContext<CommandSourceStack> context, BaobabTreeGenerator.Variant variant, BlockPos requestedPos) {
        CommandSourceStack source = context.getSource();
        ServerLevel level = source.getLevel();
        BlockPos origin = resolveSurfaceOrigin(level, requestedPos);
        RandomSource random = level.getRandom();

        if (!BaobabTreeGenerator.generate(level, origin, random, variant)) {
            source.sendFailure(Component.literal("Failed to generate " + variantName(variant) + " baobab at " + formatPos(origin) + "."));
            return 0;
        }

        source.sendSuccess(() -> Component.literal("Generated " + variantName(variant) + " baobab at " + formatPos(origin) + "."), true);
        return 1;
    }

    private static int spawnAll(CommandContext<CommandSourceStack> context, BlockPos requestedPos, int spacing) {
        CommandSourceStack source = context.getSource();
        ServerLevel level = source.getLevel();
        int successes = 0;

        BlockPos youngPos = resolveSurfaceOrigin(level, requestedPos);
        successes += spawnVariantAt(source, level, youngPos, BaobabTreeGenerator.Variant.YOUNG) ? 1 : 0;

        BlockPos maturePos = resolveSurfaceOrigin(level, requestedPos.offset(spacing, 0, 0));
        successes += spawnVariantAt(source, level, maturePos, BaobabTreeGenerator.Variant.MATURE) ? 1 : 0;

        BlockPos ancientPos = resolveSurfaceOrigin(level, requestedPos.offset(spacing * 2, 0, 0));
        successes += spawnVariantAt(source, level, ancientPos, BaobabTreeGenerator.Variant.ANCIENT) ? 1 : 0;

        BlockPos fallenPos = resolveSurfaceOrigin(level, requestedPos.offset(spacing * 3, 0, 0));
        successes += spawnVariantAt(source, level, fallenPos, BaobabTreeGenerator.Variant.FALLEN) ? 1 : 0;

        if (successes == 0) {
            source.sendFailure(Component.literal("Failed to generate any baobabs near " + formatPos(resolveSurfaceOrigin(level, requestedPos)) + "."));
            return 0;
        }

        int finalSuccesses = successes;
        source.sendSuccess(() -> Component.literal("Generated " + finalSuccesses + "/4 baobab variants. Young at "
                + formatPos(youngPos) + ", mature at " + formatPos(maturePos) + ", ancient at " + formatPos(ancientPos) + ", fallen at " + formatPos(fallenPos) + "."), true);
        return successes;
    }

    private static boolean spawnVariantAt(CommandSourceStack source, ServerLevel level, BlockPos origin, BaobabTreeGenerator.Variant variant) {
        boolean success = BaobabTreeGenerator.generate(level, origin, level.getRandom(), variant);
        if (!success) {
            source.sendFailure(Component.literal("Failed to generate " + variantName(variant) + " baobab at " + formatPos(origin) + "."));
        }
        return success;
    }

    private static BlockPos sourcePos(CommandContext<CommandSourceStack> context) {
        return BlockPos.containing(context.getSource().getPosition());
    }

    private static BlockPos resolveSurfaceOrigin(ServerLevel level, BlockPos requestedPos) {
        int surfaceY = level.getHeight(Heightmap.Types.OCEAN_FLOOR, requestedPos.getX(), requestedPos.getZ());
        return new BlockPos(requestedPos.getX(), surfaceY, requestedPos.getZ());
    }

    private static String variantName(BaobabTreeGenerator.Variant variant) {
        return switch (variant) {
            case YOUNG -> "young";
            case MATURE -> "mature";
            case ANCIENT -> "ancient";
            case FALLEN -> "fallen";
        };
    }

    private static String formatPos(BlockPos pos) {
        return pos.getX() + " " + pos.getY() + " " + pos.getZ();
    }
}
