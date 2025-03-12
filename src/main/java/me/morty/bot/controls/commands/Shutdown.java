package me.morty.bot.controls.commands;

import me.morty.bot.Config;
import me.morty.bot.controls.CommandContext;
import me.morty.bot.controls.ICommand;

public class Shutdown implements ICommand {
    @Override
    public void handle(CommandContext ctx) {

        if (ctx.getAuthor().getId().equals(Config.getAdmin())) {
            ctx.getChannel().sendMessage("App is shutting down").queue();
            ctx.getJDA().shutdown();
        } else {
            ctx.getChannel().sendMessage(String.format("Only admin - %s --- %s - has permissions " +
                    "to shut down the bot", ctx.getJDA().getUserById(Config.getAdmin()), Config.getAdmin())).queue();
        }
    }

    @Override
    public String getName() {
        return "shutdown";
    }

    @Override
    public String getHelp() {
        return null;
    }
}
