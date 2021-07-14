package me.morty.bot.app.controls.commands;

import me.morty.bot.app.Config;
import me.morty.bot.app.controls.CommandContext;
import me.morty.bot.app.controls.ICommand;

public class Shutdown implements ICommand {
    @Override
    public void handle(CommandContext ctx) {

        if (ctx.getAuthor().getId().equals(Config.getAdmin())) {
            ctx.getChannel().sendMessage("Bot is shutting down").queue();
            ctx.getJDA().shutdown();
            return;
        }
        else {
            ctx.getChannel().sendMessage(String.format("Only admin - %s --- %s - has permissions " +
                    "to shut down the bot",  ctx.getJDA().getUserById(Config.getAdmin()), Config.getAdmin())).queue();
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
