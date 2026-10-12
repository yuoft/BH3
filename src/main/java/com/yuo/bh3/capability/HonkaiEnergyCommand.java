package com.yuo.bh3.capability;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.yuo.bh3.BH3;
import com.yuo.bh3.packet.SyncHonkaiEnergyPacket;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;
import java.util.List;

@Mod.EventBusSubscriber(modid = BH3.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HonkaiEnergyCommand {

    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("honkaienergy")
                .requires(src -> src.hasPermission(2))   // 需要 OP 权限
                // /honkaienergy get [player]
                .then(Commands.literal("get")
                        .executes(ctx -> query(ctx, List.of(ctx.getSource().getPlayerOrException())))
                        .then(Commands.argument("targets", EntityArgument.players())
                                .executes(ctx -> query(ctx, EntityArgument.getPlayers(ctx, "targets")))))
                // /honkaienergy set <value> [targets]
                .then(Commands.literal("set")
                        .then(Commands.argument("value", FloatArgumentType.floatArg(0f))
                                .executes(ctx -> setValue(ctx,
                                        List.of(ctx.getSource().getPlayerOrException()),
                                        FloatArgumentType.getFloat(ctx, "value")))
                                .then(Commands.argument("targets", EntityArgument.players())
                                        .executes(ctx -> setValue(ctx,
                                                EntityArgument.getPlayers(ctx, "targets"),
                                                FloatArgumentType.getFloat(ctx, "value"))))))
                // /honkaienergy add <value> [targets]
                .then(Commands.literal("add")
                        .then(Commands.argument("value", FloatArgumentType.floatArg())
                                .executes(ctx -> addValue(ctx,
                                        List.of(ctx.getSource().getPlayerOrException()),
                                        FloatArgumentType.getFloat(ctx, "value")))
                                .then(Commands.argument("targets", EntityArgument.players())
                                        .executes(ctx -> addValue(ctx,
                                                EntityArgument.getPlayers(ctx, "targets"),
                                                FloatArgumentType.getFloat(ctx, "value"))))))
                // /honkaienergy max <value> [targets]
                .then(Commands.literal("max")
                        .then(Commands.argument("value", FloatArgumentType.floatArg(1f))
                                .executes(ctx -> setMax(ctx,
                                        List.of(ctx.getSource().getPlayerOrException()),
                                        FloatArgumentType.getFloat(ctx, "value")))
                                .then(Commands.argument("targets", EntityArgument.players())
                                        .executes(ctx -> setMax(ctx,
                                                EntityArgument.getPlayers(ctx, "targets"),
                                                FloatArgumentType.getFloat(ctx, "value"))))));

        dispatcher.register(root);
    }

    // ===================== 子命令实现 =====================

    /** 查询 */
    private static int query(CommandContext<CommandSourceStack> ctx, Collection<ServerPlayer> targets) {
        CommandSourceStack src = ctx.getSource();
        for (ServerPlayer p : targets) {
            p.getCapability(HonkaiEnergyProvider.CAP).ifPresent(energy -> {
                src.sendSuccess(() -> Component.translatable(
                        "commands.honkaienergy.info.get",
                        p.getName().getString(),
                        energy.getEnergy(),
                        energy.getMaxEnergy(),
                        energy.getRatio() * 100f
                ), false);
            });
        }
        return targets.size();
    }

    /** 设置为指定值（自动 clamp 到 [0, max]） */
    private static int setValue(CommandContext<CommandSourceStack> ctx, Collection<ServerPlayer> targets, float value) {
        CommandSourceStack src = ctx.getSource();
        for (ServerPlayer p : targets) {
            p.getCapability(HonkaiEnergyProvider.CAP).ifPresent(energy -> {
                energy.setEnergy(value);

                // ★ 立即同步到客户端（HUD 需要）
                SyncHonkaiEnergyPacket.send(p, energy.getEnergy(), energy.getMaxEnergy());

                src.sendSuccess(() -> Component.translatable("commands.honkaienergy.info.set",
                        p.getName().getString(), energy.getEnergy()
                ), true);
            });
        }
        return targets.size();
    }

    /** 增加/减少（传负值即扣） */
    private static int addValue(CommandContext<CommandSourceStack> ctx, Collection<ServerPlayer> targets, float value) {
        CommandSourceStack src = ctx.getSource();
        for (ServerPlayer p : targets) {
            p.getCapability(HonkaiEnergyProvider.CAP).ifPresent(energy -> {
                energy.add(value);

                SyncHonkaiEnergyPacket.send(p, energy.getEnergy(), energy.getMaxEnergy());

                src.sendSuccess(() -> Component.translatable(
                        "commands.honkaienergy.info.add",
                        Component.translatable(value >= 0
                                ? "commands.honkaienergy.info.add0"
                                : "commands.honkaienergy.info.add1"),
                        p.getName().getString(),
                        Math.abs(value),
                        energy.getEnergy(),
                        energy.getMaxEnergy()
                ), true);
            });
        }
        return targets.size();
    }

    /** 修改上限 */
    private static int setMax(CommandContext<CommandSourceStack> ctx, Collection<ServerPlayer> targets, float value) {
        CommandSourceStack src = ctx.getSource();
        for (ServerPlayer p : targets) {
            p.getCapability(HonkaiEnergyProvider.CAP).ifPresent(energy -> {
                energy.setMaxEnergy(value);

                SyncHonkaiEnergyPacket.send(p, energy.getEnergy(), energy.getMaxEnergy());

                src.sendSuccess(() -> Component.translatable("commands.honkaienergy.info.max",
                        p.getName().getString(),
                        energy.getMaxEnergy(),
                        energy.getEnergy()
                ), true);
            });
        }
        return targets.size();
    }
}