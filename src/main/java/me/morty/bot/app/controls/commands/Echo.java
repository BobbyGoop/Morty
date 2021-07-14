package me.morty.bot.app.controls.commands;

import me.morty.bot.app.controls.CommandContext;
import me.morty.bot.app.controls.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class Echo implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();

        if (ctx.getArgs().isEmpty()) {
            channel.sendMessage("Мне нечего ответить.").queue();
            return;
        }

        channel.sendMessageFormat(String.join(" ", args)).queue();
    }

    @Override
    public String getName() {
        return "echo";
    }

    @Override
    public String getHelp(){
       return " Отвечает тем же сообщением";
    }
}

