package com.mc1510ty.AsyncTpa.paperfolia.v26_1_2;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private java.util.logging.Logger logger;

    @Override
    public void onEnable() {
        logger.info("Loading AsyncTpa for Paper and Folia v26.1.2");
        registerCommands();
    }

    public void registerCommands() {
        LiteralArgumentBuilder<CommandSourceStack> tpa = Commands.literal("tpa")
                .then(Commands.argument("target", ArgumentTypes.player())
                        .executes(ctx -> {
                            return Command.SINGLE_SUCCESS;
                        }
                        )
                );
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(tpa.build());
        });
    }
}
