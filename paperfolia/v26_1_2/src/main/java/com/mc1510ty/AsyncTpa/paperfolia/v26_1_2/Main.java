package com.mc1510ty.AsyncTpa.paperfolia.v26_1_2;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Main extends JavaPlugin {

    private java.util.logging.Logger logger;

    private final Map<UUID, UUID> tpaRequests = new ConcurrentHashMap<>();

    @Override
    public void onEnable() {
        String version = getPluginMeta().getVersion();
        logger.info("Loading AsyncTpa " + version + " for Paper and Folia v26.1.2");
        registerCommands();
    }

    public void registerCommands() {
        LiteralArgumentBuilder<CommandSourceStack> tpa = Commands.literal("tpa")
                .requires(source -> source.getExecutor() instanceof Player)
                .then(Commands.argument("target", ArgumentTypes.player())
                        .executes(context -> {
                                    Player sender = (Player) context.getSource().getExecutor(); //送信者の取得

                                    //ターゲットの取得
                                    var selector = context.getArgument("target", PlayerSelectorArgumentResolver.class);
                                    Player target = selector.resolve(context.getSource()).getFirst();

                                    // UUIDへ変換
                                    UUID targetUuid = target.getUniqueId();
                                    UUID senderUuid = sender.getUniqueId();

                                    tpaRequests.put(targetUuid, senderUuid);

                                    // 送信側へのメッセージ
                                    sender.sendMessage("§a" + target.getName() + " にテレポートリクエストを送信しました！");

                                    // 受信側へのメッセージ
                                    target.sendMessage("§e" + sender.getName() + " からテレポートリクエストが届いています。");
                                    target.sendMessage("§e/tpaccept で承認します。");

                                    return Command.SINGLE_SUCCESS;
                        }
                        )
                );
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(tpa.build());
        });
    }
}
